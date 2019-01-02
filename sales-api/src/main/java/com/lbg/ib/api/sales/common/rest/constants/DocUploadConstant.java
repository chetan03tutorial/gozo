/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 * All Rights Reserved.
 * Class Name: DocUploadConstant
 * Author(s):8768724
 * Date: 1 Oct 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.common.rest.constants;

public class DocUploadConstant extends Object {

    public static final String SUCCESS                           = "Success";

    public static final String FAILURE                           = "Failure";

    public static final String ERROR                             = "Error";

    public static final int    ZERO                              = 0;

    public static final int    ONE                               = 1;

    public static final int    TWO                               = 2;

    public static final int    THREE                             = 3;

    public static final int    FOUR                              = 4;

    public static final int    FIVE                              = 5;

    public static final int    NINE                              = 9;

    public static final int    SIX                               = 6;

    public static final int    SEVEN                             = 7;

    public static final int    TEN                               = 10;

    public static final int    TWENTY                            = 20;

    public static final int    FOURTY                            = 40;

    public static final int    FIFTY                             = 50;

    public static final int    EIGHTY                            = 80;

    public static final int    NINTY                             = 90;

    public static final int    HUNDRED                           = 100;

    public static final int    THOUSAND                          = 1000;

    public static final String NO                                = "No";

    public static final String YES                               = "Yes";

    /** The cache key for the reference type data. */
    public static final String CACHE_KEY_REF_TYPE_DATA           = "CACHED_DOC_UPLOAD_REFERENCE_TYPE_DATA";

    public static final String PROCESS_CODE_HEADER               = "processCode";

    public static final String JWT_SID_HEADER                    = "jwtSessionId";

    public static final String BRAND_VALUE                       = "channelValue";

    public static final String CACHE_DOCUPLOAD_REFDATA_NAMESPACE = "cache.docupload.refdata.namespace";

    public static final String EMPTY_STRING                      = "";

    public static final int    JWT_VALID_COUNT                   = 5;

    public static final String JWT_SUCCESS_CODE                  = "00";

    public static final String UTF_EIGHT                         = "UTF-8";

    public static final char   SEPARATOR_CHAR                    = '.';

    public static final String SPLIT_CHAR                        = "\\.";

    public static final String PRINT_JWT_VALIDATION_EVENT        = "T10128";

    public static final String RETRIEVE_DOC_EVENT                = "T10130";

    public static final String PATH_PARAM_PROCESS_CD             = "processCD";

    public static final String EVIDENCETYPE_KEY                  = "processEvidenceDocumentList";

    public static final String FILEFORMAT_KEY                    = "processFileFormat";

    public static final String MAX_FILE_SIZE_KEY                 = "uploadSizeLimit";

    public static final String FILE_LIMIT_KEY                    = "docUploadFileLimit";

    public static final String CASE_REFERENCE_NO_HEADER          = "caseRefNumber";

    public static final String SLASH_CHAR                        = "/";

    public static final String BRAND_SECTION                     = "BRAND_NAME";

    public static final String JWT_BRAND_SECTION                 = "JWT_BRAND_NAME";

    public static final String BRAND_DISPLAY_VALUE               = "brandDisplayValue";

    public static final String SCHEDULER_CALL                    = "scheduler_call";

    public static final String PATH_PARAM_CASE_REF_NO            = "caseRefNo";

    public static final String PATH_PARAM_CASE_STATUS            = "status";

    public static final String PRINT_UPLOAD_DOCUMENT_EVENT       = "T10129";

    public static final String OPERATION_TYPE_SUBMIT             = "SUBMIT";

    public static final String SERVICE_PASSED                    = "Service passed";

    public static final String CASE_STATUS_OPEN                  = "OPEN_STATUS";

    public static final String REMOXO_CODE                       = "AOOIDV-ON-LTB";

    public static final String ROLL_NUMBER                       = "Mortgage_Roll_Number";

    public static final String CASE_STATUS_SECTION               = "DOC_UPLOAD_CASE_STATUS";

    public static final String PROCESS_CODE                      = "DOC_UPLOAD_PROCESS_CODE";

    public static final String CUSTOMER                          = "Customer";

    public static final String ONLINE                            = "Online";

    public static final String DATE_FORMAT                       = "yyyy-MM-dd";

    public static final String DATE_FORMAT_ZULU                  = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String Y_VALUE                           = "Y";

    public static final String SUBMIT_RESPONSE_KEY               = "status";

    public static final String ALLOWED_CHARCTERS                 = "^[a-zA-Z0-9]*$";

    public static final String ALLOWED_NUMBERS                   = "(?i)[0-9]*";

    public static final String ALLOWED_SPECIAL_SOME_CHARACTERS   = "^[a-zA-Z0-9-{}]*$";

    public static final String STAFF                             = "Staff";

    public static final String OK                                = "ok";

    public static final String SINGLETON_STRING                  = "SingletonString";

    public static final String CE_BRAND                          = "CE_sBrand";

    public static final String CE_PROCESS_ID                     = "CE_sProcessIdentifier";

    public static final String CE_CHANNEL                        = "CE_sChannel";

    public static final String CE_CASE_REF_NO                    = "CE_sCaseReference";

    public static final String CE_EXPIRY_DATE                    = "CE_dExpiryDate";

    public static final String CE_SRC_APP                        = "CE_sSourceApplication";

    public static final String CE_CONTENT_TYPE                   = "CE_sContentType";

    public static final String CE_DOC_CLASSIFICATION             = "CE_sDocumentClassification";

    public static final String CE_DCO_TYPE                       = "CE_sDocumentType";

    public static final String CE_PARTY_DETAILS                  = "CE_sPartyDetails";

    public static final String CE_EXT_REF                        = "CE_sExternalReference";

    public static final String CE_PROJECT_DATA                   = "CE_sProjectDataList";

    public static final String DOC_TITLE                         = "DocumentTitle";

    public static final String FUN_CODE                          = "ContentStream";

    public static final String PREVIEW_JSON_PART                 = "previewResponse";

    public static final String CONTENT_TYPE                      = "Content-Type";

    public static final String CONTENT_DISPOSITION               = "Content-Disposition";

    public static final String CONTENT_DISPOSITION_VALUE         = "attachment; name=\"";

    public static final String MTOM                              = "MTOM";

    public static final String HYPEN                             = "-";

    public static final String DU_SESSIONS                       = "DU_Sessions";

    public static final String DOCUMENT_CLASS_ID                 = "DOCUMENT_CLASS_ID";

    public static final String CREATE_DOC_JSON_PART              = "createDocumentationRequest";

    public static final String CREATE_DOC_FILE                   = "fileAttached";

    public static final String MULTIPART_RELATED                 = "multipart/related";

    public static final String CREATE_DOC_EVENT                  = "T10140";

    public static final String DOCUMENTATION_SERVICE             = "DocumentationManagerService";

    public static final int    KB                                = 1024;

    public static final String UPLOAD_STATUS_OPEN                = "OP";

    public static final String SCAN_STATUS                       = "ScanStatus";

    public static final String VALID_SCAN_CODE                   = "204";

    public static final String DEFAULT_SCAN_CODE                 = "500";

    public static final String SPACE_REMOVER                     = "\\s+";

    public static final String JWT_TOKEN_TYPE                    = "JWT";

    public static final String DOC_UPLOAD_FILTER                 = "DOC_UPLOAD_FILTER";

    public static final String JWT_VALID_TOKEN_COUNT             = "JWT_VALID_TOKEN_COUNT";

}
