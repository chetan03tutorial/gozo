package com.lbg.ib.api.sales.dao.product.retrieve;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.ProductDTO;
import com.lbg.ib.api.shared.domain.ProductIdDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdFamilyIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductFamily;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.BaseRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.conditions.IA_RetrieveProductConditions;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsRequest;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;

@Component
public class RetrieveProductDAOImpl implements RetrieveProductDAO {

    private static final String          MNEMONIC_SYSTEM_CODE = "00010";
    public static final String           METHOD_NAME          = "retrieveProduct";

    @Autowired
    private IA_RetrieveProductConditions retrieveService;

    @Autowired
    private GBOHeaderUtility             headerUtility;

    @Autowired
    private MCAHeaderUtility             mcaHeaderUtility;

    @Autowired
    private SessionManagementDAO         session;

    @Autowired
    private DAOExceptionHandler          daoExceptionHandler;

    @Autowired
    private ConfigurationDAO             configuration;


    /**
     * Retrieve the product based on product Family Identifier
     * @param retrieveProduct
     * @return
     */
    @TraceLog
    public DAOResponse<RetrieveProductConditionsResponse> retrieveProductFromFamily(RetrieveProductDTO retrieveProduct) {
        try {
            RetrieveProductConditionsRequest request = createRequest(retrieveProduct);
            RetrieveProductConditionsResponse response = retrieveService.retrieveProductConditions(request);
            return withResult(response);

        } catch (Exception ex) {
            DAOError daoError = daoExceptionHandler.handleException(ex, RetrieveProductDAOImpl.class, METHOD_NAME,
                    retrieveProduct);
            return withError(daoError);
        }
    }

    @TraceLog
    public DAOResponse<ProductDTO> retrieveProduct(ProductIdDTO requestDTO) {

        try {
            RetrieveProductConditionsRequest request = createRequest(requestDTO);
            RetrieveProductConditionsResponse response = retrieveService.retrieveProductConditions(request);
            ProductDTO responseDTO = mapResponseStructure(response);
            return withResult(responseDTO);

        } catch (Exception ex) {
            DAOError daoError = daoExceptionHandler.handleException(ex, RetrieveProductDAOImpl.class, METHOD_NAME,
                    requestDTO);
            return withError(daoError);
        }
    }

    /**
     * Retrieve the product based on product Family Identifier
     * @param retrieveProduct
     * @return
     */
    @TraceLog
    public DAOResponse<List<ProductDTO>> retrieveProduct(RetrieveProductDTO retrieveProduct) {
        try {
            RetrieveProductConditionsRequest request = createRequest(retrieveProduct);
            RetrieveProductConditionsResponse response = retrieveService.retrieveProductConditions(request);
            List<ProductDTO> responseDTO = mapResponseStructureForProductFamily(response);
            return withResult(responseDTO);

        } catch (Exception ex) {
            DAOError daoError = daoExceptionHandler.handleException(ex, RetrieveProductDAOImpl.class, METHOD_NAME,
                    retrieveProduct);
            return withError(daoError);
        }
    }

    private RetrieveProductConditionsRequest createRequest(ProductIdDTO requestDTO) {
        RetrieveProductConditionsRequest request = new RetrieveProductConditionsRequest();
        request.setHeader(populateHeaders().getHeader());
        request.setProduct(createProduct(requestDTO));
        return request;
    }

    private RetrieveProductConditionsRequest createRequest(RetrieveProductDTO retrieveProductDTO) {
        RetrieveProductConditionsRequest request = new RetrieveProductConditionsRequest();
        request.setHeader(populateHeaders().getHeader());
        request.setProductFamily(createProductFamily(retrieveProductDTO));
        return request;
    }


    private Product createProduct(ProductIdDTO requestDTO) {
        Product product = new Product();
        if (null != requestDTO.getCbsProductId()) {
            ExtSysProdIdentifier extSysProdIdentifier = new ExtSysProdIdentifier();
            extSysProdIdentifier.setSystemCode("00004");
            extSysProdIdentifier.setProductIdentifier(requestDTO.getCbsProductId());
            ExtSysProdIdentifier[] extSysProdIdentifierArray = new ExtSysProdIdentifier[1];
            extSysProdIdentifierArray[0] = extSysProdIdentifier;
            product.setExternalSystemProductIdentifier(extSysProdIdentifierArray);
        }
        product.setProductIdentifier(requestDTO.getValue());

        return product;
    }

    private ProductFamily[] createProductFamily(RetrieveProductDTO retrieveProductDTO) {
        ProductFamily[] product = new ProductFamily[1];
        ProductFamily productFamily = new ProductFamily();

        ExtSysProdFamilyIdentifier extSysProdFamilyIdentifier = new ExtSysProdFamilyIdentifier();
        extSysProdFamilyIdentifier.setSystemCode(retrieveProductDTO.getSystemCode());
        extSysProdFamilyIdentifier.setProductFamilyIdentifier(retrieveProductDTO.getProductFamilyIdentifier());

        ExtSysProdFamilyIdentifier[] extSysProdIdentifierArray = new ExtSysProdFamilyIdentifier[1];
        extSysProdIdentifierArray[0] = extSysProdFamilyIdentifier;
        productFamily.setExtsysprodfamilyidentifier(extSysProdIdentifierArray);
        product[0] = productFamily;
        return product;
    }
    /**
     * Method to populate the headers for retrieveProductConditions web service.
     *
     * @return - baseRequest.
     */
    private BaseRequest populateHeaders() {
        RequestHeader requestHeader = new RequestHeader();
        List<SOAPHeader> soapHeaders = null;
        if (null != session.getBranchContext()) {
            soapHeaders = mcaHeaderUtility.prepareSoapHeader("retrieveProductConditions", "retrieveProductConditions");
        } else {
            soapHeaders = headerUtility.prepareSoapHeader("retrieveProductConditions", "retrieveProductConditions");
        }

        if (null != session.getUserContext()) {
            Map<String, Object> map = configuration.getConfigurationItems("ChannelIdMapping");
            requestHeader.setChannelId(map.get(session.getUserContext().getChannelId()).toString());
        }

        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));
        requestHeader.setBusinessTransaction("retrieveProductConditions");
        requestHeader.setInteractionId(session.getSessionId());

        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setHeader(requestHeader);
        return baseRequest;
    }

    private ProductDTO mapResponseStructure(RetrieveProductConditionsResponse response) {
        if (response != null && response.getProduct() != null) {
            Product[] productArray = response.getProduct();
            for (Product product : productArray) {
                Map<String, String> options = new HashMap<String, String>();
                Map<String, List<String>> extIds = new HashMap<String, List<String>>();

                String productIdentifier = product.getProductIdentifier();
                String productName = product.getProductName();
                String pdtFamilyIdentifier = null;

                // Cross-sell changes - Added the null check here
                // ProductFamilyIdentifier will not be expected for a Savings
                // product
                pdtFamilyIdentifier = populateProductFamilyIdentifier(product,
                        pdtFamilyIdentifier);

                ProductOptions[] pdtOptionsArray = product.getProductoptions();
                for (ProductOptions pdtOptions : pdtOptionsArray) {
                    options.put(pdtOptions.getOptionsName(), pdtOptions.getOptionsValue());
                }

                String mnemonic = null;
                ExtSysProdIdentifier[] ids = product.getExternalSystemProductIdentifier();
                for (ExtSysProdIdentifier id : ids) {
                    if (id.getSystemCode().equals(MNEMONIC_SYSTEM_CODE)) {
                        mnemonic = id.getProductIdentifier();
                    }
                    extIds.put(id.getSystemCode(), Lists.newArrayList(id.getProductIdentifier()));
                }
                return new ProductDTO(productName, productIdentifier, mnemonic, pdtFamilyIdentifier, options, extIds,
                        null);
            }
            return null;
        } else {
            return null;
        }

    }

    /**
     *
     * @param response
     * @return
     */
    private List<ProductDTO> mapResponseStructureForProductFamily(RetrieveProductConditionsResponse response) {
        List<ProductDTO> products = Lists.newArrayList();
        if (response != null && response.getProduct() != null) {
            Product[] productArray = response.getProduct();
            for (Product product : productArray) {
                Map<String, String> options = Maps.newHashMap();
                Map<String, List<String>> extIds = Maps.newHashMap();

                String productIdentifier = product.getProductIdentifier();
                String productName = product.getProductName();
                String pdtFamilyIdentifier = null;

                pdtFamilyIdentifier = populateProductFamilyIdentifier(product,
                        pdtFamilyIdentifier);

                ProductOptions[] pdtOptionsArray = product.getProductoptions();
                for (ProductOptions pdtOptions : pdtOptionsArray) {
                    options.put(pdtOptions.getOptionsName(), pdtOptions.getOptionsValue());
                }

                String mnemonic = null;
                ExtSysProdIdentifier[] ids = product.getExternalSystemProductIdentifier();
                for (ExtSysProdIdentifier id : ids) {
                    if (id.getSystemCode().equals(MNEMONIC_SYSTEM_CODE)) {
                        mnemonic = id.getProductIdentifier();
                    }
                    if(extIds.containsKey(id.getSystemCode())) {
                        (extIds.get(id.getSystemCode())).add(id.getProductIdentifier());
                    } else {
                        extIds.put(id.getSystemCode(), Lists.newArrayList(id.getProductIdentifier()));
                    }
                }
                ProductDTO productDTO =  new ProductDTO(productName, productIdentifier, mnemonic, pdtFamilyIdentifier, options, extIds,
                        null);
                products.add(productDTO);
            }
        }
        return products;

    }

    /**
     * @param product
     * @param pdtFamilyIdentifier
     * @return
     */
    private String populateProductFamilyIdentifier(Product product,
                                                   String pdtFamilyIdentifier) {
        if (product.getAssociatedFamily() != null) {
            ProductFamily[] associatedFamily = product.getAssociatedFamily();
            for (ProductFamily pdtFamily : associatedFamily) {
                ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifiers = pdtFamily
                        .getExtsysprodfamilyidentifier();
                for (ExtSysProdFamilyIdentifier extsysprodfamilyidentifier : extsysprodfamilyidentifiers) {
                    pdtFamilyIdentifier = extsysprodfamilyidentifier.getProductFamilyIdentifier();
                    break;
                }
            }
        }
        return pdtFamilyIdentifier;
    }

    public void setRetrieveService(IA_RetrieveProductConditions retrieveService) {
        this.retrieveService = retrieveService;
    }
}
