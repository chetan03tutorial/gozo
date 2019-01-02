/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.filter;

import java.util.concurrent.ConcurrentMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.application.switching.GlobalApplicationSwitches;
import com.lloydstsb.ea.application.switching.data.ApplicationSwitch;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;

/**
 * @author 8735182
 *
 */
public class DocUploadServiceFilter extends GenericFilterBean {

    private static final String  DOC_UPLOAD_SWITCH = "DOC_UPLOAD_SWITCH";

    private static final String  BRANDED_SWITCH    = "BRANDED_SWITCH";

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private LoggerDAO            logger;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            if (isDocUploadSwitchEnabled(httpServletRequest)) {
                filterChain.doFilter(request, response);
            } else {
                logger.logError("403", "Doc-upload Rest API request rejected - Feature Switch: ", this.getClass());
                // throw 403 in case switch is disable
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception exception) {
            if (logger.debugEnabled()) {
                logger.logDebug(this.getClass(), "IOException catched in DocUpload filter");
            }
            logger.logException(this.getClass(), exception);
            // sending error 500 in case of IO exception
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isDocUploadSwitchEnabled(HttpServletRequest aHttpServletRequest) {
        String switchName = configurationService.getConfigurationValueAsString(DOC_UPLOAD_SWITCH, BRANDED_SWITCH);
        String brandValue = ApplicationRequestContext.get(ApplicationAttribute.CHANNEL).toString();

        final ConcurrentMap<String, ApplicationSwitch> globalApplicationSwitches = GlobalApplicationSwitches
                .getApplicationSwitches(brandValue);
        if (globalApplicationSwitches != null) {
            final ApplicationSwitch anApplicationSwitch = globalApplicationSwitches.get(switchName);
            if (anApplicationSwitch != null) {
                return anApplicationSwitch.getSwitchValue();
            }
        }
        // to do need to change to false
        return true;
    }

}
