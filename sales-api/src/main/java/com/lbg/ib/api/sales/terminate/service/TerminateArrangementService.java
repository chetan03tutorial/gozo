package com.lbg.ib.api.sales.terminate.service;


import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangement;
import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangementResponse;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 16thFeb2017
 ***********************************************************************/
public interface TerminateArrangementService {
   TerminateArrangementResponse terminateArrangement(TerminateArrangement terminateArrangement);
}
