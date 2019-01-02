package com.lbg.ib.api.sales.docupload.delegate;

import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CreateCaseRequestDTO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

public interface CaseDelegate {
    public CaseDTO createCase(CreateCaseRequestDTO createCaseRequestDTO) throws DocUploadServiceException;
}
