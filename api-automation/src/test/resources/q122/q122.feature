#############################################################
#############################################################
@api 
Feature: Q122 Service 

Scenario Outline: Test Q122 for Accept 
	Given token service is up and running "<brand>" "<tokenRequestFile>"
	And user info service is up and running "<brand>" 
	Given overdraft upfront service is up and running "<brand>" "<accountNumber>" "<sortCode>"
	And asm decision service is up and running "<brand>" "<q122Request>"  
	And validate the asm decision code as "<creditResult>" 
	
	Examples: 
		| brand   | tokenRequestFile                         | accountNumber|sortCode |q122Request|creditResult|
		| lloyds  | jsonRequestFiles/overdraft/1631000_TOKEN | 00001361      |163100   |jsonRequestFiles/q122/q122Request|1|
		
		
Scenario Outline: Test Q122 for Refer 
	Given token service is up and running "<brand>" "<tokenRequestFile>"
	And user info service is up and running "<brand>" 
	Given overdraft upfront service is up and running "<brand>" "<accountNumber>" "<sortCode>"
	And asm decision service is up and running "<brand>" "<q122Request>"  
	And validate the asm decision code as "<creditResult>"
	
	Examples: 
		| brand   | tokenRequestFile                         | accountNumber|sortCode |q122Request|creditResult|
		| lloyds  | jsonRequestFiles/overdraft/1631012_TOKEN | 13001361      |163110   |jsonRequestFiles/q122/q122Request|2|
		

Scenario Outline: Test Q122 for Decline 
	Given token service is up and running "<brand>" "<tokenRequestFile>"
	And user info service is up and running "<brand>" 
	Given overdraft upfront service is up and running "<brand>" "<accountNumber>" "<sortCode>"
	And asm decision service is up and running "<brand>" "<q122Request>"  
	And validate the asm decision code as "<creditResult>"
	
	Examples: 
		| brand   | tokenRequestFile                         | accountNumber|sortCode |q122Request|creditResult|
		| lloyds  | jsonRequestFiles/overdraft/1631013_TOKEN | 14001361      |163110   |jsonRequestFiles/q122/q122Request|3|
		

Scenario Outline: Test Q122 for Downsale 
	Given token service is up and running "<brand>" "<tokenRequestFile>"
	And user info service is up and running "<brand>" 
	Given overdraft upfront service is up and running "<brand>" "<accountNumber>" "<sortCode>"
	And asm decision service is up and running "<brand>" "<q122Request>"  
	And validate the asm decision code as "<creditResult>" 
	
	Examples: 
		| brand   | tokenRequestFile                         | accountNumber|sortCode |q122Request|creditResult|
		| lloyds  | jsonRequestFiles/overdraft/1631001_TOKEN | 10001361      |163101   |jsonRequestFiles/q122/q122Request|2|
		
