package com.lbg.ib.api.sales.bankwizard.domain;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 29thJuly2016
 ***********************************************************************/
public class BankWizardResponse {
    private boolean isValid;
    private String  message;

    public BankWizardResponse() {
        /* jackson */}

    public BankWizardResponse(boolean isValid) {
        this.isValid = isValid;
        this.message = "Invalid combination of sort code and account number";
        if (isValid) {
            this.message = "Valid combination of sort code and account number";
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BankWizardResponse [isValid=" + isValid + ", message=" + message + "]";
    }

}
