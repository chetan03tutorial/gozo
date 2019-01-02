/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: CreateCaseDelegate
 *
 * Author(s):Parameshwaran Kangamuthu(1146728)
 *
 * Date: 04 Jan 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate;

import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author 1146728
 * 
 */
public interface CreateCaseDelegate {

    /**
     * @param caseDTO
     * @return
     * @throws DocUploadServiceException
     */
    CaseDTO createCase(CaseDTO caseDTO) throws DocUploadServiceException;

}
