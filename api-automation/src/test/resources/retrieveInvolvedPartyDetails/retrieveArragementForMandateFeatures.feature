#############################################################
#############################################################
@api
Feature: Retrieve Involved Party Details

  Scenario Outline: Retrieve the involved party details
    Given the user has selected a particular product "<product>" of a particular brand "<brand>"
    And the user has been offered "<offer>" a product of that particular brand "<brand>"
    Then retrieve the involved party information for this user "<brand>"
    Then verify the email "<email>" of the party

    Examples: 
      | brand   | product       | offer                                       | email   |
      | halifax | urcaccountHLX | jsonRequestFiles/offer/HlxNonDownsellAccept | a@b.com |
