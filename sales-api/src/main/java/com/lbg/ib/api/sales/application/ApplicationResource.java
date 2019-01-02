package com.lbg.ib.api.sales.application;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.lbg.ib.api.sales.common.util.DateUtil;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.application.service.ApplicationService;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;

@Path("/application")
public class ApplicationResource extends Base {

    @Autowired
    private ApiServiceProperties apiServiceProperties;

    @Autowired
    private ApplicationService   applicationService;

    @GET
    @Produces("application/json")
    @Path("/version/{sessiondata}")
    @TraceLog
    public Response appConfig(@PathParam("sessiondata") String sessionData) {
        // Cleanup session before initiating a journey
        if (!"keep".equalsIgnoreCase(sessionData)) {
            applicationService.clearPipeLineData();

        }
        return getVersionInfo();
    }

    @GET
    @Produces("application/json")
    @Path("/version")
    @TraceLog
    public Response appConfig() {
        applicationService.clearPipeLineData();
        return getVersionInfo();
    }

    public Response getVersionInfo() {
        logger.traceLog(this.getClass(), "Fetching ApplicationResource app Config");
        Map<String, Object> versionInfo = new HashMap<String, Object>(
                apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES));
        // get activex flag as well
        Map<String, Object> activeXConfigMap = apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES);
        logger.traceLog(this.getClass(), "Loading the properties Map");
        String brand = applicationService.getCurrentBrand();
        String activexFlag = (String) activeXConfigMap.get(brand);
        logger.traceLog(this.getClass(), "Loading the activexFlag");

        Map<String, Object> acquActiveXConfigMap = apiServiceProperties
                .getConfigurationItems(Constants.ACQU_ACTIVEX_PROPERTIES);
        logger.traceLog(this.getClass(), "Loading the properties Map");
        brand = applicationService.getCurrentBrand();
        String acquActivexFlag = (String) acquActiveXConfigMap.get(brand);
        logger.traceLog(this.getClass(), "Loading the activexFlag");

        if (!versionInfo.isEmpty()) {
            versionInfo.put(Constants.ACTIVE_X_FLAG, activexFlag);
            versionInfo.put(Constants.ACQU_ACTIVE_X_FLAG, acquActivexFlag);
            //PCA 6523-- adding currentData in version response
            versionInfo.put(Constants.CURRENT_DATE, DateUtil.getCurrentUKDateAsString(true));

            return respond(Status.OK, versionInfo);
        }

        return null;
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
