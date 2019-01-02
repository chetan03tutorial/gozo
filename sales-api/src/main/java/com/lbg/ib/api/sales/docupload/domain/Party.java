package com.lbg.ib.api.sales.docupload.domain;

import java.util.List;

public class Party {

    private Integer        casePartyID;
    private Integer        externalSystemId;
    private String         externalPartyId;
    private String         title;
    private String         surName;
    private String         foreName;
    private String         emailId;
    private String         mobileNumber;
    private String         postCode;
    private String         creationDate;
    private String         updateDate;
    private List<Evidence> evidences;

    public List<Evidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        this.evidences = evidences;
    }

    public Integer getCasePartyID() {
        return casePartyID;
    }

    public void setCasePartyID(Integer casePartyID) {
        this.casePartyID = casePartyID;
    }

    public Integer getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(Integer externalSystemId) {
        this.externalSystemId = externalSystemId;
    }

    public String getExternalPartyId() {
        return externalPartyId;
    }

    public void setExternalPartyId(String externalPartyId) {
        this.externalPartyId = externalPartyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getForeName() {
        return foreName;
    }

    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}
