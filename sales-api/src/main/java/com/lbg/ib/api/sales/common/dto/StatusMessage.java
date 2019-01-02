package com.lbg.ib.api.sales.common.dto;

/**
 * Created by rabaja on 13/10/2016.
 */
public class StatusMessage {

    private String statusMessage;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        return "StatusMessage [" +
                "statusMessage=" + statusMessage + "]";
    }
}