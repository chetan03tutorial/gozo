/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dto.product.eligibility;

import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PcaEligibilityRequestDTO {

    private String                        arrangementType;
    private String                        brand;
    private Date                          birthDate;
    private String                        customerIdentifier;
    private String[]                      candidateInstructions;
    private List<Account>                 accounts;
    private ExistingProductArrangementDTO existingProductArrangements;
    private String                        productIdentifier;
    private String                        sortCode;
    private PrimaryInvolvedParty          primaryInvolvedParty;

    public PcaEligibilityRequestDTO() {
        // Sonar avoidance comments
    }

    public PcaEligibilityRequestDTO(String arrangementType, Date birthDate, String customerIdentifier,
            ExistingProductArrangementDTO existingProductArrangements, String[] candidateInstructions,
            String productIdentifier) {
        this.arrangementType = arrangementType;
        this.birthDate = birthDate;
        this.customerIdentifier = customerIdentifier;
        this.existingProductArrangements = existingProductArrangements;
        this.candidateInstructions = candidateInstructions;
        this.productIdentifier = productIdentifier;
    }

    public PcaEligibilityRequestDTO(String arrangementType, PrimaryInvolvedParty primaryInvolvedParty,
            String[] candidateInstructions, List<Account> accounts) {
        this.arrangementType = arrangementType;
        this.primaryInvolvedParty = primaryInvolvedParty;
        this.candidateInstructions = candidateInstructions;
        this.accounts = accounts;
    }

    public PcaEligibilityRequestDTO(String arrangementType, PrimaryInvolvedParty primaryInvolvedParty,
            String[] candidateInstructions, ExistingProductArrangementDTO existingProductArrangements,
            List<Account> accounts) {
        this.arrangementType = arrangementType;
        this.primaryInvolvedParty = primaryInvolvedParty;
        this.candidateInstructions = candidateInstructions;
        this.existingProductArrangements = existingProductArrangements;
        this.accounts = accounts;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getArrangementType() {
        return arrangementType;
    }

    public void setArrangementType(String arrangementType) {
        this.arrangementType = arrangementType;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public String[] getCandidateInstructions() {
        return candidateInstructions;
    }

    public void setCandidateInstructions(String[] candidateInstructions) {
        this.candidateInstructions = candidateInstructions;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public ExistingProductArrangementDTO getExistingProductArrangements() {
        return existingProductArrangements;
    }

    public void setExistingProductArrangements(ExistingProductArrangementDTO existingProductArrangements) {
        this.existingProductArrangements = existingProductArrangements;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String arrangementType() {
        return arrangementType;
    }

    public Date birthDate() {
        return birthDate;
    }

    public String customerIdentifier() {
        return customerIdentifier;
    }

    public PrimaryInvolvedParty getPrimaryInvolvedParty() {
        return primaryInvolvedParty;
    }

    public void setPrimaryInvolvedParty(PrimaryInvolvedParty primaryInvolvedParty) {
        this.primaryInvolvedParty = primaryInvolvedParty;
    }

    public String[] candidateInstructions() {
        return candidateInstructions;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public String getSortCode() {
        return sortCode;
    }

    public Object[] existingProductArrangements() {
        return existingProductArrangements.getExistingProductArrangments();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        /*
         * if (getClass() != obj.getClass()) { return false; }
         */
        PcaEligibilityRequestDTO other = (PcaEligibilityRequestDTO) obj;
        /*
         * if (arrangementType == null) { if (other.arrangementType != null) {
         * return false; } } else if
         * (!arrangementType.equals(other.arrangementType)) { return false; }
         */
        if (sortCode == null) {
            if (other.sortCode != null) {
                return false;
            }
        } else if (!sortCode.equals(other.sortCode)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arrangementType == null) ? 0 : arrangementType.hashCode());
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + Arrays.hashCode(candidateInstructions);
        result = prime * result + ((customerIdentifier == null) ? 0 : customerIdentifier.hashCode());
        result = prime * result + ((accounts == null) ? 0 : accounts.hashCode());
        result = prime * result + ((productIdentifier == null) ? 0 : productIdentifier.hashCode());
        result = prime * result + ((sortCode == null) ? 0 : sortCode.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "EligibilityRequestDTO [arrangementType=" + arrangementType + ", birthDate=" + birthDate
                + ", customerIdentifier=" + customerIdentifier + ", candidateInstructions="
                + Arrays.toString(candidateInstructions) + ", accounts=" + accounts + ", productIdentifier="
                + productIdentifier + ", sortCode=" + sortCode + ", primaryInvolvedParty=" + primaryInvolvedParty + "]";
    }
}
