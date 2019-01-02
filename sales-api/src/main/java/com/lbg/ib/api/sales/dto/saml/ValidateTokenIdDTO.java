package com.lbg.ib.api.sales.dto.saml;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;


/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved. The ValidateTokenIdDTO is the validated token DTO.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 ***********************************************************************/

@Validate
public class ValidateTokenIdDTO {
    @RequiredFieldValidation
    private String tokenID;

    public ValidateTokenIdDTO() {
        /* jackson */
    }

    public ValidateTokenIdDTO(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getTokenID() {
        return tokenID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValidateTokenIdDTO that = (ValidateTokenIdDTO) o;

        if (tokenID != null ? !tokenID.equals(that.tokenID) : that.tokenID != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = tokenID != null ? tokenID.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "TokenIdValidation{" + "tokenID='" + tokenID + '\'' + '}';
    }
}
