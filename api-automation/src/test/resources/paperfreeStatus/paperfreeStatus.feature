#############################################################
#############################################################
@api
Feature: Retrieve Involved Party Details

  Scenario Outline: Retrieve the involved party details
    Given the user has selected a particular product "<product>" of a particular brand "<brand>"
    And the user has been offered "<offer>" a product of that particular brand "<brand>"
    Then retrieve the green status for product holdings "<brand>"
    Then verify the green status "<greenStatus>"

    Examples: 
      | brand   | product       | offer                                       | greenStatus |
      | halifax | urcaccountHLX | jsonRequestFiles/offer/HlxNonDownsellAccept | FULL PAPER  |
