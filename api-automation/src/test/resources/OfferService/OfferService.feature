#############################################################
##API Acceptance Tests for PCA-12 (RecordArrangementQuestionnaire)
#############################################################
@API @REGRESSION @PCA-100
Feature: OfferService

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
      ################################################ HALIFAX NON DOWNSELL ###########################################################################################
      | env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellAccept    | Accept    | Accept   |
      | env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellProcessed | Accept    | Refer    |
      | env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellRefer     | Refer     | Accept   |
      ################################################ HALIFAX DOWNSELL ###########################################################################################
      | env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellAccept       | Accept    | Accept   |
      #| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellProcessed    | Accept    | Refer    |
      #| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellRefer        | Refer     | Accept   |
      ################################################ LLOYDS NON DOWNSELL ###########################################################################################
      | env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellAccept    | Accept    | Accept   |
      | env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellProcessed | Accept    | Refer    |
      | env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellRefer     | Refer     | Accept   |
      ################################################ LLOYDS DOWNSELL ###########################################################################################
      | env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellAccept       | Accept    | Accept   |
      #| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellProcessed    | Accept    | Refer    |
      #| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellRefer        | Refer     | Accept   |
      ################################################ BOS NON DOWNSELL ###########################################################################################
      | env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellAccept    | Accept    | Accept   |
      | env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellProcessed | Accept    | Refer    |
      | env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellRefer     | Refer     | Accept   |
      ################################################ BOS DOWNSELL ###########################################################################################
      #| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS |        200 | P_CLASSIC   |P_CLASSIC   | BosDownsellAccept       | Accept    | Accept   |
      #| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS |        200 |P_CLASSIC   | P_CLASSIC   | BosDownsellProcessed    | Accept    | Refer    |
      #| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS |        200 |P_CLASSIC   | P_CLASSIC   | BosDownsellRefer        | Refer     | Accept   |
      
      
      
    
    #####################################################--Negative scenarios--######################################################################################
    @Negative
    Scenario Outline: Validate the response error for missing fields
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then verify error code is "<code>" 
    Then verify error msg is "<errormsg>"
    
    Examples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest                         | code   |errormsg                                                          |
      ########################################### HALIFAX NON DOWNSELL ########################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellAcceptNoEmail            |9200008 |'email' required to be set but it was 'null'                      |
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAcceptWrongEmailFormat   |9200008 |'bholenath@universal' is not matching with format in field 'email'|
      | env.retrieve.lloyds | classicaccountLTB        | P_CLASSIC     | env.productarrangement.lloyds   | LydNonDownsellAgeError                 |9200008 |'dob' required to be set but it was 'null'|
      | env.retrieve.lloyds | classicaccountLTB        | P_CLASSIC     | env.productarrangement.lloyds   | LydDOBLessthan3yrs                     |9200008 |'Customer's age is null or  Customer cannot be younger than 18 years.|
      | env.retrieve.lloyds | classicaccountLTB        | P_CLASSIC     | env.productarrangement.lloyds   | LydNonDownsellAcceptInvalidPostalCode  |9200008 |'E20 1BFRT' is not matching with format in field 'postcode'      |
      
      
         