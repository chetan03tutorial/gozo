
package com.lbg.ib.api.sales.dto.party;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ChannelDTO {

    private String channelId;

    private String channelDescription;

    public ChannelDTO(String channelDescription, String channelId) {
        this.channelId = channelId;
        this.channelDescription = channelDescription;
    }

    public String getId() {
        return channelId;
    }

    public void setId(String id) {
        this.channelId = id;
    }

    public String getDescription() {
        return channelDescription;
    }

    public void setDescription(String description) {
        this.channelDescription = description;
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
