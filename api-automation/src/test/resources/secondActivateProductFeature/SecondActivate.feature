#############################################################
##API Acceptance Tests for PCA-1546(Activate Product Features)
#############################################################
@api @regression @pca-1546-01

Feature: validate second activate response for adult/student/youth

  	Scenario Outline: Validate the document reference details for student
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"
    And we send a request for activate service "<activateEndpoint>" "<secondActivateRequest>"
    Then validate the response of activate service "<secondApplicationStatus>"
    
   
    Examples:     
      | retrieveEndpoint     | productRequest          | defaultProduct   | offerEndpoint                  | offerRequest              | offeredProduct | eidvScore | asmScore | activateEndpoint     | activateRequest | applicationStatus |  secondActivateRequest      | secondApplicationStatus |
      ########################################### HALIFAX DOWNSELL ######################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE       | env.productarrangement.halifax | HlxNonDownsellRefer       | P_ULTIMATE     | Refer     | Accept   | env.activate.halifax | ActivateRequest |              1007 |  DocActivateRequestStudent  | 1010					|
    
  
  	Scenario Outline: Validate single document reference details for adult
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"
    And we send a request for activate service "<activateEndpoint>" "<secondActivateRequest>"
    Then validate the response of activate service "<secondApplicationStatus>"
    
    Examples:     
      | retrieveEndpoint     | productRequest          | defaultProduct   | offerEndpoint                  | offerRequest              | offeredProduct | eidvScore | asmScore | activateEndpoint     | activateRequest | applicationStatus |  secondActivateRequest              | secondApplicationStatus |
      ########################################### HALIFAX DOWNSELL ######################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE       | env.productarrangement.halifax | HlxNonDownsellRefer       | P_ULTIMATE     | Refer     | Accept   | env.activate.halifax | ActivateRequest |              1007 |  DocActivateRequestAdult_singledoc  | 1010					|
        