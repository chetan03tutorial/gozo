package com.lbg.ib.api.sales.shared.invoker;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.dummy.Dummy;
import com.lbg.ib.api.sales.shared.exception.handlers.DAOExceptionMapper;

@RunWith(MockitoJUnitRunner.class)
public class SOAInvokerTest {

    @Mock
    private LoggerDAO          logger;

    @Mock
    private DAOExceptionMapper exceptionMapper;

    @Mock
    private ModuleContext      moduleContext;

    @InjectMocks
    private SOAInvokerImpl     soaInvoker;

    private ServiceException   serviceException = new ServiceException(
            new ResponseError("NoSuchMethod", "No Such Method"));

    private Class[]            argTypes;
    private Object[]           args;

    private Set<Object>        arguments;

    @Before
    public void setup() {
        when(moduleContext.getService(Dummy.class)).thenReturn(new Dummy());
        when(exceptionMapper.resolveServiceException(any(Exception.class), any(Class.class), anyString(), anyObject()))
                .thenReturn(serviceException);
        argTypes = new Class[1];
        argTypes[0] = String.class;
        args = new Object[1];
        args[0] = "Welcome Message";
        String welcome = "Welcome Message";
        arguments = new LinkedHashSet<Object>();
        arguments.add(welcome);
    }

    @Test
    public void testSuccessfulResponse() {
        String result = (String) soaInvoker.invoke(Dummy.class, "getResult", argTypes, args);
        assertNotNull(result);
    }

    @Test(expected = ServiceException.class)
    public void testNoSuchMethodException() {
        soaInvoker.invoke(Dummy.class, "unknownMethod", argTypes, args);
    }

    @Test(expected = ServiceException.class)
    public void testIllegalAccessException() {
        soaInvoker.invoke(Dummy.class, "privateMethod", argTypes, args);
    }

    @Test(expected = ServiceException.class)
    public void testInvocationTargetException() {
        soaInvoker.invoke(Dummy.class, "invocationTargetException", argTypes, args);
    }

}
