package com.lbg.ib.api.sales.shared.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sso.domain.mca.BranchContext;

public class SessionServiceUtil {

    private static final String sessionId = "SOME_RANDOM_SESSION_ID";

    public static UserContext prepareUserContext(String brand) {
        return new UserContext("871515", "192.168.1.1", sessionId, "+100000", "1204629", brand, "chansecMode", "Chrome", "En", "inboxIdClient", "host");
    }

    public static BranchContext prepareBranchContext() {
        BranchContext branchContext = new BranchContext();
        branchContext.setColleagueId("871515");
        branchContext.setDomain("TEST01GLOBAL");
        List<String> roleList = new LinkedList<String>();
        roleList.add("BranchAdmin");
        branchContext.setRoles(roleList);
        branchContext.setOriginatingSortCode("EC1Y4XX");
        branchContext.setTellerId("tellerId");
        return branchContext;
    }

    public static Map<String, PartyDetails> buildParties(int num) {
        Map<String, PartyDetails> parties = new HashMap<String, PartyDetails>();
        for(int index=0 ; index < num; index++){
            parties.put(String.valueOf(index), buildParty(index));
        }
        return parties;
    }

    private static PartyDetails buildParty(int index) {
        PartyDetails party = null;
        party = new PartyDetails();
        party.setEmail("email" + index + "@email.com");
        party.setFirstName("firstName" + index);
        party.setTitle("Mr");
        party.setPostalCode("N" + index + "HR");
        party.setIsPrimaryParty(index==0?true:false);
        String[] addressLines = new String[4];
        addressLines[0] = "Address1";
        addressLines[1] = "Address2";
        addressLines[2] = "Address3";
        addressLines[3] = "Address4";
        party.setAddressLines(addressLines);
        return party;
    }
    
    
    private Arrangement arrangement() {
		Arrangement arrangement = new Arrangement();
		List<Account> accounts = Lists.newArrayList();
		PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
		primaryInvolvedParty.setFirstName("Test");
		primaryInvolvedParty.setLastName("User");
		primaryInvolvedParty.setEmail("abc@abc.com");
		primaryInvolvedParty.setDob("1995-11-23");
		accounts.add(createAccount("100000", "909090", "0", new Double(100)));
		accounts.add(createAccount("100001", "909090", "10", new Double(100)));
		accounts.add(createAccount("100002", "909090", "20", new Double(100)));
		accounts.add(createAccount("100003", "909090", "300", new Double(100)));
		accounts.add(createAccount("100004", "909090", "40", new Double(100)));
		arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
		arrangement.setAccounts(accounts);
		return arrangement;
	}
    
    private Account createAccount(String accountNumber, String sortcode, String odLimit, Double availableBalance) {
		Account account = new Account();
		account.setAccountNumber(accountNumber);
		account.setSortCode(sortcode);
		account.setOverdraftLimit(new Double(odLimit));
		account.setAvailableBal(availableBalance);
		return account;
	}
    
    

}
