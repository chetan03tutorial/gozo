/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.asm.service.B274Service;
import com.lbg.ib.api.sales.common.auditing.FraudAuditor;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sales.dao.product.activate.ActivateProductDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dao.switches.SwitchesManagementDAO;
import com.lbg.ib.api.sales.dto.b274.B274RequestDTO;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductResponseDTO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.activate.AccountSwitching;
import com.lbg.ib.api.sales.product.domain.activate.Activated;
import com.lbg.ib.api.sales.product.domain.activate.Activation;
import com.lbg.ib.api.sales.product.domain.activate.ArrangementId;
import com.lbg.ib.api.sales.product.domain.activate.InvolvedPartyRole;
import com.lbg.ib.api.sales.product.domain.activate.Location;
import com.lbg.ib.api.sales.product.domain.arrangement.AccountDetails;
import com.lbg.ib.api.sales.product.domain.arrangement.Overdraft;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.UserContext;

@RunWith(MockitoJUnitRunner.class)
public class ActivateProductServiceImplTest {

    @InjectMocks
    private ActivateProductServiceImpl    activateProductService;

    @Mock
    private ActivateProductDAO            dao;

    @Mock
    private ReferenceDataServiceDAO       referenceDataDAO;

    @Mock
    private GalaxyErrorCodeResolver       resolver;

    @Mock
    private LoggerDAO                     logger;

    @Mock
    private SessionManagementDAO          session;

    @Mock
    private FraudAuditor                  fraudAuditor;

    @Mock
    private ChannelBrandingDAO            channelBrandingService;

    @Mock
    private DAOResponse<ChannelBrandDTO>  channelBrandDTOResponse;

    @Mock
    private ChannelBrandDTO               channelBrandDTO;
    
    @Mock
    private B274Service b274Service;

    @Mock
    private SwitchesManagementDAO         switchesDAO;
    private static Overdraft              overdraftFeatures       = new Overdraft();
    private static final AccountSwitching accountSwitchingDetails = new AccountSwitching();

    private static final Activation       ACTIVATION              = new Activation(
            new InvolvedPartyRole("userName", "pass"), new Condition[] { new Condition("cname", 1, "vname") },
            new Location("10", "20"), overdraftFeatures, accountSwitchingDetails, false, "productIdentifier", null,
            null, null, null);

    private static final Activation       DOWNSELL_ACTIVATION     = new Activation(
            new InvolvedPartyRole("userName", "pass"), new Condition[] { new Condition("cname", 1, "vname") },
            new Location("10", "20"), overdraftFeatures, accountSwitchingDetails, false, "altProductIdentifier", null,
            null, null, null);

    private static final SelectedProduct  SELECTED_PRODUCT        = new SelectedProduct("productName",
            "productIdentifier", null, null, "P_CLASSIC", null,null);

    private static final SelectedProduct  ALTERNATE_PRODUCT       = new SelectedProduct("altProductName",
            "altProductIdentifier", null, null, "P_CLASSIC", null,null);

    private static final ResponseError    CUSTOM_IB_ERROR         = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Customer Msg");

    private ArrangeToActivateParameters   PARAMS;
    private static final ResponseError    RESPONSE_ERROR          = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable");
    private static final ArrangementId    ARRANGEMNET_ID          = new ArrangementId("87546");
    private UserContext                   userContext             = new UserContext("userId", "ipAddress", "sessionId",
            "partyId", "ocisId", "channelId", "chansecMode", "userAgent", "language", "inboxIdClient", "host");

    @Before
    public void setUp() {
        when(session.getSelectedProduct()).thenReturn(SELECTED_PRODUCT);
        when(session.getUserContext()).thenReturn(userContext);
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        when(channelBrandDTOResponse.getResult()).thenReturn(channelBrandDTO);
        when(channelBrandDTO.getChannelId()).thenReturn("IBL");
        when(channelBrandingService.getChannelBrand()).thenReturn(channelBrandDTOResponse);
        PARAMS = new ArrangeToActivateParameters();
        PARAMS.setArrangementType("arrangementType");
        PARAMS.setApplicationType("applicationType");
        PARAMS.setOcisId("ocis");
        PARAMS.setPartyId("party");
    }

    @Test
    public void shouldDownsellTheProduct() throws Exception {
        SELECTED_PRODUCT.addAlternateProduct(ALTERNATE_PRODUCT);
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(withResult(expectedActiveDownsellProductResponseDto()));
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        when(session.getSelectedProduct()).thenReturn(SELECTED_PRODUCT);
        when(session.getCustomerDetails()).thenReturn(new CustomerInfo());

        Activated activated = activateProductService.activateProduct(ARRANGEMNET_ID, DOWNSELL_ACTIVATION);

        assertThat(activated, is(new Activated("dsProductName", "dsMnemonic", "appStatus", "appSubStatus", "aid",
                new AccountDetails("sortCode", "accountNo"), null, "1234567890", null)));
        // Mockito.verify(session).clearSessionAttributeForPipelineChasing();
    }

    @Test
    public void shouldDownsellTheProductWithBranchContext() throws Exception {
        SELECTED_PRODUCT.addAlternateProduct(ALTERNATE_PRODUCT);
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(withResult(expectedActiveDownsellProductResponseDto()));
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        when(session.getSelectedProduct()).thenReturn(SELECTED_PRODUCT);
        when(session.getCustomerDetails()).thenReturn(new CustomerInfo());
        when(session.getBranchContext()).thenReturn(new BranchContext("", "", "", ""));

        Activated activated = activateProductService.activateProduct(ARRANGEMNET_ID, DOWNSELL_ACTIVATION);

        assertThat(activated, is(new Activated("dsProductName", "dsMnemonic", "appStatus", "appSubStatus", "aid",
                new AccountDetails("sortCode", "accountNo"), null, "1234567890", null)));
        // Mockito.verify(session).clearSessionAttributeForPipelineChasing();
    }

    @Test
    public void shouldDownsellTheProductOldVantage() throws Exception {
        SELECTED_PRODUCT.addAlternateProduct(ALTERNATE_PRODUCT);
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(withResult(expectedActiveDownsellProductResponseDto()));

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementType("arrangementType");
        arrangeToActivateParameters.setApplicationType("applicationType");
        arrangeToActivateParameters.setOcisId("ocis");
        arrangeToActivateParameters.setPartyId("party");
        arrangeToActivateParameters.setAlternateVantagePrdIdentifier("dummyIdentifier");

        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(session.getSelectedProduct()).thenReturn(SELECTED_PRODUCT);

        Activated activated = activateProductService.activateProduct(ARRANGEMNET_ID, DOWNSELL_ACTIVATION);

        assertThat(activated, is(new Activated("dsProductName", "dsMnemonic", "appStatus", "appSubStatus", "aid",
                new AccountDetails("sortCode", "accountNo"), null, "1234567890", null)));
        // Mockito.verify(session).clearSessionAttributeForPipelineChasing();
    }

    @Test(expected = ServiceException.class)
    public void shouldNotDownsellTheProductWhenAlternateProductIsNotSet() throws Exception {
        // SELECTED_PRODUCT.addAlternateProduct(ALTERNATE_PRODUCT);
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(withResult(expectedActiveDownsellProductResponseDto()));
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        when(session.getSelectedProduct()).thenReturn(SELECTED_PRODUCT);

        Activated activated = activateProductService.activateProduct(ARRANGEMNET_ID, DOWNSELL_ACTIVATION);

        assertThat(activated, is(new Activated("dsProductName", "dsMnemonic", "appStatus", "appSubStatus", "aid",
                new AccountDetails("sortCode", "accountNo"), null, null, null)));
        // Mockito.verify(session).clearSessionAttributeForPipelineChasing();
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionWhenNoProductIsSetInSession() throws Exception {
        when(session.getSelectedProduct()).thenReturn(null);
        // when(SELECTED_PRODUCT.getAlternateProduct(any(String.class))).thenReturn(null);
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(withResult(expectedActiveDownsellProductResponseDto()));
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);
    }

    @Test
    public void shouldReturnServiceResponseWhenDaoReturnsWithResult() throws Exception {
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(withResult(expectedActiveProductResponseDto()));
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        Activated activated = activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);

        assertThat(activated, is(new Activated("productName", "mnemonic", "appStatus", "appSubStatus", "aid",
                new AccountDetails("sortCode", "accountNo"), null, "1234567890", null)));
        // Mockito.verify(session).clearSessionAttributeForPipelineChasing();
    }

    @Test
    public void testToVerfiySessionAttributeIsClearedForSucessResponse() throws Exception {
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(withResult(expectedActiveProductResponseDto()));
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);
        // Mockito.verify(session).clearSessionAttributeForPipelineChasing();
    }

    @Test
    public void shouldThrowServiceExceptionWhenDaoReturnsWithError() throws Exception {
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(DAOResponse.<ActivateProductResponseDTO> withError(new DAOError("code", "message")));
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        when(session.getCustomerDetails()).thenReturn(new CustomerInfo());
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        when(resolver.customResolve("code", "message")).thenReturn(CUSTOM_IB_ERROR);
        try {
            activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(CUSTOM_IB_ERROR));
        }
    }

    @Test
    public void shouldThrowServiceExceptionWhenDaoReturnsWithErrorWithNullMsg() throws Exception {
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(DAOResponse.<ActivateProductResponseDTO> withError(new DAOError("code", null)));
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        when(resolver.customResolve("code", "message")).thenReturn(CUSTOM_IB_ERROR);
        try {
            activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(RESPONSE_ERROR));
        }
    }

    @Test
    public void shouldLogUnHandledExceptionAndThrowAsServiceExceptionThrownByDao() throws Exception {
        RuntimeException EXCEPTION = new RuntimeException();
        when(dao.activateProduct(any(ActivateProductDTO.class))).thenThrow(EXCEPTION);
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        try {
            activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(resolver.resolve(ResponseErrorConstants.SERVICE_EXCEPTION)));
            verify(logger).logException(ActivateProductServiceImpl.class, EXCEPTION);
        }
    }

    private ActivateProductResponseDTO expectedActiveProductResponseDto() {
        return new ActivateProductResponseDTO("productName", "mnemonic", "CC", "aid", "appStatus", "appSubStatus",
                "accountNo", "sortCode", "1234567890", null, null, null);
    }

    private ActivateProductResponseDTO expectedActiveProductResponseDtoForCrossSell() {
        return new ActivateProductResponseDTO("productName", "mnemonic", "CC", "aid", "1010", "appSubStatus",
                "accountNo", "sortCode", "1234567890", null, null, null);
    }

    private ActivateProductResponseDTO expectedActiveDownsellProductResponseDto() {
        return new ActivateProductResponseDTO("dsProductName", "dsMnemonic", "CC", "aid", "appStatus", "appSubStatus",
                "accountNo", "sortCode", "1234567890", null, null, null);
    }

    @Test
    public void shouldReturnServiceResponseWithCrossSellProductName() throws Exception {
        TreeMap<String, String> crossSellMnemonicMap = new TreeMap<String, String>();
        crossSellMnemonicMap.put("1", "P_EXC_SVR");
        crossSellMnemonicMap.put("2", "P_CREDIT_CARD");

        PARAMS.setCrossSellMnemonicMap(crossSellMnemonicMap);

        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);
        when(dao.activateProduct(any(ActivateProductDTO.class)))
                .thenReturn(withResult(expectedActiveProductResponseDtoForCrossSell()));
        when(referenceDataDAO.getProductName(crossSellMnemonicMap.get("1"))).thenReturn("clubexclusivesaver");
        Activated activated = activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);

        assertThat(activated, is(new Activated("productName", "mnemonic", "1010", "appSubStatus", "aid",
                new AccountDetails("sortCode", "accountNo"), "clubexclusivesaver", "1234567890", null)));
    }

    @Test
    public void testSetSIRAWorkflowName() {

        when(channelBrandDTOResponse.getResult()).thenReturn(channelBrandDTO);
        when(channelBrandDTO.getChannelId()).thenReturn("IBL");
        when(channelBrandingService.getChannelBrand()).thenReturn(channelBrandDTOResponse);

        ActivateProductDTO offerProductDTO = new ActivateProductDTO();
        activateProductService.setChannelBrandingService(channelBrandingService);
        activateProductService.setSIRAWorkflowName(offerProductDTO);
    }

    @Test
    public void tetCheckErrorFailureCase() {
        List<ConditionDTO> conditionList = new ArrayList<ConditionDTO>();
        ConditionDTO conditionDTO = new ConditionDTO("code", null, null);
        conditionList.add(conditionDTO);
        ActivateProductResponseDTO result = new ActivateProductResponseDTO();
        result.setConditionDTO(conditionList);

        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        assertTrue(!activateProductService.checkErrorFailureCase(result).isEmpty());
    }

    @Test
    public void shouldLogUnHandledExceptionAndThrowAsServiceExceptionThrownByDaoWithProductIdNull() throws Exception {
        RuntimeException EXCEPTION = new RuntimeException();
        when(dao.activateProduct(any(ActivateProductDTO.class))).thenThrow(EXCEPTION);
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);

        Activation ACTIVATION = new Activation(new InvolvedPartyRole("userName", "pass"),
                new Condition[] { new Condition("cname", 1, "vname") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, null, null, null, null, null);

        try {
            ArrangementId ARRANGEMNET_ID = new ArrangementId(null);
            activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(resolver.resolve(ResponseErrorConstants.SERVICE_EXCEPTION)));
            verify(logger).logException(ActivateProductServiceImpl.class, EXCEPTION);
        }
    }

    @Test
    public void shouldLogUnHandledExceptionAndThrowAsServiceExceptionThrownByDaoWithProductIdNullVantageOpted()
            throws Exception {
        RuntimeException EXCEPTION = new RuntimeException();
        when(dao.activateProduct(any(ActivateProductDTO.class))).thenThrow(EXCEPTION);
        PARAMS.setVantagePrdIdentifier("DUMMY_VANTAGE_IDENTIFIER");
        when(session.getArrangeToActivateParameters()).thenReturn(PARAMS);

        Activation ACTIVATION = new Activation(new InvolvedPartyRole("userName", "pass"),
                new Condition[] { new Condition("cname", 1, "vname") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, true, null, null, null, null, null);

        try {
            ArrangementId ARRANGEMNET_ID = new ArrangementId(null);
            activateProductService.activateProduct(ARRANGEMNET_ID, ACTIVATION);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(resolver.resolve(ResponseErrorConstants.SERVICE_EXCEPTION)));
            verify(logger).logException(ActivateProductServiceImpl.class, EXCEPTION);
        }
    }
    
    @Test
    public void shouldDownsellTheProductWithBranchContextWithOverdraft() throws Exception {
        when(dao.activateProduct(any(ActivateProductDTO.class))).thenReturn(withResult(expectedActiveDownsellProductResponseDto()));
        when(session.getArrangeToActivateParameters()).thenReturn(new ArrangeToActivateParameters());
        when(session.getSelectedProduct()).thenReturn(new SelectedProduct("productName","productIdentifier", null, null, "P_CLASSIC", null,null));
        when(session.getCustomerDetails()).thenReturn(new CustomerInfo());
        when(session.getBranchContext()).thenReturn(new BranchContext("", "", "", ""));
        
        Overdraft  overdraftFeatures       = new Overdraft();
        BigDecimal amountOpted= new BigDecimal("1234");
        overdraftFeatures.setAmount(amountOpted);
        
        Activation OERDRAFT_MODIFIED     = new Activation();
        
        B274RequestDTO b274RequestDTO=new B274RequestDTO();
        
        when(b274Service.mapB274Request(amountOpted)).thenReturn(b274RequestDTO);
        Activated activatedObj = activateProductService.activateProduct( new ArrangementId("87546"), OERDRAFT_MODIFIED);

        assertNotNull(activatedObj);
    }
    
   
    
    
}