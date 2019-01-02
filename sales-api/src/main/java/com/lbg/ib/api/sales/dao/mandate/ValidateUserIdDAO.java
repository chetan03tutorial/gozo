package com.lbg.ib.api.sales.dao.mandate;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.mandate.ValidateUserIdDTO;

import java.util.List;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface ValidateUserIdDAO {
    DAOResponse<List<String>> validate(ValidateUserIdDTO dto) throws Exception;
}
