package com.lbg.ib.api.sales.product.domain.features;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class DeviceID {
    private String server;
    private String org;
    private String id;

    public DeviceID() {
        /* jackson */}

    public DeviceID(String server, String org, String id) {
        this.server = server;
        this.org = org;
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public String getOrg() {
        return org;
    }

    public String getId() {
        return id;
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
