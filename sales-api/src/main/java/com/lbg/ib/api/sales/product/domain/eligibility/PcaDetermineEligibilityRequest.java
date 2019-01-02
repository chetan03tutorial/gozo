/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.eligibility;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.validation.AccountTypeValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;
import com.lbg.ib.api.sales.product.domain.suitability.ProductQualiferOptions;

@Validate
public class PcaDetermineEligibilityRequest {

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    private AccountType                  arrangementType;

    @RequiredFieldValidation
    private String                       dob;

    @RequiredFieldValidation
    private Boolean                      existingCustomer;

    @RequiredFieldValidation
    private List<ProductQualiferOptions> productOptions;

    private List<String>                 candidateInstructions = null;

    public PcaDetermineEligibilityRequest() {
        // default comments for Sonar violations avoidance.
    }

    public AccountType getArrangementType() {
        return arrangementType;
    }

    public void setArrangementType(AccountType arrangementType) {
        this.arrangementType = arrangementType;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Boolean getExistingCustomer() {
        return existingCustomer;
    }

    public void setExistingCustomer(Boolean existingCustomer) {
        this.existingCustomer = existingCustomer;
    }

    public List<ProductQualiferOptions> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductQualiferOptions> productOptions) {
        this.productOptions = productOptions;
    }

    public List<String> getCandidateInstructions() {
        return candidateInstructions;
    }

    public void setCandidateInstructions(List<String> candidateInstructions) {
        this.candidateInstructions = candidateInstructions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arrangementType == null) ? 0 : arrangementType.hashCode());
        result = prime * result + ((dob == null) ? 0 : dob.hashCode());
        result = prime * result + ((existingCustomer == null) ? 0 : existingCustomer.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PcaDetermineEligibilityRequest other = (PcaDetermineEligibilityRequest) obj;
        if (arrangementType != other.arrangementType) {
            return false;
        }
        if (dob == null) {
            if (other.dob != null) {
                return false;
            }
        } else if (!dob.equals(other.dob)) {
            return false;
        }
        if (existingCustomer == null) {
            if (other.existingCustomer != null) {
                return false;
            }
        } else if (!existingCustomer.equals(other.existingCustomer)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "dob");
    }
}
