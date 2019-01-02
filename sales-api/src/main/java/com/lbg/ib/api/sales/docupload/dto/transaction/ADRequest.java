package com.lbg.ib.api.sales.docupload.dto.transaction;

public class ADRequest {

    private String       processName;
    private String       processCode;
    private ColleagueDTO colleague;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public ColleagueDTO getColleague() {
        return colleague;
    }

    public void setColleague(ColleagueDTO colleague) {
        this.colleague = colleague;
    }

}
