package com.lbg.ib.api.sales.dao.product.modify;


import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;
import com.lbg.ib.api.shared.domain.DAOResponse;

public interface ModifyProductArrangementDAO {
    DAOResponse<ModifyProductArrangement> modifyProductArrangement(ModifyProductArrangement modifyProductArrangement);
}
