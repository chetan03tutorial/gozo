package com.lbg.ib.api.sales.dao.product.holding;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.ProductHoldingResponseMapper;
import com.lbg.ib.api.sales.dto.product.ProductHoldingRequestDTO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface ProductHoldingDAO {
    DAOResponse<com.lbg.ib.api.sales.product.domain.ProductHolding> fetchProductHoldings(
            ProductHoldingRequestDTO request);

    void setLoggerDAO(LoggerDAO logger);

    void setProductHoldingResponseMapper(ProductHoldingResponseMapper responseMapper);

    void setIsTestRun(boolean isTestRun);
}
