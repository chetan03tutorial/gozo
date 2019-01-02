/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.eligibility;

import java.util.List;

public class PcaDetermineEligibilityResponse {

    private String                   msg;

    private List<EligibilityDetails> products;

    private List<String>             suitableProducts;

    private boolean                  bankruptcyIndicator;

    public boolean isBankruptcyIndicator() {
        return bankruptcyIndicator;
    }

    public void setBankruptcyIndicator(boolean bankruptcyIndicator) {
        this.bankruptcyIndicator = bankruptcyIndicator;
    }

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

    public List<EligibilityDetails> getProducts() {
        return products;
    }

    public void setProducts(List<EligibilityDetails> products) {
        this.products = products;
    }

}
