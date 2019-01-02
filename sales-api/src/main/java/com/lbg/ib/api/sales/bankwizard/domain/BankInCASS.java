package com.lbg.ib.api.sales.bankwizard.domain;

/**
 * Created by Debashish on 15/02/2018.
 */
 
public class BankInCASS {
    private boolean isBankInCASS = true;
    private String  code;

    public boolean getIsBankInCASS() {
        return isBankInCASS;
    }

    public void setIsBankInCASS(boolean isBankInCASS) {
        this.isBankInCASS = isBankInCASS;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
