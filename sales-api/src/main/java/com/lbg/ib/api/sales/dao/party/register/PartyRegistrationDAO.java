package com.lbg.ib.api.sales.dao.party.register;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.party.PartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.PartyResponseDTO;

/**
 * @author 6538334
 *
 */
public interface PartyRegistrationDAO {

    /**
     * CALL bapi B801 to retrieve the user mandate
     *
     * @param party
     * @return if not found will return null
     */
    DAOResponse<PartyResponseDTO> retrievePartyMandate(PartyRequestDTO partyRequest);

}
