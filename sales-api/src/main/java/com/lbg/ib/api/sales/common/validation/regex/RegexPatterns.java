package com.lbg.ib.api.sales.common.validation.regex;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class RegexPatterns {
    
    public static final String REQUIRED_ALPHA_NUMERIC_WITH_SPECIAL_CHARACTERS         = "^[\\w @&+\\-,./']+$";
    public static final String OPTIONAL_ALPHA_NUMERIC_WITH_SPECIAL_CHARACTERS         = "^[\\w @&+\\-,./']*$";
    public static final String REQUIRED_ALPHA_NUMERIC_WITH_SPECIAL_CHARACTERS_BRACKET = "^[\\w @&+\\-,./'()]+$";
    public static final String OPTIONAL_ALPHA_NUMERIC_WITH_SPECIAL_CHARACTERS_BRACKET = "^[\\w @&+\\-,./'()]*$";
    public static final String REQUIRED_ALPHA_NUMERIC                                 = "^[a-zA-Z0-9]+$";
    public static final String OPTIONAL_ALPHA_NUMERIC                                 = "^[a-zA-Z0-9]*$";
    public static final String REQUIRED_ALPHA_SPACE                                   = "^[a-zA-Z](\\w?\\s?\\-?\\'?)+$";
    public static final String REQUIRED_ALPHA                                         = "^[a-zA-Z]+$";
    public static final String REQUIRED_ALPHA_WITH_UNDERSCORE                         = "^[a-zA-Z_]+$";
    public static final String OPTIONAL_ALPHA                                         = "^[a-zA-Z]*$";
    public static final String EMAIL                                                  = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9]{2}(?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
    public static final String REQUIRED_4_DIGITS                                      = "^\\d{4}$";
    public static final String REQUIRED_3_DIGITS                                      = "^\\d{3}$";
    public static final String REQUIRED_2_DIGITS                                      = "^\\d{2}$";
    public static final String POSTCODE                                               = "^((GIR 0AA)|(((A[BL]|B[ABDHLNRSTX]?|C[ABFHMORTVW]|D[ADEGHLNTY]|E[HNX]?|F[KY]|G[LUY]?|H[ADGPRSUX]|I[GMPV]|JE|K[ATWY]|L[ADELNSU]?|M[EKL]?|N[EGNPRW]?|O[LX]|P[AEHLOR]|R[GHM]|S[AEGKLMNOPRSTY]?|T[ADFNQRSW]|UB|W[ADFNRSV]|YO|ZE)[1-9]?[0-9]|((E|N|NW|SE|SW|W)1|EC[1-4]|WC[12])[A-HJKMNPR-Y]|(SW|W)([2-9]|[1-9][0-9])|EC[1-9][0-9])? {0,1}[0-9][ABD-HJLNP-UW-Z]{2}))$";
    public static final String REQUIRED_NUMERIC                                       = "^[0-9]+$";
    public static final String OPTIONAL_NUMERIC                                       = "^[0-9]*$";
    public static final String PASSWORD                                               = REQUIRED_ALPHA_NUMERIC;
    public static final String BIRTHCITY                                              = "^(?!.*?tbc|TBC|not available|NOT AVAILABLE|not applicable|NOT APPLICABLE|[0-9]|,|\\?|\\.|([a-zA-Z])\\1{2,}|(n\\/a)|(N\\/A)).*";
    public static final String PAPERLESS_OR_PAPER                                     = "^Paperless|Paper$";
    private RegexPatterns() {
    }
}
