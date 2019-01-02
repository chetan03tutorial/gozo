/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.mandate.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.mandate.domain.UserIdValidation;
import com.lbg.ib.api.sales.mandate.service.ValidateUserIdService;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

public class ValidateUserIdResourceTest {

    @InjectMocks
    ValidateUserIdResource          validateUserIdResource = new ValidateUserIdResource();;

    @Mock
    private RequestBodyResolver     resolver;

    @Mock
    private FieldValidator          fieldValidator;

    @Mock
    private ValidateUserIdService   service;

    @Mock
    private GalaxyErrorCodeResolver errorResolver;

    private LoggerDAO               logger                 = mock(LoggerDAO.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateUserIdAndPasswordTest() throws Exception {
        when(resolver.resolve(any(String.class), eq(UserIdValidation.class))).thenReturn(new UserIdValidation());
        service.validate(new UserIdValidation());
        ValidateUserIdResource resource = new ValidateUserIdResource(resolver, service, errorResolver, logger);
        Response response = resource.validate("Request");
        assertEquals(response.getStatus(), 200);
    }

    @Test
    public void shouldThrowInvalidFormatExceptionIfValidationErrorIsPresent() throws Exception {

        when(resolver.resolve(any(String.class), eq(UserIdValidation.class)))
                .thenThrow(new InvalidFormatException("Incorrect Data"));
        ResponseError error = new ResponseError("1234", "error");
        when(errorResolver.createResponseError(ResponseErrorConstants.BAD_REQUEST_FORMAT)).thenReturn(error);
        Response response = validateUserIdResource.validate("Request");
        assertEquals(((ResponseError) response.getEntity()).getCode(), "1234");

    }

}
