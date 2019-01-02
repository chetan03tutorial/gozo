package com.lbg.ib.api.sales.application.service;

public interface ApplicationService {

    public String getCurrentBrand();

    /**
     * clear pipeline data for existing journey
     */
    public void clearPipeLineData();
    public String getReverseBrandName();
}
