
/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.communication.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.communication.domain.InvolvedPartyDetails;
import com.lbg.ib.api.sales.dto.communicationparty.CommunicationPartyDetailsDTO;

public interface CommunicationPartyService {
    /**
     * Method is called to set the party details in the session for pipeline
     * chasing once the customer has ended the session
     *
     * @param partyDetails
     * @throws ServiceException
     */
    void savePartyDetailsForSendCommunication(InvolvedPartyDetails partyDetails) throws ServiceException;

    /**
     * Method is called for sending an pipeline chaser email once the user has
     * ended the session
     *
     * @param partyDetails
     * @param mnemonic
     */
    void sendEmailCommunictaion(CommunicationPartyDetailsDTO partyDetails, String mnemonic, UserContext userContext);
}
