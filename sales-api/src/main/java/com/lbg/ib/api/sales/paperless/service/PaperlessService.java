/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.service;

import com.lbg.ib.api.sales.paperless.dto.PersonalDetails;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoResult;

/**
 * Interface for Paperless services.
 * 
 * @author tkhann
 */

public interface PaperlessService {

    /**
     * Method to retrieve Use Mandate information.
     * 
     * @param ocisId
     *            String
     * @return UserMandateInfoResult
     */
    public UserMandateInfoResult getUserMandateInfo(String ocisId);

    /**
     * Method to update the email address.
     * 
     * @param personalDetails
     *            String
     */
    public void updateEmail(PersonalDetails personalDetails,String ocisId,String partyId);

}
