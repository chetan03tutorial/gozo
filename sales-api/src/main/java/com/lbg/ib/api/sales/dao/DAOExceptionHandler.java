package com.lbg.ib.api.sales.dao;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.GENERAL_EXCEPTION;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.REMOTE_EXCEPTION;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.RESOURCE_NOT_AVAILABLE;
import static com.lbg.ib.api.shared.util.logger.ServiceLogger.formatMessage;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.join;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.ws.webservices.engine.WebServicesFault;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.constants.DAOErrorConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.product.domain.pending.ArrangementId;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.DatabaseServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.InternalServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ResourceNotAvailableError;

/**
 * Owner of the various strategies mapping the Exception type to the
 * ExceptionHandler. Each ExceptionHandler knows how to log the exceptions and
 * create a DAOError for it.
 */
@Component
public class DAOExceptionHandler {

    public static final HashMap<Class, ExceptionHandler> EXCEPTION_HANDLERS = createMapOfExceptionHandlers();

    private static HashMap<Class, ExceptionHandler> createMapOfExceptionHandlers() {
        HashMap<Class, ExceptionHandler> exceptionHandlers = new HashMap<Class, ExceptionHandler>();
        exceptionHandlers.put(ExternalBusinessError.class, new ExternalBusinessErrorHandler());
        exceptionHandlers.put(ExternalServiceError.class, new ExternalServiceErrorHandler());
        exceptionHandlers.put(InternalServiceError.class, new InternalServiceErrorHandler());
        exceptionHandlers.put(DatabaseServiceError.class, new DatabaseServiceErrorHandler());
        exceptionHandlers.put(ResourceNotAvailableError.class, new ResourceNotAvailableErrorHandler());
        exceptionHandlers.put(RemoteException.class, new RemoteExceptionHandler());
        exceptionHandlers.put(WebServicesFault.class, new WebServicesFaultHandler());
        return exceptionHandlers;
    }

    @Autowired
    private LoggerDAO logger;

    @TraceLog
    public DAOError handleException(Exception e, Class<?> className, String methodName, Object requestDTO) {
        if (EXCEPTION_HANDLERS.containsKey(e.getClass())) {
            ExceptionHandler exceptionHandler = EXCEPTION_HANDLERS.get(e.getClass());
            return exceptionHandler.handleException(e, logger, className, methodName, requestDTO);
        } else {
            logger.logException(className, e);
            return new DAOError(GENERAL_EXCEPTION, "General exception occurred in " + methodName);
        }
    }

    private interface ExceptionHandler<T extends Exception> {
        DAOError handleException(T exception, LoggerDAO logger, Class<?> className, String methodName,
                Object requestDTO);

    }

    private static class ExternalBusinessErrorHandler implements ExceptionHandler<ExternalBusinessError> {

        public DAOError handleException(ExternalBusinessError e, LoggerDAO logger, Class<?> className,
                String methodName, Object requestDTO) {
            logger.logError(e.getReasonCode(),
                    generateErrorMessage(e.getReasonText(), e.getDescription(), e.getMessage(), methodName, requestDTO),
                    className);
            return new DAOError(e.getReasonCode(), e.getReasonText());
        }
    }

    private static class ExternalServiceErrorHandler implements ExceptionHandler<ExternalServiceError> {

        public DAOError handleException(ExternalServiceError e, LoggerDAO logger, Class<?> className, String methodName,
                Object requestDTO) {
            logger.logError(e.getReasonCode(),
                    generateErrorMessage(e.getReasonText(), e.getDescription(), e.getMessage(), methodName, requestDTO),
                    className);
            return new DAOError(e.getReasonCode(), e.getReasonText());

        }
    }

    private static class InternalServiceErrorHandler implements ExceptionHandler<InternalServiceError> {

        public DAOError handleException(InternalServiceError e, LoggerDAO logger, Class<?> className, String methodName,
                Object requestDTO) {
            logger.logError(e.getReasonCode(),
                    generateErrorMessage(e.getReasonText(), e.getDescription(), e.getMessage(), methodName, requestDTO),
                    className);
            if ("".equals(e.getReasonCode()) && (requestDTO instanceof ArrangementId)
                    && e.getDescription().contains("does not belong to")) {
                return new DAOError(DAOErrorConstants.INTERNAL_SERVICE_WRONG_BRAND, e.getDescription());
            }
            return new DAOError(e.getReasonCode(), e.getReasonText());

        }
    }

    private static class DatabaseServiceErrorHandler implements ExceptionHandler<DatabaseServiceError> {

        public DAOError handleException(DatabaseServiceError e, LoggerDAO logger, Class<?> className, String methodName,
                Object requestDTO) {
            logger.logError(e.getEntity(),
                    generateErrorMessage(null, e.getDescription(), e.getMessage(), methodName, requestDTO), className);
            return new DAOError(e.getEntity(), e.getDescription());
        }
    }

    private static class ResourceNotAvailableErrorHandler implements ExceptionHandler<ResourceNotAvailableError> {

        public DAOError handleException(ResourceNotAvailableError e, LoggerDAO logger, Class<?> className,
                String methodName, Object requestDTO) {
            logger.logError(RESOURCE_NOT_AVAILABLE,
                    formatMessage(join(asList(e.getDescription(), e.getMessage()), ","), methodName, requestDTO),
                    className);
            return new DAOError(RESOURCE_NOT_AVAILABLE, e.getDescription());

        }
    }

    private static class RemoteExceptionHandler implements ExceptionHandler<RemoteException> {

        public DAOError handleException(RemoteException e, LoggerDAO logger, Class<?> className, String methodName,
                Object requestDTO) {
            logger.logException(className, e);
            return new DAOError(REMOTE_EXCEPTION, "Remote connection exception in " + methodName);
        }
    }

    private static class WebServicesFaultHandler implements ExceptionHandler<WebServicesFault> {
        public DAOError handleException(WebServicesFault e, LoggerDAO logger, Class<?> className, String methodName,
                Object requestDTO) {
            return new DAOError(GENERAL_EXCEPTION, "General exception occurred in " + e.getFaultString());
        }
    }

    private static String generateErrorMessage(String reasonText, String errorDesc, String errorMessage,
            String methodName, Object requestDTO) {
        return formatMessage(join(asList(reasonText, errorDesc, errorMessage), ","), methodName, requestDTO);
    }
}
