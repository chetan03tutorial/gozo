package com.lbg.ib.api.sales.dao.threatmatrix;

import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.common.*;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.lcsm.ErrorInfo;

import java.rmi.RemoteException;

import static org.apache.commons.lang.StringEscapeUtils.unescapeHtml;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class StubThreatMatrix implements ThreadMatrix {
    private String               expectedRequestBlob;
    private ThreadMatrixResponse primedResponse;
    private Throwable            throwable;

    public ThreadMatrixResponse invoke(ThreadMatrixRequest threatMatrixRequest) throws RemoteException, ErrorInfo {
        throwsExceptionWhenSet();
        String actualBlob = unescapeHtml(threatMatrixRequest.getBlob().getBlob());
        if (!expectedRequestBlob.equals(actualBlob)) {
            throw new IllegalStateException(
                    String.format("Expected blob is not matching: %nExpected blob:%n%s%n%n Actual blob:%n%s%n%n",
                            expectedRequestBlob, actualBlob));
        }
        return primedResponse;
    }

    private void throwsExceptionWhenSet() throws RemoteException, ErrorInfo {
        if (throwable == null) {
            return;
        } else if (throwable instanceof RemoteException) {
            throw (RemoteException) throwable;
        } else if (throwable instanceof ErrorInfo) {
            throw (ErrorInfo) throwable;
        } else {
            throw new IllegalArgumentException(
                    "Invalid exception type for this service: " + throwable.getClass().getName());
        }
    }

    StubThreatMatrix returnsResponse(String blobContent) {
        primedResponse = new ThreadMatrixResponse();
        BlobResponse blob = new BlobResponse();
        blob.setBlob(blobContent);
        primedResponse.setBlob(blob);
        return this;
    }

    StubThreatMatrix forRequest(String blob) {
        expectedRequestBlob = blob;
        return this;
    }

    public static StubThreatMatrix threadMatrix() {
        return new StubThreatMatrix();
    }

    public StubThreatMatrix throwz(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }
}
