package com.lbg.ib.api.sales.switching.service;

import com.lbg.ib.api.sales.switching.domain.AccountSwitchingRequest;
import com.lbg.ib.api.sales.switching.domain.AccountSwitchingResponse;

public interface AccountSwitchingService {
    AccountSwitchingResponse switchAccount(AccountSwitchingRequest accountSwitchingRequest);
}
