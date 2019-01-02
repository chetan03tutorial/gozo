package com.lbg.ib.api.sales.application.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.lloydstsb.ea.filters.securityfilter.SecurityFilter;

public class CustomerSecurityFilter extends SecurityFilter {

    public void destroy() {
        super.destroy();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String url = ((HttpServletRequest) request).getRequestURL().toString();
            if (!url.contains("Tealeaf")) {
                super.doFilter(request, response, filterChain);
            } else {
                filterChain.doFilter(request, response);
            }
        }

    }

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

}
