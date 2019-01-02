package com.lbg.ib.api.sales.asm.dto;

import com.lbg.ib.api.sales.asm.domain.ApplicationType;

public class C078RequestDto {

    private String creditScoreRequestNo;
    private ApplicationType applicationType;
    private String ocisId;
    private int maxRepeatGroupQuantity;
    private String processDate;
    private String organisationCode;
    private String creditScoreReferenceNumber;
    private String creditScoreSourceSystemCode;
    private String applicationSourceCode;
    
    public String getCreditScoreRequestNo() {
        return creditScoreRequestNo;
    }

    public void setCreditScoreRequestNo(String creditScoreRequestNo) {
        this.creditScoreRequestNo = creditScoreRequestNo;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public String getOcisId() {
        return ocisId;
    }

    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }

    public int getMaxRepeatGroupQuantity() {
        return maxRepeatGroupQuantity;
    }

    public void setMaxRepeatGroupQuantity(int maxRepeatGroupQuantity) {
        this.maxRepeatGroupQuantity = maxRepeatGroupQuantity;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public String getOrganisationCode() {
        return organisationCode;
    }

    public void setOrganisationCode(String organisationCode) {
        this.organisationCode = organisationCode;
    }

    public String getCreditScoreReferenceNumber() {
        return creditScoreReferenceNumber;
    }

    public void setCreditScoreReferenceNumber(String creditScoreReferenceNumber) {
        this.creditScoreReferenceNumber = creditScoreReferenceNumber;
    }

    public String getCreditScoreSourceSystemCode() {
        return creditScoreSourceSystemCode;
    }

    public void setCreditScoreSourceSystemCode(String creditScoreSourceSystemCode) {
        this.creditScoreSourceSystemCode = creditScoreSourceSystemCode;
    }

    public String getApplicationSourceCode() {
        return applicationSourceCode;
    }

    public void setApplicationSourceCode(String applicationSourceCode) {
        this.applicationSourceCode = applicationSourceCode;
    }

    
    
}
