package com.lbg.ib.api.sales.dto.party;

public class TriadResultDTO {

    private String actionID;
    private String actionValue;

    public TriadResultDTO (String actionID, String actionValue) {
        this.actionID = actionID;
        this.actionValue = actionValue;
    }
    public String getActionID() {
        return actionID;
    }

    public String getActionValue() {
        return actionValue;
    }

}
