#############################################################
##API Acceptance Tests for UserMandateInfo
#############################################################
@api @UserMandateInfo @API @REGRESSION @sprint-1 @pcaAuth @pca-108
Feature: Validating UserMandateInfo service by hitting the endpoint

  Scenario Outline: get User Mandate Info for valid post request
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the userMandateInfo API "<userMandateInfoUrl>"
    Then I should see response with following user Mandate details
      | userRegStateCode |
      | userRegStateDesc |
      | dateFirstLogon   |
      | dateLastLogon    |
      | dateLastLogon    |

    Examples: 
      | httpStatusCode | key       | validateTokenHost | tokenValue           | userMandateInfoUrl  | userRegStateCode | dateFirstLogon | dateLastLogon | lastLoginInMins |
      |            200 | validated | env.validateToken | paperless/1319519899 | env.userMandateInfo |                0 | null           | null          |               0 |

  Scenario Outline: get User Mandate Info for valid post request
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the userMandateInfo API "<userMandateInfoUrl>"
    Then I should see response with following user Mandate details
      | userRegStateCode |
      | userRegStateDesc |
      | dateFirstLogon   |
      | dateLastLogon    |
      | dateLastLogon    |

    Examples: 
      | httpStatusCode | key       | validateTokenHost | tokenValue           | userMandateInfoUrl  | userRegStateCode | dateFirstLogon | dateLastLogon | lastLoginInMins |
      |            200 | validated | env.validateToken | paperless/1476789725 | env.userMandateInfo |                1 | null           | null          |               0 |

  Scenario Outline: get User Mandate Info for valid post request
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the userMandateInfo API "<userMandateInfoUrl>"
    Then I should see response with following user Mandate details
      | userRegStateCode |
      | userRegStateDesc |
      | dateFirstLogon   |
      | dateLastLogon    |
      | dateLastLogon    |

    Examples: 
      | httpStatusCode | key       | validateTokenHost | tokenValue           | userMandateInfoUrl  | userRegStateCode | dateFirstLogon               | dateLastLogon                | lastLoginInMins |
      |            200 | validated | env.validateToken | paperless/0986848953 | env.userMandateInfo |                4 | Mon Sep 01 00:00:00 BST 2014 | Thu Jun 15 00:00:00 BST 2017 |               0 |

  Scenario Outline: get User Mandate Info for valid post request
    Given I Post the SAML token to Token Validate API as "<validateTokenHost>" having value "<tokenValue>"
    Then I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    When I hit the userMandateInfo API "<userMandateInfoUrl>"
    Then I should see response with following user Mandate details
      | userRegStateCode |
      | userRegStateDesc |
      | dateFirstLogon   |
      | dateLastLogon    |
      | dateLastLogon    |

    Examples: 
      | httpStatusCode | key       | validateTokenHost | tokenValue           | userMandateInfoUrl  | userRegStateCode | dateFirstLogon               | dateLastLogon                | lastLoginInMins |
      |            200 | validated | env.validateToken | paperless/2087795898 | env.userMandateInfo |                5 | Mon Sep 01 00:00:00 BST 2014 | Wed Feb 15 00:00:00 GMT 2017 |               0 |
