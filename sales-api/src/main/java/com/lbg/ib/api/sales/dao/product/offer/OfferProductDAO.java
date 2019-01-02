package com.lbg.ib.api.sales.dao.product.offer;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.product.offer.OfferProductDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface OfferProductDAO {
    DAOResponse<ProductOfferedDTO> offer(OfferProductDTO offerProductDTO) throws Exception;
}
