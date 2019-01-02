/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * All Rights Reserved.
 * Class Name: CreateCaseRequestDTO
 * Author(s): 8768724
 * Date: 12 Jan 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.transaction;

/**
 * @author 8768724
 * 
 */
public class CreateCaseRequestDTO {

    private CaseDTO caseDetails;

    public CaseDTO getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(CaseDTO caseDetails) {
        this.caseDetails = caseDetails;
    }
}
