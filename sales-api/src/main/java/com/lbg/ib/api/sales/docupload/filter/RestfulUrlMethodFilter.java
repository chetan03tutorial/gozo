/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.  
 *   
 * All Rights Reserved.   
 *  
 * Class Name: RestfulUrlMethodFilter  
 *   
 * Author(s): Parameshwaran Kangamuthu
 *  
 * Date: 19 Apr 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.JsonMappingException;

import com.lloydstsb.rest.common.filter.RestfulUrlMethodTransformer;
import com.lloydstsb.rest.common.marshaller.ObjectMarshaller;
import com.lloydstsb.rest.v1.valueobjects.Error;
import com.lloydstsb.rest.v1.valueobjects.Error.ErrorCategory;

public class RestfulUrlMethodFilter implements Filter {

    private RestfulUrlMethodTransformer restfulUrlMethodTransformer;

    private ObjectMarshaller            objectMarshaller;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ServletRequest wrappedRequest = null;
        try {
            /*
             * httpResponse.addHeader("Access-Control-Allow-Origin", "*");
             * httpResponse.addHeader("Access-Control-Allow-Methods",
             * "GET,POST,PUT,DELETE");
             * httpResponse.addHeader("Access-Control-Allow-Headers",
             * "Content-Type");
             * httpResponse.addHeader("Access-Control-Allow-Credentials",
             * "true");
             */
            wrappedRequest = restfulUrlMethodTransformer.checkForMethodParameterAndWrapRequest(request);
        } catch (IllegalArgumentException e) {
            httpResponse.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            marshallExceptionToErrorReponse(httpRequest, httpResponse, e, ErrorCategory.BAD_REQUEST);
            return;
        } catch (Exception t) {
            httpResponse.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            marshallExceptionToErrorReponse(httpRequest, httpResponse, t, ErrorCategory.TRY_AGAIN);
            return;
        }
        filterChain.doFilter(wrappedRequest, response);
    }

    private void marshallExceptionToErrorReponse(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Throwable t, ErrorCategory errorCategory) throws IOException, JsonMappingException {
        Error error = new Error("", t.getMessage(), errorCategory);
        objectMarshaller.marshallObjectToResponse(httpRequest, httpResponse, error);
    }

    public void destroy() {
        // nothing to be done
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.restfulUrlMethodTransformer = new RestfulUrlMethodTransformer();
        this.objectMarshaller = new ObjectMarshaller();
    }
}
