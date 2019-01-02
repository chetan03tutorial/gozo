/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.colleagues.involvedparty.service;

import com.lbg.ib.api.sales.paperless.dto.UserPreferences;

/**
 * Service for communication profiles.
 * 
 * @author tkhann
 */
public interface ModifyCommunicationProfileService {
    /**
     * Method to update preferences.
     * 
     * @param preferences
     *            UserPreferences
     * @param ocisId
     *            String
     * @param partyId
     *            String
     */
    public void updatePreferences(UserPreferences preferences, String ocisId, String partyId);

}
