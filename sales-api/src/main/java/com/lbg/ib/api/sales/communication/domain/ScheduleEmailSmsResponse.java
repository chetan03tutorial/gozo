package com.lbg.ib.api.sales.communication.domain;

import java.util.List;
import java.util.Map;

public class ScheduleEmailSmsResponse {

    private Map<String,Map<String, List<String>>> emailSentInfo;
    public Map<String, Map<String, List<String>>> getEmailSentInfo() {
        return emailSentInfo;
    }

    public void setEmailSentInfo(Map<String, Map<String, List<String>>> emailSentInfo) {
        this.emailSentInfo = emailSentInfo;
    }

}
