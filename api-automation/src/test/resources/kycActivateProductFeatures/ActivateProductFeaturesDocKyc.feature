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
      | retrieveEndpoint     | productRequest           | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore | activateEndpoint     | activateRequest 			| applicationStatus |
      ########################################### HALIFAX DOWNSELL ######################################################################################################################################################################
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellRefer     | P_CLASSIC      | Refer     | Accept   | env.activate.bos     | ActivateRequest             |              1007 |
