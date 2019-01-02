/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.mandate.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mandate.ValidateUserIdDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.mandate.ValidateUserIdDTO;
import com.lbg.ib.api.sales.mandate.domain.UserIdValidated;
import com.lbg.ib.api.sales.mandate.domain.UserIdValidation;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;

public class ValidateUserIdServiceImplTest {
    public static final ResponseError   RESPONSE_ERROR = new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE,
            "Service Unavailable");
    public static final Exception       EXCEPTION      = new RuntimeException();
    private ChannelBrandingDAO          channel        = mock(ChannelBrandingDAO.class);
    private ValidateUserIdDAO           dao            = mock(ValidateUserIdDAO.class);
    private SessionManagementDAO        session        = mock(SessionManagementDAO.class);
    private GalaxyErrorCodeResolver     resolver       = mock(GalaxyErrorCodeResolver.class);
    private LoggerDAO                   logger         = mock(LoggerDAO.class);
    private ArrangeToActivateParameters params;

    @Before
    public void setUp() {
        params = new ArrangeToActivateParameters();
        params.setOcisId("ocis");
        params.setPartyId("party");
    }

    @Test
    public void shouldReturnUserIdAvailableWhenDaoReturnsEmptySuggestions() throws Exception {
        when(session.getArrangeToActivateParameters()).thenReturn(params);
        when(channel.getChannelBrand()).thenReturn(withResult(new ChannelBrandDTO(null, null, "cid")));
        when(dao.validate(new ValidateUserIdDTO("ocis", "party", "cid", "passwd", "uname")))
                .thenReturn(withResult(Collections.<String> emptyList()));
        UserIdValidated validated = new ValidateUserIdServiceImpl(dao, channel, session, resolver, logger)
                .validate(new UserIdValidation("aid", "uname", "passwd"));
        assertThat(validated.isAvailable(), is(true));
        assertThat(validated.getName(), is(nullValue()));
    }

    @Test
    public void shouldReturn_3_AlternativeUserIdsWhenDaoReturnsEmptySuggestions() throws Exception {
        when(session.getArrangeToActivateParameters()).thenReturn(params);
        when(channel.getChannelBrand()).thenReturn(withResult(new ChannelBrandDTO(null, null, "cid")));
        when(dao.validate(new ValidateUserIdDTO("ocis", "party", "cid", "passwd", "uname")))
                .thenReturn(withResult(asList("n1", "n2", "n3", "n4")));
        UserIdValidated validated = new ValidateUserIdServiceImpl(dao, channel, session, resolver, logger)
                .validate(new UserIdValidation("aid", "uname", "passwd"));
        assertThat(validated.isAvailable(), is(false));
        assertThat(validated.getName(), is(asList("n1", "n2", "n3")));
    }

    @Test
    public void shouldReturnAllAlternativeUserIdsWhenDaoReturnsEmptySuggestionsLessThan_3() throws Exception {
        when(session.getArrangeToActivateParameters()).thenReturn(params);
        when(channel.getChannelBrand()).thenReturn(withResult(new ChannelBrandDTO(null, null, "cid")));
        when(dao.validate(new ValidateUserIdDTO("ocis", "party", "cid", "passwd", "uname")))
                .thenReturn(withResult(asList("n1")));
        UserIdValidated validated = new ValidateUserIdServiceImpl(dao, channel, session, resolver, logger)
                .validate(new UserIdValidation("aid", "uname", "passwd"));
        assertThat(validated.isAvailable(), is(false));
        assertThat(validated.getName(), is(asList("n1")));
    }

    @Test
    public void shouldThrowServiceExceptionWhenUserIdValidationDaoReturnsError() throws Exception {
        when(session.getArrangeToActivateParameters()).thenReturn(params);
        when(channel.getChannelBrand()).thenReturn(withResult(new ChannelBrandDTO(null, null, "cid")));
        when(dao.validate(new ValidateUserIdDTO("ocis", "party", "cid", "passwd", "uname")))
                .thenReturn(DAOResponse.<List<String>> withError(new DAOError("code", "message")));
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        try {
            new ValidateUserIdServiceImpl(dao, channel, session, resolver, logger)
                    .validate(new UserIdValidation("aid", "uname", "passwd"));
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(RESPONSE_ERROR));
        }
    }

    @Test
    public void shouldLogInCaseOfUnexpectedException() throws Exception {
        when(session.getArrangeToActivateParameters()).thenThrow(EXCEPTION);
        try {
            new ValidateUserIdServiceImpl(dao, channel, session, resolver, logger)
                    .validate(new UserIdValidation("aid", "uname", "passwd"));
            fail();
        } catch (ServiceException e) {
            verify(logger).logException(ValidateUserIdServiceImpl.class, EXCEPTION);
            assertThat(e.getResponseError(), is(resolver.resolve(ResponseErrorConstants.UNDEFINED_ERROR)));
        }

    }
}