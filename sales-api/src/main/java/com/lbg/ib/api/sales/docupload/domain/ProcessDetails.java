package com.lbg.ib.api.sales.docupload.domain;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessDetails {

    // request fields
    private String            colleagueId;

    // Response Fields
    private String            serviceResponse;

    private List<ProcessInfo> processList;

    public String getServiceResponse() {
        return serviceResponse;
    }

    public void setServiceResponse(String serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    public List<ProcessInfo> getProcessList() {
        if (processList == null) {
            processList = new LinkedList<ProcessInfo>();
        }
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
