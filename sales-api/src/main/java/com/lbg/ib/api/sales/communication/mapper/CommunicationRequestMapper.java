package com.lbg.ib.api.sales.communication.mapper;

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.communication.dto.CommunicationRequestDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Communication;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CommunicationTemplate;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InformationContent;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.communicationmanager.reqrsp.SendCommunicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CommunicationRequestMapper {

    private static final String SERVICE_NAME = "SendCommunication";

    private static final String SERVICE_ACTION = "sendCommunication";

    @Autowired
    private GBOHeaderUtility gboHeaderUtility;

    public SendCommunicationRequest mapSendCommunicationRequestAttributes(CommunicationRequestDTO requestDTO, UserContext userContext) {
        SendCommunicationRequest sendCommunicationRequest = new SendCommunicationRequest();
        sendCommunicationRequest.setHeader(prepareSoapHeaders(userContext));
        Communication communication = mapCommunicationDetailsToRequest(requestDTO);
        sendCommunicationRequest.setCommunication(communication);
        return sendCommunicationRequest;
    }

    private List<InformationContent> mapCommunicationContentList(CommunicationRequestDTO requestDTO) {
        Map<String, String> tokenisedMap = requestDTO.getTokenisedMap();
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

    private Communication mapCommunicationDetailsToRequest(CommunicationRequestDTO requestDTO) {

        Communication communication = new Communication();
        CommunicationTemplate communicationTemplates = new CommunicationTemplate();
        communicationTemplates.setTemplateId(requestDTO.getTemplateID());
        communication.setCommunicationTemplate(communicationTemplates);
        communication.setCommunicationType(requestDTO.getCommunicationType());
        communication.setContactPointId(requestDTO.getContactPointID());
        communication.setBrand(requestDTO.getBrand());
        List<InformationContent> hasCommunictaionContentList = mapCommunicationContentList(requestDTO);
        InformationContent[] hasCommunictaionContent = hasCommunictaionContentList.toArray(new InformationContent[hasCommunictaionContentList.size()]);
        communication.setHasCommunicationContent(hasCommunictaionContent);
        return communication;
    }



    private RequestHeader prepareSoapHeaders(UserContext userContext) {
        RequestHeader requestHeader = new RequestHeader();
        List<SOAPHeader> soapHeaders = gboHeaderUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME, userContext);
        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));
        requestHeader.setBusinessTransaction(SERVICE_NAME);
        requestHeader.setInteractionId(userContext.getSessionId());
        return requestHeader;
    }
    

}
