/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.dao;

import java.math.BigInteger;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.paperless.dto.PersonalDetailsDTO;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoDTO;

/**
 * Interface to retrieve paperless details from DB. Created by pbabb1 on
 * 9/6/2016.
 */
public interface PaperlessDAO {

    /**
     * Retrieve mandate information based on ocisID.
     *
     * @param ocisId
     * @return UserMandateInfoDTO
     */
    public DAOResponse<UserMandateInfoDTO> getUserMandateData(BigInteger ocisId);

    /**
     * Update personal details of user.
     *
     * @param details
     *            PersonalDetails
     * @return null if success
     */
    public DAOResponse<String> updatePersonalDetails(PersonalDetailsDTO details);
}
