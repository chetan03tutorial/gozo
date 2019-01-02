package com.lbg.ib.api.sales.sortcode.resource;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PropositionIndex {

    @JsonProperty("SortCodes")
    private List<SortCodes> sortCodes;

    public List<SortCodes> getSortCodes() {
        return sortCodes;
    }

    public void setSortCodes(List<SortCodes> sortCodes) {
        this.sortCodes = sortCodes;
    }

}
