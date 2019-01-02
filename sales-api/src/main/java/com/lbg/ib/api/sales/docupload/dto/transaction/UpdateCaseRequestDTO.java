/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * All Rights Reserved.
 * Class Name: UpdateCaseRequestDTO
 * Author(s): 8768724
 * Date: 12 Jan 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.transaction;

import java.util.List;

/**
 * @author 8768724
 * 
 */
public class UpdateCaseRequestDTO {

    private String        operationType;

    private List<CaseDTO> caseDetails;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    /**
     * @return the caseDetails
     */
    public List<CaseDTO> getCaseDetails() {
        return caseDetails;
    }

    /**
     * @param caseDetails
     *            the caseDetails to set
     */
    public void setCaseDetails(List<CaseDTO> caseDetails) {
        this.caseDetails = caseDetails;
    }

}
