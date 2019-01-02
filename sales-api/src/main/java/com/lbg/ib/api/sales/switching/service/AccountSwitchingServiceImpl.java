package com.lbg.ib.api.sales.switching.service;

import com.lbg.ib.api.sales.switching.domain.AccountSwitchingRequest;
import com.lbg.ib.api.sales.switching.domain.AccountSwitchingResponse;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountSwitchingServiceImpl implements AccountSwitchingService {
    private PEGAService accountSwitchingPEGAService;
    @Autowired
    public AccountSwitchingServiceImpl(PEGAService accountSwitchingPEGAService) {
        this.accountSwitchingPEGAService = accountSwitchingPEGAService;
    }

    @TraceLog
    public AccountSwitchingResponse switchAccount(AccountSwitchingRequest accountSwitchingRequest) {
        final String pegaCaseId = accountSwitchingPEGAService.createPegaCase(accountSwitchingRequest);
        return new AccountSwitchingResponse(pegaCaseId);
    }
}
