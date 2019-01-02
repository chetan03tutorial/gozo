@api @CMS
Feature: Content API Validation

  Scenario Outline: Validate that content is coming for different products
    Given I have  service available for feature "<feature>" and brand "<brand>"
    When I try to get response from the API for the request
    Then I should see  field "productMnemonic" as "<mnemonicValue>"
    And I should see  label "<label>" as label value "<labelValue>"

    Examples: 
      | feature        | brand   | mnemonicValue | label                  | labelValue          |
      | productContent | lloyds  | P_EASY_SVR    | title                  | Esaver              |
      | productContent | halifax | P_J_ISA_15    | productRank            | 1                   |
      | productContent | bos     | P_JR_ISA      | productOverviewSummary | The everday account |
