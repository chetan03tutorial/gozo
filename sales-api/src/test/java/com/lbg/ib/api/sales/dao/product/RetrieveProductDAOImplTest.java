package com.lbg.ib.api.sales.dao.product;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.product.retrieve.RetrieveProductDAOImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.ProductDTO;
import com.lbg.ib.api.shared.domain.ProductIdDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdFamilyIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductFamily;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.conditions.IA_RetrieveProductConditions;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsRequest;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

@RunWith(MockitoJUnitRunner.class)
public class RetrieveProductDAOImplTest {

    @InjectMocks
    private RetrieveProductDAOImpl       retrieveProductDAOImpl;

    @Mock
    private DAOExceptionHandler          daoExceptionHandler;

    @Mock
    private SessionManagementDAO         session;

    @Mock
    private UserContext                  context;

    @Mock
    private GBOHeaderUtility             headerUtility;

    @Mock
    private ConfigurationDAO             configuration;

    @Mock
    private IA_RetrieveProductConditions service;

    private static final SOAPHeader      LLOYDS_SOAP_HEADER    = new SOAPHeader();
    private static final String          PRODUCT_ID            = "20198";
    private final ProductIdDTO           requestDTO            = new ProductIdDTO(PRODUCT_ID);
    private static final String          PRODUCT_NAME          = "Cash Account";
    private static final String          MNEMONIC              = "P_CASH";
    private static final String          PDT_FAMILY_IDENTIFIER = "503";

    private Map<String, String>          OPTIONS               = new HashMap<String, String>() {
                                                                   {
                                                                       put("opName", "opValue");
                                                                   }
                                                               };
    private Map<String, List<String>>          EXT_IDS               = new HashMap<String, List<String>>() {
                                                                   {
                                                                       put("extCode", Lists.newArrayList("extId"));
                                                                       put("00010", Lists.newArrayList("P_CASH"));
                                                                   }
                                                               };

    @Before
    public void setup() {
        when(context.getChannelId()).thenReturn("dummy_channel_id");
        when(session.getUserContext()).thenReturn(context);
        when(headerUtility.prepareSoapHeader("retrieveProductConditions", "RetrieveProductConditions"))
                .thenReturn(asList(LLOYDS_SOAP_HEADER));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dummy_channel_id", "dummy_channel");
        when(configuration.getConfigurationItems("ChannelIdMapping")).thenReturn(map);
    }

    @Test
    public void shouldRetrieveProductFeaturesWhenTheBackendReturnsWithFeatures() throws Exception {
        when(service.retrieveProductConditions(any(RetrieveProductConditionsRequest.class))).thenReturn(testResponse());
        requestDTO.setCbsProductId("dummyId");
        DAOResponse<ProductDTO> pid = retrieveProductDAOImpl.retrieveProduct(requestDTO);

        assertThat(pid.getResult(),
                is(new ProductDTO(PRODUCT_NAME, PRODUCT_ID, MNEMONIC, PDT_FAMILY_IDENTIFIER, OPTIONS, EXT_IDS, null)));
    }

    @Test
    public void shouldRetrieveProductFeaturesByProductFamilyIdentifier() throws Exception {
        when(service.retrieveProductConditions(any(RetrieveProductConditionsRequest.class))).thenReturn(testResponse());
       // requestDTO.setCbsProductId("dummyId");
        RetrieveProductDTO retrieveProductDTO = new RetrieveProductDTO("01005", "106116");
        DAOResponse<List<ProductDTO>> pid = retrieveProductDAOImpl.retrieveProduct(retrieveProductDTO);
        assertNotNull(pid);
        assertThat(pid.getResult().size(), is(1));
        assertThat(pid.getResult().get(0),
                is(new ProductDTO(PRODUCT_NAME, PRODUCT_ID, MNEMONIC, PDT_FAMILY_IDENTIFIER, OPTIONS, EXT_IDS, null)));
    }

    @Test
    public void shouldReturnErrorFromExceptionHandlerWhenServiceThrowsError() throws Exception {

        ExternalBusinessError expectedException = new ExternalBusinessError(null, null, "code", "message");
        when(service.retrieveProductConditions(any(RetrieveProductConditionsRequest.class)))
                .thenThrow(expectedException);

        DAOResponse.DAOError expectedDaoError = new DAOResponse.DAOError("code", "message");
        when(daoExceptionHandler.handleException(expectedException, RetrieveProductDAOImpl.class, "retrieveProduct",
                requestDTO)).thenReturn(expectedDaoError);

        DAOResponse<ProductDTO> response = retrieveProductDAOImpl.retrieveProduct(requestDTO);

        assertThat(response.getError(), is(expectedDaoError));
    }

    private RetrieveProductConditionsResponse testResponse() {
        RetrieveProductConditionsResponse response = new RetrieveProductConditionsResponse();
        Product product = new Product();
        product.setProductIdentifier(PRODUCT_ID);
        product.setProductName(PRODUCT_NAME);
        product.setProductoptions(testOptions());
        product.setExternalSystemProductIdentifier(testExtSysIds());
        product.setAssociatedFamily(testFamily());
        response.setProduct(new Product[] { product });
        return response;
    }

    private ProductFamily[] testFamily() {
        ProductFamily productFamily = new ProductFamily();
        ExtSysProdFamilyIdentifier extSysProdFamilyIdentifier = new ExtSysProdFamilyIdentifier();
        extSysProdFamilyIdentifier.setProductFamilyIdentifier("503");
        productFamily.setExtsysprodfamilyidentifier(new ExtSysProdFamilyIdentifier[] { extSysProdFamilyIdentifier });
        return new ProductFamily[] { productFamily };
    }

    private ExtSysProdIdentifier[] testExtSysIds() {
        ExtSysProdIdentifier extSysId = new ExtSysProdIdentifier();
        extSysId.setSystemCode("extCode");
        extSysId.setProductIdentifier("extId");
        ExtSysProdIdentifier mnemonic = new ExtSysProdIdentifier();
        mnemonic.setSystemCode("00010");
        mnemonic.setProductIdentifier("P_CASH");
        return new ExtSysProdIdentifier[] { extSysId, mnemonic };
    }

    private ProductOptions[] testOptions() {
        ProductOptions options = new ProductOptions();
        options.setOptionsName("opName");
        options.setOptionsValue("opValue");
        return new ProductOptions[] { options };
    }

}