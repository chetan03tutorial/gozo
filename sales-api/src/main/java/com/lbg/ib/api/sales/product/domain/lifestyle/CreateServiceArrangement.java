package com.lbg.ib.api.sales.product.domain.lifestyle;

import com.lbg.ib.api.sales.dto.product.offer.EmploymentDTO;
import com.lbg.ib.api.sales.dto.product.offer.PhoneDTO;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by 8796528 on 08/03/2018.
 */
public class CreateServiceArrangement {

    private String accountNumber;
    private String sortCode;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String middleName;
    private String prefixTitleName;
    private Date birthDate;
    private String maritalStatus;
    private String gender;
    private BigInteger numberOfDependants;
    private String employmentStatus;
    private String birthCity;
    private EmploymentDTO employmentDTO;
    private PhoneDTO mobilePhone;
    private PhoneDTO homePhone;


    public PhoneDTO getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(PhoneDTO workPhone) {
        this.workPhone = workPhone;
    }


    public PhoneDTO getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(PhoneDTO homePhone) {
        this.homePhone = homePhone;
    }

    public PhoneDTO getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(PhoneDTO mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    private PhoneDTO workPhone;

    public EmploymentDTO getEmploymentDTO() {
        return employmentDTO;
    }

    public void setEmploymentDTO(EmploymentDTO employmentDTO) {
        this.employmentDTO = employmentDTO;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public BigInteger getNumberOfDependants() {
        return numberOfDependants;
    }

    public void setNumberOfDependants(BigInteger numberOfDependants) {
        this.numberOfDependants = numberOfDependants;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefixTitleName() {
        return prefixTitleName;
    }

    public void setPrefixTitleName(String prefixTitleName) {
        this.prefixTitleName = prefixTitleName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}

