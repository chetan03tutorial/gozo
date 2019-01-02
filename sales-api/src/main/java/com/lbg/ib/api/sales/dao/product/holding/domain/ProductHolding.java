package com.lbg.ib.api.sales.dao.product.holding.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductHolding {

    private String ibSessionId;

    private List<Arrangement> arrangements;

    private Customer customer;

    private String code;

    private String message;

    public String getIbSessionId() {
        return ibSessionId;
    }

    public void setIbSessionId(String ibSessionId) {
        this.ibSessionId = ibSessionId;
    }

    public List<Arrangement> getArrangements() {
        return arrangements;
    }

    public void setArrangements(List<Arrangement> arrangements) {
        this.arrangements = arrangements;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductHolding that = (ProductHolding) o;

        /*
         * if (customer != null ? !customer.equals(that.customer) : that.customer != null) { return
         * false; } if (arrangements != null ? !arrangements.equals(that.arrangements) :
         * that.arrangements != null) { return false; }
         */
        if (ibSessionId != null ? !ibSessionId.equals(that.ibSessionId) : that.ibSessionId != null) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result = customer != null ? customer.hashCode() : 0;
        result = 31 * result + (arrangements != null ? arrangements.hashCode() : 0);
        result = 31 * result + (ibSessionId != null ? ibSessionId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductHolding {" + "customer='" + customer + '\'' + ", arrangements='" + arrangements + '\''
                + ", ibSessionId='" + ibSessionId + '\'' + ", code='" + code + '\'' + ", message='" + message + '\''
                + '}';
    }

}
