@pca-39 @REGRESSION @pcaAuth @sprint-2
Feature: validate product eligibility mock service

  Scenario Outline: Validate product eligibility mock service with success response
    Given I have a service running on "<envHost>" for a brand
    And urlidentifier as "<urlIdentifier>"
    When I try to get response from the API
    And I should see http statuscode as "<httpStatusCode>"
    And I should see asmcode as "<asmCode>"
    And I should see user first name as "<firstName>"

    Examples: 
      | envHost                      | urlIdentifier                  | httpStatusCode | asmCode    | firstName |
      | env.productEligibile.lloyds  | /product/party/segment/Michael | 200            | 701        | Michael   |
      | env.productEligibile.lloyds  | /product/party/segment/Adam    | 200            | 703        | Adam      |
      | env.productEligibile.lloyds  | /product/party/segment/Naom    | 200            | 705        | Naom      |
      
      
  Scenario Outline: Validate product eligibility mock service with success response for non-eligible customer
    Given I have a service running on "<envHost>" for a brand
    And urlidentifier as "<urlIdentifier>"
    When I try to get response from the API
    And I should see http statuscode as "<httpStatusCode>"
    And I should see asmcode as "<asmCode>"
    And I should see user error msg as "<Error>"

    Examples: 
      | envHost                      | urlIdentifier                  | httpStatusCode | asmCode    | firstName| Error |
      | env.productEligibile.lloyds  | /product/party/segment/Amit    | 200            | -1         | Amit     |You are under 18 and are thus rejected |
      | env.productEligibile.lloyds  | /product/party/segment/David   | 200            | -1         | David    |You have more that 10 PCA accounts |
      | env.productEligibile.lloyds  | /product/party/segment/Goliath | 200            | -1         | Goliath  |You have more that 10 Classic accounts |
      | env.productEligibile.lloyds  | /product/party/segment/Richter | 200            | -1         | Richter  |You belong to a high risk nationality |
      | env.productEligibile.lloyds  | /product/party/segment/Kane    | 200            | -1         | Richter  |You have been rejected by CBS |
      | env.productEligibile.lloyds  | /product/party/segment/Bruce   | 200            | -1         | Richter  |You have been rejected by CBS |
      
      
  Scenario Outline: Validate product eligibility mock service response failure with invalid identifer
    Given I have a service running on "<envHost>" for a brand
    And urlidentifier as "<urlIdentifier>"
    When I try to get response from the API
    And I should see http statuscode as "<httpStatusCode>"
    And I should see asmcode as "<asmCode>"
    And I should see user error msg as "<Error>"

    Examples: 
      | envHost                      | urlIdentifier                  | httpStatusCode | asmCode | Error |
      | env.productEligibile.lloyds  | /product/party/segment/xxx     | 200            | -1      | The User is an Invalid User |
      