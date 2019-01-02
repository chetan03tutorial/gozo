/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.resources;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.colleagues.involvedparty.service.ModifyCommunicationProfileService;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.paperless.constants.PaperlessConstants;
import com.lbg.ib.api.sales.paperless.dto.Account;
import com.lbg.ib.api.sales.paperless.dto.PaperlessResult;
import com.lbg.ib.api.sales.paperless.dto.PersonalDetails;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoResult;
import com.lbg.ib.api.sales.paperless.dto.UserPreferences;
import com.lbg.ib.api.sales.paperless.service.PaperlessService;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sales.user.service.UserProductHoldingService;
import com.lbg.ib.api.sales.user.service.UserProductHoldingServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PaperlessResourceTest {

    @Mock
    private SessionManagementDAO              session;

    @Mock
    private PaperlessService                  paperlessService;

    @InjectMocks
    private PaperlessResource                 paperlessResource;

    @Mock
    private GalaxyErrorCodeResolver           resolver;

    @Mock
    private RequestBodyResolver               ReqResolver;

    @Mock
    private FieldValidator                    fieldValidator;

    @Mock
    private UserProductHoldingServiceImpl     userProductHoldingService;

    @Mock
    private ModifyCommunicationProfileService modifyCommunicationProfileService;

    @Mock
    private ModuleContext                     moduleContext;

    private final ResponseError               DETAILS_NOT_FOUND_IN_SESSION = new ResponseError("99000091",
            "DETAILS_NOT_FOUND_IN_SESSION");
    private final ResponseError               PRODUCT_NOT_FOUND            = new ResponseError("9310005",
            "PRODUCT_NOT_FOUND");
    @Mock
    private LoggerDAO logger;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(resolver.resolve(ResponseErrorConstants.DETAILS_NOT_FOUND_IN_SESSION))
                .thenReturn(DETAILS_NOT_FOUND_IN_SESSION);
        when(resolver.resolve(ResponseErrorConstants.PRODUCT_NOT_FOUND)).thenReturn(PRODUCT_NOT_FOUND);

        when(moduleContext.getService(UserProductHoldingService.class)).thenReturn(userProductHoldingService);
        when(moduleContext.getService(ModifyCommunicationProfileService.class))
                .thenReturn(modifyCommunicationProfileService);

        UserContext context = SessionServiceUtil.prepareUserContext("lloyds");

        when(session.getUserContext()).thenReturn(context);

    }

    private static final String sessionId = "SOME_RANDOM_SESSION_ID";

    public static UserContext prepareUserContext(String brand) {
        return new UserContext("871515", "192.168.1.1", sessionId, "+100000", "1204629", brand, "chansecMode", "Chrome",
                "En", "inboxIdClient", "host");
    }

    private Arrangement arrangementWithValidPartyDetails() {
        Arrangement arrangement = new Arrangement();
        arrangement.setOcisId("1000");
        arrangement.setPartyId("+100000");
        return arrangement;
    }

    @Test
    public void getUserMandateInfo() {
        UserContext context = prepareUserContext("lloyds");
        when(session.getUserContext()).thenReturn(context);
        when(paperlessService.getUserMandateInfo(anyString())).thenReturn(mandateInfoResult("0"));
        Response infoResult = paperlessResource.getUserMandateInfo();
        assertEquals(200, infoResult.getStatus());
        assertNotNull(infoResult.getEntity());
    }

    @Test(expected = ServiceException.class)
    public void getUserMandateWithInvalidUserContextInfo() {
        UserContext context = null;
        when(session.getUserContext()).thenReturn(context);
        when(paperlessService.getUserMandateInfo(anyString())).thenReturn(mandateInfoResult("0"));
        Response infoResult = paperlessResource.getUserMandateInfo();
        assertEquals(200, infoResult.getStatus());
        assertNotNull(infoResult.getEntity());
    }

    private UserMandateInfoResult mandateInfoResult(String code) {
        UserMandateInfoResult result = new UserMandateInfoResult();
        result.setUserRegStateCode(code);
        return result;
    }

    @Test
    public void updateEmailSuccess() throws Exception {
        PersonalDetails details = new PersonalDetails();
        details.setEmailAddress("test@gmail.com");
        when(fieldValidator.validateInstanceFields(details)).thenReturn(null);
        when(session.getUserInfo()).thenReturn(arrangementWithValidPartyDetails());
        when(ReqResolver.resolve("test@gmail.com", PersonalDetails.class)).thenReturn(details);
        Response infoResult = paperlessResource.updateEmail("test@gmail.com");
        assertEquals(200, infoResult.getStatus());
    }

    @Test(expected = Exception.class)
    public void updateEmailFail() throws Exception {
        PersonalDetails details = new PersonalDetails();
        ValidationError error = new ValidationError("Error");
        details.setEmailAddress("test@gmail.com");
        when(fieldValidator.validateInstanceFields(details)).thenReturn(error);
        when(session.getUserInfo()).thenReturn(arrangementWithValidPartyDetails());
        when(ReqResolver.resolve("test@gmail.com", PersonalDetails.class)).thenReturn(details);
        paperlessResource.updateEmail("test@gmail.com");

    }

    @Test
    public void testFullPaperStatus() throws InvalidFormatException {
        List<Account> accountList = new LinkedList<Account>();
        accountList.add(account(PaperlessConstants.FULL_PAPER));
        when(userProductHoldingService.retrieveProductHolding(anyString(), any(BigInteger.class)))
                .thenReturn(accountList);
        Response paperlessData = paperlessResource.getGoGreenStatus();
        PaperlessResult result = (PaperlessResult) paperlessData.getEntity();
        assertEquals(PaperlessConstants.FULL_PAPER, result.getGoGreenStatus());
        assertNotNull(paperlessData.getEntity());
    }

    @Test
    public void testPartialPaperStatus() throws InvalidFormatException {
        List<Account> accountList = new LinkedList<Account>();
        accountList.add(account(PaperlessConstants.FULL_PAPER));
        accountList.add(account(PaperlessConstants.PAPER_LESS));
        accountList.add(account(PaperlessConstants.SOME_PAPER));
        accountList.add(account(PaperlessConstants.NO_DATA));
        when(userProductHoldingService.retrieveProductHolding(anyString(), any(BigInteger.class)))
                .thenReturn(accountList);
        Response paperlessData = paperlessResource.getGoGreenStatus();
        PaperlessResult result = (PaperlessResult) paperlessData.getEntity();
        assertEquals(PaperlessConstants.SOME_PAPER, result.getGoGreenStatus());
        assertNotNull(paperlessData.getEntity());
    }

    @Test
    public void testParperlessStatus() throws InvalidFormatException {
        List<Account> accountList = new LinkedList<Account>();
        accountList.add(account(PaperlessConstants.PAPER_LESS));
        accountList.add(account(PaperlessConstants.PAPER_LESS));
        accountList.add(account(PaperlessConstants.NO_DATA));
        when(userProductHoldingService.retrieveProductHolding(anyString(), any(BigInteger.class)))
                .thenReturn(accountList);
        Response paperlessData = paperlessResource.getGoGreenStatus();
        PaperlessResult result = (PaperlessResult) paperlessData.getEntity();
        assertEquals(PaperlessConstants.PAPER_LESS, result.getGoGreenStatus());
        assertNotNull(paperlessData.getEntity());
    }

    @Test(expected = com.lbg.ib.api.sales.shared.exception.InvalidFormatException.class)
    public void testPaperlessStatusWithInvalidPartyId() throws InvalidFormatException {
        List<Account> accountList = new LinkedList<Account>();
        accountList.add(account(PaperlessConstants.FULL_PAPER));
        when(userProductHoldingService.retrieveProductHolding(anyString(), any(BigInteger.class)))
                .thenReturn(accountList);
        session.getUserContext().setPartyId("123432423");
        Response paperlessData = paperlessResource.getGoGreenStatus();
        PaperlessResult result = (PaperlessResult) paperlessData.getEntity();
        assertEquals(PaperlessConstants.FULL_PAPER, result.getGoGreenStatus());
        assertNotNull(paperlessData.getEntity());
    }

    @Test(expected = com.lbg.ib.api.sales.shared.exception.InvalidFormatException.class)
    public void testPaperlessStatusWithInvalidPartyId2() throws InvalidFormatException {
        List<Account> accountList = new LinkedList<Account>();
        accountList.add(account(PaperlessConstants.FULL_PAPER));
        when(userProductHoldingService.retrieveProductHolding(anyString(), any(BigInteger.class)))
                .thenReturn(accountList);
        session.getUserContext().setPartyId("");
        Response paperlessData = paperlessResource.getGoGreenStatus();
        PaperlessResult result = (PaperlessResult) paperlessData.getEntity();
        assertEquals(PaperlessConstants.FULL_PAPER, result.getGoGreenStatus());
        assertNotNull(paperlessData.getEntity());
    }

    @Test(expected = com.lbg.ib.api.sales.shared.exception.InvalidFormatException.class)
    public void testPaperlessStatusWithInvalidOcisId() throws InvalidFormatException {
        List<Account> accountList = new LinkedList<Account>();
        accountList.add(account(PaperlessConstants.FULL_PAPER));
        when(userProductHoldingService.retrieveProductHolding(anyString(), any(BigInteger.class)))
                .thenReturn(accountList);
        session.getUserContext().setOcisId("au2198");
        Response paperlessData = paperlessResource.getGoGreenStatus();
        PaperlessResult result = (PaperlessResult) paperlessData.getEntity();
        assertEquals(PaperlessConstants.FULL_PAPER, result.getGoGreenStatus());
        assertNotNull(paperlessData.getEntity());
    }

    @Test(expected = com.lbg.ib.api.sales.shared.exception.InvalidFormatException.class)
    public void testPaperlessStatusWithInvalidOcisId2() throws InvalidFormatException {
        List<Account> accountList = new LinkedList<Account>();
        accountList.add(account(PaperlessConstants.FULL_PAPER));
        when(userProductHoldingService.retrieveProductHolding(anyString(), any(BigInteger.class)))
                .thenReturn(accountList);
        session.getUserContext().setOcisId("");
        Response paperlessData = paperlessResource.getGoGreenStatus();
        PaperlessResult result = (PaperlessResult) paperlessData.getEntity();
        assertEquals(PaperlessConstants.FULL_PAPER, result.getGoGreenStatus());
        assertNotNull(paperlessData.getEntity());
    }

    @Test
    public void testUpdatePreferencesSucess() throws Exception {
        UserPreferences preferences = new UserPreferences();
        List<Account> accountList = new LinkedList<Account>();
        Account account = new Account();
        account.setName("Classic");
        account.setStatementType("Paperless");
        account.setCorrespondanceType("Paper");
        account.setAccountNumber("53260768");
        account.setSortCode("772237");
        account.setProductType("Account");
        account.setExternalSystemProductId("12334");
        account.setExternalSystem(4);
        account.setExternalSystemProductHeldId("7722375326076800000");
        account.setExternalPartyIdentifierText("30921510283104");
        accountList.add(account);
        preferences.setAccounts(accountList);
        when(fieldValidator.validateInstanceFields(preferences)).thenReturn(null);
        when(session.getUserInfo()).thenReturn(arrangementWithValidPartyDetails());
        modifyCommunicationProfileService.updatePreferences(preferences, "1061335723",
        		"+1061335723");
        // when(modifyCommunicationProfileService.updatePreferences(preferences,
        // anyString(),anyString())).thenReturn(null);
        when(ReqResolver.resolve(anyString(), any(Class.class))).thenReturn(preferences);
        Response response = paperlessResource.updatePreferences("requestBody");
        assertEquals(200, response.getStatus());

    }

    @Test
    public void testUpdatePreferencesSucess2() throws Exception {
        UserPreferences preferences = new UserPreferences();
        List<Account> accountList = new LinkedList<Account>();
        Account account = new Account();
        account.setName("Classic");
        account.setStatementType("Paperless");
        account.setCorrespondanceType("Paper");
        account.setAccountNumber("53260768");
        account.setSortCode("772237");
        account.setProductType("Account");
        account.setExternalSystemProductId("12334");
        account.setExternalSystem(4);
        account.setExternalSystemProductHeldId("7722375326076800000");
        account.setExternalPartyIdentifierText("30921510283104");
        accountList.add(account);
        preferences.setAccounts(accountList);
        when(fieldValidator.validateInstanceFields(preferences)).thenReturn(null);
        when(session.getUserInfo()).thenReturn(arrangementWithValidPartyDetails());
        modifyCommunicationProfileService.updatePreferences(preferences, "1061335723",
        		"+1061335723");
        // when(modifyCommunicationProfileService.updatePreferences(preferences,
        // anyString(),anyString())).thenReturn(null);
        when(ReqResolver.resolve(anyString(), any(Class.class))).thenReturn(preferences);
        Response response = paperlessResource.updatePreferences("requestBody");
        assertEquals(200, response.getStatus());

    }

    @Test(expected = InvalidFormatException.class)
    public void testUpdatePreferencesError() throws Exception {
        UserPreferences preferences = new UserPreferences();
        List<Account> accountList = new LinkedList<Account>();
        Account account = new Account();
        account.setCorrespondanceType("PP");
        accountList.add(account);
        preferences.setAccounts(accountList);
        ValidationError error = new ValidationError("Error");
        when(fieldValidator.validateInstanceFields(preferences)).thenReturn(error);
        when(session.getUserInfo()).thenReturn(arrangementWithValidPartyDetails());
        when(ReqResolver.resolve(anyString(), any(Class.class))).thenReturn(preferences);
        Response response = paperlessResource.updatePreferences("requestBody");

    }

    private Account account(String status) {
        Account account = new Account();
        account.setGreenStatus(status);
        return account;
    }
}
