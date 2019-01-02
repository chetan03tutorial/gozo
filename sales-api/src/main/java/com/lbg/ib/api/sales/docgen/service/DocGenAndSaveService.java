package com.lbg.ib.api.sales.docgen.service;
/*
Created by 8601769 at 07/05/2018 12:38
*/

import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveResponseParty;

import java.util.List;

public interface DocGenAndSaveService {

    /**
     * API to generate document and upload.
     * @param docGenAndSaveRequest
     * @return
     */
    public List<DocGenAndSaveResponseParty> generateAndSaveDocument(DocGenAndSaveRequest docGenAndSaveRequest);
}
