package com.lbg.ib.api.sales.docupload.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessInfo {

    private String processCode;
    private String processName;
    private String searchKey;
    private String searchKeyLabel;

    public String getSearchKeyLabel() {
        return searchKeyLabel;
    }

    public void setSearchKeyLabel(String searchKeyLabel) {
        this.searchKeyLabel = searchKeyLabel;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
