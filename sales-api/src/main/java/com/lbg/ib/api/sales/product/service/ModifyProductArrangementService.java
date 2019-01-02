package com.lbg.ib.api.sales.product.service;


import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;
import com.lbg.ib.api.shared.exception.ServiceException;

/**
 * Created by 8796528 on 09/08/2018.
 */
public interface ModifyProductArrangementService {
    public ModifyProductArrangement modifyProductArrangement(ModifyProductArrangement modifyProductArrangement) throws ServiceException;
}
