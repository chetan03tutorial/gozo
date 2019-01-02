/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dto.party;

import java.util.LinkedHashSet;
import java.util.Set;

import com.lbg.ib.api.shared.domain.TaxResidencyDetails;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ClassifyPartyRequestDTO {
    private String                   birthCountry;
    private Set<String>              nationalities;
    private Set<TaxResidencyDetails> taxResidencies;

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public Set<String> getNationalities() {
        return nationalities;
    }

    public void setNationalities(Set<String> nationalities) {
        this.nationalities = nationalities;
    }

    public Set<TaxResidencyDetails> getTaxResidencies() {
        return taxResidencies;
    }

    public void setTaxResidencies(Set<TaxResidencyDetails> taxResidencies) {
        this.taxResidencies = taxResidencies;
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
