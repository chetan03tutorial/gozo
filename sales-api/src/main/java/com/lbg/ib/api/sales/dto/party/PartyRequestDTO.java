package com.lbg.ib.api.sales.dto.party;

import java.util.Calendar;

import com.lbg.ib.api.sales.dto.product.offer.AccountDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class PartyRequestDTO {
    private AccountDTO accountDTO;
    private String     postCode;
    private String     title;
    private String     firstName;
    private String     surName;
    private Calendar   dateOfBirth;
    private String     cvv;
    private String     partyId;
    private String     accountType;

    public PartyRequestDTO(AccountDTO accountDTO, String postCode, String title, String firstName, String surName,
            Calendar dateOfBirth, String cvv, String accountType, String partyId) {
        this.accountDTO = accountDTO;
        this.postCode = postCode;
        this.title = title;
        this.firstName = firstName;
        this.surName = surName;
        this.dateOfBirth = dateOfBirth;
        this.cvv = cvv;
        this.accountType = accountType;
        this.partyId = partyId;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCvv() {
        return cvv;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getPartyId() {
        return partyId;
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
        return ReflectionToStringBuilder.toStringExclude(this, "dateOfBirth");
    }

}
