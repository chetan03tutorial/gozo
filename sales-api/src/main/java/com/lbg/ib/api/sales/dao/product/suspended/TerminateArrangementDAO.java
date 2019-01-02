package com.lbg.ib.api.sales.dao.product.suspended;


import com.lbg.ib.api.sales.dto.product.pendingarrangement.TerminateArrangementDTO;
import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangement;
import com.lbg.ib.api.shared.domain.DAOResponse;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 16thFeb2017
 ***********************************************************************/

public interface TerminateArrangementDAO {
    DAOResponse<TerminateArrangementDTO> terminateArrangement(TerminateArrangement terminateArrangement);
            
}
