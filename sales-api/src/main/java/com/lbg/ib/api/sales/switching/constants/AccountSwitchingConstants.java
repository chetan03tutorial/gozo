package com.lbg.ib.api.sales.switching.constants;

import com.lloydsbanking.xml.AccountProductCategoryType;
import com.lloydsbanking.xml.SwitchScenarioType;
import com.lloydsbanking.xml.SwitchTypeType;

public class AccountSwitchingConstants {
    public static final int    MAX_CHARACTER_ACCOUNT_NAME       = 18;
    public static final int    ACCOUNT_PARTY_5PAN_DIGITS_LENGHT = 5;
    public static final int    LOWER_DATE_LIMIT                 = 0;
    public static final int    UPPER_DATE_LIMIT                 = 2;
    public static final int    LOWER_YEAR_LIMIT                 = 3;
    public static final int    UPPER_YEAR_LIMIT                 = 5;
    public static final String CURRENT_ACCOUNT                  = "CURRENT_ACCOUNT";
    public static final String PRIMARY                          = "PRIM";
    public static final String JOINT                            = "JOIN";
    public static final String SPACE                            = " ";
    public static final String ACCOUNT_NAME_JOINER              = "&";

    public static final     String                     PCA_ONLINE              = "PCAOnline";
    public static final     String                     MODE_ONLINE             = "Online";
    public static final     String                     COUNTRY                 = "GB";
    public static final     SwitchScenarioType         IAS_RETAIL_CURRENT_FULL = SwitchScenarioType.value4;
    public static final     SwitchTypeType             FULL                    = SwitchTypeType.FULL;
    public static final     AccountProductCategoryType CURRENT                 = AccountProductCategoryType.Current;
    public static final     int                        POST_CODE_SIZE          = 16;
    public static final     int                        ADDRESS_LINE_SIZE       = 70;
}
