/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * All Rights Reserved.
 * Class Name: UpdateCaseResponseDTO
 * Author(s): 8768724
 * Date: 22 Mar 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.transaction;

/**
 * @author 8768724
 * 
 */
public class UpdateCaseResponseDTO {

    private String  serviceResponse;

    private CaseDTO caseDetails;

    public String getServiceResponse() {
        return serviceResponse;
    }

    public void setServiceResponse(String serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * @return the caseDetails
     */
    public CaseDTO getCaseDetails() {
        return caseDetails;
    }

    /**
     * @param caseDetails
     *            the caseDetails to set
     */
    public void setCaseDetails(CaseDTO caseDetails) {
        this.caseDetails = caseDetails;
    }

}
