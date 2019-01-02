/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.eligibility;

import java.util.List;

public class DetermineEligibilityResponse {

    private String                   msg;

    private List<EligibilityDetails> eligibilityDetails;

    private List<String>             suitableProducts;

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

    public List<String> getSuitableProducts() {
        return suitableProducts;
    }

    public void setSuitableProducts(List<String> suitableProducts) {
        this.suitableProducts = suitableProducts;
    }

    public List<EligibilityDetails> getEligibilityDetails() {
        return eligibilityDetails;
    }

    public void setEligibilityDetails(List<EligibilityDetails> products) {
        this.eligibilityDetails = products;
    }

}
