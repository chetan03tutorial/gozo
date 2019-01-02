#############################################################
##API Acceptance Tests for PCA-12 (RecordArrangementQuestionnaire)
#############################################################
@API @REGRESSION @PCA-162
Feature: Youth - OfferService

  #################################################################################################
  #Scenario Outline: Using valid product
  #Given I have a "<envHost>" for the valid "<product>"
  #When I try to get response from the API
  #And I should see the response details with statuscode "<httpStatusCode>"
  #And I should see the response details with "<mnemonic>"
  #Examples:
  #| envHost              | product       | httpStatusCode | mnemonic   |
  #| env.retrieve.halifax | urcaccountHLX |            200 | P_ULTIMATE |
  Scenario Outline: Validate the response from Arrangement
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<product>"
    Then validate the response of retrieve service "<mnemonic>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then verify product mnemonic "<offeredProduct>"
    Then verify eidv score "<eidvScore>"
    Then verify asm score "<asmScore>"

    Examples: 
      | retrieveEndpoint     | offerEndpoint                  | product                 | httpStatus | mnemonic   | offeredProduct | offerRequest            | eidvScore | asmScore |
      ################################################ LLOYDS NON DOWNSELL ###########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthAccept    		 | REFER    | Accept   |

      ################################################ LLOYDS NON DOWNSELL ###########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthDecline    		 | REFER    | DECLINE   |

      ################################################ BOS  NORMAL ###########################################################################################
      | env.retrieve.bos     |env.productarrangement.bos     |under19accountBOS       |        200 | P_UNDER19  | P_UNDER19      | BosYouthAccept    		 | REFER    | Accept   |

      ################################################ BOS  DECLINE ###########################################################################################
      | env.retrieve.bos     |env.productarrangement.bos     |under19accountBOS   |        200 | P_UNDER19  | P_UNDER19      | BosYouthDecline    		 | REFER    | DECLINE   |
          
      ################################################ Halifax  Accept ###########################################################################################
      | env.retrieve.halifax |env.productarrangement.halifax     | expresscashHLX     |        200 | P_EXPRESSCASH  | P_EXPRESSCASH      | HalifaxYouthAccept    		 | REFER    | Accept   |

      ################################################ Halifax  DECLINE ###########################################################################################
      | env.retrieve.halifax |env.productarrangement.halifax     | expresscashHLX     |        200 | P_EXPRESSCASH | P_EXPRESSCASH      | HalifaxYouthDecline    		 | REFER    | DECLINE   |


###########################################################  NEGATIVE  ########################################################

 @NEGATIVE
 Scenario Outline: Validate the response from Arrangement
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<product>"
    Then validate the response of retrieve service "<mnemonic>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then verify error msg is "<message>"

    Examples: 
      | retrieveEndpoint     | offerEndpoint                  | product                 | httpStatus | mnemonic   | offeredProduct | offerRequest            | message									 |
      ################################################ LLOYDS NON DOWNSELL ###########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthMissingName   		 |'firstName' required to be set but it was ''|
      | env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthMissingEmail   		 |'email' required to be set but it was ''|
      | env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthWrongEmailFormat   	 |'dd@dd' is not matching with format in field 'email'|
      | env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthMissingDateOfBirth	 |'dob' required to be set but it was 'null'|
      | env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthMissingAddress  		 |Either 'buildingName' or 'buildingNumber' must be specified|
      | env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthWrongPostCode   		 |'E20 1BFSS' is not matching with format in field 'postcode'|
 
