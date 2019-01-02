@api @getUserInfo @API @REGRESSION @sprint-1 @pcaAuth @pca-108
Feature: userInfo API Validation

  Scenario Outline: get User Info for valid post request
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    And I Get the response from userInfo from API "<userInfoUrl>"
    Then I should see response with following user details
      | title           |
      | firstName       |
      | lastName        |
      | dob             |
      | email           |
      | applicantType   |
      | occupnType      |
      | gender          |
      | nationalities   |
      | birthCountry    |
      | birthCity       |
      | currentAddress  |

    Examples:
      | envHost            | httpStatusCode | key      | value |user   | validateTokenHost |userInfoUrl  | 
      | env.samlAssertion  | 200            | validated| true  |Adam   | env.validateToken |env.userInfo |
      
  Scenario Outline: get User Info for valid post request
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    And I Get the response from userInfo from API "<userInfoUrl>"
    Then I should see response with following user details validate isBFPO address
      | title           |
      | firstName       |
      | lastName        |
      | dob             |
      | email           |
      | applicantType   |
      | occupnType      |
      | gender          |
      | nationalities   |
      | birthCountry    |
      | birthCity       |
      | currentAddress  |

    Examples:
      | envHost            | httpStatusCode | key      | value |user                   | validateTokenHost |userInfoUrl  |
      | env.samlAssertion  | 200            | validated| true  |Arnold_A701_IDV_BFPO   | env.validateToken |env.userInfo |

