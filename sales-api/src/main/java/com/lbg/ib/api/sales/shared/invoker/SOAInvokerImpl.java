package com.lbg.ib.api.sales.shared.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.exception.handlers.DAOExceptionMapper;

@Component
public class SOAInvokerImpl implements SOAInvoker {

    @Autowired
    private DAOExceptionMapper exceptionMapper;
    @Autowired
    private LoggerDAO          logger;
    @Autowired
    private ModuleContext      moduleContext;
    public static final String EXTERNAL_SERVICE_UNAVAILABLE         = "600140";
    public static final String EXTERNAL_SERVICE_UNAVAILABLE_MESSAGE = "Error while invoking external service";

    @TraceLog
    public <T> Object invoke(final Class<T> clazz, String methodName, Class[] argType, Object[] args) {
        Object result = null;
        T serviceInterface = moduleContext.getService(clazz);
        try {
            Method method = clazz.getDeclaredMethod(methodName, argType);
            result = method.invoke(serviceInterface, args);
        } catch (NoSuchMethodException ex) {
            logger.traceLog(this.getClass(), ex);
            throw new ServiceException(
                    new ResponseError(EXTERNAL_SERVICE_UNAVAILABLE, EXTERNAL_SERVICE_UNAVAILABLE_MESSAGE));
        } catch (IllegalAccessException ex) {
            logger.traceLog(this.getClass(), ex);
            throw new ServiceException(
                    new ResponseError(EXTERNAL_SERVICE_UNAVAILABLE, EXTERNAL_SERVICE_UNAVAILABLE_MESSAGE));
        } catch (InvocationTargetException ex) {
            logger.traceLog(this.getClass(), ex);
            logger.traceLog(this.getClass(), ex.getTargetException());
            logger.logException(this.getClass(), (Exception) ex.getTargetException());
            throw exceptionMapper.resolveServiceException((Exception) ex.getTargetException(), clazz, methodName, args);
        } catch (Exception ex) {
            logger.traceLog(this.getClass(), ex);
            logger.logException(this.getClass(), ex);
            throw exceptionMapper.resolveServiceException(ex, clazz, methodName, args[0]);
        }
        return result;
    }
}
