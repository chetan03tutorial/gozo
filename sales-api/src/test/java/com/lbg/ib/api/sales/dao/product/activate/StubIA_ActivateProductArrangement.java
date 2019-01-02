package com.lbg.ib.api.sales.dao.product.activate;

import java.rmi.RemoteException;

import com.lbg.ib.api.sales.soapapis.activateproduct.conditions.IA_ActivateProductArrangement;
import com.lbg.ib.api.sales.soapapis.activateproduct.reqresp.ActivateProductArrangementRequest;
import com.lbg.ib.api.sales.soapapis.activateproduct.reqresp.ActivateProductArrangementResponse;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.DatabaseServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.InternalServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ResourceNotAvailableError;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class StubIA_ActivateProductArrangement implements IA_ActivateProductArrangement {
    private ActivateProductArrangementResponse response;
    private ActivateProductArrangementRequest  calledWith;

    private StubIA_ActivateProductArrangement(ActivateProductArrangementResponse response) {
        this.response = response;
    }

    public static StubIA_ActivateProductArrangement returnWith(ActivateProductArrangementResponse response) {
        return new StubIA_ActivateProductArrangement(response);
    }

    public ActivateProductArrangementResponse activateProductArrangement(ActivateProductArrangementRequest request)
            throws RemoteException, ExternalBusinessError, ExternalServiceError, DatabaseServiceError,
            InternalServiceError, ResourceNotAvailableError {
        this.calledWith = request;
        return response;
    }

    public ActivateProductArrangementRequest isCalledWith() {
        return calledWith;
    }
}
