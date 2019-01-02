/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.switches.service;

import com.lbg.ib.api.sales.common.exception.SwitchLoadFailedException;

public interface SwitchesManagementService {

    public void loadSwitches() throws SwitchLoadFailedException;

}
