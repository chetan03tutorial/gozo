package com.lbg.ib.api.sales.communication.mapper;
/*
Created by Rohit.Soni at 20/06/2018 10:48
*/

import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsRequest;
import com.lbg.ib.api.sales.communication.dto.PartyCommunicationDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SendCommunicationMapper {

    private static final String EMAIL_COMMUNICATION_TYPE = "AttachmentPDF";

    public PartyCommunicationDetails buildSendCommunicationDTO(ScheduleEmailSmsRequest userRequest, Map<String, String> emailTokens, List<String> failedEmailList){
        PartyCommunicationDetails partyCommunicationDetails = new PartyCommunicationDetails();
        partyCommunicationDetails.setTemplateId(userRequest.getTemplateName());
        partyCommunicationDetails.setTokenMap(emailTokens);
        String[] emailList = new String[failedEmailList.size()];
        partyCommunicationDetails.setRecipientEmail(failedEmailList.toArray(emailList));
        partyCommunicationDetails.setEmailCommunicationType(EMAIL_COMMUNICATION_TYPE);
        return partyCommunicationDetails;
    }
}
