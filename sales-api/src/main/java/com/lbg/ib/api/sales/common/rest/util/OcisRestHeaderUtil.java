package com.lbg.ib.api.sales.common.rest.util;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OcisRestHeaderUtil {

    @Autowired
    private SessionManagementDAO sessionService;
    @Autowired
    private ConfigurationDAO configurationService;

    @TraceLog
    public Map<String, Object> getSalsaHeader() {
        Map<String, Object> defaultRequestHeader = configurationService.getConfigurationItems("RestRequestHeaderDetails");
        Map<String, Object> requestHeader = new HashMap<String, Object>(defaultRequestHeader);
        if (null != sessionService.getBranchContext()) {
            BranchContext branchContext = sessionService.getBranchContext();
            String colleagueId = branchContext.getColleagueId();
            requestHeader.put("X-LBG-USER-ID", colleagueId);
            requestHeader.put("X-LBG-OUTLET-ID", branchContext.getOriginatingSortCode());
        }
        return requestHeader;
    }

}
