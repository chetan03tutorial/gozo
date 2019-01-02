@PCA-1585 @api @p1 @mocks

Feature: Fetch the customer details for an arrangement in refer (manual ID & V) using RetrieveArrangement API 

#############################################
#####1
#############################################

  Scenario Outline: Search Customer Details of a pending customer using valid arrangement ID
    Given for a  branch colleague, I have an arrangementId"<arrangementId>" of a customer
	 When I make a call to the retrieveProductArrangement API at retrieveArrangement-endpoint "<retrieveArrangement-endpoint>"
	Then I should see the repsonse status as "<httpStatusCode>"
      And I should see customer details response as "<retrieveArrangement-validResponse>"

    Examples:
     | brand   | product       | retrieveArrangement-endpoint       	   | httpStatusCode | arrangementId | retrieveArrangement-validResponse  |
     | lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 210771    	 | lastName  						 |
     | lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 210771    	 | firstName  						 |
     | lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 210771    	 | nationality						 |
     | lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 210771    	 | dob								 |
     | lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 210771    	 | gender							 |
     | lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 210771    	 | postalAddComp					 |
     | lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 210771    	 | associatedProduct				 |
     | lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 210771    	 | customerDocuments				 |
     | bos     |               | env.retrievePendingProductLookUp.bos      |     200        | 210772     	 | lastName  						 |
     | halifax |               | env.retrievePendingProductLookUp.halifax  |     200        | 210764    	 | lastName  						 |
     

#############################################
#####4
#############################################

 Scenario Outline: Test Error Codes for RetrieveArrangement API
    Given for a  branch colleague, I have an arrangementId"<arrangementId>" of a customer
    When I make a call to the retrieveProductArrangement API at retrieveArrangement-endpoint "<retrieveArrangement-endpoint>"
    Then I should see the repsonse status as "<httpStatusCode>"
    And I should see response with response code "<responseCode>"
    And I should see message "<message>"

    Examples: 
      | retrieveArrangement-endpoint	  		 | arrangementId | responseCode | httpStatusCode | message                                    |	brand   |
      | env.retrievePendingProductLookUp.lloyds  | abcde         | 9200008      | 200            | Arrangement ID should be a numeric value   |	lloyds  |
      | env.retrievePendingProductLookUp.lloyds  |  $$$$$$       | 9200008      | 200            | Arrangement ID should be a numeric value   |	lloyds  |
      | env.retrievePendingProductLookUp.lloyds  | 999999        | 7500002      | 200            | Application not found                      |	lloyds  |
      | env.retrievePendingProductLookUp.lloyds  | 210764        | 7500001      | 200            | Application is for a different brand       |	lloyds  |
