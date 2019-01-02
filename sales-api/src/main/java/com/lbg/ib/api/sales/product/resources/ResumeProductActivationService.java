package com.lbg.ib.api.sales.product.resources;

import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;

/**
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved. The class UserResource is the RestApi responsible for
 * fetching the UserInfo
 *
 * @author Atul Choudhary/Debashish Bhattacharjee
 * @version 1.0
 * @since 9thMarch2016
 */
public interface ResumeProductActivationService {
    void resumeProductActivation(CustomerPendingDetails customerPendingDetails);
}
