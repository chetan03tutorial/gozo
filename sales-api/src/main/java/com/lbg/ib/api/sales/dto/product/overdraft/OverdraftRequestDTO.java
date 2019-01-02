package com.lbg.ib.api.sales.dto.product.overdraft;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.lbg.ib.api.sales.dto.product.base.BaseDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;

public class OverdraftRequestDTO extends BaseDTO {

    private StHeader   stheader;
    private String     sortcode;
    private String     accountnumber;
    private BigDecimal amtOverdraft;
    private BigInteger cbsprodnum;
    private String     cbstariff;

    public StHeader getStheader() {
        return this.stheader;
    }

    public void setStheader(StHeader stheader) {
        this.stheader = stheader;
    }

    public String getSortcode() {
        return this.sortcode;
    }

    public void setSortcode(String sortcode) {
        this.sortcode = sortcode;
    }

    public String getAccountnumber() {
        return this.accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public BigDecimal getAmtOverdraft() {
        return this.amtOverdraft;
    }

    public void setAmtOverdraft(BigDecimal amtOverdraft) {
        this.amtOverdraft = amtOverdraft;
    }

    public BigInteger getCbsprodnum() {
        return this.cbsprodnum;
    }

    public void setCbsprodnum(BigInteger cbsprodnum) {
        this.cbsprodnum = cbsprodnum;
    }

    public String getCbstariff() {
        return this.cbstariff;
    }

    public void setCbstariff(String cbstariff) {
        this.cbstariff = cbstariff;
    }
}
