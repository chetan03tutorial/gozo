package com.lbg.ib.api.sales.communication.dto;
/*
Created by Rohit.Soni at 20/06/2018 11:17
*/

import java.util.Map;

public class ScheduleEmailDTO {

    private String medium;

    private String channel;

    private String templateId;

    private String email;

    private Map<String, String> contentKeys;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, String> getContentKeys() {
        return contentKeys;
    }

    public void setContentKeys(Map<String, String> contentKeys) {
        this.contentKeys = contentKeys;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
