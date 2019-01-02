package com.lbg.ib.api.sales.user.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.UserDetails;
import com.lbg.ib.api.sales.user.service.PartyDetailService;
import com.lbg.ib.api.sales.user.service.UserService;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sso.domain.user.Arrangement;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    @Mock
    private UserService userService;

    @Mock
    private LoggerDAO logger = mock(LoggerDAO.class);

    @Mock
    private ModuleContext moduleContext;

    @Mock
    private PartyDetailService partyDetailService;

    @Mock
    private RequestBodyResolver requestResolver;

    @InjectMocks
    private UserResource resource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(moduleContext.getService(UserService.class)).thenReturn(userService);
    }

    @Test
    public void shouldReturnUserInfoInResponseWhenFetchUserInfoIsCalledAndUserInfoIsFound()
            throws ServiceException, InvalidFormatException {
        Arrangement userInfo = new Arrangement();
        when(userService.fetchUserInfo(false)).thenReturn(userInfo);
        Response response = resource.fetchUserInfo(null, null);
        assertThat(response.getStatus(), is(200));
        assertEquals(userInfo, response.getEntity());
    }

    @Test
    public void shouldReturnUserInfoInResponseWhenFetchUserInfoForGoJointIsCalledAndUserInfoIsFound()
            throws ServiceException, InvalidFormatException {
        Arrangement userInfo = new Arrangement();
        when(userService.fetchUserInfo(true)).thenReturn(userInfo);
        Response response = resource.fetchUserInfoForGoJoint(null, null);
        assertThat(response.getStatus(), is(200));
        assertEquals(userInfo, response.getEntity());
    }

    @Test
    public void shouldReturnUserNotFoundErrorWhenUserInfoIsCalledAndUserNotFound()
            throws ServiceException, InvalidFormatException {
        when(userService.fetchUserInfo(false)).thenReturn(null);
        Response response = resource.fetchUserInfo(null, null);
        assertThat(response.getStatus(), is(200));
        Object entity = response.getEntity();
        assertTrue(entity instanceof ResponseError);
        ResponseError responseError = (ResponseError) entity;
        assertThat(responseError.getCode(), is("9900004"));
        assertThat(responseError.getMessage(), is("Cannot find user Info"));
    }

    @Test
    public void shouldReturnUserNotFoundErrorWhenUserInfoForGoJointIsCalledAndUserNotFound()
            throws ServiceException, InvalidFormatException {
        when(userService.fetchUserInfo(true)).thenReturn(null);
        Response response = resource.fetchUserInfoForGoJoint(null, null);
        assertThat(response.getStatus(), is(200));
        Object entity = response.getEntity();
        assertTrue(entity instanceof ResponseError);
        ResponseError responseError = (ResponseError) entity;
        assertThat(responseError.getCode(), is("9900004"));
        assertThat(responseError.getMessage(), is("Cannot find user Info"));
    }

    @Test
    public void fetchUserInfoSuccess() throws ServiceException, InvalidFormatException {
        UserDetails userInfo = new UserDetails();
        String requestBody = "OWEGXWFPRK2YXZKXKZHXIFMIFNOP2AT5XROC64HIEDP3L3Q6BNDQ";
        when(userService.fetchUserInfo("C", requestBody, false)).thenReturn(userInfo);
        Response response = resource.fetchUserInfo("1", requestBody);
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void fetchUserInfoForGoJointSuccess() throws ServiceException, InvalidFormatException {
        UserDetails userInfo = new UserDetails();
        String requestBody = "OWEGXWFPRK2YXZKXKZHXIFMIFNOP2AT5XROC64HIEDP3L3Q6BNDQ";
        when(userService.fetchUserInfo("C", requestBody, true)).thenReturn(userInfo);
        Response response = resource.fetchUserInfoForGoJoint("1", requestBody);
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void fetchUserInfoFail() throws ServiceException, InvalidFormatException {
        when(userService.fetchUserInfo("C", null, false)).thenReturn(null);
        Response response = resource.fetchUserInfo(null, null);
        assertThat(response.getStatus(), is(200));
        Object entity = response.getEntity();
        assertTrue(entity instanceof ResponseError);
        ResponseError responseError = (ResponseError) entity;
        assertThat(responseError.getCode(), is("9900004"));
        assertThat(responseError.getMessage(), is("Cannot find user Info"));
    }

    @Test
    public void fetchUserInfoForGoJointFail() throws ServiceException, InvalidFormatException {
        when(userService.fetchUserInfo("C", null, true)).thenReturn(null);
        Response response = resource.fetchUserInfoForGoJoint(null, null);
        assertThat(response.getStatus(), is(200));
        Object entity = response.getEntity();
        assertTrue(entity instanceof ResponseError);
        ResponseError responseError = (ResponseError) entity;
        assertThat(responseError.getCode(), is("9900004"));
        assertThat(responseError.getMessage(), is("Cannot find user Info"));
    }

    @Test
    public void shouldRetrieveUserDemographicDetails() throws InvalidFormatException {
        when(moduleContext.getService(PartyDetailService.class)).thenReturn(partyDetailService);
        when(partyDetailService.retrievePartyDetails()).thenReturn(partyDetails());
        Response response = resource.retrieveInvolvedPartyDemographicDetails();
        PartyDetails party = (PartyDetails) response.getEntity();
        assertEquals("IAMDANGEROUS", party.getFirstName());
    }

    private PartyDetails partyDetails() {
        PartyDetails partyDetails = new PartyDetails();
        partyDetails.setFirstName("IAMDANGEROUS");
        return partyDetails;
    }
}
