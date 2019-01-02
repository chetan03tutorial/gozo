/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.service;

import java.util.List;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.party.domain.ClassifiedPartyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;

public interface TinDetailsService {
    List<ClassifiedPartyDetails> identify(TinDetails partyDetails) throws ServiceException;
}
