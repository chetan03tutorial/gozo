package com.lbg.ib.api.sales.conversion.dto;
/*
Created by Rohit.Soni at 15/06/2018 14:16
*/

import com.lbg.ib.api.sales.conversion.domain.AdditionalPartyDetails;
import com.lbg.ib.api.sales.user.domain.PartyDetails;

public class PartyRecordingInfoDTO {
    private PartyDetails partyDetails;
    private AdditionalPartyDetails additionalPartyDetails;

    public PartyDetails getPartyDetails() {
        return partyDetails;
    }

    public void setPartyDetails(PartyDetails partyDetails) {
        this.partyDetails = partyDetails;
    }

    public AdditionalPartyDetails getAdditionalPartyDetails() {
        return additionalPartyDetails;
    }

    public void setAdditionalPartyDetails(AdditionalPartyDetails additionalPartyDetails) {
        this.additionalPartyDetails = additionalPartyDetails;
    }
}
