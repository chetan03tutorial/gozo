@api @samlToken @API @REGRESSION @sprint-1 @pcaAuth @pca-108
Feature: getToken API Validation

  Scenario Outline: Using valid request
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken API for an existing user with userId "<userId>"
    And I should see the response details with SAML token
    And I should see http statuscode as "<httpStatusCode>"
    And I should see message "<message>"

    Examples: 
      | envHost           | httpStatusCode | message        | userId |
      | env.samlAssertion | 200            | tokenID | Adam    |

      
Scenario Outline: Get Token api validation with invalid requests
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken API for an existing user with userId "<userId>"
    And I should see http statuscode as "<httpStatusCode>"
    And I should see the response details with error "<responseCode>"

    Examples:
       | envHost    	   | urlIdentifier | responseCode | httpStatusCode | userId |
       | env.samlAssertion | invalid       | 9200001      | 200            | 123	|
