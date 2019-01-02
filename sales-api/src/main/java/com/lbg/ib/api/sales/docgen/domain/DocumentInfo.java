package com.lbg.ib.api.sales.docgen.domain;
/*
Created by Rohit.Soni at 08/05/2018 18:38
*/

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;

public class DocumentInfo {
    @RequiredFieldValidation
    private DocumentCodeTypeEnum documentCode;
    @RequiredFieldValidation
    private String documentPurpose;

    public DocumentCodeTypeEnum getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(DocumentCodeTypeEnum documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentPurpose() {
        return documentPurpose;
    }

    public void setDocumentPurpose(String documentPurpose) {
        this.documentPurpose = documentPurpose;
    }


}
