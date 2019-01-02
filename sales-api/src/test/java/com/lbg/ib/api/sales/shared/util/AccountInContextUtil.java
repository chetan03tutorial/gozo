package com.lbg.ib.api.sales.shared.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;

public class AccountInContextUtil {

	public Arrangement getUserInfo() {
		return arrangement();
	}

	public SelectedAccount getSelectedAccount() {
		SelectedAccount selectedAccount = new SelectedAccount();
		Account account = productHolding().get(0);
		selectedAccount.setAccountNumber(account.getAccountNumber());
		selectedAccount.setSortCode(account.getSortCode());
		selectedAccount.setOverdraft(account.getOverdraftLimit());
		return selectedAccount;
	}

	private static Arrangement arrangement() {
		String partyId = "";
		String ocisId = "";
		Arrangement arrangement = new Arrangement();
		arrangement.setPartyId(partyId);
		arrangement.setOcisId(ocisId);
		arrangement.setPrimaryInvolvedParty(primaryInvolvedParty());
		arrangement.setAccounts(productHolding());
		return arrangement;
	}

	private static Account createAccount(String accountNumber, String sortcode, String odLimit, Double availableBalance,Double ledgerBalance,
			String accountType) {
		Account account = new Account();
		account.setAccountNumber(accountNumber);
		account.setSortCode(sortcode);
		account.setOverdraftLimit(new Double(odLimit));
		account.setAvailableBal(availableBalance);
		account.setAccountType(accountType);
		account.setLedgerBal(ledgerBalance);
		return account;
	}

	private static PrimaryInvolvedParty primaryInvolvedParty() {
		PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
		primaryInvolvedParty.setFirstName("Test");
		primaryInvolvedParty.setLastName("User");
		primaryInvolvedParty.setEmail("abc@abc.com");
		primaryInvolvedParty.setDob("1995-11-23");
		return primaryInvolvedParty;
	}

	private static List<Account> productHolding() {
		List<Account> accounts = Lists.newArrayList();
		accounts.add(createAccount("100000", "909090", "0", new Double(400),new Double(100), "T100010"));
		accounts.add(createAccount("100001", "909090", "10", new Double(400),new Double(600), "T100010"));
		accounts.add(createAccount("100002", "909090", "20", new Double(400),new Double(100), "T100010"));
		accounts.add(createAccount("100003", "909090", "300", new Double(200),new Double(100), "T100010"));
		accounts.add(createAccount("100004", "909090", "40", new Double(400),new Double(100), "T100010"));
		accounts.add(createAccount("100005", "909090", "1200", new Double(2000),new Double(600), "T100010"));
		return accounts;
	}
	
	public Map<String, PartyDetails> getParties(){
		Map<String, PartyDetails> partyDetails = new HashMap<String, PartyDetails>();
		for(int index=1000; index < 1002; index++) {
			PartyDetails party = createParty(true);
			partyDetails.put(String.valueOf(index),party);
		}
		return partyDetails;
	}
	
	public PartyDetails createParty(boolean partyType) {
		PartyDetails party = new PartyDetails();
		party.setFirstName("");
		party.setEmail("");
		party.setIsPrimaryParty(partyType);
		return party;
	}
	
	
}
