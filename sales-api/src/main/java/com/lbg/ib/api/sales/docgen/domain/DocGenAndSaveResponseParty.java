package com.lbg.ib.api.sales.docgen.domain;
/*
Created by 8601769 at 07/05/2018 12:46
This will serve as the response for Doc generation and save utility.
*/


import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class DocGenAndSaveResponseParty {
    @JsonProperty("success")
    private Boolean isDocumentRecordingSuccessful = Boolean.FALSE;
    @JsonIgnore
    private String fileRefId;
    private String errorMessage;
    private Boolean isPrimaryParty = Boolean.FALSE;
    @JsonIgnore
    private boolean error = Boolean.FALSE;

    public Boolean getDocumentRecordingSuccessful() {
        return isDocumentRecordingSuccessful;
    }

    public void setDocumentRecordingSuccessful(Boolean documentRecordingSuccessful) {
        isDocumentRecordingSuccessful = documentRecordingSuccessful;
    }

    public String getFileRefId() {
        return fileRefId;
    }

    public void setFileRefId(String fileRefId) {
        this.fileRefId = fileRefId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getPrimaryParty() {
        return isPrimaryParty;
    }

    public void setPrimaryParty(Boolean primaryParty) {
        isPrimaryParty = primaryParty;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
