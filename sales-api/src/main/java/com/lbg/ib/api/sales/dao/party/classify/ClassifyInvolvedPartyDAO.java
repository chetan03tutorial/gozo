/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.party.classify;

import java.util.List;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyResponseDTO;

public interface ClassifyInvolvedPartyDAO {

    /**
     * CALL Classify Involved party service
     *
     * @param list
     *            of ClassifyPartyRequestDTO
     * @return if not found will return null
     */
    DAOResponse<List<ClassifyPartyResponseDTO>> classify(ClassifyPartyRequestDTO partyRequest);

}
