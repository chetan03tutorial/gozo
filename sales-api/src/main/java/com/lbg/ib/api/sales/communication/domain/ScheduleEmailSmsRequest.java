package com.lbg.ib.api.sales.communication.domain;

import com.lbg.ib.api.sales.conversion.domain.AdditionalPartyDetails;

import java.util.List;
import java.util.Map;

public class ScheduleEmailSmsRequest {

   
    private String templateName;

    private String communicationMedia;
    
    private String productMnemonic;
    
    private Map<String, String> emailTokens;

    private String groupEmailId;

    private String opsEmail;

    private List<AdditionalPartyDetails> additionalPartyDetailsList;
 
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getCommunicationMedia() {
        return communicationMedia;
    }

    public void setCommunicationMedia(String communicationMedia) {
        this.communicationMedia = communicationMedia;
    }

    public String getProductMnemonic() {
        return productMnemonic;
    }

    public void setProductMnemonic(String productMnemonic) {
        this.productMnemonic = productMnemonic;
    }

    public Map<String, String> getEmailTokens() {
        return emailTokens;
    }

    public void setEmailTokens(Map<String, String> emailTokens) {
        this.emailTokens = emailTokens;
    }

    public String getGroupEmailId() {
        return groupEmailId;
    }

    public void setGroupEmailId(String groupEmailId) {
        this.groupEmailId = groupEmailId;
    }

    public String getOpsEmail() {
        return opsEmail;
    }

    public void setOpsEmail(String opsEmail) {
        this.opsEmail = opsEmail;
    }

    public List<AdditionalPartyDetails> getAdditionalPartyDetailsList() {
        return additionalPartyDetailsList;
    }

    public void setAdditionalPartyDetailsList(List<AdditionalPartyDetails> additionalPartyDetailsList) {
        this.additionalPartyDetailsList = additionalPartyDetailsList;
    }
}
