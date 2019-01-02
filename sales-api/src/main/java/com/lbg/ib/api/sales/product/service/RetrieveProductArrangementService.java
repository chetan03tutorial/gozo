package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.product.domain.pending.ArrangementId;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;

public interface RetrieveProductArrangementService {
    CustomerPendingDetails retrieveProductArrangement(ArrangementId reference) throws ServiceException;
}
