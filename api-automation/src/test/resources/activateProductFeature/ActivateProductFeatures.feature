#############################################################
##API Acceptance Tests for PCA-687 (Activate Product Features)
#############################################################
@api @regression @pca-687
Feature: Modify Activate Product Service

  Scenario Outline: Validate the response from Arrangement
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"

    Examples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore | activateEndpoint     | activateRequest | applicationStatus |
      ########################################### HALIFAX DOWNSELL ######################################################################################################################################################################
      | env.retrieve.halifax | rewardcurrentaccountHLX | P_REWARD       | env.productarrangement.halifax | HlxDownsellAccept       | P_NEW_BASIC    | Accept    | Accept   | env.activate.halifax | ActivateRequest |              1010 |
      #| env.retrieve.halifax | rewardcurrentaccountHLX | P_REWARD       | env.productarrangement.halifax | HlxDownsellProcessed    | P_NEW_BASIC    | Accept    | Refer    | env.activate.halifax | ActivateRequest |              1008 |
      #| env.retrieve.halifax | rewardcurrentaccountHLX | P_REWARD       | env.productarrangement.halifax | HlxDownsellRefer        | P_NEW_BASIC    | Refer     | Accept   | env.activate.halifax | ActivateRequest |              1007 |


      ############################################### LLOYDS NON DOWNSELL ################################################################################################################################################################
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept    | P_CLASSIC      | Accept    | Accept   | env.activate.lloyds  | ActivateRequest |              1010 |
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellProcessed | P_CLASSIC      | Accept    | Refer    | env.activate.lloyds  | ActivateRequest |              1008 |
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellRefer     | P_CLASSIC      | Refer     | Accept   | env.activate.lloyds  | ActivateRequest |              1007 |


      ########################################### LLOYDS DOWNSELL #########################################################################################################################################################################
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydDownsellAccept       | P_NEW_BASIC    | Accept    | Accept   | env.activate.lloyds  | ActivateRequest |              1010 |
      #| env.retrieve.lloyds | classicaccountLTB | P_CLASSIC       | env.productarrangement.lloyds | LydDownsellProcessed    | P_NEW_BASIC    | Accept    | Refer    | env.activate.lloyds | ActivateRequest |              1008 |
      #| env.retrieve.lloyds | classicaccountLTB | P_CLASSIC       | env.productarrangement.lloyds | LydDownsellRefer        | P_NEW_BASIC    | Refer     | Accept   | env.activate.lloyds | ActivateRequest |              1007 |



      ########################################### BOS NON DOWNSELL #########################################################################################################################################################################
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellAccept    | P_CLASSIC      | Accept    | Accept   | env.activate.bos     | ActivateRequest |              1010 |
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellProcessed | P_CLASSIC      | Accept    | Refer    | env.activate.bos     | ActivateRequest |              1008 |
      #| env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellRefer     | P_CLASSIC      | Refer     | Accept   | env.activate.bos     | ActivateRequest |              1007 |


      ########################################### BOS DOWNSELL #########################################################################################################################################################################
      #| env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosDownsellAccept       | P_NEW_BASIC    | Accept    | Accept   | env.activate.bos     | ActivateRequest |              1010 |
      #| env.retrieve.bos | classicaccountBOS | P_CLASSIC       | env.productarrangement.bos | BosDownsellProcessed    | P_NEW_BASIC    | Accept    | Refer    | env.activate.bos | ActivateRequest |              1008 |
      #| env.retrieve.bos | classicaccountBOS | P_CLASSIC       | env.productarrangement.bos | BosDownsellRefer        | P_NEW_BASIC    | Refer     | Accept   | env.activate.bos | ActivateRequest |              1007 |
      #| env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosDownsellAccept       | P_NEW_BASIC    | Accept    | Accept   | env.activate.bos     | ActivateRequest |              1010 |


   @Negative
   Scenario Outline: Validate the error for invalid activation request
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then verify error code is "<errorcode>"
    Then verify error msg is "<errromsg>"



    Examples:
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore | activateEndpoint     | activateRequest | applicationStatus |errorcode|errromsg|
      ########################################### HALIFAX DOWNSELL ###########################################################################################################################################################################################################
      | env.retrieve.halifax | rewardcurrentaccountHLX | P_REWARD       | env.productarrangement.halifax | HlxDownsellAccept       | P_NEW_BASIC    | Accept    | Accept   | env.activate.halifax | NegActivateRequest |              1010 | 9200008     |'name' required to be set but it was ''|
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellAccept    | P_CLASSIC      | Accept    | Accept   | env.activate.bos     | NegActivateRequest |              1010 | 9200008     | 'name' required to be set but it was ''                                  |
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept    | P_CLASSIC      | Accept    | Accept   | env.activate.lloyds  | NegActivateRequest |              1010 | 9200008     | 'name' required to be set but it was ''                                   |


      
      
      
      
      
      
      