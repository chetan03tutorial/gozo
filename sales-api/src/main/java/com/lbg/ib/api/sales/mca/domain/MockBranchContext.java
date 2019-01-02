/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.mca.domain;

public class MockBranchContext {

    private String originatingSortCode;

    public MockBranchContext(String originatingSortCode) {
        this.originatingSortCode = originatingSortCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MockBranchContext other = (MockBranchContext) obj;
        if (originatingSortCode == null) {
            if (other.originatingSortCode != null) {
                return false;
            }
        } else if (!originatingSortCode.equals(other.originatingSortCode)) {
            return false;
        }
        return true;
    }

    public String getOriginatingSortCode() {
        return originatingSortCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((originatingSortCode == null) ? 0 : originatingSortCode.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "originatingSortCode [originatingSortCode=" + originatingSortCode + "]";
    }

}
