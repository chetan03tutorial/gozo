package com.lbg.ib.api.sales.docgen.service;
/*
Created by Rohit.Soni at 02/05/2018 10:58
*/

import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.user.domain.PartyDetails;

public interface GenerateDocumentService {

    byte[] generateDocument(DocGenAndSaveRequest docGenAndSaveRequest, PartyDetails party);
}
