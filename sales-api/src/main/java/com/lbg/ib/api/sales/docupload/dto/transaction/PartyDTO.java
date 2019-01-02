/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * All Rights Reserved.
 * Class Name: PartyDTO
 * Author(s): 8768724
 * Timestamp: 12 Dec 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.transaction;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PartyDTO {

    private Integer         casePartyID;

    private Integer         externalSystemId;

    private String          externalPartyId;

    private String          title;

    private String          surName;

    private String          foreName;

    private String          emailId;

    private String          mobileNumber;

    private String          postCode;

    // Set CreationDate as String for display only at UI
    private String          creationDate;

    // Set UpdateDate as String for display only at UI
    private String          updateDate;

    private List<UploadDTO> attachmentDetails;

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

    public List<UploadDTO> getAttachmentDetails() {
        return attachmentDetails;
    }

    public void setAttachmentDetails(List<UploadDTO> attachmentDetails) {
        this.attachmentDetails = attachmentDetails;
    }

}
