@api @getUserInfo @API @REGRESSION @sprint-1 @pcaAuth @pca-108
Feature: conversion eligibility API

  Scenario Outline: Conversion eligibility request for authenticated Users for lloyds
    Given Service is running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    And I Get the response from userInfo from API "<userInfoUrl>"
    And I Get the response from ocisId from API
    And we send a request for upgrade eligibility service "<upgradeEligibleEndPoint>" "<deciRequest>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see "msg" as "<msgValue>"
    And I should see the response details with "<productMnemonic>" and "<eligibilityValue>"

    Examples:
      | upgradeEligibleEndPoint                | envHost           | user | validateTokenHost | httpStatusCode | key       | value | userInfoUrl  | msgValue                                              | deciRequest                     | productMnemonic | eligibilityValue |
      | lloydspcaConversionEligibilityEndpoint | env.samlAssertion | Adam | env.validateToken | 200            | validated | true  | env.userInfo | Successfully fetched the upgrade eligibility details. | conversionEligibilitySuccess    | P_CLASSIC       | false            |
      | lloydspcaConversionEligibilityEndpoint | env.samlAssertion | Adam | env.validateToken | 200            | validated | true  | env.userInfo | Successfully fetched the upgrade eligibility details. | conversionEligibilitySuccess    | P_PLAT          | true            |