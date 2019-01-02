package com.lbg.ib.api.sales.docupload.domain;

import java.util.List;

public class Evidence {

    private String         evidenceTypeCode;
    private List<Document> documents;

    public String getEvidenceTypeCode() {
        return evidenceTypeCode;
    }

    public void setEvidenceTypeCode(String evidenceTypeCode) {
        this.evidenceTypeCode = evidenceTypeCode;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

}
