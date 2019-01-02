package com.lbg.ib.api.sales.colleagues.involvedparty.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.paperless.dto.Account;
import com.lbg.ib.api.sales.paperless.dto.UserPreferences;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lloydstsb.ea.infrastructure.soap.Condition;
import com.lloydstsb.ea.infrastructure.soap.ResultCondition;
import com.lloydstsb.ea.lcsm.ResponseHeader;
import com.lloydstsb.lcsm.arrangement.Arrangement;
import com.lloydstsb.lcsm.arrangement.DepositArrangement;
import com.lloydstsb.lcsm.common.AlternateId;
import com.lloydstsb.lcsm.common.AttributeCondition;
import com.lloydstsb.lcsm.common.AttributeConditionValue;
import com.lloydstsb.lcsm.common.ObjectReference;
import com.lloydstsb.lcsm.common.RuleCondition;
import com.lloydstsb.lcsm.involvedparty.ContactPoint;
import com.lloydstsb.lcsm.involvedparty.ContactPreference;
import com.lloydstsb.lcsm.involvedparty.Individual;
import com.lloydstsb.lcsm.involvedparty.InvolvedParty;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRole;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRoleType;
import com.lloydstsb.lcsm.involvedpartymanagement.InvolvedPartyManagement;
import com.lloydstsb.lcsm.involvedpartymanagement.InvolvedPartyManagementServiceLocator;
import com.lloydstsb.lcsm.involvedpartymanagement.ModifyCommunicationProfilesRequest;
import com.lloydstsb.lcsm.involvedpartymanagement.ModifyCommunicationProfilesResponse;
import com.lloydstsb.lcsm.product.Product;
import com.lloydstsb.lcsm.product.ProductType;

@RunWith(MockitoJUnitRunner.class)
public class ModifyCommunicationProfileServiceImplTest {
	@InjectMocks
	ModifyCommunicationProfileServiceImpl modifyCommunicationProfileService;

	@Mock
	private SessionManagementDAO sessionManagementDAO;

	@Mock
	private InvolvedPartyManagement involvedPartyManagement;

	@Mock
	private GalaxyErrorCodeResolver resolver;

	@Mock
	private InvolvedPartyManagementServiceLocator serviceLocator;
	@Mock
	private ChannelBrandingDAO channelService;

	@Mock
	private LoggerDAO logger;

	@Mock
	private ModuleContext moduleContext;

	@Mock
	private FoundationServerUtil foundationServerUtil;

	@Mock
	private HandlerRegistry handleRegistry;

	@Mock
	private SOAInvoker soaInvoker;

	@Mock
	private SoapHeaderGenerator soapHeaderGenerator;

	private static final ChannelBrandDTO CHANNEL = new ChannelBrandDTO("ch",
			"br", "cid");

	private static final int SUCCESS = 0;
	private static final int FAILURE = 1;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);

		when(sessionManagementDAO.getUserContext()).thenReturn(userContext());
		when(sessionManagementDAO.getCustomerDetails()).thenReturn(
				customerInfo());
		when(sessionManagementDAO.getUserContext()).thenReturn(
				SessionServiceUtil.prepareUserContext("LTB"));
		when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
		when(serviceLocator.getServiceName()).thenReturn(
				new QName("involvedParty"));
		when(serviceLocator.getInvolvedPartyManagementSOAPPortWSDDPortName())
				.thenReturn("ivp");
		when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString()))
				.thenReturn(HeaderServiceUtil.genericHeaderData());
		when(soapHeaderGenerator.getGenericSoapHeader(anyString(), anyString(), anyBoolean()))
				.thenReturn(HeaderServiceUtil.prepareSoapHeaders());
		when(channelService.getChannelBrand()).thenReturn(withResult(CHANNEL));
		UserContext context = SessionServiceUtil.prepareUserContext("lloyds");
		when(sessionManagementDAO.getUserContext()).thenReturn(context);

	}

	private CustomerInfo customerInfo() {
		CustomerInfo customerInfo = new CustomerInfo();
		customerInfo.setAccountNumber("34658709");
		customerInfo.setSortCode("1292OPSA");
		return customerInfo;
	}

	private UserContext userContext() {
		UserContext userContext = new UserContext("userId", "ipAddress",
				"sessionId",

				"partyId", "ocisId", "channelId", "chansecMode", "userAgent",
				"language", "inboxIdClient",

				"host");
		return userContext;
	}

	@Test(expected = ServiceException.class)
	public void testUpdatePreferencesFail() throws Exception {
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
		account.setType("T0031776000");
		preferences.setAccounts(accountList);
		modifyCommunicationProfileService.updatePreferences(preferences,
				"1061335723", "+1061335723");
		// when(modifyCommunicationProfileService.updatePreferences(preferences,
		// anyString(),anyString())).thenReturn(null);

	}

	@Test
	public void testUpdatePreferencesSuccess() throws Exception {
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
		account.setType("T0031776000");
		accountList.add(account);
		preferences.setAccounts(accountList);
		/*
		 * when(soaInvoker.invoke(any(Class.class), anyString(),
		 * any(Class[].class), any(Object[].class)))
		 * .thenReturn(externalServiceResponse(SUCCESS, false));
		 */
		when(
				involvedPartyManagement
						.modifyCommunicationProfiles(any(ModifyCommunicationProfilesRequest.class)))
				.thenReturn(externalServiceResponse(SUCCESS, false));
		modifyCommunicationProfileService.updatePreferences(preferences,
				"1061335723", "+1061335723");
		// when(modifyCommunicationProfileService.updatePreferences(preferences,
		// anyString(),anyString())).thenReturn(null);

	}

	@Test
	public void testUpdatePreferencesSuccess2() throws Exception {
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
		account.setType("T0031776000");
		accountList.add(account);
		preferences.setAccounts(null);
		/*
		 * when(soaInvoker.invoke(any(Class.class), anyString(),
		 * any(Class[].class), any(Object[].class)))
		 * .thenReturn(externalServiceResponse(SUCCESS, false));
		 */
		when(
				involvedPartyManagement
						.modifyCommunicationProfiles(any(ModifyCommunicationProfilesRequest.class)))
				.thenReturn(externalServiceResponse(SUCCESS, false));

		modifyCommunicationProfileService.updatePreferences(preferences,
				"1061335723", "+1061335723");
		// when(modifyCommunicationProfileService.updatePreferences(preferences,
		// anyString(),anyString())).thenReturn(null);

	}

	@Test
	public void testUpdatePreferencesSuccess1() throws Exception {
		UserPreferences preferences = null;
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
		account.setType("T0031776000");
		accountList.add(account);
		/*
		 * when(soaInvoker.invoke(any(Class.class), anyString(),
		 * any(Class[].class), any(Object[].class)))
		 * .thenReturn(externalServiceResponse(SUCCESS, false));
		 */
		when(
				involvedPartyManagement
						.modifyCommunicationProfiles(any(ModifyCommunicationProfilesRequest.class)))
				.thenReturn(externalServiceResponse(SUCCESS, false));

		modifyCommunicationProfileService.updatePreferences(preferences,
				"1061335723", "+1061335723");
		// when(modifyCommunicationProfileService.updatePreferences(preferences,
		// anyString(),anyString())).thenReturn(null);

	}

	@Test(expected = ServiceException.class)
	public void testUpdatePreferencesError() throws Exception {
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
		account.setType("T0031776000");
		preferences.setAccounts(accountList);
		/*
		 * when(soaInvoker.invoke(any(Class.class), anyString(),
		 * any(Class[].class), any(Object[].class)))
		 * .thenReturn(externalServiceResponse(FAILURE, false));
		 */
		when(
				involvedPartyManagement
						.modifyCommunicationProfiles(any(ModifyCommunicationProfilesRequest.class)))
				.thenReturn(externalServiceResponse(FAILURE, false));
		modifyCommunicationProfileService.updatePreferences(preferences,
				"1061335723", "+1061335723");
		// when(modifyCommunicationProfileService.updatePreferences(preferences,
		// anyString(),anyString())).thenReturn(null);

	}

	@Test(expected = ServiceException.class)
	public void testUpdatePreferencesError1() throws Exception {
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
		account.setType("T0031776000");
		accountList.add(account);
		preferences.setAccounts(accountList);
		/*
		 * when(soaInvoker.invoke(any(Class.class), anyString(),
		 * any(Class[].class), any(Object[].class)))
		 * .thenReturn(externalServiceResponse(SUCCESS, true));
		 */
		when(
				involvedPartyManagement
						.modifyCommunicationProfiles(any(ModifyCommunicationProfilesRequest.class)))
				.thenReturn(externalServiceResponse(SUCCESS, true));

		modifyCommunicationProfileService.updatePreferences(preferences,
				"1061335723", "+1061335723");
		// when(modifyCommunicationProfileService.updatePreferences(preferences,
		// anyString(),anyString())).thenReturn(null);

	}

	private ModifyCommunicationProfilesResponse modifyCommunicationProfilesResponse(
			boolean isError) {
		ModifyCommunicationProfilesResponse response = new ModifyCommunicationProfilesResponse();
		InvolvedParty invParty = new Individual();
		ContactPoint[] contactPoints = new ContactPoint[4];
		invParty.setContactPoint(contactPoints);
		InvolvedPartyRole involvedPartyRole = new InvolvedPartyRole();
		InvolvedPartyRoleType partyRoleType = new InvolvedPartyRoleType();
		partyRoleType.setValue("CUSTOMER");
		ObjectReference objectReference = new ObjectReference();
		AlternateId[] alternateIds = new AlternateId[1];
		alternateIds[0] = buildAlternateId("CUSTOMER_IDENTIFIER", "1338706705");
		objectReference.setAlternateId(alternateIds);
		involvedPartyRole.setType(partyRoleType);
		invParty.setInvolvedPartyRole(involvedPartyRole);
		response.setPartyDetails(invParty);
		Arrangement[] arrangements = new Arrangement[1];
		arrangements[0] = getModifiedArrangement(isError);
		response.setModifiedArrangements(arrangements);
		return response;
	}

	public DepositArrangement getModifiedArrangement(boolean isError) {
		DepositArrangement depositArrangement = new DepositArrangement();
		Product product = new Product();
		ObjectReference objectReference = new ObjectReference();
		AlternateId[] alternateIds = new AlternateId[1];
		alternateIds[0] = buildAlternateId(
				"EXTERNAL_SYSTEM_PRODUCT_IDENTIFIER", "0320306000");
		alternateIds[0].setSourceLogicalId("4");
		product.setObjectReference(objectReference);
		ProductType hasProductType = new ProductType();
		hasProductType.setName("Account");
		product.setHasProductType(hasProductType);
		depositArrangement.setProduct(product);

		ObjectReference objectReference1 = new ObjectReference();
		AlternateId[] alternateId1s = new AlternateId[1];
		alternateId1s[0] = buildAlternateId(
				"EXTERNAL_SYSTEM_PRODUCT_IDENTIFIER", "0320306000");
		depositArrangement.setObjectReference(objectReference1);

		InvolvedPartyRole[] roles = new InvolvedPartyRole[1];
		InvolvedPartyRoleType type = new InvolvedPartyRoleType();
		type.setValue("CUSTOMER");
		roles[0] = new InvolvedPartyRole();
		roles[0].setType(type);

		ObjectReference objectReference2 = new ObjectReference();
		AlternateId[] alternateId2s = new AlternateId[1];
		alternateId2s[0] = buildAlternateId("SORT_CODE", "309215");
		roles[0].setObjectReference(objectReference2);

		ContactPreference[] contactPreferences = new ContactPreference[1];
		contactPreferences[0] = new ContactPreference();

		RuleCondition[] conditions = new RuleCondition[1];
		conditions[0] = new RuleCondition();
		conditions[0].setName("REASON_CODE");
		AttributeCondition[] arrangement = new AttributeCondition[1];
		arrangement[0] = new AttributeCondition();
		arrangement[0].setDataItem("ERROR_CODE_DETAILS");

		if (isError) {
			AttributeConditionValue[] hasAttributeConditionValues = new AttributeConditionValue[1];
			hasAttributeConditionValues[0] = new AttributeConditionValue();
			hasAttributeConditionValues[0].setCode("24017");
			hasAttributeConditionValues[0].setValue("Call IT HD");
			arrangement[0]
					.setHasAttributeConditionValues(hasAttributeConditionValues);
		}
		conditions[0].setRuleAttributes(arrangement);
		contactPreferences[0].setHasObjectConditions(conditions);

		roles[0].setContactPreferences(contactPreferences);

		depositArrangement.setRoles(roles);
		return depositArrangement;
	}

	public AlternateId buildAlternateId(String key, String value) {
		AlternateId alternateId = new AlternateId();
		alternateId.setAttributeString(key);
		alternateId.setValue(value);
		return alternateId;
	}

	private ModifyCommunicationProfilesResponse externalServiceResponse(
			int scenario, boolean isError) {
		ModifyCommunicationProfilesResponse response = modifyCommunicationProfilesResponse(isError);
		ResponseHeader headers = null;
		switch (scenario) {
		case 0:
			headers = successHeaders(isError);
			break;
		case 1:
			headers = errorHeaders();
			break;
		default:
			headers = successHeaders(isError);
		}
		response.setResponseHeader(headers);
		return response;
	}

	private ResponseHeader errorHeaders() {
		ResponseHeader headers = defaultResponseHeader(1);
		com.lloydstsb.ea.infrastructure.soap.Condition[] conditions = headers
				.getResultCondition().getExtraConditions();
		conditions[0] = condition(1000, "SOME_ERROR");
		return headers;
	}

	private ResponseHeader successHeaders(boolean isError) {
		ResponseHeader headers = defaultResponseHeader(1);
		Condition[] conditions = headers.getResultCondition()
				.getExtraConditions();
		conditions[0] = condition(0, null);
		ModifyCommunicationProfilesResponse response = modifyCommunicationProfilesResponse(isError);
		response.setResponseHeader(headers);
		return headers;
	}

	private ResponseHeader defaultResponseHeader(int numOfCondition) {
		ResponseHeader headers = new ResponseHeader();
		ResultCondition resultCondition = resultCondition(numOfCondition);
		headers.setResultCondition(resultCondition);
		return headers;
	}

	private ResultCondition resultCondition(int numOfCondition) {
		ResultCondition resultCondition = new ResultCondition();
		Condition[] conditions = new Condition[numOfCondition];
		resultCondition.setExtraConditions(conditions);
		return resultCondition;
	}

	private Condition condition(int reasonCode, String reasonText) {
		Condition condition = new Condition();
		condition.setReasonCode(reasonCode);
		condition.setReasonText(reasonText);
		return condition;
	}
}