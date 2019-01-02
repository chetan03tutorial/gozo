package com.lbg.ib.api.sales.docgen.domain;
/*
Created by 8601769 at 07/05/2018 12:46
This will serve as the response for Doc generation and save utility.
*/

import java.util.List;

public class DocGenAndSaveResponse {
    List<DocGenAndSaveResponseParty> docGenAndSaveResponsePartyList;

    public List<DocGenAndSaveResponseParty> getDocGenAndSaveResponsePartyList() {
        return docGenAndSaveResponsePartyList;
    }

    public void setDocGenAndSaveResponsePartyList(List<DocGenAndSaveResponseParty> docGenAndSaveResponsePartyList) {
        this.docGenAndSaveResponsePartyList = docGenAndSaveResponsePartyList;
    }
}
