package com.lbg.ib.api.sales.mandate.domain;

import java.util.List;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class UserIdValidated {
    private boolean      isAvailable;
    private List<String> name;

    public UserIdValidated() {
        /* jackson */}

    public UserIdValidated(boolean isAvailable, List<String> name) {
        this.isAvailable = isAvailable;
        this.name = name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public List<String> getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserIdValidated that = (UserIdValidated) o;

        if (isAvailable != that.isAvailable) {
            return false;
        }
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = (isAvailable ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserIdValidated{" + "isAvailable=" + isAvailable + '}';
    }
}
