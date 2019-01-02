@api @getUserInfo @API @REGRESSION @sprint-1 @pcaAuth @pca-108
Feature: Conversion fulfilment API

  Scenario Outline: Conversion fulfilment successful
    Given Service is running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    And I Get the response from userInfo from API "<userInfoUrl>"
    And I Get the response from ocisId from API
    And we send a request for upgrade eligibility service "<upgradeEligibleEndPoint>" "<conversionEligibilityRequest>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see "msg" as "<msgValue>"
    And I should see the response details with "<productMnemonic>" and "<eligibilityValue>"
    And we send a request for conversion fulfilment service "<conversionFulfilmentEndPoint>" "<conversionFulfilmentRequest>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see the response details with "upgradeDone" as "<cbsUpdate>"
    And I should see the response details with "updateOcisDone" as "<ocisUpdate>"

    Examples:
      | upgradeEligibleEndPoint                | conversionFulfilmentEndPoint          | envHost           | user | validateTokenHost | httpStatusCode | key       | value | userInfoUrl  | msgValue                                              | conversionEligibilityRequest    | productMnemonic | eligibilityValue | conversionFulfilmentRequest           | cbsUpdate | ocisUpdate |
      | lloydspcaConversionEligibilityEndpoint | lloydspcaConversionFulfilmentEndpoint | env.samlAssertion | Adam | env.validateToken | 200            | validated | true  | env.userInfo | Successfully fetched the upgrade eligibility details. | conversionEligibilitySuccess    | P_PLAT          | true             | conversionFulfilmentLloydsSuccess     | true      | true |
      | lloydspcaConversionEligibilityEndpoint | lloydspcaConversionFulfilmentEndpoint | env.samlAssertion | Adam | env.validateToken | 200            | validated | true  | env.userInfo | Successfully fetched the upgrade eligibility details. | conversionEligibilitySuccess    | P_CLUB          | true             | conversionFulfilmentLloydsOcisFailure | true      | false |