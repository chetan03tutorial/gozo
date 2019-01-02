package com.lbg.ib.api.sales.shared;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtil;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@RunWith(MockitoJUnitRunner.class)
public class AccountInContextUtilityTest {

	@InjectMocks
	private AccountInContextUtility contextUtility;

	@Mock
	private SessionManagementDAO sessionManager;
	@Mock
	private LoggerDAO logger;
	private AccountInContextUtil contextHelper = new AccountInContextUtil();
	private static final String MAX_OVERDRAFT_LIMIT_BY_ASM = "maxOdLimitAsm";

	@Before
	public void setup() {
		when(sessionManager.getUserInfo()).thenReturn(contextHelper.getUserInfo());
		when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(contextHelper.getParties());
		when(sessionManager.getAccountToConvertInContext()).thenReturn(contextHelper.getSelectedAccount());
		when(sessionManager.getFromUserMap(MAX_OVERDRAFT_LIMIT_BY_ASM)).thenReturn(new Double(200));
		doNothing().when(sessionManager).setInUserMap(anyString(), any());
	}

	@Test
	public void testRetrievalOfPrimaryInvolvedParty() {
		contextUtility.getPrimaryInvolvedParty();
	}

	@Test
	public void testRetrievalOfPrimaryParty() {
		contextUtility.getPrimaryPartyDetails();
	}

	@Test
	public void testRetrievalOfJointParty() {
		contextUtility.getJointPartyDetails();
		contextUtility.getAvailedOverdraft();
	}

	@Test
	public void testRetrievalOfPrimaryPartyId() {
		contextUtility.getPrimaryPartyOcisId();
	}

	@Test
	public void testRetrievalOfSelectedAccount() {
		contextUtility.getSelectedAccountDetail();
	}

	@Test
	public void testRetrievalOfAsmOverdraftLimit() {
		contextUtility.getMaximumOverdraftLimit();
	}

	/*@Test
	public void testRetrievalOfTriadOverdraftLimit() {
		when(sessionManager.getMaxOverDraftLimit()).thenReturn("100");
		contextUtility.getMaximumTriadOverdraftLimit();
	}*/
}
