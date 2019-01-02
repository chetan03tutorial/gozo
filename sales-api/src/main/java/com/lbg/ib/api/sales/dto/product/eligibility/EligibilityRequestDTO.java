/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dto.product.eligibility;

import java.util.Arrays;
import java.util.Date;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class EligibilityRequestDTO {

    private String                              anArrangementType;
    private Date                                aBirthDate;
    private String                              aCustomerIdentifier;
    private String[]                            aCandidateInstructions;
    private final ExistingProductArrangementDTO anExistingProductArrangements;
    private String                              aProductIdentifier;
    private String                              aSortCode;
    private PrimaryInvolvedParty                primaryInvolvedParty;

    public EligibilityRequestDTO(String arrangementType, Date birthDate, String customerIdentifier,
            ExistingProductArrangementDTO existingProductArrangements, String[] candidateInstructions,
            String productIdentifier, String sortCode) {
        this.anArrangementType = arrangementType;
        this.aBirthDate = birthDate;
        this.aCustomerIdentifier = customerIdentifier;
        this.aCandidateInstructions = candidateInstructions;
        this.anExistingProductArrangements = existingProductArrangements;
        this.aProductIdentifier = productIdentifier;
        this.aSortCode = sortCode;
    }

    public EligibilityRequestDTO(String arrangementType, PrimaryInvolvedParty primaryInvolvedParty,
            String[] candidateInstructions, ExistingProductArrangementDTO existingProductArrangements) {
        this.anArrangementType = arrangementType;
        this.primaryInvolvedParty = primaryInvolvedParty;
        this.aCandidateInstructions = candidateInstructions;
        this.anExistingProductArrangements = existingProductArrangements;
    }

    public String arrangementType() {
        return anArrangementType;
    }

    public Date birthDate() {
        return aBirthDate;
    }

    public String customerIdentifier() {
        return aCustomerIdentifier;
    }

    public PrimaryInvolvedParty getPrimaryInvolvedParty() {
        return primaryInvolvedParty;
    }

    public void setPrimaryInvolvedParty(PrimaryInvolvedParty primaryInvolvedParty) {
        this.primaryInvolvedParty = primaryInvolvedParty;
    }

    public String[] candidateInstructions() {
        return aCandidateInstructions;
    }

    public Object[] existingProductArrangements() {
        Object[] objArray = {};
        if (anExistingProductArrangements != null) {
            return anExistingProductArrangements.getExistingProductArrangments();
        }
        return objArray;
    }

    public String getProductIdentifier() {
        return aProductIdentifier;
    }

    public String getSortCode() {
        return aSortCode;
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
        return ReflectionToStringBuilder.toStringExclude(this, "aBirthDate");
    }
}
