@api @samlValidation @REGRESSION @pcaAuth
Feature: saml token validation API 

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |


  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |
      
      
  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |
      
  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            
  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            

  Scenario Outline: validate Token api  with valid Token
    Given I have a service running on "<envHost>"
    When I try to get response from the getToken for user "<user>" API
    And I should see the response details with SAML token
    And I Post the SAML token to Token Validate API as "<validateTokenHost>"

    Examples:
      | envHost            | httpStatusCode | key      | value | validateTokenHost | user |
      | env.samlAssertion  | 200            | validated| true  | env.validateToken | Adam |            
      