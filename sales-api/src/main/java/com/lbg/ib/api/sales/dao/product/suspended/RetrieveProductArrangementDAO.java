package com.lbg.ib.api.sales.dao.product.suspended;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.product.pendingarrangement.ProductArrangementDTO;
import com.lbg.ib.api.sales.product.domain.pending.ArrangementId;

public interface RetrieveProductArrangementDAO {
    DAOResponse<ProductArrangementDTO> retrieveProductArrangementPending(ArrangementId referenceId) throws Exception;
}
