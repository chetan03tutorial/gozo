@ApiTesting @Regression @ACO-587 @smokeTest
Feature: C078 API Validation

  ########################
  ####1
  ########################

  Scenario Outline: Valid application Id and application Tpe
    Given I have a "<endpoint>" for valid "<applicationType>" and  "<applicationId>"
    When I try to get the response from the API
    Then I should see http status code as "<httpStatusCode>"


    Examples:
      | endpoint          | applicationType |applicationId | httpStatusCode |
      | env.C078          | QWEL           | 65347        | 200            |


  Scenario Outline: InValid application Id and application Tpe
    Given I have a "<endpoint>" for valid "<applicationType>" and  "<applicationId>"
    When I try to get the response from the API
    Then I should see http status code as "<httpStatusCode>" and should get error message "<errorMessage>"


    Examples:
      | endpoint          | applicationType |applicationId | httpStatusCode  | errorMessage                                             |
      | env.C078          | QWEL           | 65347J        | 200            | Requested application not found on Credit Score Database |