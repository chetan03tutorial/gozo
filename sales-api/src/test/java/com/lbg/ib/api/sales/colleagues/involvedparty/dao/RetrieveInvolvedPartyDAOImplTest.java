package com.lbg.ib.api.sales.colleagues.involvedparty.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.colleagues.involvedparty.dto.RetrieveInvolvedPartyResponseDTO;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.common.util.SOAPMCAHeaderUtility;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.infrastructure.soap.ResultCondition;
import com.lloydstsb.ea.lcsm.ErrorInfo;
import com.lloydstsb.ea.lcsm.ResponseHeader;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRole;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRoleType;
import com.lloydstsb.lcsm.involvedpartymanagement.InvolvedPartyManagement;
import com.lloydstsb.lcsm.involvedpartymanagement.RetrieveInvolvedPartyRoleDetailsRequest;
import com.lloydstsb.lcsm.involvedpartymanagement.RetrieveInvolvedPartyRoleDetailsResponse;

/**
 * Created by dbhatt on 7/22/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class RetrieveInvolvedPartyDAOImplTest {

    @InjectMocks
    RetrieveInvolvedPartyDAOImpl retrieveInvolvedPartyDAOImpl;

    @Mock
    SOAPMCAHeaderUtility         soapmcaHeaderUtility;

    @Mock
    private SessionManagementDAO session;

    @Mock
    LoggerDAO                    logger;

    @Mock
    UserContext                  userContext;
    @Mock
    InvolvedPartyManagement      involvedPartyManagement;
    @Mock
    DAOExceptionHandler          exceptionHandler;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testRetrieveRolesForInvolvedParty() throws Exception {

        RetrieveInvolvedPartyRoleDetailsRequest involvedPartyRoleDetailsRequest = new RetrieveInvolvedPartyRoleDetailsRequest();
        HeaderData headerData = new HeaderData();
        when(soapmcaHeaderUtility.prepareHeaderData("retrieveInvolvedPartyRoleDetails",
                "retrieveInvolvedPartyRoleDetails")).thenReturn(headerData);
        when(session.getUserContext()).thenReturn(userContext);
        ClientContext clientContext = new ClientContext();
        when(userContext.toClientContext()).thenReturn(clientContext);
        RetrieveInvolvedPartyRoleDetailsResponse involvedPartyRoleDetailsResponse = new RetrieveInvolvedPartyRoleDetailsResponse();
        InvolvedPartyRole roleDetails = new InvolvedPartyRole();
        InvolvedPartyRoleType type = new InvolvedPartyRoleType();
        type.setName("name");
        type.setValue("value");
        roleDetails.setType(type);
        involvedPartyRoleDetailsResponse.setRoleDetails(roleDetails);
        ResponseHeader resHeader = new ResponseHeader();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode(123456);
        resultCondition.setReasonText("12345");
        resHeader.setResultCondition(resultCondition);
        involvedPartyRoleDetailsResponse.setResponseHeader(resHeader);
        when(involvedPartyManagement.retrieveInvolvedPartyRoleDetails(involvedPartyRoleDetailsRequest))
                .thenReturn(involvedPartyRoleDetailsResponse);
        RetrieveInvolvedPartyDAOImpl dao = new RetrieveInvolvedPartyDAOImpl(session, soapmcaHeaderUtility,
                exceptionHandler, logger, involvedPartyManagement);
        RetrieveInvolvedPartyDAOImpl spyDao = Mockito.spy(dao);
        Mockito.doNothing().when(spyDao).addHeaderData(clientContext, headerData);
        Mockito.doReturn(null).when(spyDao).addHeaderDetails();
        DAOResponse<RetrieveInvolvedPartyResponseDTO> response = spyDao
                .retrieveRolesForInvolvedParty(involvedPartyRoleDetailsRequest);
        assertEquals(((RetrieveInvolvedPartyResponseDTO) response.getResult()).getInvolvedPartyRoleValue(), "value");
    }

    @Test
    public void testRetrieveRolesForInvolvedPartyWhenResultConditionIsNull() throws Exception {

        RetrieveInvolvedPartyRoleDetailsRequest involvedPartyRoleDetailsRequest = new RetrieveInvolvedPartyRoleDetailsRequest();
        HeaderData headerData = new HeaderData();
        when(soapmcaHeaderUtility.prepareHeaderData("retrieveInvolvedPartyRoleDetails",
                "retrieveInvolvedPartyRoleDetails")).thenReturn(headerData);
        when(session.getUserContext()).thenReturn(userContext);
        ClientContext clientContext = new ClientContext();
        when(userContext.toClientContext()).thenReturn(clientContext);
        RetrieveInvolvedPartyRoleDetailsResponse involvedPartyRoleDetailsResponse = new RetrieveInvolvedPartyRoleDetailsResponse();
        InvolvedPartyRole roleDetails = new InvolvedPartyRole();
        InvolvedPartyRoleType type = new InvolvedPartyRoleType();
        type.setName("name");
        type.setValue("value");
        roleDetails.setType(type);
        involvedPartyRoleDetailsResponse.setRoleDetails(roleDetails);
        ResponseHeader resHeader = new ResponseHeader();
        involvedPartyRoleDetailsResponse.setResponseHeader(resHeader);
        when(involvedPartyManagement.retrieveInvolvedPartyRoleDetails(involvedPartyRoleDetailsRequest))
                .thenReturn(involvedPartyRoleDetailsResponse);
        RetrieveInvolvedPartyDAOImpl dao = new RetrieveInvolvedPartyDAOImpl(session, soapmcaHeaderUtility,
                exceptionHandler, logger, involvedPartyManagement);
        RetrieveInvolvedPartyDAOImpl spyDao = Mockito.spy(dao);
        Mockito.doNothing().when(spyDao).addHeaderData(clientContext, headerData);
        Mockito.doReturn(null).when(spyDao).addHeaderDetails();
        DAOResponse<RetrieveInvolvedPartyResponseDTO> response = spyDao
                .retrieveRolesForInvolvedParty(involvedPartyRoleDetailsRequest);
        assertEquals(((RetrieveInvolvedPartyResponseDTO) response.getResult()).getInvolvedPartyRoleValue(), "value");
    }

    @Test
    public void testRetrieveRolesForInvolvedPartyForFailure() throws Exception {

        RetrieveInvolvedPartyRoleDetailsRequest involvedPartyRoleDetailsRequest = new RetrieveInvolvedPartyRoleDetailsRequest();
        HeaderData headerData = new HeaderData();
        when(soapmcaHeaderUtility.prepareHeaderData("retrieveInvolvedPartyRoleDetails",
                "retrieveInvolvedPartyRoleDetails")).thenReturn(headerData);
        when(session.getUserContext()).thenReturn(userContext);
        ClientContext clientContext = new ClientContext();
        when(userContext.toClientContext()).thenReturn(clientContext);
        ErrorInfo ex = new ErrorInfo("errorMessageType", "errorCode", "errorMessageText", "errorState");
        when(involvedPartyManagement.retrieveInvolvedPartyRoleDetails(involvedPartyRoleDetailsRequest)).thenThrow(ex);
        DAOError error = new DAOError("error", "errorMessage");
        when(exceptionHandler.handleException(Mockito.any(ErrorInfo.class), Mockito.any(Class.class),
                Mockito.anyString(), Mockito.any(RetrieveInvolvedPartyRoleDetailsRequest.class))).thenReturn(error);
        RetrieveInvolvedPartyDAOImpl dao = new RetrieveInvolvedPartyDAOImpl(session, soapmcaHeaderUtility,
                exceptionHandler, logger, involvedPartyManagement);
        RetrieveInvolvedPartyDAOImpl spyDao = Mockito.spy(dao);
        Mockito.doNothing().when(spyDao).addHeaderData(clientContext, headerData);
        Mockito.doReturn(null).when(spyDao).addHeaderDetails();
        DAOResponse<RetrieveInvolvedPartyResponseDTO> response = spyDao
                .retrieveRolesForInvolvedParty(involvedPartyRoleDetailsRequest);
        assertEquals(response.getError().getErrorCode(), "error");
    }

    @Test
    public void testRetrieveRolesForInvolvedPartyForInvalidResponse() throws Exception {

        RetrieveInvolvedPartyRoleDetailsRequest involvedPartyRoleDetailsRequest = new RetrieveInvolvedPartyRoleDetailsRequest();
        HeaderData headerData = new HeaderData();
        when(soapmcaHeaderUtility.prepareHeaderData("retrieveInvolvedPartyRoleDetails",
                "retrieveInvolvedPartyRoleDetails")).thenReturn(headerData);
        when(session.getUserContext()).thenReturn(userContext);
        ClientContext clientContext = new ClientContext();
        when(userContext.toClientContext()).thenReturn(clientContext);
        RetrieveInvolvedPartyRoleDetailsResponse involvedPartyRoleDetailsResponse = new RetrieveInvolvedPartyRoleDetailsResponse();
        InvolvedPartyRole roleDetails = new InvolvedPartyRole();
        involvedPartyRoleDetailsResponse.setRoleDetails(roleDetails);
        ResponseHeader resHeader = new ResponseHeader();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode(123456);
        resultCondition.setReasonText("12345");
        resHeader.setResultCondition(resultCondition);
        involvedPartyRoleDetailsResponse.setResponseHeader(resHeader);
        ErrorInfo ex = new ErrorInfo("errorMessageType", "errorCode", "errorMessageText", "errorState");
        when(involvedPartyManagement.retrieveInvolvedPartyRoleDetails(involvedPartyRoleDetailsRequest))
                .thenReturn(involvedPartyRoleDetailsResponse);
        DAOError error = new DAOError("error", "errorMessage");
        when(exceptionHandler.handleException(Mockito.any(ErrorInfo.class), Mockito.any(Class.class),
                Mockito.anyString(), Mockito.any(RetrieveInvolvedPartyRoleDetailsRequest.class))).thenReturn(error);
        RetrieveInvolvedPartyDAOImpl dao = new RetrieveInvolvedPartyDAOImpl(session, soapmcaHeaderUtility,
                exceptionHandler, logger, involvedPartyManagement);
        RetrieveInvolvedPartyDAOImpl spyDao = Mockito.spy(dao);
        Mockito.doNothing().when(spyDao).addHeaderData(clientContext, headerData);
        Mockito.doReturn(null).when(spyDao).addHeaderDetails();
        DAOResponse<RetrieveInvolvedPartyResponseDTO> response = spyDao
                .retrieveRolesForInvolvedParty(involvedPartyRoleDetailsRequest);
        assertEquals(response.getError().getErrorCode(), "error");
    }

    @Test
    public void testMapResponseToDTO() {
        RetrieveInvolvedPartyRoleDetailsResponse response = new RetrieveInvolvedPartyRoleDetailsResponse();

        com.lloydstsb.lcsm.involvedparty.InvolvedPartyRole involvedPartyRole = new com.lloydstsb.lcsm.involvedparty.InvolvedPartyRole();
        com.lloydstsb.lcsm.involvedparty.InvolvedPartyRoleType type = new com.lloydstsb.lcsm.involvedparty.InvolvedPartyRoleType();
        com.lloydstsb.ea.lcsm.ResponseHeader header = new com.lloydstsb.ea.lcsm.ResponseHeader();

        type.setValue("COLLEAGUE");
        type.setName("GG_RETAIL_BRANCH_SAVING_SELLER|GG_SDI_RemoteDesktop");
        involvedPartyRole.setType(type);
        response.setRoleDetails(involvedPartyRole);
        response.setResponseHeader(header);
        RetrieveInvolvedPartyResponseDTO retrieveInvolvedPartyResponseDTO = retrieveInvolvedPartyDAOImpl
                .mapResponseToDTO(response);
        assertNotNull(retrieveInvolvedPartyResponseDTO);
        assertTrue(retrieveInvolvedPartyResponseDTO.getRoles().size() == 2);
    }

}