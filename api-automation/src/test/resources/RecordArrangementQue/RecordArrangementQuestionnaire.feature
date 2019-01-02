#############################################################
##API Acceptance Tests for PCA-12 (RecordArrangementQuestionnaire)
#############################################################
@API @REGRESSION @PCA12
Feature: RecordArrangementQuestionnaire by passing product Identifier

  
  
#################################################################################################

	Scenario Outline: Service Available

    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we have a record questionnarie service running on "<envHost>" for a brand
	When we try to get response from the API "<JsonFileName>"
    And we should see the response code "<reasonCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
	xamples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore |reasonCode|httpStatusCode |JsonFileName   |envHost| 
      #################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellAccept    | P_ULTIMATE     | Accept    | Accept   |0		   |200			   |qnValidRequest|test.recordque.halifax|
      
      
    
   
    Scenario Outline: Bad request due to Invalid parameter name

    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
	And we have a record questionnarie service running on "<envHost>" for a brand
	When we try to get response from the API "<JsonFileName>"
    And we should see the error code "<errorCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
    | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore |errorCode	|httpStatusCode	   |JsonFileName  			 |envHost|
    | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellAccept    | P_ULTIMATE     | Accept    | Accept   |9200008	|400			   |qnInvalidParamName		 | test.recordque.halifax|


#################################################################################################

  								######### PCA-180 ###############

#################################################################################################



    Scenario Outline: Validate atleast one product feature is present

	Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
 	And we have a record questionnarie service running on "<envHost>" for a brand
	When we try to get response from the API "<JsonFileName>"
    And we should see the response code "<reasonCode>"
    And we should see http statuscode as "<httpStatusCode>"



	Examples:
    | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore |reasonCode	|httpStatusCode	   |JsonFileName  			         	 |envHost			     |
    | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellAccept    | P_ULTIMATE     | Accept    | Accept   |0			|200			   |qnOneProductFeature	 | test.recordque.halifax|

#################################################################################################

 Scenario Outline: Validate standalone insurance product feature is present

 
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
	And we have a record questionnarie service running on "<envHost>" for a brand
	When we try to get response from the API "<JsonFileName>"
    And we should see the response code "<reasonCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
    | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore |reasonCode|httpStatusCode |JsonFileName  |envHost|
    | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellAccept    | P_ULTIMATE     | Accept    | Accept   |0		   |200			   |qnValidRequest|test.recordque.halifax|


########################################### Neg scenarios ######################################################

    Scenario Outline: Validate the error code if no product feature is present
    
	Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
 	And we have a record questionnarie service running on "<envHost>" for a brand
	When we try to get response from the API "<JsonFileName>"
    And we should see the error code "<errorCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
    | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore |errorCode	|httpStatusCode	   |JsonFileName  			         |envHost|
    | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellAccept    | P_ULTIMATE     | Accept    | Accept   |9200008	|400			   |qnMissingProductFeature	 | test.recordque.halifax|



    Scenario Outline: Validate standalone insurance product feature is not present
 
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
	And we have a record questionnarie service running on "<envHost>" for a brand
	When we try to get response from the API "<JsonFileName>"
    And we should see the response code "<reasonCode>"
    And we should see http statuscode as "<httpStatusCode>"
    
    	 
	Examples: 
    | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest            | offeredProduct | eidvScore | asmScore |reasonCode|httpStatusCode |JsonFileName  |envHost|
    | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | HlxNonDownsellAccept    | P_ULTIMATE     | Accept    | Accept   |0		   |200			   |qnValidRequest|test.recordque.halifax|
    
    




   