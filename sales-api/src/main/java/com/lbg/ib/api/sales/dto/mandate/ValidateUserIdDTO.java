package com.lbg.ib.api.sales.dto.mandate;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ValidateUserIdDTO {
    private String ocisId;
    private String partyId;
    private String channelId;
    private String password;
    private String username;

    public ValidateUserIdDTO(String ocisId, String partyId, String channelId, String password, String username) {
        this.ocisId = ocisId;
        this.partyId = partyId;
        this.channelId = channelId;
        this.password = password;
        this.username = username;
    }

    public String getOcisId() {
        return ocisId;
    }

    public String getPartyId() {
        return partyId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
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
        return ReflectionToStringBuilder.toStringExclude(this, "password", "username");
    }
}
