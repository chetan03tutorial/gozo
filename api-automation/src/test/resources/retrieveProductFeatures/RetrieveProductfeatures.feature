#############################################################
##API Acceptance Tests for PCA-176 (Retrieve Product features)
##/product/features/{urlidentifier}
#############################################################
@API @REGRESSION @pca-176
Feature: RetrieveProductFeatures by passing url Identifier to /product/features/{urlidentifier}

  ##################################################1#######################################
  Scenario Outline: Using valid urlIdentifier
    Given product retrieve service is up and running "<retrieveEndpoint>" "<urlIdentifier>"
    Then verify product name "<productName>"
    Then verify product mnemonic "<productMnemonic>"
    Then verify service status "<httpStatusCode>"

    Examples: 
      | retrieveEndpoint     | urlIdentifier    | httpStatusCode | productName                     | productMnemonic |
      | env.retrieve.lloyds  | esaver           |            200 | eSavings                        | P_ESAVER        |
      | env.retrieve.halifax | isasavervariable |            200 | ISA Saver Variable              | P_ISA_VAR       |
      | env.retrieve.bos     | accesssaver      |            200 | Access Saver                    | P_EASY_SVR      |
      | env.retrieve.bos     | fixedratebond2ym |            200 | Fixed Rate Bond 2 Year          | P_TD_2Y_M       |
      | env.retrieve.bos     | fixedratebond2yy |            200 | Fixed Rate Bond 2 Year          | P_TD_2Y_Y       |
      | env.retrieve.halifax | urcaccountHLX    |            200 | Ultimate Reward Current Account | P_ULTIMATE      |

  ##################################################2#######################################
  Scenario Outline: Retrieve product features by passing valid product identifier
    Given product retrieve service is up and running "<retrieveEndpoint>" "<productIdentifier>"
    Then verify product name "<productName>"
    Then verify product mnemonic "<productMnemonic>"
    Then verify service status "<httpStatusCode>"

    Examples: 
      | retrieveEndpoint     | productIdentifier | httpStatusCode | productName                     | productMnemonic |
      | env.retrieve.halifax | 3002/idType       |            200 | Ultimate Reward Current Account | P_ULTIMATE      |
      | env.retrieve.halifax | 3001/idType       |            200 | Reward Current Account          | P_REWARD        |

  ##################################################3#######################################
  Scenario Outline: Invalid Identifier
    Given product retrieve service is up and running "<retrieveEndpoint>" "<urlIdentifier>"
    Then verify response code "<responseCode>"
    And verify service status "<httpStatusCode>"

    Examples: 
      | retrieveEndpoint     | urlIdentifier | responseCode | httpStatusCode |
      | env.retrieve.lloyds  | invalid       |      9310005 |            200 |
      | env.retrieve.halifax | invalid       |      9310005 |            200 |
      | env.retrieve.bos     | invalid       |      9310005 |            200 |
      | env.retrieve.lloyds  | esaver/esaver |      9310002 |            200 |
