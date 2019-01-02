package com.lbg.ib.api.sales.overdraft.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OverdraftMetaDataIndex {

    @JsonProperty("OverdraftParam")
    private OverdraftMetaData overdraftMetaData;

    public OverdraftMetaData getOverdraftMetaData() {
        return overdraftMetaData;
    }

    public void setOverdraftMetaData(OverdraftMetaData overdraftMetaData) {
        this.overdraftMetaData = overdraftMetaData;
    }
}
