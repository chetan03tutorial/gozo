/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.communication.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.cache.SessionExpiryListener;
import com.lbg.ib.api.sales.communication.domain.InvolvedPartyDetails;
import com.lbg.ib.api.sales.dao.communicationmanagement.CommunicationMangementDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementDTO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementResponseDTO;
import com.lbg.ib.api.sales.dto.communicationparty.CommunicationPartyDetailsDTO;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.user.UserContext;

@Component
public class CommunicationPartyServiceImpl implements CommunicationPartyService {

    private static final String  KEY_FOR_TITLE      = "IB.Customer.Title";

    private static final String  KEY_FOR_FIRST_NAME = "IB.Customer.FirstName";

    private static final String  KEY_FOR_LAST_NAME  = "IB.Customer.LastName";

    private static final String  KEY_FOR_POST_CODE  = "IB.Customer.Addr.PostCode";

    private static final String  TEMPLATE_ID        = "EM_PCA_Pipeline_chasing";

    private static final String  KEY_FOR_MNEMONIC   = "IB.Product.Mnemonic";

    private static final String  BRAND              = "BRAND_NAME_MAPPING";

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    CommunicationMangementDAO    communicationManagementDAO;

    @Autowired
    ConfigurationDAO             configDAO;

    @Autowired
    LoggerDAO                    logger;

    @TraceLog
    public void savePartyDetailsForSendCommunication(InvolvedPartyDetails partyDetails) {
        CommunicationPartyDetailsDTO communicationPartyDetails = mapCommunicationPartyDetails(partyDetails);
        session.setSendCommunicationPartyDetails(communicationPartyDetails);
    }

    /*
     * As Per Architecture Design the domain objects should be used beyond the
     * service layer hence changing it into a new DTO
     */
    private CommunicationPartyDetailsDTO mapCommunicationPartyDetails(InvolvedPartyDetails partyDetails) {

        CommunicationPartyDetailsDTO communicationPartyDetails = new CommunicationPartyDetailsDTO();
        communicationPartyDetails.setEmail(partyDetails.getEmail());
        communicationPartyDetails.setFirstName(partyDetails.getFirstName());
        communicationPartyDetails.setLastName(partyDetails.getLastName());
        communicationPartyDetails.setTitle(partyDetails.getTitle());
        communicationPartyDetails.setPostCode(partyDetails.getPostCode());

        return communicationPartyDetails;
    }

    @TraceLog
    public void sendEmailCommunictaion(CommunicationPartyDetailsDTO partyDetails, String mnemonic,
            UserContext userContext) {
        CommunicationManagementDTO communicationManagementRequestDTO = mapSendCommunicationRequestDTO(partyDetails,
                mnemonic, userContext);
        DAOResponse<CommunicationManagementResponseDTO> sendCommunicationResponse = communicationManagementDAO
                .sendEmailCommunictaion(communicationManagementRequestDTO, userContext);
        if (sendCommunicationResponse != null && sendCommunicationResponse.getResult() != null) {
            logger.logDebug(CommunicationPartyServiceImpl.class,
                    "SendCommunication API call is completed sucessfully and returned:",
                    sendCommunicationResponse.getResult().getIsSuccessful());
        } else {
            logger.logDebug(SessionExpiryListener.class, "Party details or product is not present in session",
                    sendCommunicationResponse);
        }
    }

    private CommunicationManagementDTO mapSendCommunicationRequestDTO(CommunicationPartyDetailsDTO partyDetails,
            String mnemonic, UserContext userContext) {
        CommunicationManagementDTO requestDTO = new CommunicationManagementDTO();
        HashMap<String, String> tokenisedMap = populateTokenisedMap(partyDetails, mnemonic);
        requestDTO.setTemplateID(TEMPLATE_ID);
        requestDTO.setContactPointID(partyDetails.getEmail());
        String brandName = mapBrandFromConfig(userContext);
        requestDTO.setBrand(brandName);
        requestDTO.setTokenisedMap(tokenisedMap);
        return requestDTO;
    }

    private String mapBrandFromConfig(UserContext userContext) {
        return configDAO.getConfigurationStringValue(BRAND, userContext.getChannelId());

    }

    private HashMap<String, String> populateTokenisedMap(CommunicationPartyDetailsDTO partyDetails, String mnemonic) {
        HashMap<String, String> tokenisedMap = new HashMap<String, String>();
        tokenisedMap.put(KEY_FOR_TITLE, partyDetails.getTitle());
        tokenisedMap.put(KEY_FOR_FIRST_NAME, partyDetails.getFirstName());
        tokenisedMap.put(KEY_FOR_LAST_NAME, partyDetails.getLastName());
        tokenisedMap.put(KEY_FOR_POST_CODE, partyDetails.getPostCode());
        tokenisedMap.put(KEY_FOR_MNEMONIC, mnemonic);
        return tokenisedMap;
    }    
}
