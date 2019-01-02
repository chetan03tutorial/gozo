/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.cbs.service;

import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;

/**
 * Interface for Convert account services.
 * @author tkhann
 */
public interface E592Service {
    /**
     * Method to convert account.
     * @param account
     * @param upgradeOption
     */
    public void convertProductE592(SelectedAccount account, UpgradeOption upgradeOption);

}
