#############################################################
#############################################################
@api 
Feature: Overdraft Appeal 

Scenario Outline: Test Appeal from Refer to Accept
	Given token service is up and running "<brand>" "<tokenRequestFile>"
	And user info service is up and running "<brand>" 
	Given overdraft upfront service is up and running "<brand>" "<accountNumber>" "<sortCode>"
	And asm decision service is up and running "<brand>" "<q122Request>"  
	And validate the asm decision code as "<creditResult>"
	Given overdraft appeal service is up and running "<brand>"
	Then validate the pld decision "<pldDecision>"
	
	Examples: 
		| brand   | tokenRequestFile                         | accountNumber|sortCode |q122Request|creditResult|pldDecision|
		| lloyds  | jsonRequestFiles/overdraft/1631012_TOKEN | 13001361      |163110   |jsonRequestFiles/q122/q122Request|2|1|
		

Scenario Outline: Test Appeal from decline to accept 
	Given token service is up and running "<brand>" "<tokenRequestFile>"
	And user info service is up and running "<brand>" 
	Given overdraft upfront service is up and running "<brand>" "<accountNumber>" "<sortCode>"
	And asm decision service is up and running "<brand>" "<q122Request>"  
	And validate the asm decision code as "<creditResult>"
	Given overdraft appeal service is up and running "<brand>"
	Then validate the pld decision "<pldDecision>"
	
	Examples: 
		| brand   | tokenRequestFile                         | accountNumber|sortCode |q122Request|creditResult|pldDecision|
		| lloyds  | jsonRequestFiles/overdraft/1631013_TOKEN | 14001361      |163110  |jsonRequestFiles/q122/q122Request|3|1|
		

Scenario Outline: Test Appeal from downsell to accept 
	Given token service is up and running "<brand>" "<tokenRequestFile>"
	And user info service is up and running "<brand>" 
	Given overdraft upfront service is up and running "<brand>" "<accountNumber>" "<sortCode>"
	And asm decision service is up and running "<brand>" "<q122Request>"  
	And validate the asm decision code as "<creditResult>"
	Given overdraft appeal service is up and running "<brand>"
	Then validate the pld decision "<pldDecision>"
	
	Examples: 
		| brand   | tokenRequestFile                         | accountNumber|sortCode |q122Request|creditResult|pldDecision|
		| lloyds  | jsonRequestFiles/overdraft/1631003_TOKEN | 30001361      |163103   |jsonRequestFiles/q122/q122Request|2|1|
		
