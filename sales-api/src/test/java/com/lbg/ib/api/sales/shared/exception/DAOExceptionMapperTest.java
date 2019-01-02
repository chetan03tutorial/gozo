package com.lbg.ib.api.sales.shared.exception;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.shared.exception.handlers.DAOExceptionMapper;
import com.sun.xml.bind.v2.schemagen.xmlschema.Any;

@RunWith(MockitoJUnitRunner.class)
public class DAOExceptionMapperTest {

    @Mock
    private DAOExceptionHandler     exceptionHandler;

    @Mock
    private GalaxyErrorCodeResolver resolver;

    @InjectMocks
    private DAOExceptionMapper      exceptionMapper;

    @Before
    public void setup() {

    }

    @Test
    public void shouldThrowServiceException() {
        DAOResponse.DAOError error = daoErrorWithErrorMessage();
        when(exceptionHandler.handleException(any(Exception.class), any(Class.class), anyString(), anyObject()))
                .thenReturn(error);
        when(resolver.customResolve(anyString(), anyString())).thenReturn(responseError());
        ServiceException serviceException = exceptionMapper.resolveServiceException(new Exception(),
                DAOExceptionMapper.class, "mapException", new Object());
        assertNotNull(serviceException);
    }

    @Test
    public void shouldThrowServiceExceptionWithoutMessage() {
        DAOResponse.DAOError error = daoErrorWithoutErrorMessage();
        when(exceptionHandler.handleException(any(Exception.class), any(Class.class), anyString(), anyObject()))
                .thenReturn(error);
        when(resolver.resolve(anyString())).thenReturn(responseError());
        ServiceException serviceException = exceptionMapper.resolveServiceException(new Exception(),
                DAOExceptionMapper.class, "mapException", new Object());
        assertNotNull(serviceException);
    }

    private DAOError daoErrorWithErrorMessage() {
        DAOResponse.DAOError daoError = new DAOError("200", "Exception");
        return daoError;
    }

    private DAOError daoErrorWithoutErrorMessage() {
        DAOResponse.DAOError daoError = new DAOError("200", null);
        return daoError;
    }

    private ResponseError responseError() {
        ResponseError responseError = new ResponseError("code", "message");
        return responseError;
    }
}
