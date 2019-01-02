package com.lbg.ib.api.sales.sortcode.resource;

import java.util.List;

public class SortCodeMapperBeanWrapper {

    List<SortCodeMapper> sortCodeMaper;
    String defaultTown;

    public List<SortCodeMapper> getSortCodeMaper() {
        return sortCodeMaper;
    }

    public void setSortCodeMaper(List<SortCodeMapper> sortCodeMaper) {
        this.sortCodeMaper = sortCodeMaper;
    }

    public String getDefaultTown() {
        return defaultTown;
    }

    public void setDefaultTown(String defaultTown) {
        this.defaultTown = defaultTown;
    }
}
