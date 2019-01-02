package com.lbg.ib.api.sales.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.StB277BAccPartiesReadJP;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.TAccPartyOtherJP;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.UserContext;

@RunWith(MockitoJUnitRunner.class)
public class AccountPortTypeServiceTest {

    @InjectMocks
    private AccountPortTypeServiceImpl accountPortTypeService;
    /**
     * Object of foundationServerUtil.
     */
    @Mock
    private FoundationServerUtil foundationServerUtil;
    /**
     * Object of session management.
     */
    @Mock
    private SessionManagementDAO session;
    /**
     * Object of resolver.
     */
    @Mock
    private GalaxyErrorCodeResolver resolver;
    /**
     * Object of logger.
     */
    @Mock
    private LoggerDAO logger;

    @Mock
    private SOAInvoker bapiInvoker;

    @Before
    public void setup() {
        when(foundationServerUtil.createStHeader(any(UserContext.class))).thenReturn(stHeader());
        when(session.getUserInfo()).thenReturn(arrangement());
        when(resolver.resolve(anyString())).thenReturn(new ResponseError("code", "message"));
    }

    @Test
    public void shouldReturnPartyWithNoError() {
        Account account = new Account();
        account.setAccountNumber("73850068");
        account.setSortCode("774042");
        when(bapiInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
                .thenReturn(b277ResponseWithNoMoreResult());
        List<String> otherPartiesOcisId = accountPortTypeService.retrieveJointPartyOcisIds(account);
        assertNotNull(otherPartiesOcisId);
        assertEquals(1, otherPartiesOcisId.size());
        assertEquals("7890", otherPartiesOcisId.get(0));
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnPartyWithError() {
        Account account = new Account();
        account.setAccountNumber(null);
        account.setSortCode("774042");
        when(bapiInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
                .thenReturn(b277ResponseWithNoMoreResult());
        List<String> otherPartiesOcisId = accountPortTypeService.retrieveJointPartyOcisIds(account);
        assertNotNull(otherPartiesOcisId);
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnPartyWithError2() {
        Account account = new Account();
        account.setAccountNumber("774043");
        account.setSortCode("774042");
        when(bapiInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
                .thenReturn(b277ResponseWithError());
        List<String> otherPartiesOcisId = accountPortTypeService.retrieveJointPartyOcisIds(account);
        assertNotNull(otherPartiesOcisId);
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnPartyWithError1() {
        Account account = new Account();
        account.setAccountNumber(null);
        account.setSortCode("774042");
        when(bapiInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
                .thenReturn(null);
        List<String> otherPartiesOcisId = accountPortTypeService.retrieveJointPartyOcisIds(account);
        assertNotNull(otherPartiesOcisId);
    }

    private StHeader stHeader() {
        StHeader stHeader = new StHeader();
        StParty stParty = new StParty();
        stParty.setPartyid("1000");
        stParty.setOcisid(new BigInteger("10"));
        stHeader.setStpartyObo(stParty);
        return stHeader;
    }

    private Arrangement arrangement() {
        Arrangement arrangement = new Arrangement();
        arrangement.setOcisId("807610516");
        arrangement.setPartyId("+00433279025");
        return arrangement;

    }

    private StB277BAccPartiesReadJP b277ResponseWithNoMoreResult() {
        StB277BAccPartiesReadJP b277Response = new StB277BAccPartiesReadJP();
        b277Response.setMoreind("N");
        TAccPartyOtherJP[] details = new TAccPartyOtherJP[1];
        TAccPartyOtherJP otherJP = new TAccPartyOtherJP();
        otherJP.setFirstname("Andrea");
        otherJP.setSurname("Speculli");
        StParty stparty = new StParty();
        stparty.setPartyid("12345");
        stparty.setOcisid(new BigInteger("7890"));
        otherJP.setStparty(stparty);
        details[0] = otherJP;
        StError stError = new StError();
        stError.setErrorno(0);
        stError.setErrormsg(null);
        b277Response.setSterror(stError);
        b277Response.setAstaccpartyOtherJP(details);
        return b277Response;
    }

    private StB277BAccPartiesReadJP b277ResponseWithError() {
        StB277BAccPartiesReadJP b277Response = new StB277BAccPartiesReadJP();
        b277Response.setMoreind("N");
        TAccPartyOtherJP[] details = new TAccPartyOtherJP[1];
        TAccPartyOtherJP otherJP = new TAccPartyOtherJP();
        otherJP.setFirstname("Andrea");
        otherJP.setSurname("Speculli");
        StParty stparty = new StParty();
        stparty.setPartyid("12345");
        stparty.setOcisid(new BigInteger("7890"));
        otherJP.setStparty(stparty);
        details[0] = otherJP;
        StError stError = new StError();
        stError.setErrorno(1);
        stError.setErrormsg("Error");
        b277Response.setSterror(stError);
        b277Response.setAstaccpartyOtherJP(details);
        return b277Response;
    }

}
