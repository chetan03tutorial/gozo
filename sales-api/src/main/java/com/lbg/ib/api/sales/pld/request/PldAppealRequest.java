package com.lbg.ib.api.sales.pld.request;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * Created by dbhatt on 17/10/2018.
 */
public class PldAppealRequest {

    private String applicationStatus;
    
    private boolean skipOverdraft;

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    
    public boolean isSkipOverdraft() {
        return skipOverdraft;
    }

    public void setSkipOverdraft(boolean skipOverdraft) {
        this.skipOverdraft = skipOverdraft;
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
