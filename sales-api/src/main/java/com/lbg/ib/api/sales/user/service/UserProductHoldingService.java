package com.lbg.ib.api.sales.user.service;

import java.math.BigInteger;
import java.util.List;

import com.lbg.ib.api.sales.paperless.dto.Account;

public interface UserProductHoldingService {

    public List<Account> retrieveProductHolding(String partyId, BigInteger ocisId);

}
