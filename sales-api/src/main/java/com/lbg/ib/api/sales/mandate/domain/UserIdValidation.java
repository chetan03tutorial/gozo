package com.lbg.ib.api.sales.mandate.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Validate
public class UserIdValidation {
    @RequiredFieldValidation
    private String arrangementID;
    @RequiredFieldValidation
    private String username;
    @RequiredFieldValidation
    private String password;

    public UserIdValidation() {
        /* jackson */}

    public UserIdValidation(String arrangementID, String username, String password) {
        this.arrangementID = arrangementID;
        this.username = username;
        this.password = password;
    }

    public String getArrangementID() {
        return arrangementID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserIdValidation that = (UserIdValidation) o;

        if (arrangementID != null ? !arrangementID.equals(that.arrangementID) : that.arrangementID != null) {
            return false;
        }
        if (username != null ? !username.equals(that.username) : that.username != null) {
            return false;
        }
        return !(password != null ? !password.equals(that.password) : that.password != null);

    }

    @Override
    public int hashCode() {
        int result = arrangementID != null ? arrangementID.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("UserIdValidation{" + "arrangementID='" + arrangementID + '\'' + "" + '}').toString();
    }

}
