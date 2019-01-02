/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.switches;

import java.util.HashMap;

import com.lbg.ib.api.shared.domain.DAOResponse;

public interface SwitchesManagementDAO {

    /**
     * This will return the switche status only for the switches configured in
     * configApplicationSwitches.xml
     *
     * @param channelId
     * @return - HashMap<SwitchName, SwitchStatus>
     */
    public DAOResponse<HashMap<String, Boolean>> getSwitches(String channelId);
}
