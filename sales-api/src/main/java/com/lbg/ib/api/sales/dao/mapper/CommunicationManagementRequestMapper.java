
package com.lbg.ib.api.sales.dao.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Communication;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CommunicationTemplate;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InformationContent;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationRequest;

@Component
public class CommunicationManagementRequestMapper {

    private static final String  SERVICE_NAME   = "SendCommunication";

    private static final String  SERVICE_ACTION = "sendCommunication";

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private GBOHeaderUtility     gboHeaderUtility;

    public SendCommunicationRequest mapSendCommunicationRequestAttributes(
            CommunicationManagementDTO communicationManagemenatRequestDTO, UserContext userContext) {

        SendCommunicationRequest sendCommunicationRequest = new SendCommunicationRequest();
        sendCommunicationRequest.setHeader(prepareSoapHeaders(userContext));
        Communication communication = mappingCommunicationDetailsToRequest(communicationManagemenatRequestDTO);

        sendCommunicationRequest.setCommunication(communication);
        return sendCommunicationRequest;
    }

    @TraceLog
    private Communication mappingCommunicationDetailsToRequest(
            CommunicationManagementDTO communicationManagemenatRequestDTO) {

        Communication communication = new Communication();
        CommunicationTemplate communicationTemplates = new CommunicationTemplate();
        communicationTemplates.setTemplateId(communicationManagemenatRequestDTO.getTemplateID());
        communication.setCommunicationTemplate(communicationTemplates);
        communication.setCommunicationType(communicationManagemenatRequestDTO.getCommunicationType());
        communication.setContactPointId(communicationManagemenatRequestDTO.getContactPointID());
        communication.setBrand(communicationManagemenatRequestDTO.getBrand());
        List<InformationContent> hasCommunictaionContentList = mappingCommunicationContenetList(
                communicationManagemenatRequestDTO);
        InformationContent[] hasCommunictaionContent = (InformationContent[]) hasCommunictaionContentList
                .toArray(new InformationContent[hasCommunictaionContentList.size()]);
        communication.setHasCommunicationContent(hasCommunictaionContent);
        return communication;
    }

    private List<InformationContent> mappingCommunicationContenetList(
            CommunicationManagementDTO communicationManagemenatRequestDTO) {
        HashMap<String, String> tokenisedMap = communicationManagemenatRequestDTO.getTokenisedMap();
        List<InformationContent> hasCommunictaionContentList = new ArrayList<InformationContent>();
        for (Map.Entry<String, String> entry : tokenisedMap.entrySet()) {
            InformationContent informationContent = new InformationContent();
            informationContent.setKey(entry.getKey());
            informationContent.setValue(entry.getValue());
            informationContent.setOrder(0);
            hasCommunictaionContentList.add(informationContent);
        }
        return hasCommunictaionContentList;
    }

    /**
     * This method will be called only during sessionExpiry Listner is called
     *
     * @param communicationManagemenatRequestDTO
     * @return
     */
    private RequestHeader prepareSoapHeaders(UserContext userContext) {
        RequestHeader requestHeader = new RequestHeader();
        List<SOAPHeader> soapHeaders = gboHeaderUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME, userContext);
        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));
        requestHeader.setBusinessTransaction(SERVICE_NAME);
        requestHeader.setInteractionId(userContext.getSessionId());
        return requestHeader;
    }

}
