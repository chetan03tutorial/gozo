@ApiTesting @Regression @ACO-587 @smokeTest
Feature: post code API Validation

  ########################
  ####1
  ########################


  Scenario Outline: Valid post code with http response
    Given I have a "<endpoint>" for the "<postcode>"
    When I try to get the response from the API
    Then I should see http status code as "<httpStatusCode>"


    Examples:
      | endpoint          | postcode | httpStatusCode |
      | env.addressLookup | E16DU    | 200            |
      | env.addressLookup | E149DR   | 200            |

  @Negative
  Scenario Outline: Invalid post code with http response
    Given I have a "<endpoint>" for the "<postcode>"
    When I try to get the response from the API
    Then I should see response having response code "<responseCode>"

    Examples:
      | endpoint          | postcode | responseCode |
      | env.addressLookup | BR13FD   | 9310003      |
      | env.addressLookup | EMPTY    | 9200008      |

  @Negative
  Scenario Outline: Neg Scenario |Invalid post code with http response
    Given I have a "<endpoint>" for the "<postcode>"
    When I try to get the response from the API
    Then I should see http status code as "<httpStatusCode>"
    Then I should see response having response code "<responseCode>"


    Examples:
      | endpoint          | postcode   | responseCode |httpStatusCode|
      | env.addressLookup | Numeric    | 9200008      |     400      |

