package com.lbg.ib.api.sales.conversion.domain;
/*
Created by Rohit.Soni at 14/06/2018 16:00
*/

public class AdditionalPartyDetails {
    boolean isPrimaryParty;
    boolean isRecordingSuccessful;
    boolean isPartyPresent;

    public boolean isPrimaryParty() {
        return isPrimaryParty;
    }

    public void setPrimaryParty(boolean primaryParty) {
        isPrimaryParty = primaryParty;
    }

    public boolean isRecordingSuccessful() {
        return isRecordingSuccessful;
    }

    public void setRecordingSuccessful(boolean recordingSuccessful) {
        isRecordingSuccessful = recordingSuccessful;
    }

    public boolean isPartyPresent() {
        return isPartyPresent;
    }

    public void setPartyPresent(boolean partyPresent) {
        isPartyPresent = partyPresent;
    }
}
