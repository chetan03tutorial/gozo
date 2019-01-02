#############################################################
#############################################################
@api
Feature: OD Fulfillment Wrapper


Scenario Outline: Existing overdraft is zero and customer is trying to decrease overdraft 
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then expect an error

    Examples:
      |brand|demandedOverdraftAmount|accountNumber|sortCode|
      |lloyds|0|00001361|163100|
      
      

Scenario Outline: Existing overdraft is zero and customer is trying to increase overdraft and requested overdraft is less than offered overdraft
      
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then create a new overdraft of amount "<demandedOverdraftAmount>"

    Examples:
      |brand|demandedOverdraftAmount|accountNumber|sortCode|
      |lloyds|100|00001361|163100|

Scenario Outline: Existing overdraft is zero and customer is trying to increase overdraft and requested overdraft is more than offered overdraft
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then expect an error

    Examples:
      |brand|demandedOverdraftAmount|accountNumber|sortCode|
      |lloyds|2500|00001361|163100|

Scenario Outline: Customer is increasing non zero existing overdraft and requested overdraft is equals existing overdraft
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then expect an error

    Examples:
       |brand|demandedOverdraftAmount|accountNumber|sortCode|
      |lloyds|450|00001362|163100|
      
Scenario Outline: Customer is increasing non zero existing overdraft and requested overdraft is less than offered overdraft
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then amend the overdraft amount to "<demandedOverdraftAmount>"

    Examples:
      |brand|demandedOverdraftAmount|accountNumber|sortCode|
      |lloyds|2450|00001362|163100|


Scenario Outline: Customer is increasing non zero existing overdraft and requested overdraft is equals offered overdraft
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then amend the overdraft amount to "<demandedOverdraftAmount>"

    Examples:
      |brand|demandedOverdraftAmount|accountNumber|sortCode|
      |lloyds|2000|00001362|163100|



Scenario Outline: Customer is increasing non zero existing overdraft and requested overdraft is more than offered overdraft
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then expect an error

    Examples:
      |brand|demandedOverdraftAmount|accountNumber|sortCode|
      |lloyds|2500|00001362|163100|
      
      

Scenario Outline: Customer is decreasing non zero existing overdraft to non zero overdraft value
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then amend the overdraft amount to "<demandedOverdraftAmount>"

    Examples:
       |brand|demandedOverdraftAmount|accountNumber|sortCode|
       |lloyds|100|00001362|163100|
      
      
      
Scenario Outline: Customer is decreasing non zero existing overdraft to zero overdraft value
      Given token service is up and running for the user "jsonRequestFiles/overdraft/1631000_TOKEN" for brand "<brand>"
      Given user info service is also up and running "<brand>"
      Given the overdraft upfront service is up and running for brand "<brand>" "<accountNumber>" "<sortCode>"
      And user request the overdraft amount "<demandedOverdraftAmount>" for brand "<brand>"
      Then remove the overdraft amount to "<demandedOverdraftAmount>"

    Examples:
      |brand|demandedOverdraftAmount|accountNumber|sortCode|
      |lloyds|0|00001362|163100|

      
      
