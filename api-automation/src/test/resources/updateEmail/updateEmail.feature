#############################################################
##API Acceptance Tests for UpdateEmail
#############################################################
@api @updateEmail @API @REGRESSION @sprint-1 @pcaAuth @pca-108
Feature: Validating UpdateEmail service by hitting the endpoint

  Scenario Outline: Update user email address for valid post request
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the updateEmail API "<updateEmailUrl>" having values "<emailAddress>"
    Then I should see message with "<messageAttr>" having value "<msgValue>"

    Examples: 
      | httpStatusCode | key       | validateTokenHost | tokenValue           | updateEmailUrl  | emailAddress  | ocisId     | partyID      | messageAttr | msgValue                     |
      |            200 | validated | env.validateToken | paperless/1174655984 | env.updateEmail | tushantk@gmail.com | 1174655984 | +00695296958 | message     | Details updated Successfully |

  Scenario Outline: Get failure message for invalid valid post request of update email address
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the updateEmail API "<updateEmailUrl>" having values "<emailAddress>"
    Then I should see message with "<messageAttr>" having value "<msgValue>"

    Examples: 
      | httpStatusCode | key       | validateTokenHost | tokenValue           | updateEmailUrl  | emailAddress  | ocisId     | partyID       | messageAttr | msgValue        |
      |            200 | validated | env.validateToken | paperless/1174655984 | env.updateEmail | tushantk@gmail.com | 1174655984a | +00695296958a | message     | For input string: \"74655984a\" |
