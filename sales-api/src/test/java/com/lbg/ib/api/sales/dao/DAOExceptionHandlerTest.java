package com.lbg.ib.api.sales.dao;

import java.rmi.RemoteException;

import com.lbg.ib.api.shared.domain.DAOResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ibm.ws.webservices.engine.WebServicesFault;
import com.lbg.ib.api.sales.dao.constants.DAOErrorConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.DatabaseServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.InternalServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ResourceNotAvailableError;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.ResponseHeader;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class DAOExceptionHandlerTest {

    public static final Class<String> IN_PARAM_CLASS_NAME  = String.class;
    public static final String        IN_PARAM_METHOD_NAME = "methodName";
    public static final String        IN_PARAM_REQUEST_DTO = "RequestDTO";

    private final ResponseHeader      unused               = null;

    @Mock
    private LoggerDAO                 logger;

    @InjectMocks
    private DAOExceptionHandler       daoExceptionHandler;

    @Test
    public void handleExternalBusinessError() throws Exception {

        ExternalBusinessError expectedException = new ExternalBusinessError(unused, "baseFaultDesc", "reasonCode",
                "reasonText");

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, expectedException.getReasonCode(), expectedException.getReasonText());

        verifyLoggedErrorMessage(expectedException.getReasonCode(),
                expectedLoggedErrorMessage(expectedException.getReasonText(), expectedException.getDescription()));
    }

    @Test
    public void handleExternalServiceError() throws Exception {

        ExternalServiceError expectedException = new ExternalServiceError(unused, "baseFaultDesc", "reasonCode",
                "reasonText");

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, expectedException.getReasonCode(), expectedException.getReasonText());

        verifyLoggedErrorMessage(expectedException.getReasonCode(),
                expectedLoggedErrorMessage(expectedException.getReasonText(), expectedException.getDescription()));
    }

    @Test
    public void handleInternalServiceErrorWithErrorCodeEmpty() throws Exception {

        InternalServiceError expectedException = new InternalServiceError(unused, "baseFaultDesc", "", "reasonText");

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, expectedException.getReasonCode(), expectedException.getReasonText());

        verifyLoggedErrorMessage(expectedException.getReasonCode(),
                expectedLoggedErrorMessage(expectedException.getReasonText(), expectedException.getDescription()));
    }

    @Test
    public void handleInternalServiceError() throws Exception {

        InternalServiceError expectedException = new InternalServiceError(unused, "baseFaultDesc", "reasonCode",
                "reasonText");

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, expectedException.getReasonCode(), expectedException.getReasonText());

        verifyLoggedErrorMessage(expectedException.getReasonCode(),
                expectedLoggedErrorMessage(expectedException.getReasonText(), expectedException.getDescription()));
    }

    @Test
    public void handleDatabaseServiceError() throws Exception {

        DatabaseServiceError expectedException = new DatabaseServiceError(unused, "baseFaultDesc", "entity", "field",
                "key");

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, expectedException.getEntity(), expectedException.getDescription());

        verifyLoggedErrorMessage(expectedException.getEntity(),
                "," + expectedException.getDescription() + ",, " + expectedErrorMessageTail());
    }

    @Test
    public void handleResourceNotAvailableError() throws Exception {

        ResourceNotAvailableError expectedException = new ResourceNotAvailableError(unused, "baseFaultDesc");

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, DAOErrorConstants.RESOURCE_NOT_AVAILABLE,
                expectedException.getDescription());

        verifyLoggedErrorMessage(DAOErrorConstants.RESOURCE_NOT_AVAILABLE,
                expectedException.getDescription() + ",, " + expectedErrorMessageTail());
    }

    @Test
    public void handlerRemoteException() throws Exception {

        RemoteException expectedException = new RemoteException();

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, DAOErrorConstants.REMOTE_EXCEPTION,
                "Remote connection exception in " + IN_PARAM_METHOD_NAME);

        Mockito.verify(logger).logException(IN_PARAM_CLASS_NAME, expectedException);
    }

    @Test
    public void handlerWebServicesFault() throws Exception {

        WebServicesFault expectedException = new WebServicesFault();

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, "9182301", "General exception occurred in ");

    }

    @Test
    public void handlerGeneralExceptionIsTreatedAsDefault() throws Exception {

        Exception expectedException = new NullPointerException();

        DAOResponse.DAOError returnedDaoError = whenHandleExceptionIsCalledWith(expectedException);

        verifyReturnedDaoError(returnedDaoError, DAOErrorConstants.GENERAL_EXCEPTION,
                "General exception occurred in " + IN_PARAM_METHOD_NAME);
    }

    private DAOResponse.DAOError whenHandleExceptionIsCalledWith(Exception expectedException) {
        return daoExceptionHandler.handleException(expectedException, IN_PARAM_CLASS_NAME, IN_PARAM_METHOD_NAME,
                IN_PARAM_REQUEST_DTO);
    }

    private void verifyReturnedDaoError(DAOResponse.DAOError returnedDaoError, String reasonCode, String reasonText) {
        Assert.assertEquals(returnedDaoError.getErrorCode(), reasonCode);
        Assert.assertEquals(returnedDaoError.getErrorMessage(), reasonText);

    }

    private void verifyLoggedErrorMessage(String errorCode, String expectedErrorMessage) {
        Mockito.verify(logger).logError(errorCode, expectedErrorMessage, IN_PARAM_CLASS_NAME);
    }

    private String expectedLoggedErrorMessage(String reasonText, String description) {
        return reasonText + "," + description + ",, " + expectedErrorMessageTail();
    }

    private String expectedErrorMessageTail() {
        return "Called Method: " + IN_PARAM_METHOD_NAME + ", Method Call Params: " + IN_PARAM_REQUEST_DTO;
    }

}