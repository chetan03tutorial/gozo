package com.lbg.ib.api.sales.overdraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.overdraft.domain.PcciResponse;
import com.lbg.ib.api.sales.overdraft.mapper.PcciOverdraftMessageMapper;
import com.lbg.ib.api.sales.product.domain.E632Response;
import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;
import com.lbg.ib.api.sales.product.service.E632Service;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;

@Component
public class PcciServiceImpl implements PcciService {

    @Autowired
    private ModuleContext beansLoader;

    /**
     * Return the pcci value with product mnemonic
     * @return
     */
    public PcciResponse getPcciOverdraftDetails(String productMnemonic) {
        E632Service service = beansLoader.getService(E632Service.class);
        E632Response e632Response = service.e632(productMnemonic);
        return PcciOverdraftMessageMapper.buildPcciResponse(e632Response);
    }

    /**
     * Return the pcci value without product mnemonic
     * @return
     */
    public PcciResponse getPcciOverdraftDetails() {
        return getPcciOverdraftDetails(null);
    }

    /**
     * Return the pcci value with OverdraftOption
     */
	public PcciResponse getPcciOverdraftDetailsWithOverdraftOption(PcciOverdraftRequest overDraftOption) {
		E632Service service = beansLoader.getService(E632Service.class);
		E632Response e632Response = service.e632WithOverDraftOption(overDraftOption);
		return PcciOverdraftMessageMapper.buildPcciResponse(e632Response);
	}

   
}
