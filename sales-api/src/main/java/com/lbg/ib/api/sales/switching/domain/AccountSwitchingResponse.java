package com.lbg.ib.api.sales.switching.domain;

public class AccountSwitchingResponse {
    private String pegaCaseId;

    public AccountSwitchingResponse() {
    }

    public AccountSwitchingResponse(String pegaCaseId) {
        this.pegaCaseId = pegaCaseId;
    }

    public String getPegaCaseId() {
        return pegaCaseId;
    }

    public void setPegaCaseId(String pegaCaseId) {
        this.pegaCaseId = pegaCaseId;
    }

    @Override
    public String toString() {
        return "AccountSwitchingResponse{" + "pegaCaseId='" + pegaCaseId + '\'' + '}';
    }
}
