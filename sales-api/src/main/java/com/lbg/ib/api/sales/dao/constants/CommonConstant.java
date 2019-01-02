/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.constants;

public interface CommonConstant {

    /**
     * KEY_USER_CONTEXT -.
     */
    String KEY_USER_CONTEXT = "UserContext";

    /**
     * System Parameter - <tt>String constant</tt>.
     */
    String SYSTEM_PARAMETER_PROP = "com.lloydstsb.gx.GalaxySystemParameter";

    /**
     * Party Id - .
     */
    String PARTY_ID = "INIT_PARTY_ID"; //$NON-NLS-1$

    /**
     * OCIS_ID -.
     */
    String OCISID_ID = "INIT_OCISID_ID";

    /**
     * CHANNEL SECONDARY MODE -.
     */
    String CHANSEC_MODE = "INIT_CHANSEC_MODE";

    /**
     * LANGUAGE - .
     */
    String LANGUAGE = "INIT_LANGUAGE";

    /**
     * INBOX_ID -.
     */
    String INBOXID_CLIENT = "INIT_INBOXID_CLIENT"; //$NON-NLS-1$

    /**
     * SERVICE_SRC_HOST.
     */
    String SERVICE_SRC_HOST = "SERVICE_SRC_HOST";

    String SESSION_NAMESPACE = "com.lbg.ib.api.sales.";

    String SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS = "emailDetails";

    String SESSION_KEY_FOR_PRODUCT_DETAILS = "product";

    String SESSION_KEY_FOR_ARRANGED = "arranged";

    String SESSION_KEY_FOR_OFFER_ARRANGED = "offerArranged";

    String DEPENDENT_APPLICANT = "dependent";

    String SELF_APPLICANT = "self";

    String PARENT_APPLICANT = "parent";

    String SESSION_KEY_FOR_USER_INFO = "userInfo";

    String SESSION_KEY_FOR_BRANCH_CONTEXT = "branchContext";

    String SESSION_KEY_FOR_CUSTOMER_DETAILS = "customerDetails";

    String SESSION_KEY_FOR_RELATED_CUSTOMER_DETAILS = "relatedCustomerDetails";

    String SESSION_KEY_FOR_SWITCHING = "switchingDetails";

    String SESSION_KEY_FOR_ADDPARTY = "addParty";

    String SESSION_KEY_PAB_SESSION_INFO = "packagedSessionInfo";

    String SESSION_KEY_ALL_PARTY_DETAILS_SESSION_INFO = "allPartyDetailsSessionInfo";

    String SESSION_KEY_FOR_SELECTED_ACCOUNT = "accountToConvert";

    String SESSION_KEY_AVAILABLE_UPGRADE_OPTIONS_SESSION_INFO = "availableUpgradeOptions";

    String END = "END";

    String EPORTAL_APPLICATION_ID = "AL05175";
    
    String TRIAD_SESSION_INFO = "triadSessionInfo";

    String MAX_OVERDRAFT_LIMIT = "maxOverDraftLimit";
    
    final String USER_MAP ="userMap";

    String SESSION_KEY_PRIMARY_PARTY_OCIS_ID_MAP = "primaryPartyOcisIDMap";

    String SESSION_KEY_EMAIL_RETRY_COUNT = "emailRetryCount";
    
    String CITY_NAME="cityName";
    
    String ENVIRONMENT_NAME = "environmentName";

    String CONTENT_SERVER_CONTEXT = "IIS_CMS_SERVER_CONTEXT";

    String CONTENT_SERVER_CONTEXT_URI = "content.context.uri";

    String SESSION_KEY_DEMANDED_OD = "demandedOD";

    String APPLICATION_SUCESS_STATUS = "1010";

    String EMPTY_STRING = " ";
    
    String DEMANDED_OVERDRAFT = "demandedOverDraft";

}
