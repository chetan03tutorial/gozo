/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package integration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.rpc.ServiceException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.communicationmanagement.CommunicationMangementDAOImpl;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.CommunicationManagementRequestMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementDTO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementResponseDTO;
import com.lbg.ib.api.sales.soapapis.communicationmanager.conditions.ID_CommunicationManagerRouter;
import com.lbg.ib.api.sales.soapapis.communicationmanager.conditions.ID_CommunicationManagerRouterExport1_ID_CommunicationManagerRouterHttpServiceLocator;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationRequest;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationResponse;

public class CommunicationManagementDAOImplIT {
    @InjectMocks
    private CommunicationMangementDAOImpl communicationMangementDAOImpl;

    @Mock
    private LoggerDAO                     logger;

    private ID_CommunicationManagerRouter service;

    @Mock
    private SendCommunicationRequest      sendCommunicationRequest;

    @Mock
    private SendCommunicationResponse     response;

    @Mock
    private DAOError                      error;

    @Mock
    private SessionManagementDAO          session;

    @Mock
    private GBOHeaderUtility              headerUtility;

    @Mock
    private UserContext                   context;

    BaseIT                                baseIT = new BaseIT();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        initialiseRequestMapper();
        initialiseSendCommunicationService();

    }

    private void initialiseSendCommunicationService() throws ServiceException, MalformedURLException {
        ID_CommunicationManagerRouterExport1_ID_CommunicationManagerRouterHttpServiceLocator locator = new ID_CommunicationManagerRouterExport1_ID_CommunicationManagerRouterHttpServiceLocator();
        service = locator.getID_CommunicationManagerRouterExport1_ID_CommunicationManagerRouterHttpPort(
                new URL("http://10.245.211.251:22910/wps/sales/v1.0/sendCommunication"));
        Whitebox.setInternalState(communicationMangementDAOImpl, "communictaionRouter", service);
    }

    private void initialiseRequestMapper() {
        CommunicationManagementRequestMapper requestMapper = new CommunicationManagementRequestMapper();
        Whitebox.setInternalState(communicationMangementDAOImpl, "requestMapper", requestMapper);
        Whitebox.setInternalState(requestMapper, "gboHeaderUtility", headerUtility);
        Whitebox.setInternalState(requestMapper, "session", session);

    }

    @Ignore
    @Test
    public void testSendCommunicationResponseReturnsTrue() throws Exception {

        when(headerUtility.prepareSoapHeader("sendCommunication", "SendCommunication"))
                .thenReturn(baseIT.populateRequestHeader());
        DAOResponse<CommunicationManagementResponseDTO> response = communicationMangementDAOImpl
                .sendEmailCommunictaion(communicationRequestDTO(), context);
        boolean successResponseFromSendCommunication = response.getResult().getIsSuccessful();
        assertThat(successResponseFromSendCommunication, is(true));

    }

    private CommunicationManagementDTO communicationRequestDTO() {

        CommunicationManagementDTO communicationRequest = new CommunicationManagementDTO();
        HashMap<String, String> tokenisedMap = new HashMap<String, String>();
        communicationRequest.setCommunicationType("Email");
        communicationRequest.setTemplateID("CAR_FINANCE_DD_REMINDER_EMAIL");
        communicationRequest.setContactPointID("GalaxyTestAccount02@LloydsTSB.co.uk");
        communicationRequest.setBrand("HLX");
        tokenisedMap.put("IB.Product.CC.AppRefNum", "59388");
        tokenisedMap.put("IB.Customer.Title", "MR");
        tokenisedMap.put("IB.Customer.LastName", "Joy");
        tokenisedMap.put("IB.Customer.FirstName", "Fedrik");
        communicationRequest.setTokenisedMap(tokenisedMap);
        return communicationRequest;
    }

}
