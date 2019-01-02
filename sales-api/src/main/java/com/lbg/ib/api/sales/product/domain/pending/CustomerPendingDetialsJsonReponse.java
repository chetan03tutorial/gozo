package com.lbg.ib.api.sales.product.domain.pending;

import java.util.ArrayList;
import java.util.List;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 8thSeptember2016
 ***********************************************************************/
public class CustomerPendingDetialsJsonReponse {

    private List<CustomerPendingDetails> customerPendingDetails;

    public List<CustomerPendingDetails> getCustomerPendingDetails() {
        if (customerPendingDetails != null) {
            return customerPendingDetails;
        } else {
            return new ArrayList<CustomerPendingDetails>();
        }
    }

    public void setCustomerPendingDetails(List<CustomerPendingDetails> customerPendingDetails) {
        this.customerPendingDetails = customerPendingDetails;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerPendingDetails == null) ? 0 : customerPendingDetails.hashCode());
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
        CustomerPendingDetialsJsonReponse other = (CustomerPendingDetialsJsonReponse) obj;
        if (customerPendingDetails == null) {
            if (other.customerPendingDetails != null) {
                return false;
            }
        } else if (!customerPendingDetails.equals(other.customerPendingDetails)) {
            return false;
        }
        return true;
    }
}
