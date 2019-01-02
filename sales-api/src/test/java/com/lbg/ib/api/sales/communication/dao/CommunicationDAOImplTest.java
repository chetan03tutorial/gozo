package com.lbg.ib.api.sales.communication.dao;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants;
import com.lbg.ib.api.sales.communication.dto.CommunicationRequestDTO;
import com.lbg.ib.api.sales.communication.dto.CommunicationResponseDTO;
import com.lbg.ib.api.sales.communication.mapper.CommunicationRequestMapper;
import com.lbg.ib.api.sales.soapapis.communicationmanager.conditions.ID_CommunicationManagerRouter;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationRequest;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.rmi.RemoteException;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.GENERAL_EXCEPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by rabaja on 19/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommunicationDAOImplTest implements CommunicationConstants {
    @Mock
    CommunicationRequestMapper requestMapper;
    @Mock
    private LoggerDAO logger;
    @Mock
    private ID_CommunicationManagerRouter communictaionRouter;
    @Mock
    private ConfigurationDAO configDAO;
    @Mock
    private SessionManagementDAO session;
    @Mock
    protected DAOExceptionHandler exceptionHandler;

    @InjectMocks
    private CommunicationDAOImpl dao;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnDAOResponseWithResultForASuccessful()throws Exception{
        UserContext userContext = new UserContext("", "", "", "", "", "", "", "", "", "", "");
        when(session.getUserContext()).thenReturn(userContext);
        CommunicationRequestDTO requestDTO = new CommunicationRequestDTO();
        SendCommunicationRequest communicationRequest = new SendCommunicationRequest();
        SendCommunicationResponse communicationResponse = new SendCommunicationResponse();
        communicationResponse.setIsSuccessful(new Boolean("true"));
        when(requestMapper.mapSendCommunicationRequestAttributes(any(CommunicationRequestDTO.class), any(UserContext.class))).thenReturn(communicationRequest);
        when(communictaionRouter.sendCommunication(any(SendCommunicationRequest.class))).thenReturn(communicationResponse);
        DAOResponse<CommunicationResponseDTO> daoResponse = dao.sendEmailCommunictaion(requestDTO);
        assertTrue(daoResponse.getResult().getIsSuccessful());
    }

    @Test
    public void sendCommunicatiomWPSCallIsFailed()throws Exception {
        UserContext userContext = new UserContext("", "", "", "", "", "", "", "", "", "", "");
        when(session.getUserContext()).thenReturn(userContext);
        CommunicationRequestDTO requestDTO = new CommunicationRequestDTO();
        SendCommunicationRequest communicationRequest = new SendCommunicationRequest();
        SendCommunicationResponse communicationResponse = new SendCommunicationResponse();
        when(requestMapper.mapSendCommunicationRequestAttributes(any(CommunicationRequestDTO.class), any(UserContext.class))).thenReturn(communicationRequest);
        when(communictaionRouter.sendCommunication(any(SendCommunicationRequest.class))).thenReturn(communicationResponse);
        DAOResponse<CommunicationResponseDTO> daoResponse = dao.sendEmailCommunictaion(requestDTO);
        assertEquals(BUSSINESS_ERROR, daoResponse.getError().getErrorCode());
        assertEquals(BUSSINESS_ERROR_MESSAGE_813003, daoResponse.getError().getErrorMessage());
    }

    @Test
    public void shouldReturnDAOResponseWithException()throws Exception{
        UserContext userContext = new UserContext("", "", "", "", "", "", "", "", "", "", "");
        when(session.getUserContext()).thenReturn(userContext);
        CommunicationRequestDTO requestDTO = new CommunicationRequestDTO();
        SendCommunicationRequest communicationRequest = new SendCommunicationRequest();
        SendCommunicationResponse communicationResponse = new SendCommunicationResponse();
        communicationResponse.setIsSuccessful(new Boolean("true"));
        when(requestMapper.mapSendCommunicationRequestAttributes(any(CommunicationRequestDTO.class), any(UserContext.class))).thenReturn(communicationRequest);
        when(communictaionRouter.sendCommunication(any(SendCommunicationRequest.class))).thenThrow(Exception.class);
        DAOResponse.DAOError daoError = new DAOResponse.DAOError(GENERAL_EXCEPTION, "General Exception");
        when(exceptionHandler.handleException(any(RemoteException.class), any(Class.class), anyString(), Matchers.anyObject())).thenReturn(daoError);
        DAOResponse<CommunicationResponseDTO> daoResponse = dao.sendEmailCommunictaion(requestDTO);
        assertEquals(GENERAL_EXCEPTION, daoResponse.getError().getErrorCode());
    }

}
