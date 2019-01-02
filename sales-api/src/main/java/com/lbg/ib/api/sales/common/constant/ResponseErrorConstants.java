package com.lbg.ib.api.sales.common.constant;

public interface ResponseErrorConstants {
    
    String PRODUCT_OFFER_SERVICE_ERROR_OFFER_DOES_NOT_EXIST = "9910003";
    String BAD_REQUEST_PRODUCT_NOT_SELECTED                 = "9210002";
    String BRANDING_NOT_FOUND                               = "7110002";
    String PRODUCT_OFFER_SERVICE_ERROR                      = "7010002";
    String BAD_REQUEST_FORMAT                               = "7310002";
    String BAD_POSTCODE_FORMAT                              = "9310002";
    String ADDRESS_NOT_FOUND                                = "9310003";
    String PRODUCT_NOT_FOUND                                = "9310005";
    String SERVICE_UNAVAILABLE                              = "9310002";
    String UNDEFINED_ERROR                                  = "8310002";
    String ARRANGEMENT_ID_NOT_FOUND                         = "9310007";
    String SERVICE_EXCEPTION                                = "9310006";
    String BACKEND_ERROR                                    = "9310007";
    String INVALID_FORMAT                                   = "9200008";
    String OVERDRAFT_BAPI_REQUEST_ERROR                     = "9910004";
    String PROMOTIONAL_CUSTOMER_INSTRUCTIONS_RESPONSE_ERROR = "9910005";
    String CLASSIFY_INVOLVED_PARTY_NOT_FOUND                = "9910006";
    String UNEXPECTED_VALUE_IN_CLASSIFY_INVOLVED_PARTY      = "9910007";
    String ERROR_FETCHING_SWITCHES                          = "9910008";
    String ERROR_SAVING_ARRANGEMENT                         = "9310008";
    String RETRIEVE_ARRANGEMENT_SERVICE_EXCEPTION           = "820001";
    String EMPTY_TOKEN                                      = "9900003";
    String USER_NOT_FOUND                                   = "9900004";
    String NO_PRODUCT_TO_ACTIVATE_SERVICE_EXCEPTION         = "820003";
    
    String CONTENT_NOT_FOUND                                = "9400001";
    
    String USER_NOT_AUTHORIZED                              = "9900002";
    String RETRIEVE_PENDING_ARRANGEMENT_SERVICE_EXCEPTION   = "999901";
    String INTERNAL_SERVICE_WRONG_BRAND                     = "7500001";
    String APPLICATION_NOT_FOUND                            = "7500002";
    String DETAILS_NOT_FOUND_IN_SESSION 					= "99000091";
    
    String EMAIL_FAILURE                                    = "9900009";
    String TEMPLATE_ID_NOT_FOUND                            = "9900008";
    String NO_ACTION_ITEMS                                  = "9900006";

    String MORE_THAN_TWO_PARTY_FOUND                        = "9310011";
    String JOINT_PARTY_ICOBS_FLAG_TRUE                      = "9310013";
    String NO_ELIGIBLE_PRODUCT_FOUND                        = "9310015";
    String ERROR_RESPONSE_FROM_Q250                         = "9310016";
    String INVALID_ACCOUNT_NUMBER_SORT_CODE                 = "9310017";
    String NOT_ABLE_TO_DETERMINE_PRIMARY_PARTY		        = "9310020";

    String ENCRYPTION_ERROR                                 = "9310014";
    String INTERNAL_SERVER_ERROR                            = "XXXXXXXXXX";
    String INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR          = "XCVBNM";
    String INVALID_PRODUCT_MNEMONIC                         = "9310018";
    String FAILED_TO_GET_OTHER_PARTY_DETAIL                 = "9310019";
    String PEGA_ERROR_INVOKING_RETRIEVE_PARTY               = "9600001";
    String PEGA_NO_PARTY_DETAILS_OCIS                       = "9600002";
    String PEGA_CREATE_PARTIES_MISMATCH                     = "9600003";
    String PEGA_PARTIES_NOT_LOCATE                          = "9600004";
    String PEGA_NO_PRIMARY_PARTY_FOUND                      = "9600005";
    String PEGA_INCORRECT_PARTY_SIZE                        = "9600006";
    String OLD_OWNER_NOT_FOUND                              = "9600007";
    String PEGA_NO_SECOND_PARTY_FOUND                       = "9600008";
    String INVALID_SWITCH_DATE                              = "9600009";
    String OCIS_TOO_MANY_PARTIES_FOUND                      = "9600010";
    String ERROR_CREATING_CREATE_CASE_REQUEST               = "9600011";
    String ERROR_GENERATING_DOCUMENT                        = "9600012";
    String NO_PARTIES_IN_SESSION                            = "9600013";
    String ERROR_ADDING_HEADERS                             = "9600014";
    String TERMINDATE_APPLICATION_EXCEPTION                 = "9900077";
    String EXCEPTION_IN_HEADERS = "9900078";
    String INVALID_OPERATIONS = "9900079";
    String INVALID_PRODUCT ="99000780";
    String MAX_EMAIL_RETRY_REACHES           			= "99000781";
}