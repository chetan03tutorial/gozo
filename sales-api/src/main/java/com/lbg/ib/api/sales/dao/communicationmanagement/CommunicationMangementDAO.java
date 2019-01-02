
/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.communicationmanagement;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementDTO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementResponseDTO;

public interface CommunicationMangementDAO {

    /**
     * Method name sendEmailCommunication
     *
     * @param CommunicationManagementDTO
     * @return SendCommunicationResponse for Email
     */
    DAOResponse<CommunicationManagementResponseDTO> sendEmailCommunictaion(CommunicationManagementDTO sendCommunication,
            UserContext userContext);

}
