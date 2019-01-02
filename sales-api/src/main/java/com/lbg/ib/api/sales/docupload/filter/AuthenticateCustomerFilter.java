/**********************************************************************
 * All Rights Reserved.
 * Class Name: TokenAtuthorisationFilter
 * Author(s): 8768724
 * Date: 23 Nov 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.filters.securityfilter.enums.ContextDetails;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;

public class AuthenticateCustomerFilter extends GenericFilterBean {

    @Autowired
    private LoggerDAO            logger;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    public AuthenticateCustomerFilter(LoggerDAO logger, ConfigurationService configurationService) {
        super();
        this.logger = logger;
        this.configurationService = configurationService;

    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {

            String path = httpServletRequest.getRequestURI();
            if (excludeFromFilter(path)) {
                String brand = getBrandFromRequest(httpResponse);
                if (brand != null) {
                    logger.traceLog(this.getClass(), "Brand value: " +brand);
                    httpServletRequest.setAttribute(DocUploadConstant.BRAND_VALUE, brand);
                    httpServletRequest.setAttribute(DocUploadConstant.BRAND_DISPLAY_VALUE,
                            configurationService.getConfigurationValueAsString(DocUploadConstant.BRAND_SECTION, brand));
                }
                nonJWTSessionManagement(path, request);

                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }

        } catch (Exception exception) {
            logger.logException(this.getClass(), exception);
            // sending error 500 in case of IO exception
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void nonJWTSessionManagement(String path, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = (HttpSession) httpServletRequest.getSession();
        session.setAttribute("", "");
        session.setMaxInactiveInterval(5 * 60);
    }

    private String getBrandFromRequest(HttpServletResponse httpResponse) throws IOException {
        String brand = null;
        try {
            // fetch the brand value from ApplicationrequestContext
            brand = ApplicationRequestContext.get(ContextDetails.LEGACY_CHANNEL.code()).toString();
        } catch (NullPointerException nullPointerException) {
            logger.logException(this.getClass(), nullPointerException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw nullPointerException;
        }
        return brand;
    }

    private boolean excludeFromFilter(String path) {
        boolean exclude = false;
        if (path.contains("/v1/authorize")) {
            exclude = true;
        }
        return true;
    }

}
