/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ResponseErrorConstants
 *
 * Author(s): 8735182
 *
 * Date: 13 Oct 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.common.rest.constants;

public class ResponseErrorConstants extends Object {

    public static final String PROCESS_CODE_NOT_FOUND                        = "600006";

    public static final String EXTERNAL_SERVICE_DOWN                         = "600100";

    public static final String INVALID_RESPONSE_FORM_REF_DATA_SERVICE        = "600110";

    public static final String INVALID_RESPONSE_FROM_JWT_SERVICE             = "600120";

    public static final String SERVICE_UNAVAILABLE                           = "600130";

    public static final String EXTERNAL_SERVICE_UNAVAILABLE                  = "600140";

    public static final String INVALID_REQUEST_STRUCTURE                     = "600150";

    public static final String INVALID_PROCESS_CODE                          = "600035";

    public static final String INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE     = "600028";

    /**
     * error code variable for case service error
     */
    public static final String CASE_DATA_NOT_FOUND                           = "600017";

    public static final String INVALID_CONTENT_TYPE                          = "600041";

    public static final String INVALID_RESPONSE_FROM_SEARCH_CASE_SERVICE     = "600200";

    public static final String INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE     = "600210";

    public static final String INVALID_CASE_REFERENCE_NUMBER                 = "600018";

    public static final String INVALID_RESPONSE_FROM_GET_PROCESS_SERVICE     = "TBD";

    public static final String PROCESSES_NOT_FOUND                           = "TBD";

    public static final String INVALID_UPLOAD_DETAILS                        = "600052";

    public static final String INVALID_EVIDENCE_TYPE                         = "600044";

    public static final String INVALID_DOCUMENT_CODE                         = "600045";

    public static final String INVALID_FILE_SIZE                             = "600042";

    public static final String INVALID_PARTY_DETAILS                         = "600036";

    public static final String INVALID_CASEPSF_DETAILS                       = "600051";

    public static final String INVALID_CREATOR_ID                            = "600058";

    public static final String INVALID_UPLOAD_SEQUENCE_NO                    = "600024";

    public static final String INVALID_CASE_ID                               = "600048";

    public static final String INVALID_CASE_STATUS                           = "600049";

    public static final String FILE_NOT_FOUND                                = "600023";

    public static final String INVALID_TMP_SYS_FILE_REF_NUM                  = "600027";

    public static final String INVALID_SESSION_ID                            = "600025";

    public static final String INVALID_CASE_PARTY_ID                         = "600030";

    public static final String INVALID_RESPONSE_FORM_DELETE_DOCUMENT_SERVICE = "600220";

    public static final String INVALID_RESPONSE_FORM_DELETE_SERVICE          = "600230";

    public static final String FAILED_TO_RETRIEVE_FILE                       = "600300";

    public static final String NUMBER_OF_FILES_EXCEEDED                      = "600043";

    public static final String FAILED_TO_UPLOAD_FILE                         = "600310";

    public static final String MALWARE_FILE_DETECTED                         = "600047";

    public static final String INVALID_OTP_RESPONSE                          = "TBC";
    
    public static final String INVALID_OVERDRAFT_AMOUNT 					= "600048";
    
    public static final String INVALID_MAXIMUM_OVERDRAFT_AMOUNT 			= "600049";
    public static final String INVALID_REQUEST_ACTIVATION_FAILED            = "600050";
}
