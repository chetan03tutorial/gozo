package com.lbg.ib.api.sales.overdraft.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.asm.dto.C078ResponseDto;
import com.lbg.ib.api.sales.asm.service.C078Service;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.E160Request;
import com.lbg.ib.api.sales.overdraft.domain.E160Response;
import com.lbg.ib.api.sales.overdraft.domain.E169Request;
import com.lbg.ib.api.sales.overdraft.domain.E169Response;
import com.lbg.ib.api.sales.overdraft.domain.E170Request;
import com.lbg.ib.api.sales.overdraft.domain.E170Response;
import com.lbg.ib.api.sales.overdraft.domain.OdFulfillmentRequest;
import com.lbg.ib.api.sales.overdraft.domain.OverdraftManagementResponse;
import com.lbg.ib.api.sales.overdraft.domain.Q122Request;
import com.lbg.ib.api.sales.overdraft.domain.Q122Response;
import com.lbg.ib.api.sales.overdraft.service.E160Service;
import com.lbg.ib.api.sales.overdraft.service.E169Service;
import com.lbg.ib.api.sales.overdraft.service.E170Service;
import com.lbg.ib.api.sales.overdraft.service.Q122Service;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsService;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtil;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.user.Account;

@RunWith(MockitoJUnitRunner.class)
public class OverdraftResourceTest {

	@InjectMocks
	private OverdraftResource resource;

	@Mock
	private LoggerDAO logger;

	@Mock
	private ModuleContext beanLoader;

	@Mock
	private E160Service e160Service;

	@Mock
	private E170Service e170Service;

	@Mock
	private E169Service e169Service;

	@Mock
	private SessionManagementDAO sessionManager;

	@Mock
	private AccountInContextUtility contextUtility;

	@Mock
	private RetrievePartyDetailsService retrievePartyService;

	@Mock
	private FieldValidator fieldValidator;

	@Mock
	private Q122Service q122Service;

	@Mock
	private C078Service c078Service;

	private static AccountInContextUtil context = new AccountInContextUtil();
	private List<Account> accountList = context.getUserInfo().getAccounts();

	private E160Response e160Response() {
		E160Response e160Response = new E160Response();
		e160Response.setCurrencyCode("GBP");
		return e160Response;
	}

	private E170Response e170Response() {
		E170Response e160Response = new E170Response();
		return e160Response;
	}

	private E169Response e169Response() {
		E169Response e160Response = new E169Response();
		return e160Response;
	}

	private Q122Response q122Response() {
		Q122Response q122Response = new Q122Response();
		q122Response.setAffordableAmount("100");
		return q122Response;
	}

	private OdFulfillmentRequest createOdFulfillmentRequest(String demandedOd) {
		OdFulfillmentRequest odFulfillmentRequest = new OdFulfillmentRequest();
		odFulfillmentRequest.setDemandedOd(demandedOd);
		return odFulfillmentRequest;
	}

	private Q122Request createQ122Request(String demandedOd) {
		Q122Request q122Request = new Q122Request();
		q122Request.setDemandedOd(demandedOd);
		return q122Request;
	}

	@Before
	public void setup() {
		// when(sessionManager.getUserInfo()).thenReturn(arrangement());
		when(beanLoader.getService(E160Service.class)).thenReturn(e160Service);
		when(beanLoader.getService(E170Service.class)).thenReturn(e170Service);
		when(beanLoader.getService(E169Service.class)).thenReturn(e169Service);
		when(beanLoader.getService(Q122Service.class)).thenReturn(q122Service);
		when(beanLoader.getService(FieldValidator.class)).thenReturn(fieldValidator);
		when(beanLoader.getService(RetrievePartyDetailsService.class)).thenReturn(retrievePartyService);
		when(e170Service.invokeE170(any(E170Request.class))).thenReturn(e170Response());
		when(e169Service.invokeE169(any(E169Request.class))).thenReturn(e169Response());
		when(e160Service.invokeE160(any(E160Request.class))).thenReturn(e160Response());
		when(fieldValidator.validateInstanceFields(any())).thenReturn(null);

	}

	@Test(expected = ServiceException.class)
	public void testOdCreationWhenQ122AintInvokedAndDemandedOdIsMoreThanTriadMaximumOdLimit() {
		String requestOdLimit = new String("2000");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(0));
		when(contextUtility.getMaximumOverdraftLimit()).thenReturn(null);
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		resource.fulfill(clientRequest);
	}

	@Test
	public void testOdCreationWhenQ122IsInvokedAndDemandedOdIsLessThanAsmMaximumOdLimit() {
		String requestOdLimit = new String("1000");
		Double maxAsmOdLimit = new Double("2000");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(0));
		when(contextUtility.getMaximumOverdraftLimit()).thenReturn(maxAsmOdLimit);
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		Response response = resource.fulfill(clientRequest);
		OverdraftManagementResponse serviceResponse = (OverdraftManagementResponse) response.getEntity();
		assertEquals(serviceResponse.getOverdraftLimit(), requestOdLimit);
	}

	@Test
	public void testOdCreationWhenDemandedOdIsMoreThanMaximumAllowedOverdraftLimit() {
		String requestOdLimit = new String("2000");
		Double maxAsmOdLimit = new Double("1000");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(0));
		when(contextUtility.getMaximumOverdraftLimit()).thenReturn(maxAsmOdLimit);
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		resource.fulfill(clientRequest);
	}

	@Test
	public void testOdRemoval() {
		String requestOdLimit = new String("0");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(5));
		when(contextUtility.getAvailedOverdraft()).thenReturn(new Double("1200"));
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		Response response = resource.fulfill(clientRequest);
		OverdraftManagementResponse serviceResponse = (OverdraftManagementResponse) response.getEntity();
		assertEquals(serviceResponse.getOverdraftLimit(), requestOdLimit);
	}

	@Test
	public void testOdRemovalWhenAvailableBalanceIsLessThanAmountReduced() {
		String requestOdLimit = new String("0");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(3));
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		Response response = resource.fulfill(clientRequest);
		assertNotNull(response.getEntity());
	}

	@Test(expected = ServiceException.class)
	public void testOdAmendWhenDemandedOdIsMoreThanMaximumOdLimit() {
		String requestOdLimit = new String("2000");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(1));
		when(contextUtility.getMaximumOverdraftLimit()).thenReturn(null);
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		resource.fulfill(clientRequest);
	}

	@Test
	public void testOdAmendmentWhenDemandedOdIsLessThanMaximumOdLimit() {
		String requestOdLimit = new String("1000");
		Double maxAsmOdLimit = new Double("2000");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(1));
		when(contextUtility.getMaximumOverdraftLimit()).thenReturn(maxAsmOdLimit);
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		Response response = resource.fulfill(clientRequest);
		OverdraftManagementResponse serviceResponse = (OverdraftManagementResponse) response.getEntity();
		assertEquals(serviceResponse.getOverdraftLimit(), requestOdLimit);
	}

	@Test(expected = ServiceException.class)
	public void testOdAmendmentWhenCustomerPossessMaximumOverdraft() {
		String requestOdLimit = new String("1000");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(1));
		when(contextUtility.getMaximumOverdraftLimit()).thenReturn(null);
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		Response response = resource.fulfill(clientRequest);
		OverdraftManagementResponse serviceResponse = (OverdraftManagementResponse) response.getEntity();
		assertEquals(serviceResponse.getOverdraftLimit(), requestOdLimit);
	}

	@Test
	public void testOdAmendDemandedOdEqualsExisting() {
		String requestOdLimit = new String("10");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(1));
		when(contextUtility.getMaximumOverdraftLimit()).thenReturn(null);
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		Response response = resource.fulfill(clientRequest);
		ResponseError serviceResponse = (ResponseError) response.getEntity();
		assertNotNull(serviceResponse);
	}

	@Test
	public void testQ122DecisionService() {
		when(contextUtility.getPrimaryPartyOcisId()).thenReturn("partyId");
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(0));
		when(retrievePartyService.getPartyDetails(anyString())).thenReturn(new PartyDetails());
		doNothing().when(contextUtility).updatePartyDetails(anyString(), any(PartyDetails.class));
		when(q122Service.invokeQ122(any(Q122Request.class), any(String.class))).thenReturn(q122Response());
		when(contextUtility.getPrimaryPartyDetails()).thenReturn(new PartyDetails());
		String requestOdLimit = new String("10");
		Response response = resource.decision(createQ122Request(requestOdLimit));
		Q122Response serviceResponse = (Q122Response) response.getEntity();
		// assertEquals(serviceResponse.getAffordableAmount(), affordableAmount);

	}

	@Test(expected = InvalidFormatException.class)
	public void testFulfillmentWhenRequestAmountIsInvalid() {
		String requestOdLimit = new String("10.00");
		when(fieldValidator.validateInstanceFields(any())).thenReturn(new ValidationError("Error in validation"));
		OdFulfillmentRequest clientRequest = createOdFulfillmentRequest(requestOdLimit);
		resource.fulfill(clientRequest);
	}

	@Test(expected = InvalidFormatException.class)
	public void testDecisionWhenRequestAmountIsInvalid() {
		String requestOdLimit = new String("10.00");
		when(fieldValidator.validateInstanceFields(any())).thenReturn(new ValidationError("Error in validation"));
		Q122Request clientRequest = createQ122Request(requestOdLimit);
		resource.decision(clientRequest);
	}

	private C078ResponseDto buildAcceptedResponse(String decision) {
		C078ResponseDto c078ResponseDto = new C078ResponseDto();
		c078ResponseDto.setCreditScore(decision);
		return c078ResponseDto;
	}

	@Test
	public void testFetchAppScoreDecisionFromA008ForAcceptedApplication() {
		when(c078Service.invokeC078(any(ApplicationType.class))).thenReturn(buildAcceptedResponse("1"));
		when(contextUtility.getDemandedOverdraft()).thenReturn(new Double("500.0"));
		resource.fetchAppScoreDecisionFromA008();
	}

	@Test
	public void testFetchAppScoreDecisionFromA008ForReferedApplication() {
		when(c078Service.invokeC078(any(ApplicationType.class))).thenReturn(buildAcceptedResponse("2"));
		when(contextUtility.getMaximumOverdraftLimit()).thenReturn(new Double(500));
		resource.fetchAppScoreDecisionFromA034();
	}

	@Test
	public void testFetchAppScoreDecisionFromA008ForDeclinedApplication() {
		when(c078Service.invokeC078(any(ApplicationType.class))).thenReturn(buildAcceptedResponse("3"));
		resource.fetchAppScoreDecisionFromA008();
	}

	@Test(expected = ServiceException.class)
	public void testFetchAppScoreDecisionFromA008ForUnknownApplication() {
		when(c078Service.invokeC078(any(ApplicationType.class))).thenReturn(buildAcceptedResponse("4"));
		resource.fetchAppScoreDecisionFromA008();
	}
}
