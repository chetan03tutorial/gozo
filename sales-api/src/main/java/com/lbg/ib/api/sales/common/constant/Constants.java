/**
 * 
 */
package com.lbg.ib.api.sales.common.constant;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ssama1
 */
public interface Constants {
    
    /**
     * for TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       TERM_DEPOSITE_PARENT_MNEMONIC                   = "G_TD";
    
    /**
     * for ONLINE_FIXED_RATE_TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       ONLINE_FIXED_RATE_TERM_DEPOSITE_PARENT_MNEMONIC = "G_ONL_FRTD";
    
    /**
     * for TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       FIXED_RATE_TERM_DEPOSITE_PARENT_MNEMONIC        = "G_FR_TD";
    
    /**
     * for TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       FIXED_BOND_TERM_DEPOSITE_PARENT_MNEMONIC        = "G_FB_TD";
    
    /**
     * for TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       TRACKER_BOND_TERM_DEPOSITE_PARENT_MNEMONIC      = "G_TB_TD";
    
    /**
     * for TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       FLEXIBLE_BOND_TERM_DEPOSITE_PARENT_MNEMONIC     = "G_FX_TD";
    
    /**
     * for TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       ACCESS_BOND_TERM_DEPOSITE_PARENT_MNEMONIC       = "G_AB_TD";
    
    /**
     * for TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       RESET_BOND_TERM_DEPOSITE_PARENT_MNEMONIC        = "G_RB_TD";
    
    /**
     * for TERM_DEPOSITE_PARENT_MNEMONIC.
     */
    public static final String       DEPOSIT_BOND_TERM_DEPOSITE_PARENT_MNEMONIC      = "G_DB_TD";
    
    public static final String       HALF_YEARLY_INTEREST_PAID_FREQUENCY             = "Half Yearly Interest Paid Frequency";
    public static final String       QUARTERLY_INTEREST_PAID_FREQUENCY               = "Quarterly Interest Paid Frequency";
    public static final String       MONTHLY_INTEREST_PAID_FREQUENCY                 = "Monthly Interest Paid Frequency";
    public static final String       ANNUAL_INTEREST_PAID_FREQUENCY                  = "Annual Interest Paid Frequency";
    
    public static final String       INVOLVED_PARTY_MANAGEMENT_URL                   = "url_involvedparty_mangement";
    public static final String       DLV_READ_ONLY                                   = "DLV";
    public static final String       TEST01GLOBAL                                    = "TEST01GLOBAL";
    public static final String       TEST_DOMAIN                                     = "TEST";
    public static final String       MCA_DOMAIN                                      = "MCA_DOMAIN";
    public static final String       CHANNEL_IDENTIFIER                              = "CHANNEL_IDENTIFIER";
    public static final String       BRANCH_TELLER                                   = "BTR";
    public static final String       BRANCH_MANAGER                                  = "BMR";
    public static final String       ROLE_TELLER                                     = "1";
    public static final String       ROLE_MANAGER                                    = "4";
    public static final String       ROLE_SUPERVISOR                                 = "2";
    public static final String       COLLEAGUE_ID                                    = "COLLEAGUE_ID";
    public static final String       MSG_21                                          = "MSG_21";
    public static final String       MSG_27                                          = "MSG_27";
    public static final String       MSG_26                                          = "MSG_26";
    public static final String       MSG_80                                          = "MSG_80";
    public static final String       MSG_14                                          = "MSG_14";
    public static final String       MSG_87                                          = "MSG_87";
    public static final String       MSG_56                                          = "MSG_56";
    public static final String       PSTCD                                           = "postcode";
    
    public static final String       POSTAL_ADDRESS_COMPONENT                        = "postalAddressComponent";
    public static final String       POSTAL_ADDRESS                                  = "postalAddress";
    public static final String       POSTAL_ADDRESS_UNSTRUCTURED                     = "unstructuredAddress";
    public static final String       PERSONAL_DETAILS                                = "personaldetails";
    public static final String       CHILD_APPLICATION                               = "childApplication";
    public static final String       CONTACT_NUMBER                                  = "contactNumber";
    public static final String       TIN_DETAILS                                     = "tinDetails";
    public static final List<String> PRODUCT_FREQUENCY_LIST                          = Arrays.asList("MIPF", "QIPF",
            "HIPF", "AIPF");
    
    public static final String       ACTIVE_X_FLAG                                   = "ActiveXFlag";
    
    public static final String       APPLICATION_PROPERTIES                          = "ApplicationProperties";
    
    public static final String       ACTIVEX_PROPERTIES                              = "ActiveXFlagConfigProperties";
    
    public static final String       ACQU_ACTIVE_X_FLAG                              = "ActiveXACQUFlag";
    
    public static final String       ACQU_ACTIVEX_PROPERTIES                         = "ActiveXACQUFlagConfigProperties";
    
    public static final String       COLLEGUE_ID                                     = "ColleagueId";

    public static final String       CURRENT_DATE                                     = "currentDate";

    public static final AtomicInteger      MAX_EMAIL_RETRY_LIMIT                            = new AtomicInteger(4);
    
    public interface OfferConstants {
        String FLAT         = "Flat";
        String STRING_SPACE = " ";
        String EMPTY_STRING = "";
    }
    
    public interface ActivateConstants {
        String CUSTOMER_BRANCH          = "1002";
        String CUSTOMER                 = "1001";
        String INTERNAL_USER_IDENTIFIER = "10.245.224.125";
        String REFER_APP_STATUS         = "REFER_APP_STATUS";
    }
    
    public interface BranchContextConstants {
        String COLLEGUE_ID            = "ColleagueId";
        String ACCREDITION_FLAG       = "accreditionFlag";
        String COLLEAGUE_DOMAIN       = "colleagueDomain";
        String APPLICATION_PROPERTIES = "ApplicationProperties";
        String CURRENT_ACCOUNTS       = "CURRENT_ACCOUNTS";
    }
    
    public interface CountryConstants {
        String[] DOMINICA_COUNTRY_LIST = { "DOM", "DMA", "DMR" };
        String   FRENCH_MNEMONIC       = "FRA";
        String   CRS                   = "CRS";
    }
    
    public interface BankWizardConstants {
        String BRANCH_NOT_WITH_IN_PARENT_COUUNTRY = "2";
        String CONDITIONS                         = "CONDITIONS";
        String ERROR                              = "ERROR";
        String FATAL_ERRORS                       = "FATAL_ERRORS";
        String FOREIGN_CURRENCY_ACCOUNT           = "64";
        String INFORMATION                        = "INFORMATION";
        String UNSUPPORTED_DD_ON_BRANCH           = "6";
        String UNSUPPORTED_DD_ON_ACCOUNT          = "5";
        String WARNINGS                           = "WARNINGS";
        String BRANCH                             = "BRANCH";
        String BRANCH_DATA                        = "BRANCH_DATA";
        List<String> BANK_NOT_IN_CASS             = Arrays.asList("79", "80");
        String BANK_NAME_CODE = "1006";
    }
    
    public interface CommunicationConstants {

        String BUSSINESS_ERROR = "813003";
        String BUSSINESS_ERROR_MESSAGE_813003 = "Send Communicatiom WPS call is failed";
        String BRAND = "BRAND_NAME_MAPPING";
        String BRAND_CODE = "BRAND_CODE_MAPPING";
        String EMAIL_COMMUNICATION_TYPE = "Email";
        String ATTACHMENT="AttachmentPDF";
        String EMAIL_SUCCESSFUL = "The mail has been sent successfully";
        String PRODUCT_MNEMONIC_SME = "P_BSAVINGS";
        String PRODUCT_MNEMONIC_RBB = "P_BS_RBB";
        String BRAND_LLOYDS = "LLOYDS";
        String CHANNEL_STL = "STL";
        String BRAND_BOS = "BOS";
        String CHANNEL_STS = "STS";
    }

    public interface SearchCaseDelegate {
        String REQUEST_HEADER = "x-lbg-brand";
        String PROCESS_CODE = "processCode";
        String CASE_REFERENCE_NO = "caseReferenceNo";
    }

    public interface RecordContant {
        String SUCCESS_MSG = "Recording of documents was successfull";
        String FAIL_MSG = "Record of documents was unsuccessfull";
        String CUSTOMER_DOCUMENT_TYPE_FIELD = ",CUSTOMER_DOCUMENT_TYPE:";
    }

    public interface RetrieveDocumentErrorConstant {
        List<String> RETRIEVE_ERROR_CODES = Arrays.asList(new String[] { "163008", "169104" });
    }

    public interface C078Constant {
        String KYC_DATE_FORMAT = "ddMMyyyy";
        String APPLICATION_SOURCE_CD_BRANCH = "002";
        String APPLICATION_SOURCE_CD_DIGITAL = "004";
        String CREDIT_SCORE_SRC_SYSTEM_QWELL = "012";
        String CREDIT_SCORE_SRC_SYSTEM_CASS = "025";
        SimpleDateFormat formatter = new SimpleDateFormat(KYC_DATE_FORMAT);
        int MAX_REPEAT_GROUP_QTY = 1;
        String CS_ORGANISATION_CD = "001";
    }

    public interface PLDConstants {
        String ICOBS = "ICOBS";
        String LIFE_STYLE_BENEFITS = "Lifestyle Benefits";
        String MNEMONIC_SYSTEM_CODE = "00010";
        String OVERDRAFT_FACITLITY_CODE = "0102";
        int NUMBER_OF_RETRIES = 2;
        String REFER = "REFER";
        String ACCEPT = "ACCEPT";
        String CBS_SYSTEM_CODE = "00004";
        String OFFERED_OVERDRAFT_AMOUNT = "OFFERED_OVERDRAFT_AMOUNT";
        String APPLICATION_APPROVED_STATUS = "1002";
        String APPLICATION_DECLINED_STATUS = "1004";
    }

    public interface B274ServiceConstants{
        String        INTEREST_RATES              = "0000000";
    }

    public interface ModifyProductArrangementConstants{
        static final String SERVICE_NAME   = "ModifyProductArrangement";
        static final String SERVICE_ACTION = "ModifyProductArrangement";

    }
}
