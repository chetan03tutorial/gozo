/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.colleagues.involvedparty.dao;

import com.lbg.ib.api.sales.colleagues.involvedparty.dto.RetrieveInvolvedPartyResponseDTO;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lloydstsb.lcsm.involvedpartymanagement.RetrieveInvolvedPartyRoleDetailsRequest;

public interface RetrieveInvolvedPartyDAO {

    /**
     * @param retrieveRolesForInvolvedPartyRequest
     * @return
     */
    public DAOResponse<RetrieveInvolvedPartyResponseDTO> retrieveRolesForInvolvedParty(
            RetrieveInvolvedPartyRoleDetailsRequest involvedPartyRoleDetailsRequest);

}
