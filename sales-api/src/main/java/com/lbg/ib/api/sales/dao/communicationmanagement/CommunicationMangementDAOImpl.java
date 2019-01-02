/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.communicationmanagement;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.CommunicationManagementRequestMapper;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementDTO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementResponseDTO;
import com.lbg.ib.api.sales.soapapis.communicationmanager.conditions.ID_CommunicationManagerRouter;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationRequest;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationResponse;

@Component
public class CommunicationMangementDAOImpl implements CommunicationMangementDAO {

    private static final Class<CommunicationMangementDAOImpl> CLASS_NAME  = CommunicationMangementDAOImpl.class;

    private static final String                               METHOD_NAME = "sendEmailCommunictaion";

    @Autowired
    private LoggerDAO                                         logger;

    @Autowired
    private CommunicationManagementRequestMapper              requestMapper;

    @Autowired
    private DAOExceptionHandler                               exceptionHandler;

    @Autowired
    private ID_CommunicationManagerRouter                     communictaionRouter;

    @TraceLog
    public DAOResponse<CommunicationManagementResponseDTO> sendEmailCommunictaion(
            CommunicationManagementDTO communicationManagemenatRequestDTO, UserContext userContext) {

        SendCommunicationRequest sendCommunicationRequest = requestMapper
                .mapSendCommunicationRequestAttributes(communicationManagemenatRequestDTO, userContext);

        try {
            SendCommunicationResponse sendCommunicationResponse = communictaionRouter
                    .sendCommunication(sendCommunicationRequest);
            DAOError error = validateResponse(sendCommunicationResponse);
            if (error == null) {
                return withResult(populateSendCommunicationResponse(sendCommunicationResponse));
            } else {
                return withError(error);
            }
        } catch (Exception ex) {
            DAOError daoError = exceptionHandler.handleException(ex, CLASS_NAME, METHOD_NAME,
                    communicationManagemenatRequestDTO);
            return withError(daoError);
        }
    }

    private DAOError validateResponse(SendCommunicationResponse response) {
        if (response != null && response.getIsSuccessful() == null) {
            DAOError error = new DAOError(BUSSINESS_ERROR, "Send Communicatiom WPS call is failed");
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
            return error;
        }
        return null;
    }

    private CommunicationManagementResponseDTO populateSendCommunicationResponse(
            SendCommunicationResponse sendCommunicationResponse) {
        CommunicationManagementResponseDTO communicationManagementResopnseDTO = new CommunicationManagementResponseDTO();
        communicationManagementResopnseDTO.setIsSuccessful(sendCommunicationResponse.getIsSuccessful());
        return communicationManagementResopnseDTO;
    }

}
