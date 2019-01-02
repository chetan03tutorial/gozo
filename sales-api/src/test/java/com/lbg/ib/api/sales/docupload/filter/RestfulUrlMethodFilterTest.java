/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.  
 *   
 * All Rights Reserved.   
 *  
 * Class Name: RestfulUrlMethodFilterTest  
 *   
 * Author(s): Parameshwaran Kangamuthu
 *  
 * Date: 19 Apr 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import javax.servlet.FilterConfig;

import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lloydstsb.rest.common.filter.RestfulUrlMethodTransformer;
import com.lloydstsb.rest.common.marshaller.ObjectMarshaller;

/**
 * @author 1146728
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class RestfulUrlMethodFilterTest {
    @InjectMocks
    RestfulUrlMethodFilter      restfulUrlMethodFilter;

    @Mock
    RestfulUrlMethodTransformer restfulUrlMethodtransformer;

    @Mock
    ObjectMarshaller            objectMarshaller;

    @Mock
    HttpServletRequest          servletRequest;

    @Mock
    HttpServletResponse         servletResponse;

    @Mock
    FilterChain                 filterChain;

    @Mock
    ServletRequest              request;

    @Mock
    FilterConfig                filterConfig;

    @Test
    public void shouldReturnExpecteddoFilterMethod() throws DocUploadServiceException, IOException, ServletException {
        Mockito.when(restfulUrlMethodtransformer.checkForMethodParameterAndWrapRequest(servletRequest))
                .thenReturn(request);
        restfulUrlMethodFilter.doFilter(servletRequest, servletResponse, filterChain);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnUnExpecteddoFilterMethod() throws DocUploadServiceException, IOException, ServletException {
        Mockito.when(restfulUrlMethodtransformer.checkForMethodParameterAndWrapRequest(servletRequest))
                .thenThrow(IllegalArgumentException.class);
        restfulUrlMethodFilter.doFilter(servletRequest, servletResponse, filterChain);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnServletException() throws IOException, ServletException {
        Mockito.when(restfulUrlMethodtransformer.checkForMethodParameterAndWrapRequest(servletRequest))
                .thenThrow(ServletException.class);
        restfulUrlMethodFilter.doFilter(servletRequest, servletResponse, filterChain);
    }

    @Test
    public void shouldReturnExpectedInit() throws ServletException {
        restfulUrlMethodFilter.init(filterConfig);
    }

    @Test
    public void shouldReturnExpectedDestroy() throws ServletException {
        restfulUrlMethodFilter.destroy();
    }

}
