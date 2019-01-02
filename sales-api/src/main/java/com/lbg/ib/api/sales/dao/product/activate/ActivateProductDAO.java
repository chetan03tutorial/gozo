/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 *
 * All Rights Reserved.
***********************************************************************/

package com.lbg.ib.api.sales.dao.product.activate;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductResponseDTO;

public interface ActivateProductDAO {

    DAOResponse<ActivateProductResponseDTO> activateProduct(ActivateProductDTO requestDTO) throws Exception;

}
