@api @bankWizard-api
Feature: Bank Wizard API Validation

  @Negative
  Scenario Outline: The API responds with TRUE output when valid Bank Account number and Sort Code are provided
    Given I have  service available for feature "bankwizard" and brand "<brand>"
    When I send a POST "<Request>"
    Then I should see http statuscode as "<ResponseCode>"
    And I should see the response value as "<isValidIndicator>"
    And I should see the isIntraBrandSwitching value as "<IntraBrandSwitching>"
    And I should see the bankname value as "<BankName>"
    And I should see the bankInCASS value as "<bankInCASS>"

    Examples: 
      | brand                  | Request         | ResponseCode | isValidIndicator |IntraBrandSwitching    |BankName           |bankInCASS|
      | env.bankwizard.lloyds  | BWTrueInputdata |          200 | true          |true                   |LLOYDS BANK PLC    |true|
      | env.bankwizard.halifax | BWTrueInputdataHLX |          200 | true          |true               |HALIFAX   |true|
      | env.bankwizard.bos     | BWTrueInputdataBOS |          200 | true          |true               |Bank of Scotland plc    |true|
      | env.bankwizard.bos     | DifferentBankName |          200 | false          |false               |MONZO BANK LIMITED    |false|

  @Negative
  Scenario Outline: The API responds with FALSE output when valid Bank Account number and Sort Code are provided

    Given I have  service available for feature "bankwizard" and brand "<brand>"
    When I send a POST "<Request>"
    Then I should see http statuscode as "<ResponseCode>"
    And I should see the response value as "<ResponseValue>"
    And I should see the isIntraBrandSwitching value as "<IntraBrandSwitching>"
    And I should see the bankname value as "<BankName>"

    Examples:
      | brand                  | Request          | ResponseCode | ResponseValue |IntraBrandSwitching    |BankName|
      | env.bankwizard.lloyds  | BWFalseInputdata |          200  | false        |true                   |LLOYDS BANK PLC    |
      | env.bankwizard.halifax | BWFalseInputdata |          200 | false         |false                   |LLOYDS BANK PLC    |
      | env.bankwizard.bos     | BWFalseInputdata |          200 | false         |false                   |LLOYDS BANK PLC    |


  @Negative
  Scenario Outline: The API responds with Bank Account number and Sort Code are null

    Given I have  service available for feature "bankwizard" and brand "<brand>"
    When I send a POST "<Request>"
    Then I should see http statuscode as "<ResponseCode>"
    And I should see the response value as "<isValidIndicator>"
    And I should see the isIntraBrandSwitching value as "<IntraBrandSwitching>"
    And I should see the bankInCASS value as "<bankInCASS>"
    And I should see the bankname value as "<BankName>"

    Examples:
      | brand                  | Request          | ResponseCode | isValidIndicator |IntraBrandSwitching   |BankName  |bankInCASS|
      | env.bankwizard.lloyds  | AccSortNull     |    200       | false          |false                   |null      | false     |
      | env.bankwizard.halifax | AccSortNull     |    200       | false          |false                   |null      | false     |
      | env.bankwizard.bos     | AccSortNull     |    200       | false          |false                   |null      | false     |

  @Negative
  Scenario Outline: The API responds with Bank name Null

    Given I have  service available for feature "bankwizard" and brand "<brand>"
    When I send a POST "<Request>"
    Then I should see http statuscode as "<ResponseCode>"
    And I should see the response value as "<isValidIndicator>"
    And I should see the isIntraBrandSwitching value as "<IntraBrandSwitching>"
    And I should see the bankInCASS value as "<bankInCASS>"
    And I should see the bankname value as "<BankName>"

    Examples:
      | brand                  | Request          | ResponseCode | isValidIndicator |IntraBrandSwitching   |BankName  |bankInCASS|
      | env.bankwizard.lloyds  | banknameNull     |    200       | false          |false                   |null      | true     |
      | env.bankwizard.halifax | banknameNull     |    200       | false          |false                   |null      | true     |
      | env.bankwizard.bos     | banknameNull     |    200       | false          |false                   |null      | true     |
