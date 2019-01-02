
/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.domain;

import java.util.List;

public class Classifications {
    private List<ClassifiedPartyDetails> classifications;

    public List<ClassifiedPartyDetails> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<ClassifiedPartyDetails> classifications) {
        this.classifications = classifications;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classifications == null) ? 0 : classifications.hashCode());
        return result;
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
        Classifications other = (Classifications) obj;
        if (classifications == null) {
            if (other.classifications != null) {
                return false;
            }
        } else if (!classifications.equals(other.classifications)) {
            return false;
        }
        return true;
    }

}
