package com.lbg.ib.api.sales.asm.service;

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.pld.request.PldAppealRequest;
import com.lbg.ib.api.sales.product.domain.features.PldProductInfo;
import com.lloydstsb.www.C078Resp;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018.
 * The class is created to work as a Service for C078 resource to fetch App Score information
 */

public interface PldAppealService {

    /* The invokeC078 is used to invoke the CO78 to fetch the App Score response

    * @param        AppScoreRequest
    * @return      The C078Resp
    */
    PldProductInfo fetchAppScoreResultsAndUpdateDecision(PldAppealRequest pldAppealRequest);

}
