package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.sales.product.domain.E632Response;
import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;

public interface E632Service {

    public E632Response e632(String request);
    
    public E632Response e632WithOverDraftOption(PcciOverdraftRequest overDraftOption);

}
