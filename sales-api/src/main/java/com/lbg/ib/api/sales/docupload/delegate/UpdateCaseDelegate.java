/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 * All Rights Reserved.
 * Class Name: SubmitCaseDelegate
 * Author(s):8768724
 * Date: 22 Jan 2016
 *********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate;

import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author 8768724
 * 
 */
public interface UpdateCaseDelegate {

    CaseDTO updateCaseDetails(CaseDTO caseDto, String operatorType) throws DocUploadServiceException;

}
