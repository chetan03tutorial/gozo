package com.lbg.ib.api.sales.communication.dao;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.communication.dto.CommunicationRequestDTO;
import com.lbg.ib.api.sales.communication.dto.CommunicationResponseDTO;
import com.lbg.ib.api.sales.communication.mapper.CommunicationRequestMapper;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.communicationmanager.conditions.ID_CommunicationManagerRouter;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationRequest;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationResponse;

/**
 * Created by rabaja on 16/11/2016.
 */
@Component
public class CommunicationDAOImpl implements CommunicationDAO,Constants.CommunicationConstants {


    private static final String METHOD_NAME = "sendEmailCommunictaion";


    @Autowired
    CommunicationRequestMapper requestMapper;
    @Autowired
    private ConfigurationDAO configDAO;
    @Autowired
    private SessionManagementDAO session;
    @Autowired
    private LoggerDAO logger;
    @Autowired
    private ID_CommunicationManagerRouter communictaionRouter;
    @Autowired
    protected DAOExceptionHandler exceptionHandler;

    @TraceLog
    public DAOResponse<CommunicationResponseDTO> sendEmailCommunictaion(CommunicationRequestDTO sendCommunication) {
        CommunicationResponseDTO responseDTO = new CommunicationResponseDTO();
        UserContext userContext = session.getUserContext();
        sendCommunication.setBrand(configDAO.getConfigurationStringValue(BRAND, userContext.getChannelId()));
        SendCommunicationRequest communicationRequest = requestMapper.mapSendCommunicationRequestAttributes(sendCommunication, userContext);
        try {
            SendCommunicationResponse communicationResponse = communictaionRouter.sendCommunication(communicationRequest);
            if ((communicationResponse != null) && (communicationResponse.getIsSuccessful() == null)) {
                return DAOResponse.withError(new DAOResponse.DAOError(BUSSINESS_ERROR, BUSSINESS_ERROR_MESSAGE_813003));
            }
            responseDTO.setIsSuccessful(communicationResponse.getIsSuccessful());
        } catch (Exception ex){
            DAOResponse.DAOError daoError = exceptionHandler.handleException(ex, this.getClass(), METHOD_NAME, communicationRequest);
            return withError(daoError);
        }
        return withResult(responseDTO);
    }
}
