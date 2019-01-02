package com.lbg.ib.api.sales.shared.exception.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;

@Component
public class DAOExceptionMapper {

    @Autowired
    private DAOExceptionHandler     exceptionHandler;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    public ServiceException resolveServiceException(Exception ex, Class<?> className, String methodName,
            Object request) {
        DAOResponse.DAOError daoError = exceptionHandler.handleException(ex, className, methodName, request);
        if (daoError.getErrorMessage() != null) {
            return new ServiceException(resolver.customResolve(daoError.getErrorCode(), daoError.getErrorMessage()));
        } else {
            return new ServiceException(resolver.resolve(daoError.getErrorCode()));
        }
    }

}
