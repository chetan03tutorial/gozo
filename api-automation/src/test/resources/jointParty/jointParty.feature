@api @getProductHoldings @REGRESSION @pcaAuth @sprint-3 @pca-450
Feature: ProductHoldings api validations

  Scenario Outline: Validate joint party info valid post request
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the jointParty API "<jointPartyInfoURL>" with request "<jointPartyReq>"
      | httpStatusCode | key       | validateTokenHost | tokenValue            | jointPartyInfoURL    | jointPartyReq | messageAttr | msgValue                     |
      |            200 | validated | env.validateToken | paperless/s1061335740 | env.jointPartyInfo | jointPartySuc | message     | Details updated Successfully |
    Then I should see message for ModifyResponse with "<messageAttr>" having value "<msgValue>"
      | firstName |
      | surName   |
      | isJoint   |

  Scenario Outline: Validate joint party info invalid post request
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the jointParty API "<jointPartyInfoURL>" with request "<updatePrefReq>"
      | httpStatusCode | key       | validateTokenHost | tokenValue            | jointPartyInfoURL  | jointPartyReq  | messageAttr | msgValue                     |
      |            200 | validated | env.validateToken | paperless/s1061335740 | env.jointPartyInfo | jointPartyFail | message     | Details updated Successfully |
    Then I should see message for ModifyResponse with "<messageAttr>" having value "<msgValue>"
      | firstName |
      | surName   |
      | isJoint   |
