/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * All Rights Reserved.
 * Class Name: CaseDTO
 * Author(s): 8768724
 * Date: 12 Dec 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.transaction;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CaseDTO {

    private Integer          caseId;

    private String           processCode;

    private String           brand;

    private String           creatorID;

    private String           caseReferenceNo;

    private String           caseStatus;

    // Set ExpiryDate as String for display only at UI
    private String           expiryDate;

    // Set CreationDate as String for display only at UI
    private String           creationDate;

    // Set UpdateDate as String for display only at UI
    private String           updateDate;

    private String           targetBatchReferenceNo;

    private List<PartyDTO>   partyDetails;

    private List<CasePsfDTO> casePSF;

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getCaseReferenceNo() {
        return caseReferenceNo;
    }

    public void setCaseReferenceNo(String caseReferenceNo) {
        this.caseReferenceNo = caseReferenceNo;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
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

    public String getTargetBatchReferenceNo() {
        return targetBatchReferenceNo;
    }

    public void setTargetBatchReferenceNo(String targetBatchReferenceNo) {
        this.targetBatchReferenceNo = targetBatchReferenceNo;
    }

    public List<PartyDTO> getPartyDetails() {
        return partyDetails;
    }

    public void setPartyDetails(List<PartyDTO> partyDetails) {
        this.partyDetails = partyDetails;
    }

    public List<CasePsfDTO> getCasePSF() {
        return casePSF;
    }

    public void setCasePSF(List<CasePsfDTO> casePSF) {
        this.casePSF = casePSF;
    }

    /**
     * @return the status
     */
    public String getCaseStatus() {
        return caseStatus;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }
}
