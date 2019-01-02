/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.cache;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.constants.CommonConstant.KEY_USER_CONTEXT;
import static com.lbg.ib.api.sales.dao.constants.CommonConstant.SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS;
import static com.lbg.ib.api.sales.dao.constants.CommonConstant.SESSION_KEY_FOR_PRODUCT_DETAILS;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import com.lbg.ib.api.sales.common.SpringContextHolder;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.communication.service.CommunicationPartyService;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.ApplicationSessionManagement;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementResponseDTO;
import com.lbg.ib.api.sales.dto.communicationparty.CommunicationPartyDetailsDTO;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationResponse;

public class SessionExpiryListnerTest {

    SessionExpiryListener                      sessionExpiryListener      = new SessionExpiryListener();

    HttpSession                                session                    = Mockito.mock(HttpSession.class);

    ApplicationContext                         applicationContext         = Mockito.mock(ApplicationContext.class);

    HttpSessionEvent                           httpSessionEvent           = Mockito.mock(HttpSessionEvent.class);

    CommunicationPartyService                  communicationpartyService  = Mockito
            .mock(CommunicationPartyService.class);

    LoggerDAO                                  mocklogger                 = Mockito.mock(LoggerDAO.class);;

    DAOResponse<SendCommunicationResponse>     sendCommunication;

    private static ExternalProductIdentifier[] externalProductIdentifiers = new ExternalProductIdentifier[] {
            new ExternalProductIdentifier("epic", "epii") };

    SelectedProduct                            selectedProduct            = new SelectedProduct("n", "PID",
            "pdtFamilyId", externalProductIdentifiers, "P_CLASSIC", null,null);

    UserContext                                userContext                = new UserContext("userId", "ipAddress",
            "sessionId", "partyId", "ocisId", "channelId", "chansecMode", "userAgent", "language", "inboxIdClient",
            "host");

    @Before
    public void setUp() throws Exception {
        SpringContextHolder springContextHolderMock = new SpringContextHolder();
        ApplicationContext appContext = Mockito.mock(ApplicationContext.class);
        springContextHolderMock.setApplicationContext(appContext);
        Mockito.when(appContext.getBean(CommunicationPartyService.class)).thenReturn(communicationpartyService);
        Mockito.when(SpringContextHolder.getBean(CommunicationPartyService.class))
                .thenReturn(communicationpartyService);
        Mockito.when(SpringContextHolder.getBean(LoggerDAO.class)).thenReturn(mocklogger);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testToVerifySendCommunicationCallIsMade() {
        Mockito.when(httpSessionEvent.getSession()).thenReturn(session);
        CommunicationPartyDetailsDTO partyDetails = new CommunicationPartyDetailsDTO();
        Mockito.when(session.getAttribute(
                ApplicationSessionManagement.buildNameSpaceKey(SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS)))
                .thenReturn(partyDetails);
        Mockito.when(
                session.getAttribute(ApplicationSessionManagement.buildNameSpaceKey(SESSION_KEY_FOR_PRODUCT_DETAILS)))
                .thenReturn(selectedProduct);
        Mockito.when(session.getAttribute(ApplicationSessionManagement.buildNameSpaceKey(KEY_USER_CONTEXT)))
                .thenReturn(userContext);
        sessionExpiryListener.sessionDestroyed(httpSessionEvent);
        Mockito.verify(communicationpartyService).sendEmailCommunictaion(partyDetails, selectedProduct.getMnemonic(),
                userContext);

    }

    @Test
    public void testToVerifySendCommunicationCallIsNotMadeIfPartyDetailsIsNull() {
        Mockito.when(httpSessionEvent.getSession()).thenReturn(session);
        CommunicationPartyDetailsDTO partyDetails = null;
        Mockito.when(session.getAttribute(
                ApplicationSessionManagement.buildNameSpaceKey(SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS)))
                .thenReturn(partyDetails);
        Mockito.when(
                session.getAttribute(ApplicationSessionManagement.buildNameSpaceKey(SESSION_KEY_FOR_PRODUCT_DETAILS)))
                .thenReturn(selectedProduct);
        sessionExpiryListener.sessionDestroyed(httpSessionEvent);
        Mockito.verify(mocklogger).logError(
                "1100017", "The session attribute values are null " + "partyDetails is null : " + false
                        + "product is null : " + true + "userContext is null : " + (false),
                SessionExpiryListener.class);

    }

    @Test
    public void testToVerifySessionIsNull() {

        Mockito.when(httpSessionEvent.getSession()).thenReturn(null);
        sessionExpiryListener.sessionDestroyed(httpSessionEvent);
        Mockito.verify(mocklogger).logError("1100017", "Session is not present", SessionExpiryListener.class);

    }

    DAOResponse<ChannelBrandDTO> channelBrandValues() {

        return withResult(new ChannelBrandDTO("IBL", "LLOYDS", "IBL"));
    }

    DAOResponse<CommunicationManagementResponseDTO> sendCommunicationValuesWithSuccessResponseTrue() {

        CommunicationManagementResponseDTO sendCommunicationResponse = new CommunicationManagementResponseDTO();
        sendCommunicationResponse.setIsSuccessful(true);
        return withResult(sendCommunicationResponse);
    }

    DAOResponse<CommunicationManagementResponseDTO> sendCommunicationValuesWithSuccessResponseFalse() {

        CommunicationManagementResponseDTO sendCommunicationResponse = new CommunicationManagementResponseDTO();
        sendCommunicationResponse.setIsSuccessful(false);
        return withResult(sendCommunicationResponse);

    }

}
