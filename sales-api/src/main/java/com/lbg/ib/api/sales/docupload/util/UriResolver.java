package com.lbg.ib.api.sales.docupload.util;

import java.net.URL;
import java.util.Map;

public class UriResolver {

    private Map<SalsaEndpoints, URL> salsaEndpoints;

    public void setSalsaEndpoints(Map<SalsaEndpoints, URL> salsaEndpoints) {
        this.salsaEndpoints = salsaEndpoints;
    }

    public String getEndpoint(SalsaEndpoints endpoint) {
        return salsaEndpoints.get(endpoint).toString();
    }
}
