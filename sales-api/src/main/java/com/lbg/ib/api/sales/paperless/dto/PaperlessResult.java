package com.lbg.ib.api.sales.paperless.dto;

import java.util.List;

import com.lbg.ib.api.sales.paperless.constants.PaperlessConstants;

/**
 * Created by pbabb1 on 9/5/2016.
 */
public class PaperlessResult {

    private String        goGreenStatus;
    private List<Account> accounts;

    public PaperlessResult() {
        goGreenStatus = PaperlessConstants.NO_DATA;
    }

    public String getGoGreenStatus() {
        return goGreenStatus;
    }

    public void setGoGreenStatus(String goGreenStatus) {
        this.goGreenStatus = goGreenStatus;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

}
