#############################################################
##API Acceptance Tests for PCA-687 (Activate Product Features)
#############################################################
@api @regression @pca-687
Feature: Modify Activate Product Service for URCA

  Scenario Outline: Validate the response from Arrangement
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we send a request for record questionnarie service "<recordRequest>" "<recordEndpoint>"
    Then validate the response of record service "<reasonCode>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"

    Examples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore | activateEndpoint     | activateRequest | applicationStatus | reasonCode | recordRequest | recordEndpoint         |
      ########################################### HALIFAX NON DOWNSELL ########################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellAccept    | P_ULTIMATE     | Accept    | Accept   | env.activate.halifax | ActivateRequest |              1010 | 0          | qnValidRequest| test.recordque.halifax |
    # | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellProcessed | P_ULTIMATE     | Accept    | Refer    | env.activate.halifax | ActivateRequest |              1008 | 0          | qnValidRequest| test.recordque.halifax |
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellRefer     | P_ULTIMATE     | Refer     | Accept   | env.activate.halifax | ActivateRequest |              1007 | 0          | qnValidRequest| test.recordque.halifax |


  @Negative
  Scenario Outline: Validate the error for invalid activation request |URCA
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then verify error code is "<errorcode>"
    Then verify error msg is "<errromsg>"


    Examples:
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore | activateEndpoint     | activateRequest | applicationStatus |errorcode|errromsg|
      ########################################### HALIFAX URCA ###########################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX            | P_ULTIMATE       | env.productarrangement.halifax | HlxNonDownsellAccept  | P_ULTIMATE    | Accept    | Accept   | env.activate.halifax | NegActivateRequest |              1010 | 9200008     |'name' required to be set but it was ''|

      
      
      
      
      
      