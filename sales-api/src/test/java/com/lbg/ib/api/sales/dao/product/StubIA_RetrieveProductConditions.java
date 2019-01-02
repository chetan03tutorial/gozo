package com.lbg.ib.api.sales.dao.product;

import java.rmi.RemoteException;

import com.lbg.ib.api.sales.soapapis.commonapi.faults.DatabaseServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.InternalServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ResourceNotAvailableError;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.conditions.IA_RetrieveProductConditions;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsRequest;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class StubIA_RetrieveProductConditions implements IA_RetrieveProductConditions {
    private RetrieveProductConditionsRequest  request;
    private RetrieveProductConditionsResponse response;

    private StubIA_RetrieveProductConditions(RetrieveProductConditionsResponse response) {
        this.response = response;
    }

    public static StubIA_RetrieveProductConditions backendServiceReturnsWith(
            RetrieveProductConditionsResponse response) {
        return new StubIA_RetrieveProductConditions(response);
    }

    public RetrieveProductConditionsResponse retrieveProductConditions(RetrieveProductConditionsRequest request)
            throws RemoteException, ExternalBusinessError, ResourceNotAvailableError, InternalServiceError,
            ExternalServiceError, DatabaseServiceError {
        this.request = request;
        return response;
    }

    public RetrieveProductConditionsRequest receivedRequest() {
        return request;
    }
}
