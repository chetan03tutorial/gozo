package com.lbg.ib.api.sales.product.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductQuestion {

    private String               status;
    private String               msg;
    private String               productIdentifier;
    private List<ProductOptions> productOptions = new ArrayList<ProductOptions>();

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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

    /**
     * @return the productIdentifier
     */
    public String getProductIdentifier() {
        return productIdentifier;
    }

    /**
     * @param productIdentifier
     *            the productIdentifier to set
     */
    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    /**
     * @return the productOptions
     */
    public List<ProductOptions> getProductOptions() {
        return productOptions;
    }

    /**
     * @param productOptions
     *            the productOptions to set
     */
    public void setProductOptions(List<ProductOptions> productOptions) {
        this.productOptions = productOptions;
    }

}
