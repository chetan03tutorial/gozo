package com.lbg.ib.api.sales.common.filter;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;
import com.lbg.ib.api.sales.common.constant.Constants.BranchContextConstants;
import com.lloydstsb.ea.logging.factory.LoggingServiceFactory;
import com.lloydstsb.ea.logging.interfaces.LoggingService;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

public class ColleagueIdExtractionFilter implements Filter {

    private static final String         PRINCIPAL_SEPARATOR = "/";
    private static final String         DOMAIN_ATTR         = "@";
    private static final LoggingService LOGGER              = LoggingServiceFactory.getLoggingService();
    private static final String         CLASSNAME           = ColleagueIdExtractionFilter.class.getName();

    public void destroy() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.logDebug(CLASSNAME, "Exit", new Object[] { "destroy" });
        }

    }

    public void doFilter(ServletRequest aServletReq, ServletResponse aServletRes, FilterChain aFilterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) aServletReq;
        try {

            Principal someNonKerberosPrincipal = null;

            LOGGER.logDebug(CLASSNAME, "PCA WSSubject :: " + (WSSubject.getCallerSubject() != null
                    ? WSSubject.getCallerSubject() : " caller subject is null"));
            final Subject callerSubject = WSSubject.getCallerSubject();
            if (callerSubject != null) {
                if (null != callerSubject.getPrincipals() && !CollectionUtils.isEmpty(callerSubject.getPrincipals())) {
                    validatePricipal(req, someNonKerberosPrincipal, callerSubject);
                }
            }
        } catch (WSSecurityException e) {
            LOGGER.logException(e);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.logDebug(CLASSNAME, "WSSecurity Exception::",
                        new Object[] { "WSSecurity Exception for Spnego" + e.getMessage() });
            }
        }

        aFilterChain.doFilter(req, aServletRes);

    }

    /**
     * @param req
     * @param accredited
     * @param someNonKerberosPrincipalLocal
     * @param callerSubject
     */
    private void validatePricipal(HttpServletRequest req, final Principal someNonKerberosPrincipal,
            final Subject callerSubject) {
        boolean accredited = false;
        Principal someNonKerberosPrincipalLocal = someNonKerberosPrincipal;
        LOGGER.logDebug(CLASSNAME, "Principal Size :: " + callerSubject.getPrincipals().size());
        for (Principal somePrincipal : callerSubject.getPrincipals()) {
            if (somePrincipal instanceof KerberosPrincipal) {
                KerberosPrincipal kerberosPrincipal = (KerberosPrincipal) somePrincipal;
                LOGGER.logDebug(CLASSNAME, "KerberosPrincipal :: " + kerberosPrincipal.getName());
                String colleagueIDWithDomain = kerberosPrincipal.getName();
                String colleagueID = colleagueIDWithDomain.contains(DOMAIN_ATTR)
                        ? colleagueIDWithDomain.substring(0, colleagueIDWithDomain.indexOf(DOMAIN_ATTR))
                        : colleagueIDWithDomain;
                String colleagueDomain = colleagueIDWithDomain.contains(DOMAIN_ATTR) ? colleagueIDWithDomain
                        .substring(colleagueIDWithDomain.indexOf(DOMAIN_ATTR) + 1, colleagueIDWithDomain.contains(".")
                                ? colleagueIDWithDomain.indexOf(".") : colleagueIDWithDomain.length())
                        : null;
                LOGGER.logDebug(CLASSNAME, "KerberosPrincipal ColleagueID :: " + colleagueID != null ? colleagueID
                        : "colleagueID is null");
                LOGGER.logDebug(CLASSNAME, "KerberosPrincipal Domain" + colleagueDomain != null ? colleagueDomain
                        : "colleagueDomain is null");

                req.getSession().setAttribute(BranchContextConstants.COLLEGUE_ID, colleagueID);
                req.getSession().setAttribute(BranchContextConstants.COLLEAGUE_DOMAIN, colleagueDomain);
                accredited = true;
                break;

            } else {
                someNonKerberosPrincipalLocal = somePrincipal;
                LOGGER.logDebug(CLASSNAME, "Default Principal :: " + somePrincipal.getName());
            }
        }

        validatenonKerberosPricipal(req, accredited, someNonKerberosPrincipalLocal);
    }

    /**
     * @param req
     * @param accredited
     * @param someNonKerberosPrincipal
     */
    private void validatenonKerberosPricipal(HttpServletRequest req, boolean accredited,
            Principal someNonKerberosPrincipal) {
        if (!accredited) {
            if (null != someNonKerberosPrincipal) {
                String nonKerberosName = someNonKerberosPrincipal.getName();
                if (StringUtils.isNotEmpty(nonKerberosName) && nonKerberosName.indexOf(PRINCIPAL_SEPARATOR) != -1) {
                    String colleagueId = nonKerberosName.substring(nonKerberosName.indexOf(PRINCIPAL_SEPARATOR) + 1);
                    req.getSession().setAttribute(BranchContextConstants.COLLEGUE_ID, colleagueId);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.logDebug(nonKerberosName, colleagueId, "Non Kerberos Principal");
                    }
                }
            }

        }
    }

    public void init(FilterConfig arg0) throws ServletException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.logDebug(CLASSNAME, "Init", new Object[] { "Initiaised" });
        }
    }

}
