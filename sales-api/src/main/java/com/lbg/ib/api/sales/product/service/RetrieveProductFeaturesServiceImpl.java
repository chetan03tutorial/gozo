package com.lbg.ib.api.sales.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.sales.dao.device.DeviceDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.product.retrieve.RetrieveProductDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.device.DeviceDTO;
import com.lbg.ib.api.sales.dto.product.ProductDTO;
import com.lbg.ib.api.shared.domain.ProductIdDTO;
import com.lbg.ib.api.shared.domain.ProductReferenceDTO;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.features.DeviceID;
import com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier;
import com.lbg.ib.api.sales.product.domain.features.Product;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;

@Component
public class RetrieveProductFeaturesServiceImpl implements RetrieveProductFeaturesService {
    private ReferenceDataServiceDAO referenceData;
    private RetrieveProductDAO      retrieveFeatures;
    private ChannelBrandingDAO      branding;
    private DeviceDAO               deviceIDDAO;
    private SessionManagementDAO    session;
    private GalaxyErrorCodeResolver resolver;
    private LoggerDAO               logger;
    private static final String     RELATED_VANTAGE_PRODUCT  = "Related Vantage product";
    private static final String     RELATED_VANTAGE_MNEMONIC = "Related Vantage Mnemonic";

    @Autowired
    public RetrieveProductFeaturesServiceImpl(ReferenceDataServiceDAO referenceData,
            RetrieveProductDAO retrieveFeatures, ChannelBrandingDAO branding, DeviceDAO deviceIDDAO,
            SessionManagementDAO session, GalaxyErrorCodeResolver resolver, LoggerDAO logger) {
        this.referenceData = referenceData;
        this.retrieveFeatures = retrieveFeatures;
        this.branding = branding;
        this.deviceIDDAO = deviceIDDAO;
        this.session = session;
        this.resolver = resolver;
        this.logger = logger;
    }

    @TraceLog
    public RetrieveProductConditionsResponse retrieveProductFromFamily(RetrieveProductDTO retrieveProductDTO) throws ServiceException{
        DAOResponse<RetrieveProductConditionsResponse> response = retrieveFeatures.retrieveProductFromFamily(retrieveProductDTO);
        DAOResponse.DAOError error = response.getError();
        if (null != error) {
            if (error.getErrorMessage() != null) {
                throw new ServiceException(resolver.customResolve(error.getErrorCode(), error.getErrorMessage()));
            } else {
                throw new ServiceException(resolver.resolve(error.getErrorCode()));
            }
        }
        return response.getResult();
    }

    @TraceLog
    public ProductFeatures retrieveProductFeaturesByProductIdNoSessionUpdate(ProductReference reference) throws ServiceException {
        ProductIdDTO productIdDto = new ProductIdDTO();
        productIdDto.setCbsProductId(reference.getValue());
        ProductDTO productDTO = retrieveProductFeaturesUsingProductId(productIdDto);
        return new ProductFeatures(product(productDTO), deviceID(channelDto()));
    }

    @TraceLog
    public ProductFeatures retrieveProductFeatures(ProductReference reference) throws ServiceException {
        ProductIdDTO productIdDto = referenceData.getProductId(new ProductReferenceDTO(reference.getValue()));
        if (productIdDto == null) {
            return null;
        }
        ProductDTO productDTO = retrieveProductFeatures(productIdDto);
        session.setSelectedProduct(selectedProduct(productDTO));

        return new ProductFeatures(product(productDTO), deviceID(channelDto()));
    }

    @TraceLog
    public ProductFeatures retrieveProductFeaturesUsingProductId(ProductReference reference) throws ServiceException {
        ProductIdDTO productIdDto = new ProductIdDTO();
        productIdDto.setCbsProductId(reference.getValue());
        Map<String, String> productVariances = new HashMap<String, String>();
        ProductDTO productDTO = retrieveProductFeaturesUsingProductId(productIdDto);
        if (null == productDTO) {
            return null;
        }
        SelectedProduct selectedProduct = selectedProduct(productDTO);
        session.setSelectedProduct(selectedProduct);

        if (CollectionUtils.isNotEmpty(selectedProduct.getChildren())) {
            for (ProductDTO prodDTO : selectedProduct.getChildren()) {
                productVariances.put(prodDTO.getProductFrequency(), prodDTO.getMnemonic());
            }
        }

        return new ProductFeatures(product(productDTO), deviceID(channelDto()), productVariances);
    }

    @TraceLog
    public ProductFeatures retrieveProductFeaturesByProductId(ProductReference reference) throws ServiceException {
        ProductIdDTO productIdDto = new ProductIdDTO(reference.getValue());
        ProductDTO productDTO = retrieveProductFeatures(productIdDto);
        setAlternateProduct(productDTO);
        return new ProductFeatures(product(productDTO), deviceID(channelDto()));
    }

    @TraceLog
    public List<ProductFeatures> retrieveProductFeaturesByProductFamilyIdentifier(RetrieveProductDTO retrieveProductDTO) throws ServiceException {
        List<ProductFeatures> productFeatures = Lists.newArrayList();
        List<ProductDTO> products;
        try {
            DAOResponse<List<ProductDTO>>  response = retrieveFeatures.retrieveProduct(retrieveProductDTO);
            DAOResponse.DAOError error = response.getError();
            if (null != error) {
                logger.traceLog(this.getClass(), "retrieveProductFeaturesByProductFamilyIdentifier: Error received from RPC: " +error.getErrorCode());
                if (error.getErrorMessage() != null) {
                    throw new ServiceException(resolver.customResolve(error.getErrorCode(), error.getErrorMessage()));
                } else {
                    throw new ServiceException(resolver.resolve(error.getErrorCode()));
                }
            } else {
                products =  response.getResult();
                logger.traceLog(this.getClass(), "retrieveProductFeaturesByProductFamilyIdentifier: List of product received: " +products.size());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.logException(RetrieveProductFeaturesServiceImpl.class, e);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.BACKEND_ERROR));
        }
        for(ProductDTO productDTO: products) {
            productFeatures.add(new ProductFeatures(product(productDTO), deviceID(channelDto())));
        }
        logger.traceLog(this.getClass(), "retrieveProductFeaturesByProductFamilyIdentifier: Returning product features: " +productFeatures.size());
        return productFeatures;
    }

    private ProductDTO retrieveProductFeaturesUsingProductId(ProductIdDTO productIdDto) throws ServiceException {
        try {
            DAOResponse<ProductDTO> response = retrieveFeatures.retrieveProduct(productIdDto);
            DAOResponse.DAOError error = response.getError();
            if (null != error) {
                if ("816001".equals(error.getErrorCode())) {
                    return null;
                } else {
                    throw new ServiceException(resolver.resolve(error.getErrorCode()));
                }
            }
            return response.getResult();

        } catch (Exception e) {
            logger.logException(RetrieveProductFeaturesServiceImpl.class, e);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.BACKEND_ERROR));
        }
    }

    private void setAlternateProduct(ProductDTO alternateProductDTO) {
        SelectedProduct selectedProduct = session.getSelectedProduct();
        SelectedProduct alternateProduct = selectedProduct(alternateProductDTO);
        if (selectedProduct != null) {
            selectedProduct.addAlternateProduct(alternateProduct);
        }
        // During downsell set the secondary Vantage Product.
        if (alternateProductDTO.getOptions() != null
                && alternateProductDTO.getOptions().containsKey(RELATED_VANTAGE_PRODUCT)) {
            String alternateVantageProductIdentifier = alternateProductDTO.getOptions().get(RELATED_VANTAGE_PRODUCT);
            String alternateVantageProductMnemonic = alternateProductDTO.getOptions().get(RELATED_VANTAGE_MNEMONIC);
            if (session.getArrangeToActivateParameters() != null) {
                session.getArrangeToActivateParameters()
                        .setAlternateVantagePrdIdentifier(alternateVantageProductIdentifier);
                session.getArrangeToActivateParameters().setAlternateVantageMnemonic(alternateVantageProductMnemonic);
            }
        }
    }

    private SelectedProduct selectedProduct(ProductDTO productDTO) {

        return new SelectedProduct(productDTO.getName(), productDTO.getIdentifier(),
                productDTO.getPdtFamilyIdentifier(), externalIdentifiers(productDTO), productDTO.getMnemonic(),
                productDTO.getChildren(),productDTO.getOptions());
    }

    private ChannelBrandDTO channelDto() throws ServiceException {
        DAOResponse<ChannelBrandDTO> brandDto = branding.getChannelBrand();
        DAOResponse.DAOError error = brandDto.getError();
        if (error != null) {
            throw new ServiceException(resolver.resolve(error.getErrorCode()));
        }
        return brandDto.getResult();
    }

    private DeviceID deviceID(ChannelBrandDTO channelBrandDto) {
        DAOResponse<DeviceDTO> device = deviceIDDAO.getDevice(channelBrandDto);
        if (device.getError() != null) {
            return null;
        } else {
            DeviceDTO deviceDTO = device.getResult();
            return new DeviceID(deviceDTO.getServer(), deviceDTO.getOrg(), deviceDTO.getId());
        }
    }

    private ProductDTO retrieveProductFeatures(ProductIdDTO productIdDto) throws ServiceException {
        try {
            DAOResponse<ProductDTO> response = retrieveFeatures.retrieveProduct(productIdDto);
            DAOResponse.DAOError error = response.getError();
            if (null != error) {
                if (error.getErrorMessage() != null) {
                    throw new ServiceException(resolver.customResolve(error.getErrorCode(), error.getErrorMessage()));
                } else {
                    throw new ServiceException(resolver.resolve(error.getErrorCode()));
                }
            } else {
                return (response.getResult());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.logException(RetrieveProductFeaturesServiceImpl.class, e);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.BACKEND_ERROR));
        }
    }

    private ExternalProductIdentifier[] externalIdentifiers(ProductDTO productDTO) {
        if (productDTO.getExtProdIdentifiers() != null) {
            Set<Map.Entry<String, List<String>>> raw = productDTO.getExtProdIdentifiers().entrySet();
            List<ExternalProductIdentifier> identifiers = new ArrayList<ExternalProductIdentifier>();
            for (Map.Entry<String, List<String>> id : raw) {
                identifiers.add(new ExternalProductIdentifier(id.getKey(), id.getValue().get(0)));
            }
            return identifiers.toArray(new ExternalProductIdentifier[identifiers.size()]);
        }

        return null;
    }

    private Product product(ProductDTO dto) {
        List<ProductAttributes> conditions = new ArrayList<ProductAttributes>();
        if (dto.getOptions() != null) {
            for (Map.Entry<String, String> entry : dto.getOptions().entrySet()) {
                conditions.add(new ProductAttributes(entry.getKey(), entry.getValue()));
            }
        }
        return new Product(dto.getName(), dto.getIdentifier(), dto.getMnemonic(), dto.getPdtFamilyIdentifier(),
                conditions, dto.getExtProdIdentifiers());
    }
}
