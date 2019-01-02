@RetrievePartyDetails @api @p1 @mocks
Feature: Retrieve Party Details
  Retrieve details of a party for a Party Id using RetrievePartyDetals API 
  
  Scenario Outline: Retrieve party details for an Ocis Id
    Given for a  branch colleague, I have an ocisId "<ocisId>" of a customer
	 When I make a call to the RetrievePartyDetails API at RetrievePartyDetails-endpoint "<RetrievePartyDetails-endpoint>"
	 Then I should see the response details with "<firstName>" and "<lastName>"
	  And I should see the response status as "<httpStatusCode>"
      
    Examples:
      | RetrievePartyDetails-endpoint     | httpStatusCode | ocisId     | firstName	|	lastName	|
      | env.retrievePartyDetails.lloyds   |     200        | 783285618   | test  		|	test		|
      | env.retrievePartyDetails.halifax  |     200        | 783285618   | test 		|	test		|
      | env.retrievePartyDetails.bos      |     200        | 783285618   | test			|	test		|

######################################################################################################################

Scenario Outline: Retrieve party details for an Ocis Id
    Given for a  branch colleague, I have an ocisId "<ocisId>" of a customer
	 When I make a call to the RetrievePartyDetails API at RetrievePartyDetails-endpoint "<RetrievePartyDetails-endpoint>"
	 Then I should see the response details with "<firstName>" and "<lastName>"
	  And I should see the response status as "<httpStatusCode>"
      
    Examples:
      | RetrievePartyDetails-endpoint     | httpStatusCode | ocisId      | firstName	|	lastName	|
      | env.retrievePartyDetails.lloyds   |     200        | 1736337307   | test  		|	test		|
      | env.retrievePartyDetails.halifax  |     200        | 1736337307   | test 		|	test		|
      | env.retrievePartyDetails.bos      |     200        | 1736337307   | test		|	test		|

######################################################################################################################

Scenario Outline: Retrieve party details for an Ocis Id
    Given for a  branch colleague, I have an ocisId "<ocisId>" of a customer
	 When I make a call to the RetrievePartyDetails API at RetrievePartyDetails-endpoint "<RetrievePartyDetails-endpoint>"
	 Then I should see the response details with "<firstName>" and "<lastName>"
	  And I should see the response status as "<httpStatusCode>"
      
    Examples:
      | RetrievePartyDetails-endpoint     | httpStatusCode | ocisId     	| firstName		|	lastName		|
      | env.retrievePartyDetails.lloyds   |     200        | 1914023063   	| Test  		|	Record 2		|
      | env.retrievePartyDetails.halifax  |     200        | 1914023063   	| Test 			|	Record 2		|
      | env.retrievePartyDetails.bos      |     200        | 1914023063	  	| Test			|	Record 2		|     
