package com.lbg.ib.api.sales.overdraft.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.common.spring.SpringContextHolder;
import com.lbg.ib.api.sales.overdraft.domain.IeCustomerDetails;
import com.lbg.ib.api.sales.overdraft.domain.Q122Request;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtil;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.PartyInformation;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lloydsbanking.xml.DecisionDetails;
import com.lloydsbanking.xml.Q122Resp;
import com.lloydsbanking.xml.Q122Result;

@RunWith(MockitoJUnitRunner.class)
public class Q122ServiceTest {

	@Mock
	private ChannelBrandingDAO channelService;

	@Mock
	private ModuleContext beanLoader;

	@Mock
	private SOAInvoker soaInvoker;

	@InjectMocks
	private Q122ServiceImpl q122Service;

	@Mock
	private AccountInContextUtility contextUtility;

	@Mock
	private ServiceErrorValidator serviceErrorValidator;

	@Mock
	private SpringContextHolder contextHolder;

	@Mock
	private LoggerDAO logger;

	private static AccountInContextUtil context = new AccountInContextUtil();
	private List<Account> accountList = context.getUserInfo().getAccounts();

	private Q122Request q122Request() {
		Q122Request q122request = new Q122Request();
		q122request.setDemandedOd(new String("100"));

		IeCustomerDetails ieCustomerDetails = new IeCustomerDetails();
		ieCustomerDetails.setAccommodationExpenses("1000");
		ieCustomerDetails.setIncomeFrequency("4");
		ieCustomerDetails.setMonthlyExpenses("2000");
		ieCustomerDetails.setMonthlyIncome("10000");

		PartyInformation primaryParty = new PartyInformation();
		primaryParty.setIeIndicator(true);
		primaryParty.setPrimary(true);
		primaryParty.setIeDetails(ieCustomerDetails);

		PartyInformation jointParty = new PartyInformation();
		jointParty.setIeIndicator(false);
		jointParty.setPrimary(false);

		ArrayList<PartyInformation> parties = new ArrayList<PartyInformation>();
		parties.add(primaryParty);
		parties.add(jointParty);
		q122request.setParties(parties);
		return q122request;
	}

	private Q122Request q122RequestWithoutIEDetails() {
		Q122Request q122request = new Q122Request();
		q122request.setDemandedOd(new String("100"));
		return q122request;
	}

	private Q122Resp q122Response() {
		Q122Resp q122Response = new Q122Resp();
		Q122Result q122Result = new Q122Result();
		ResultCondition resultCondition = new ResultCondition();
		q122Result.setResultCondition(resultCondition);
		q122Response.setQ122Result(q122Result);
		q122Response.setAffordableAm("1000");

		DecisionDetails[] decisionList = new DecisionDetails[1];
		DecisionDetails decisionDetail = new DecisionDetails();
		decisionDetail.setCSDecisnReasonTypeNr("Accept");
		decisionDetail.setCSDecisionReasonTypeCd("601");
		decisionList[0] = decisionDetail;
		q122Response.setDecisionDetails(decisionList);
		q122Response.setASMCreditScoreResultCd("1");
		return q122Response;
	}

	@Before
	public void setup() {
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(q122Response());
		when(contextUtility.getSelectedAccountDetail()).thenReturn(accountList.get(0));
		when(contextUtility.getPrimaryPartyDetails()).thenReturn(party());
		when(contextUtility.getPrimaryPartyOcisId()).thenReturn("10001");
		when(contextUtility.getJointPartyOcisId()).thenReturn("10002");
		when(contextUtility.getJointPartyDetails()).thenReturn(party());
		when(contextUtility.getPrimaryPartyCidPersonalId()).thenReturn("+000400001");
		when(contextUtility.getPrimaryPartyCbsCustomerNumber()).thenReturn("9012023");
		doNothing().when(serviceErrorValidator).validateResponseError(any(ResultCondition.class));
		doNothing().when(contextUtility).setMaximumOverdraftLimit(anyDouble());
		when(channelService.getChannelBrand())
				.thenReturn(DAOResponse.withResult(new ChannelBrandDTO("Lloyds", "LLOYDS", "IBL")));
	}

	@Test
	public void testSuccessOfQ122WithIncomeExpenditure() {
		q122Service.invokeQ122(q122Request(), ApplicationType.A034.getValue());
	}

	@Test
	public void testSuccessOfQ122WithoutIncomeExpenditure() {
		q122Service.invokeQ122(q122Request(), ApplicationType.A034.getValue());
	}

	public PartyDetails party() {
		PartyDetails party = new PartyDetails();
		party.setDob("2014-03-31");
		String[] addressLines = new String[4];
		addressLines[0] = "1";
		addressLines[1] = "2";
		addressLines[2] = "3";
		addressLines[3] = "4";
		party.setAddressLines(addressLines);
		return party;
	}
}
