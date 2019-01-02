package com.lbg.ib.api.sales.docgen.domain;
/*
Created by Rohit.Soni at 08/05/2018 18:38
*/

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;

public class EvidenceInfo {
    @RequiredFieldValidation
    private String processCode;
    @RequiredFieldValidation
    private String evidenceTypeCode;

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getEvidenceTypeCode() {
        return evidenceTypeCode;
    }

    public void setEvidenceTypeCode(String evidenceTypeCode) {
        this.evidenceTypeCode = evidenceTypeCode;
    }
}
