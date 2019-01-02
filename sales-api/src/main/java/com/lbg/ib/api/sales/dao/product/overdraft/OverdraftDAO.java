package com.lbg.ib.api.sales.dao.product.overdraft;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftRequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;

public interface OverdraftDAO {

    DAOResponse<OverdraftResponseDTO> fetchOverdraftDetails(OverdraftRequestDTO result);

}
