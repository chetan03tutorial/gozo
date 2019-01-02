/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.activate;

import java.util.List;

import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.arrangement.AccountDetails;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;

public class Activated {

    private String                 productName;
    private String                 mnemonic;
    private String                 applicationStatus;
    private String                 applicationSubStatus;
    private String                 arrangementId;
    private AccountDetails         accountDetails;
    private String                 crossSellProductName;
    private String                 customerNumber;
    private List<Condition>        condition;
    private List<CustomerDocument> customerDocuments;

    public Activated(String productName, String mnemonic, String applicationStatus, String applicationSubStatus,
            String arrangementId, AccountDetails accountDetails, String crossSellProductName, String customerNumber,
            List<Condition> condition) {
        this.productName = productName;
        this.mnemonic = mnemonic;
        this.applicationStatus = applicationStatus;
        this.applicationSubStatus = applicationSubStatus;
        this.arrangementId = arrangementId;
        this.accountDetails = accountDetails;
        this.crossSellProductName = crossSellProductName;
        this.customerNumber = customerNumber;
        this.condition = condition;
    }

    public String getProductName() {
        return productName;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public String getApplicationSubStatus() {
        return applicationSubStatus;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public String getCrossSellProductName() {
        return crossSellProductName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public List<Condition> getCondition() {
        return condition;
    }

    public List<CustomerDocument> getCustomerDocuments() {
        return customerDocuments;
    }

    public void setCustomerDocuments(List<CustomerDocument> customerDocuments) {
        this.customerDocuments = customerDocuments;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
