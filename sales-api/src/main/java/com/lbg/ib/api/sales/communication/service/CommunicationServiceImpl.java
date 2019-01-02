package com.lbg.ib.api.sales.communication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.communication.dao.CommunicationDAO;
import com.lbg.ib.api.sales.communication.dto.CommunicationRequestDTO;
import com.lbg.ib.api.sales.communication.dto.CommunicationResponseDTO;
import com.lbg.ib.api.sales.communication.dto.PartyCommunicationDetails;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;

/**
 * Created by rabaja on 16/11/2016.
 */
@Component
public class CommunicationServiceImpl implements CommunicationService, CommunicationConstants {
    @Autowired
    private LoggerDAO               logger;
    @Autowired
    private CommunicationDAO        communicationDAO;
    @Autowired
    private SessionManagementDAO    session;
    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @TraceLog
    public Boolean sendEmailCommunication(PartyCommunicationDetails communicationDetails) throws ServiceException {
        List<CommunicationRequestDTO> requestDTOs = populateCommunicationRequestDTO(communicationDetails);
        DAOResponse<CommunicationResponseDTO> responseDTO = null;
        if(requestDTOs!=null){
            for(CommunicationRequestDTO requestDTO:requestDTOs){
                responseDTO = communicationDAO.sendEmailCommunictaion(requestDTO);
            }
        }
        if(responseDTO.getError()!=null){
            return false;
        }
        return responseDTO.getResult().getIsSuccessful();
    }

    private List<CommunicationRequestDTO> populateCommunicationRequestDTO(
            PartyCommunicationDetails communicationDetails) throws ServiceException {
        List<CommunicationRequestDTO> communicationRequestDTOs = new ArrayList<CommunicationRequestDTO>();
        if (communicationDetails.getRecipientEmail() != null) {
            for (String email : communicationDetails.getRecipientEmail()) {
                CommunicationRequestDTO requestDTO = new CommunicationRequestDTO();
                requestDTO.setTemplateID(communicationDetails.getTemplateId());
                requestDTO.setCommunicationType(communicationDetails.getEmailCommunicationType());
                requestDTO.setContactPointID(email);
                requestDTO.setTokenisedMap(communicationDetails.getTokenMap());
                communicationRequestDTOs.add(requestDTO);
            }
        }

        return communicationRequestDTOs;

    }


}
