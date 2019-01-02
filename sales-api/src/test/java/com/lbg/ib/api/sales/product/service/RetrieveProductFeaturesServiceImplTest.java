/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import org.junit.Test;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
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
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.features.DeviceID;
import com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier;
import com.lbg.ib.api.sales.product.domain.features.Product;
import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;

public class RetrieveProductFeaturesServiceImplTest {
    private static final ProductIdDTO          PARENT_PRODUCT_ID     = new ProductIdDTO("");
    private static final ProductReference      REFERENCE             = new ProductReference("erf");
    private static final ProductIdDTO          PRODUCT_ID            = new ProductIdDTO("");
    private static final ChannelBrandDTO       CHANNEL               = new ChannelBrandDTO("ch", "br", "cid");
    private static final String                MNEMONIC              = "mnemonic";
    private static final String                PDT_FAMILY_IDENTIFIER = "1501";

    private static final ProductDTO            PRODUCT               = new ProductDTO("name", "id", MNEMONIC,
            PDT_FAMILY_IDENTIFIER, new HashMap<String, String>() {
                                                                                 {
                                                                                     put("n", "v");
                                                                                 }
                                                                             },
            new HashMap<String, List<String>>() {
                {
                    put("extn", Lists.newArrayList("extv"));
                }
            }, null);
    private static final DeviceDTO             DEVICE                = new DeviceDTO("server", "org", "id");
    private static final ResponseError         IB_ERROR              = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable");

    private static final ResponseError         CUSTOM_IB_ERROR       = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Customer Msg");
    private ReferenceDataServiceDAO            referenceData         = mock(ReferenceDataServiceDAO.class);
    private RetrieveProductDAO                 retrieveFeatures      = mock(RetrieveProductDAO.class);
    private ChannelBrandingDAO                 branding              = mock(ChannelBrandingDAO.class);
    private DeviceDAO                          device                = mock(DeviceDAO.class);
    private GalaxyErrorCodeResolver            errorResolver         = mock(GalaxyErrorCodeResolver.class);
    private LoggerDAO                          logger                = mock(LoggerDAO.class);
    private SessionManagementDAO               session               = mock(SessionManagementDAO.class);
    private RetrieveProductFeaturesServiceImpl service               = new RetrieveProductFeaturesServiceImpl(
            referenceData, retrieveFeatures, branding, device, session, errorResolver, logger);

    private static final ProductDTO            parentProduct         = parentProduct();

    @Test
    public void shouldReturnProductFeaturesWhenRetrieveFeaturesReturnResponse() throws Exception {
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(withResult(PRODUCT));

        ProductFeatures productFeatures = service.retrieveProductFeatures(REFERENCE);

        assertThat(productFeatures,
                is(new ProductFeatures(
                        new Product(PRODUCT.getName(), PRODUCT.getIdentifier(), MNEMONIC, PDT_FAMILY_IDENTIFIER,
                                conditions(PRODUCT), PRODUCT.getExtProdIdentifiers()),
                        new DeviceID(DEVICE.getServer(), DEVICE.getOrg(), DEVICE.getId()))));
    }

    @Test
    public void shouldReturnProductFeaturesAndSetAlternateProductWhenRetrieveFeaturesByIdReturnResponse()
            throws Exception {
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(withResult(PRODUCT));
        when(session.getSelectedProduct()).thenReturn(createSelectedProduct());

        ProductFeatures productFeatures = service.retrieveProductFeaturesByProductId(REFERENCE);

        assertThat(productFeatures,
                is(new ProductFeatures(
                        new Product(PRODUCT.getName(), PRODUCT.getIdentifier(), MNEMONIC, PDT_FAMILY_IDENTIFIER,
                                conditions(PRODUCT), PRODUCT.getExtProdIdentifiers()),
                        new DeviceID(DEVICE.getServer(), DEVICE.getOrg(), DEVICE.getId()))));
    }

    @Test
    public void shouldReturnProductFeaturesAndSetAlternateProductWhenRetrieveFeaturesByIdReturnVantageResponse()
            throws Exception {
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));

        ProductDTO productsMap = new ProductDTO("name", "id", MNEMONIC, PDT_FAMILY_IDENTIFIER,
                new HashMap<String, String>() {
                    {
                        put("n", "v");
                    }
                    {
                        put("Related Vantage product", "v");
                    }
                }, new HashMap<String, List<String>>() {
                    {
                        put("extn", Lists.newArrayList("extv"));
                    }

                }, null);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(withResult(productsMap));
        when(session.getSelectedProduct()).thenReturn(createSelectedProduct());

        ProductFeatures productFeatures = service.retrieveProductFeaturesByProductId(REFERENCE);

        assertTrue(productFeatures != null);
    }

    @Test
    public void shouldReturnProductFeaturesAndNotSetAlternateProductWhenRetrieveFeaturesByIdReturnResponse()
            throws Exception {
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(withResult(PRODUCT));
        when(session.getSelectedProduct()).thenReturn(null);

        ProductFeatures productFeatures = service.retrieveProductFeaturesByProductId(REFERENCE);

        assertThat(productFeatures,
                is(new ProductFeatures(
                        new Product(PRODUCT.getName(), PRODUCT.getIdentifier(), MNEMONIC, PDT_FAMILY_IDENTIFIER,
                                conditions(PRODUCT), PRODUCT.getExtProdIdentifiers()),
                        new DeviceID(DEVICE.getServer(), DEVICE.getOrg(), DEVICE.getId()))));
    }

    private List<ProductAttributes> conditions(ProductDTO product) {
        Map.Entry<String, String> con = product.getOptions().entrySet().iterator().next();
        return asList(new ProductAttributes(con.getKey(), con.getValue()));
    }

    private SelectedProduct createSelectedProduct() {
        ExternalProductIdentifier externalProductIdentifier = new ExternalProductIdentifier("CODE", "ID");
        ExternalProductIdentifier[] externalProductIdentifierArray = new ExternalProductIdentifier[1];
        externalProductIdentifierArray[0] = externalProductIdentifier;
        SelectedProduct selectedProduct = new SelectedProduct("URCA", "3002", "3002-PFID",
                externalProductIdentifierArray, "P_URCA", null,null);
        return selectedProduct;
    }

    @Test
    public void shouldReturnNullWhenReferenceDataDaoDoesNotMatchForReference() throws Exception {
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(null);
        assertThat(service.retrieveProductFeatures(REFERENCE), is(nullValue()));
    }

    @Test
    public void shouldReturnNullAndLogErrorForDeviceIdWhenDeviceServiceReturnsError() throws Exception {
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL))
                .thenReturn(DAOResponse.<DeviceDTO> withError(new DAOError("errorCode", "errorMessage")));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(withResult(PRODUCT));

        ProductFeatures productFeatures = service.retrieveProductFeatures(REFERENCE);

        assertThat(productFeatures.getDeviceID(), is(nullValue()));
    }

    @Test
    public void shouldServiceExceptionAndLogWithGalaxyErrorCodeWhenRetrieveFeaturesServiceReturnsError()
            throws Exception {
        when(errorResolver.customResolve("backendError", "backendMessage")).thenReturn(CUSTOM_IB_ERROR);
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL))
                .thenReturn(DAOResponse.<DeviceDTO> withError(new DAOError("errorCode", "errorMessage")));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class)))
                .thenReturn(DAOResponse.<ProductDTO> withError(new DAOError("backendError", "backendMessage")));

        try {
            service.retrieveProductFeatures(REFERENCE);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(CUSTOM_IB_ERROR));
        }

    }

    @Test
    public void shouldServiceExceptionAndLogWithGalaxyErrorCodeWhenRetrieveFeaturesServiceReturnsErrorWithoutMsg()
            throws Exception {
        when(errorResolver.customResolve("backendError", "backendMessage")).thenReturn(CUSTOM_IB_ERROR);
        when(errorResolver.resolve(any(String.class))).thenReturn(CUSTOM_IB_ERROR);
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL))
                .thenReturn(DAOResponse.<DeviceDTO> withError(new DAOError("errorCode", "errorMessage")));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class)))
                .thenReturn(DAOResponse.<ProductDTO> withError(new DAOError("backendError", null)));

        try {
            service.retrieveProductFeatures(REFERENCE);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(CUSTOM_IB_ERROR));
        }

    }

    @Test
    public void shouldServiceExceptionAndLogWithGalaxyErrorCodeWhenRetrieveFeaturesServiceThrowsException()
            throws Exception {
        when(errorResolver.customResolve("backendError", "backendMessage")).thenReturn(CUSTOM_IB_ERROR);
        when(errorResolver.resolve(any(String.class))).thenReturn(CUSTOM_IB_ERROR);
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL))
                .thenReturn(DAOResponse.<DeviceDTO> withError(new DAOError("errorCode", "errorMessage")));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenThrow(new Exception());

        try {
            service.retrieveProductFeatures(REFERENCE);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(CUSTOM_IB_ERROR));
        }

    }

    @Test
    public void shouldThrowServiceExceptionWhenChannelDaoReturnsWithError() throws Exception {
        when(errorResolver.resolve("channelError")).thenReturn(IB_ERROR);
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(withResult(PRODUCT));
        when(branding.getChannelBrand())
                .thenReturn(DAOResponse.<ChannelBrandDTO> withError(new DAOError("channelError", "message")));
        try {
            service.retrieveProductFeatures(REFERENCE);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(IB_ERROR));
        }
    }

    @Test
    public void testProductRetrievalByProductId() throws Exception {
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PARENT_PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(withResult(parentProduct()));

        ProductFeatures productFeatures = service.retrieveProductFeaturesUsingProductId(REFERENCE);
        assertEquals(productFeatures.getProduct().getIdentifier(), parentProduct.getIdentifier());
    }

    @Test
    public void shouldReturnProductFeaturesListByProductFamilyIdentifier()
            throws Exception {
       // when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));
        List<ProductDTO> productList = Lists.newArrayList(PRODUCT);
        when(retrieveFeatures.retrieveProduct(any(RetrieveProductDTO.class)))
                .thenReturn(withResult(productList));
      //  when(session.getSelectedProduct()).thenReturn(null);
        RetrieveProductDTO retrieveProductDTO = new RetrieveProductDTO("01005", "106100");
        List<ProductFeatures> productFeatures = service.retrieveProductFeaturesByProductFamilyIdentifier(retrieveProductDTO);
        assertThat(productFeatures.size(), is(1));
        assertThat(productFeatures.get(0),
                is(new ProductFeatures(
                        new Product(PRODUCT.getName(), PRODUCT.getIdentifier(), MNEMONIC, PDT_FAMILY_IDENTIFIER,
                                conditions(PRODUCT), PRODUCT.getExtProdIdentifiers()),
                        new DeviceID(DEVICE.getServer(), DEVICE.getOrg(), DEVICE.getId()))));
    }


    @Test
    public void testFailureInProductRetrievalByProductId() throws Exception {
        DAOResponse.DAOError error = new DAOResponse.DAOError("816001", "ErrorMessage");
        DAOResponse<ProductDTO> errorResponse = DAOResponse.withError(error);
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PARENT_PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(errorResponse);

        ProductFeatures productFeatures = service.retrieveProductFeaturesUsingProductId(REFERENCE);
        assertNull(productFeatures);
    }

    @Test(expected = ServiceException.class)
    public void testErrorInProductRetrievalByProductId() throws Exception {
        DAOResponse.DAOError error = new DAOResponse.DAOError("errorCode", "errorMessage");
        DAOResponse<ProductDTO> errorResponse = DAOResponse.withError(error);
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PARENT_PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(errorResponse);
        when(errorResolver.resolve(anyString())).thenReturn(new ResponseError());
        service.retrieveProductFeaturesUsingProductId(REFERENCE);
    }

    @Test
    public void shouldReturnProductFeaturesWhenRetrieveFeaturesByIdWithNoSessionUpdateReturnResponse()
            throws Exception {
        when(referenceData.getProductId(any(ProductReferenceDTO.class))).thenReturn(PRODUCT_ID);
        when(branding.getChannelBrand()).thenReturn(withResult(CHANNEL));
        when(device.getDevice(CHANNEL)).thenReturn(withResult(DEVICE));
        when(retrieveFeatures.retrieveProduct(any(ProductIdDTO.class))).thenReturn(withResult(PRODUCT));
        when(session.getSelectedProduct()).thenReturn(null);

        ProductFeatures productFeatures = service.retrieveProductFeaturesByProductIdNoSessionUpdate(REFERENCE);

        assertThat(productFeatures,
                is(new ProductFeatures(
                        new Product(PRODUCT.getName(), PRODUCT.getIdentifier(), MNEMONIC, PDT_FAMILY_IDENTIFIER,
                                conditions(PRODUCT), PRODUCT.getExtProdIdentifiers()),
                        new DeviceID(DEVICE.getServer(), DEVICE.getOrg(), DEVICE.getId()))));
    }

    private static ProductDTO parentProduct() {

        ProductDTO parent = new ProductDTO();
        parent.setName("FamilyHead");
        ProductOptions productOptions = new ProductOptions();
        productOptions.setOptionsType("MIPF");
        Map<String, String> externalProductIdentifierMap = new HashMap<String, String>();
        externalProductIdentifierMap.put("urca", "AccountHLX");
        List<ProductDTO> childProducts = new LinkedList<ProductDTO>();
        return parent;
    }
}