#############################################################
##API Acceptance Tests for updatePreference
#############################################################
@api @modifyPreference @API @REGRESSION @sprint-1 @pcaAuth @pca-108
Feature: Validating updatePreference service by hitting the endpoint

  Scenario Outline: Update user preference for valid post request
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the updatePreference API "<updatePrefUrl>" with request "<updatePrefReq>"
    Then I should see message for ModifyResponse with "<messageAttr>" having value "<msgValue>"

    Examples: 
      | httpStatusCode | key       | validateTokenHost | tokenValue           | updatePrefUrl        | updatePrefReq       | messageAttr | msgValue                     |
      |            200 | validated | env.validateToken | paperless/s1061335740 | env.updatePreference | updatePreferenceSuc | message     | Details updated Successfully |

  Scenario Outline: Get failure message for invalid valid post request of update preference
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the updatePreference API "<updatePrefUrl>" with request "<updatePrefReq>"
    Then I should see message for ModifyResponse with "<messageAttr>" having value "<msgValue>"

    Examples: 
      | httpStatusCode | key       | validateTokenHost | tokenValue           | updatePrefUrl        | updatePrefReq        | messageAttr | msgValue                                                                     |
      |            200 | validated | env.validateToken | paperless/1174655984 | env.updatePreference | updatePreferenceFail | message     | Statement suppression could not be updated, maximum limit has been exceeded. |
