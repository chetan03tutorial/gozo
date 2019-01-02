package com.lbg.ib.api.sales.dto.party;

import java.util.List;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class PartyResponseDTO {
    private String           pwdState;
    private List<ChannelDTO> channels;
    private Boolean          mandateAvailable;

    public PartyResponseDTO(String pwdState, List<ChannelDTO> channels, Boolean isMandateAvailable) {
        this.pwdState = pwdState;
        this.channels = channels;
        this.mandateAvailable = isMandateAvailable;
    }

    public Boolean isMandateAvailable() {
        return mandateAvailable;
    }

    public String getPwdState() {
        return pwdState;
    }

    public List<ChannelDTO> getChannels() {
        return channels;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

}
