/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.eligibility;

import java.util.List;

public class UpgradeEligibilityResponse {
    
    private String                   msg;

    //defaulting to null for one party cases
    private Boolean isSecondaryPartyEmailPresent = null;

    private List<EligibilityDetails> products;

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }
    
    /**
     * @param msg
     *            the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public List<EligibilityDetails> getProducts() {
        return products;
    }
    
    public void setProducts(List<EligibilityDetails> products) {
        this.products = products;
    }

    public Boolean getSecondaryPartyEmailPresent() {
        return isSecondaryPartyEmailPresent;
    }

    public void setSecondaryPartyEmailPresent(Boolean secondaryPartyEmailPresent) {
        isSecondaryPartyEmailPresent = secondaryPartyEmailPresent;
    }
}