#############################################################
##API Acceptance Tests for ACO- (Q226, Q227, APTDA)
#############################################################
@API @Regression @Q226
Feature: Add a party services

  Scenario Outline: Unique Result while searching customer by product number
    Given I have  service available for feature "<feature>" and brand "<brand>"
    And requesting the customer details for the agreement identifier "<agreementIdentifier>" and type "<type>"
    When I try to get response from the API for the request
    Then result count should be "<numberOfCustomer>"
    Then I should see the response details with "<firstName>" and "<lastName>"
    And I should see http statuscode as "<httpStatusCode>"

    Examples: 
      | feature    | brand   | httpStatusCode | firstName | lastName | agreementIdentifier | numberOfCustomer |type|
      | q226Search | lloyds  |            200 | Richard   | Smith    |     377064515310457 |                2 |CREDIT_CARD|
