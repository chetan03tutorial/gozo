@api @getUserInfo @API @REGRESSION @sprint-1 @pcaAuth @pca-108
Feature: userInfo API Validation

  Scenario Outline: get User Info for valid post request for authenticated Users for lloyds
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    And I Get the response from userInfo from API "<userInfoUrl>"
	And I Get the response from ocisId from API
    And we send a request for offer service "<deciEndPoint>" "<deciRequest>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see "msg" as "<msgValue>"
    And I should see the response details with "<productMnemonic>" and "<eligibilityValue>"    	 

    Examples:
      | deciEndPoint               	   		 | envHost            |user   				    | validateTokenHost | httpStatusCode | key      | value |userInfoUrl  |msgValue 									   |deciRequest                                   | productMnemonic |  eligibilityValue |
      | lloydspcaProductEligibilityEndpoint  | env.samlAssertion  |Adam        				| env.validateToken | 200            | validated| true  |env.userInfo |Successfully fetched the eligibility details.   |productEligibilityExistingCustomerEligible    | P_PREM          |  true             |
      | lloydspcaProductEligibilityEndpoint  | env.samlAssertion  |Adam        				| env.validateToken | 200            | validated| true  |env.userInfo |Successfully fetched the eligibility details.   |productEligibilityExistingCustomerEligible    | P_GOLD_CLB_INT  |  true             |
      | lloydspcaProductEligibilityEndpoint  | env.samlAssertion  |Adam        				| env.validateToken | 200            | validated| true  |env.userInfo |Successfully fetched the eligibility details.   |productEligibilityExistingCustomerEligible 	  | P_SLVR_CLB      |  true             |
      | lloydspcaProductEligibilityEndpoint  | env.samlAssertion  |Adam        				| env.validateToken | 200            | validated| true  |env.userInfo |Successfully fetched the eligibility details.   |productEligibilityExistingCustomerEligible    | P_NEW_BASIC     |  true             |
      | lloydspcaProductEligibilityEndpoint  | env.samlAssertion  |Adam        				| env.validateToken | 200            | validated| true  |env.userInfo |Successfully fetched the eligibility details.   |productEligibilityNonExistingCustomerEligible | P_UNDER19       |  false            |
      | lloydspcaProductEligibilityEndpoint  | env.samlAssertion  |Adam        				| env.validateToken | 200            | validated| true  |env.userInfo |Successfully fetched the eligibility details.   |productEligibilityNonExistingCustomerEligible | P_UNDER19       |  false            |


      
  Scenario Outline: get User Info for valid post request for un-authenticated Users for lloyds
    Given we send a request for offer service "<deciEndPoint>" "<deciRequest>"
    And I should see "msg" as "<msgValue>"
    And I should see the response pca details with "<productMnemonic>" and "<eligibilityValue>"       
  	And I should have the following suitable products "<suitableProductList>"
    Examples:    
      | deciEndPoint               	   		 |deciRequest                                   		   | productMnemonic  |  eligibilityValue |msgValue  									   |suitableProductList     |
      | lloydspcaProductEligibilityEndpoint  |productEligibilityNonExistingCustomerEligible_UNDER19    | P_UNDER19		  |  true 			  |Successfully fetched the eligibility details.   |under19suitableProducts |
      | lloydspcaProductEligibilityEndpoint  |productEligibilityNonExistingCustomerEligible_NONUNDER19 | P_UNDER19		  |  false 			  |Successfully fetched the eligibility details.   |under19suitableProducts |