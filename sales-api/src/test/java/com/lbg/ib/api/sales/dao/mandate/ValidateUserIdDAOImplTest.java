/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.mandate;

import static com.lbg.ib.api.sales.dao.mandate.ValidateUserIdDAOImpl.CANNOT_CONNECT_TO_REMOTE_POINT;
import static java.lang.Integer.parseInt;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.mandate.ValidateUserIdDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.ApplicationPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.StB802AUseridValidate;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.StB802BUseridValidate;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;

@RunWith(MockitoJUnitRunner.class)
public class ValidateUserIdDAOImplTest {

    @InjectMocks
    private ValidateUserIdDAOImpl       validateUserIdDAOImpl;

    @Mock
    private ApplicationPortType         applicationPortType;

    @Mock
    private LoggerDAO                   logger;

    @Mock
    private FoundationServerUtil        foundationServerUtil;

    @Mock
    private SessionManagementDAO        sessionManagementDAO;

    @Mock
    private UserContext                 userContext;

    @Mock
    private StHeader                    stHeader;

    public static final String          CHANNEL_ID       = "IBS";
    public static final String          PASSWORD         = "pwd";
    public static final String          USERNAME         = "uname";
    public static final String          ERROR_CODE       = "1312312";
    public static final String          ERROR_MESSAGE    = "message";
    public static final RemoteException REMOTE_EXCEPTION = new RemoteException();
    public static final String          OCIS             = "793194257";
    public static final String          PARTY            = "+00556861003";

    @Before
    public void setUp() {
        when(sessionManagementDAO.getUserContext()).thenReturn(userContext);
        when(foundationServerUtil.createStHeader(userContext)).thenReturn(stHeader);
    }

    @Test
    public void shouldReturnWithSuggestedNames() throws Exception {
        when(applicationPortType.b802UseridValidate(argThat(expectedRequest()))).thenReturn(listOfSuggestedResponse());
        DAOResponse<List<String>> suggestions = validateUserIdDAOImpl
                .validate(new ValidateUserIdDTO(OCIS, PARTY, CHANNEL_ID, PASSWORD, USERNAME));
        assertThat(suggestions.getResult().get(0), is("suggested1"));
    }

    @Test
    public void shouldReturnEmptySuggestionWhenTheValueIsNull() throws Exception {
        when(applicationPortType.b802UseridValidate(argThat(expectedRequest())))
                .thenReturn(listOfSuggestionsValueIsNull());
        DAOResponse<List<String>> suggestions = validateUserIdDAOImpl
                .validate(new ValidateUserIdDTO(OCIS, PARTY, CHANNEL_ID, PASSWORD, USERNAME));
        assertThat(suggestions.getResult().isEmpty(), is(true));
    }

    @Test
    public void shouldReturnEmptySuggestionWhenTheValueIsEmptyArray() throws Exception {
        when(applicationPortType.b802UseridValidate(argThat(expectedRequest())))
                .thenReturn(listOfSuggestionsValueIsEmptyArray());
        DAOResponse<List<String>> suggestions = validateUserIdDAOImpl
                .validate(new ValidateUserIdDTO(OCIS, PARTY, CHANNEL_ID, PASSWORD, USERNAME));
        assertThat(suggestions.getResult().isEmpty(), is(true));
    }

    @Test
    public void shouldReturnErrorResponseWhenServiceReturnsError() throws Exception {
        when(applicationPortType.b802UseridValidate(argThat(expectedRequest())))
                .thenReturn(errorResponse(ERROR_CODE, ERROR_MESSAGE));
        DAOResponse<List<String>> suggestions = validateUserIdDAOImpl
                .validate(new ValidateUserIdDTO(OCIS, PARTY, CHANNEL_ID, PASSWORD, USERNAME));
        assertThat(suggestions.getError(), is(new DAOError(ERROR_CODE, ERROR_MESSAGE)));
        verify(logger).logError(eq(ERROR_CODE), any(String.class), eq(ValidateUserIdDAOImpl.class));
    }

    @Test
    public void shouldReturnErrorAndLogWhenTheServiceThrowsRemoteException() throws Exception {
        when(applicationPortType.b802UseridValidate(argThat(expectedRequest()))).thenThrow(REMOTE_EXCEPTION);
        DAOResponse<List<String>> suggestions = validateUserIdDAOImpl
                .validate(new ValidateUserIdDTO(OCIS, PARTY, CHANNEL_ID, PASSWORD, USERNAME));
        assertThat(suggestions.getError(), is(new DAOError(CANNOT_CONNECT_TO_REMOTE_POINT,
                "Cannot connect to remote location for ValidateUserID service")));
        verify(logger).logException(ValidateUserIdDAOImpl.class, REMOTE_EXCEPTION);
    }

    @Test
    public void shouldNotContainPasswordInValidateUserIdDToString() {
        ValidateUserIdDTO validateUserIdDto = new ValidateUserIdDTO(OCIS, PARTY, CHANNEL_ID, PASSWORD, USERNAME);
        assertFalse(validateUserIdDto.toString().contains("password"));
    }

    private StB802BUseridValidate errorResponse(String errorCode, String message) {
        StB802BUseridValidate response = new StB802BUseridValidate();
        StError error = new StError();
        error.setErrorno(parseInt(errorCode));
        error.setErrormsg(message);
        response.setSterror(error);
        return response;
    }

    private StB802BUseridValidate listOfSuggestionsValueIsEmptyArray() {
        StB802BUseridValidate validate = new StB802BUseridValidate();
        validate.setAstpartyUseridSuggested(new StParty[] {});
        return validate;
    }

    private StB802BUseridValidate listOfSuggestionsValueIsNull() {
        StB802BUseridValidate validate = new StB802BUseridValidate();
        validate.setAstpartyUseridSuggested(null);
        return validate;
    }

    private StB802BUseridValidate listOfSuggestedResponse() {
        StB802BUseridValidate response = new StB802BUseridValidate();
        StParty stParty = new StParty();
        stParty.setPartyid("suggested1");
        response.setAstpartyUseridSuggested(new StParty[] { stParty });
        return response;
    }

    private ArgumentMatcher<StB802AUseridValidate> expectedRequest() {
        return new ArgumentMatcher<StB802AUseridValidate>() {
            @Override
            public boolean matches(Object o) {
                StB802AUseridValidate request = (StB802AUseridValidate) o;
                return request.getChanid().equals(CHANNEL_ID) && request.getStheader().equals(stHeader) &&

                        !request.isBConnectedMandate() &&

                        request.getStpartyResolved().getHost().equals("T")
                        && request.getStpartyResolved().getPartyid().equals(PARTY)
                        && request.getStpartyResolved().getOcisid().equals(new BigInteger(OCIS)) &&

                        request.getStpartyUseridEntered().getHost().equals("T")
                        && request.getStpartyUseridEntered().getPartyid().equals(USERNAME)
                        && request.getPasswordClear().equals(PASSWORD);

            }
        };
    }
}