package com.lbg.ib.api.sales.user.service;

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
import com.lbg.ib.api.sales.paperless.dto.Account;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StAccount;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StAccountListDetail;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StCommPrefData;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB162BUserAccReadList;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.UserContext;

@RunWith(MockitoJUnitRunner.class)
public class UserProductHoldingServiceTest {
    @InjectMocks
    private UserProductHoldingServiceImpl userProductHoldingService;

    @Mock
    private SessionManagementDAO          sessionManagementDAO;

    @Mock
    private FoundationServerUtil          foundationServerUtil;

    @Mock
    private SOAInvoker                    bapiInvoker;

    @Mock
    private GalaxyErrorCodeResolver       resolver;

    private static final String           partyId = "+100000";

    private static final BigInteger       ocisId  = BigInteger.valueOf(1000);

    @Before
    public void setup() {
        when(foundationServerUtil.createStHeader(any(UserContext.class))).thenReturn(stHeader());
        when(sessionManagementDAO.getUserContext()).thenReturn(userContext());
        when(resolver.resolve(anyString())).thenReturn(new ResponseError("code", "message"));
    }

    @Test
    public void shouldReturnAccountListWithNoError() {
        when(bapiInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
                .thenReturn(b162ResponseWithNoMoreResult());
        List<Account> accountList = userProductHoldingService.retrieveProductHolding(partyId, ocisId);
        assertNotNull(accountList);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowServiceExceptionWhenExternalResponseReturnsAnError() {
        when(bapiInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any

        (Object[].class))).thenReturn(b162ResponseWithError());
        userProductHoldingService.retrieveProductHolding(partyId, ocisId);
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnEmptyListWhenNoRecordIsFound() {
        when(bapiInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any

        (Object[].class))).thenReturn(b162ResponseWithNoResult());
        userProductHoldingService.retrieveProductHolding(partyId, ocisId);
    }

    private StAccountListDetail[] getAccountList() {
        StAccountListDetail[] accountList = new StAccountListDetail[9];
        accountList[0] = account("AccountWithPaperlessStatementAndPaperlessCommunication",

                "paperless", "paperless");
        accountList[1] = account("AccountWithPaperlessStatementAndPaperCommunication",

                "paperless", "paper");
        accountList[2] = account("AccountWithPaperStatementAndPaperlessCommunication",

                "paper", "paperless");
        accountList[3] = account("AccountWithPaperStatementAndPaperCommunication", "paper",

                "paper");
        accountList[4] = account("AccountWithNAStatementAndPaperCommunication", null,

                "paper");
        accountList[5] = account("AccountWithNAStatementAndPaperCommunication", "paper",

                null);
        accountList[6] = account("AccountWithPaperlessStatementAndNACommunication",

                "paperless", null);
        accountList[7] = account("AccountWithPaperlessStatementAndNACommunication", null,

                "paperless");
        accountList[8] = account("AccountWithPaperlessStatementAndNACommunication", null,

                null);
        return accountList;
    }

    private StAccountListDetail account(String accountName, String statementType, String

    correspondanceType) {
        StAccountListDetail productDetail = new StAccountListDetail();
        StAccount account = new StAccount();
        account.setAccno("6578902139");
        account.setSortcode("09-902-23");
        productDetail.setStacc(account);
        productDetail.setAccname("Savings");
        StCommPrefData statementPreference = new StCommPrefData();
        statementPreference.setCommOptTx(statementType);
        StCommPrefData correspondancePreferance = new StCommPrefData();
        correspondancePreferance.setCommOptTx(correspondanceType);
        productDetail.setStcommprefdata(statementPreference);
        productDetail.setStcorrspondncdata(correspondancePreferance);
        return productDetail;
    }

    private StB162BUserAccReadList b162ResponseWithNoMoreResult() {
        StB162BUserAccReadList b162Response = new StB162BUserAccReadList();
        b162Response.setMoreind("N");
        b162Response.setAstacclistdetail(getAccountList());
        StError stError = new StError();
        stError.setErrorno(0);
        stError.setErrormsg(null);
        b162Response.setSterror(stError);
        return b162Response;
    }

    private StB162BUserAccReadList b162ResponseWithNoResult() {
        StB162BUserAccReadList b162Response = new StB162BUserAccReadList();
        b162Response.setMoreind("N");
        b162Response.setAstacclistdetail(null);
        StError stError = new StError();
        stError.setErrorno(0);
        stError.setErrormsg(null);
        b162Response.setSterror(stError);
        return b162Response;
    }

    private UserContext userContext() {
        UserContext userContext = new UserContext("userId", "ipAddress", "sessionId",

                "partyId", "ocisId", "channelId", "chansecMode", "userAgent", "language", "inboxIdClient",

                "host");
        return userContext;
    }

    private StB162BUserAccReadList b162ResponseWithError() {
        StB162BUserAccReadList b162Response = new StB162BUserAccReadList();
        StError stError = new StError();
        stError.setErrorno(1000);
        stError.setErrormsg("errorMessage");
        b162Response.setSterror(stError);
        return b162Response;
    }

    private DAOResponse.DAOError daoError() {
        DAOResponse.DAOError daoError = new DAOResponse.DAOError("errorCode",

                "errorMessage");
        return daoError;
    }

    private StHeader stHeader() {
        StHeader stHeader = new StHeader();
        StParty stParty = new StParty();
        stParty.setPartyid("1000");
        stParty.setOcisid(new BigInteger("10"));
        stHeader.setStpartyObo(stParty);
        return stHeader;
    }

}
