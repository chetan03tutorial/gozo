package com.lbg.ib.api.sales.docupload.dto.transaction;

import java.util.List;

import com.lbg.ib.api.sales.docupload.domain.ProcessInfo;

public class ADResponse {

    private String            serviceMessage;
    private List<ProcessInfo> processList;
    private String            colleagueId;

    public String getServiceMessage() {
        return serviceMessage;
    }

    public void setServiceMessage(String serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    public List<ProcessInfo> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessInfo> processList) {
        this.processList = processList;
    }

    public String getColleagueId() {
        return colleagueId;
    }

    public void setColleagueId(String colleagueId) {
        this.colleagueId = colleagueId;
    }

}
