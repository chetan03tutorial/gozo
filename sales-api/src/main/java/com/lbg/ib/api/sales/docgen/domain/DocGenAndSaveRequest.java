package com.lbg.ib.api.sales.docgen.domain;
/*
Created by Rohit.Soni at 07/05/2018 12:40
This will serve as the request for Doc generation and save utility.
*/

import com.lbg.ib.api.sales.docgen.util.UniqueReferenceNumber;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

import java.util.Map;

@Validate
public class DocGenAndSaveRequest {

    @RequiredFieldValidation
    private String templateName;
    private Map<String, String> emailTokens;
    private String productMnemonic;
    @RequiredFieldValidation
    private EvidenceInfo evidenceInfo;
    @RequiredFieldValidation
    private DocumentInfo documentInfo;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getProductMnemonic() {
        return productMnemonic;
    }

    public void setProductMnemonic(String productMnemonic) {
        this.productMnemonic = productMnemonic;
    }

    public String getCaseReferenceNo() {
        return String.valueOf(UniqueReferenceNumber.get());
    }

    public EvidenceInfo getEvidenceInfo() {
        return evidenceInfo;
    }

    public void setEvidenceInfo(EvidenceInfo evidenceInfo) {
        this.evidenceInfo = evidenceInfo;
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    public Map<String, String> getEmailTokens() {
        return emailTokens;
    }

    public void setEmailTokens(Map<String, String> emailTokens) {
        this.emailTokens = emailTokens;
    }
}
