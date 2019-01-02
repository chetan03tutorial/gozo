package com.lbg.ib.api.sales.overdraft.service;

import com.lbg.ib.api.sales.overdraft.domain.PcciResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;

public interface PcciService {

    public PcciResponse getPcciOverdraftDetails(String productMnemonic);

    public PcciResponse getPcciOverdraftDetails();
    
    public PcciResponse getPcciOverdraftDetailsWithOverdraftOption(PcciOverdraftRequest overDraftOption);
    
    
}
