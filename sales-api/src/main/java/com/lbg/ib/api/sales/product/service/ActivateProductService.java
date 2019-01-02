/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 *
 * All Rights Reserved.
***********************************************************************/

package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.product.domain.activate.Activated;
import com.lbg.ib.api.sales.product.domain.activate.Activation;
import com.lbg.ib.api.sales.product.domain.activate.ArrangementId;

public interface ActivateProductService {

    Activated activateProduct(ArrangementId arrangementId, Activation activation) throws ServiceException;

}
