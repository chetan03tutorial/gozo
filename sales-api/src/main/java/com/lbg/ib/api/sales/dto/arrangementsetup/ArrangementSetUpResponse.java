package com.lbg.ib.api.sales.dto.arrangementsetup;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * Created by 8796528 on 12/03/2018.
 */
public class ArrangementSetUpResponse {

    private String severityCode;
    private String reasonCode;
    private String reasonText;
    private String reasonDetail;


    public ArrangementSetUpResponse(){
    }

    public ArrangementSetUpResponse(String severityCode,String reasonCode, String reasonText,String reasonDetail){
        this.severityCode = severityCode;
        this.reasonCode = reasonCode;
        this.reasonText = reasonText;
        this.reasonDetail = reasonDetail;
    }

    public String getSeverityCode() {
        return severityCode;
    }

    public void setSeverityCode(String severityCode) {
        this.severityCode = severityCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public String getReasonDetail() {
        return reasonDetail;
    }

    public void setReasonDetail(String reasonDetail) {
        this.reasonDetail = reasonDetail;
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
