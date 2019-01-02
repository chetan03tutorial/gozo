package com.lbg.ib.api.sales.dto.party;
/*
Created by Rohit.Soni at 31/05/2018 11:27
*/

public class CFCNewLimitResultDTO {

    private String cfcNewLimitId;
    private String cfcNewlimitValue;

    public CFCNewLimitResultDTO(String cfcNewLimitId, String cfcNewlimitValue) {
        this.cfcNewLimitId = cfcNewLimitId;
        this.cfcNewlimitValue = cfcNewlimitValue;
    }

    public String getCfcNewLimitId() {
        return cfcNewLimitId;
    }

    public void setCfcNewLimitId(String cfcNewLimitId) {
        this.cfcNewLimitId = cfcNewLimitId;
    }

    public String getCfcNewlimitValue() {
        return cfcNewlimitValue;
    }

    public void setCfcNewlimitValue(String cfcNewlimitValue) {
        this.cfcNewlimitValue = cfcNewlimitValue;
    }
}
