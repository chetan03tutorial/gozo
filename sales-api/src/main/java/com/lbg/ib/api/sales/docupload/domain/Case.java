package com.lbg.ib.api.sales.docupload.domain;

import java.util.LinkedList;
import java.util.List;

import com.lbg.ib.api.sales.docupload.dto.transaction.CasePsfDTO;

public class Case {
    
    private Integer          caseId;
    private String           processCode;
    private String           brand;
    private String           creatorID;
    private String           caseReferenceNo;
    private String           caseStatus;
    private String           expiryDate;
    private String           creationDate;
    private String           updateDate;
    private String           targetBatchReferenceNo;
    private List<Party>      partyDetails;
    private List<CasePsfDTO> casePSF;
    private String           applicationName;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

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
    
    public List<Party> getPartyDetails() {
        if (partyDetails == null) {
            partyDetails = new LinkedList<Party>();
        }
        return partyDetails;
    }
    
    public void setPartyDetails(List<Party> partyDetails) {
        this.partyDetails = partyDetails;
    }
    
    public List<CasePsfDTO> getCasePSF() {
        if (casePSF == null) {
            casePSF = new LinkedList<CasePsfDTO>();
        }
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
