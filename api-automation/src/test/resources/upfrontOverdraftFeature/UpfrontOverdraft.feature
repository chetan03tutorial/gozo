#############################################################
#############################################################
@api
Feature: Upfront Overdraft BDD

  Scenario Outline: Request upfront overdraft amount
  
  	Given token service is up and running "<brand>" "<tokenRequestFile>"
	And user info service is up and running "<brand>" 
	Given overdraft upfront service is up and running "<brand>" "<accountNumber>" "<sortCode>"
	Then verify the upfront overdraft limit "<brand>" "<overdraftLimit>" 

    Examples:
      | brand   | tokenRequestFile                        |  overdraftLimit    |accountNumber|sortCode |
      | lloyds | jsonRequestFiles/overdraft/1631000_TOKEN |  +002000           |0001361      |163100   |

