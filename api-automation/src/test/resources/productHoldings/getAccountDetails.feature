@api @getProductHoldings @REGRESSION @pcaAuth @sprint-3 @pca-450

Feature: ProductHoldings api validations

  Scenario: get customer product holdings which are eligible to recieve payments(current,savings,Monthly saving Accounts and ISA Accounts)
    Given I have a service running on "env.validateToken"
    When I try to get response from the getToken API "env.samlAssertion" the user "Adam"
    Then I should see the response details with SAML token
    When I Post the SAML token to Token Validate API
    Then I should see request message with "validated" having value "true"
    When I Get the response from userInfo API for the customer having eligible current,savings,Monthly saving and ISA Accounts from API "env.userInfo"
    Then I should see response with following user details
      | title          |
      | firstName      |
      | lastName       |
      | dob            |
      | email          |
      | applicantType  |
      | occupnType     |
      | gender         |
      | nationalities  |
      | birthCountry   |
      | birthCity      |
      | mobileNumber   |
      | currentAddress |
      |lastLoggedInTime |

    And I should see JSON response with following products as productType in accounts section
      | Savings       |
      | Current       |
      | ISA           |
      | Monthly Saver |

    And I should see following fileds for current,savings,Monthly saving and ISA Accounts in accounts section
      | accountName   |
      | accountNumber |
      | sortCode      |
      | productType   |
    And I should see accountsFetched field as true
    
    
    Scenario: one of customer accounts is in dormant and user can't select dormant account for recieve payments(current ,savings account in dormant ,Monthly saving Accounts and ISA Accounts)
    Given I have a service running on "env.validateToken"
    When I try to get response from the getToken API "env.samlAssertion" the user "Michael"
    Then I should see the response details with SAML token
    When I Post the SAML token to Token Validate API
    Then I should see request message with "validated" having value "true"
    When I Get the response from userInfo API for the customer having eligible current,savings,Monthly saving and ISA Accounts from API "env.userInfo"
    Then I should see response with following user details
      | title          |
      | firstName      |
      | lastName       |
      | dob            |
      | email          |
      | applicantType  |
      | occupnType     |
      | gender         |
      | nationalities  |
      | birthCountry   |
      | birthCity      |
      | mobileNumber   |
      | currentAddress |
      |lastLoggedInTime |

    And I should see JSON response with following products as productType in accounts section
      | Savings       |
      | Current       |
      | ISA           |
      | Monthly Saver |

    And I should see following fileds for current,savings,Monthly saving and ISA Accounts in accounts section
      | accountName   |
      | accountNumber |
      | sortCode      |
      | productType   |
    And I should see accountsFetched field as true
 
  
  Scenario: customer doesn't have any eleigible existing accounts to receive payments
    Given I have a service running on "env.validateToken"
    When I try to get response from the getToken API "env.samlAssertion" the user "David"
    Then I should see the response details with SAML token
    When I Post the SAML token to Token Validate API
    Then I should see request message with "validated" having value "true"
    When I Get the response from userInfo API for the customer having no eligible current,savings,Monthly saving and ISA Accounts from API "env.userInfo"
    Then I should see response with following user details
      | title          |
      | firstName      |
      | lastName       |
      | dob            |
      | email          |
      | applicantType  |
      | occupnType     |
      | gender         |
      | nationalities  |
      | birthCountry   |
      | birthCity      |
      | mobileNumber   |
      | currentAddress |
      |lastLoggedInTime |

    And I should not see JSON response with following products as productType in accounts section
      | Savings       |
      | Current       |
      | ISA           |
      | Monthly Saver |
      
    And I should not see following fileds for current,savings,Monthly saving and ISA Accounts in accounts section
      | accountName   |
      | accountNumber |
      | sortCode      |
      | productType   |
      
    And I should see accountsFetched field as true