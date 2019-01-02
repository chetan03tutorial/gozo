@ApiTesting @Regression @ACO-587 @smokeTest
Feature: Fetch Working Days API Validation
  ########################
  ####1
  ########################
  Scenario Outline: Fetch the working Days and validate that the number of days is less than 30 as it excludes weekends and Bank Holidays
    Given I have a "<endpoint>"
    When I try to get the response from the API
    Then I should see http status code as "<httpStatusCode>"
    Then I should check the number of holidays to be less than 30 days
    Examples:
      | endpoint          | httpStatusCode |
      | env.workingDays   | 200            |