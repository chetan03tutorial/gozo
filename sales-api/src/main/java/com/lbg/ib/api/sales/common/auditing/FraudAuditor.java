
package com.lbg.ib.api.sales.common.auditing;

import com.lbg.ib.api.sales.dto.product.activate.ActivateProductResponseDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface FraudAuditor {

    void audit(ProductOfferedDTO auditee);

    void audit(ActivateProductResponseDTO auditee);
}
