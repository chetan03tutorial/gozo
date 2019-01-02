 #############################################################
 ##API Acceptance Tests for PCA-465 (DetermineEligibilityCI)
 #############################################################
 
@api @regression @pca-465
Feature: DetermineEligibilityCI 

   Scenario Outline: Validate the response if user is eligible for RCA by posting arrangement type as CA
   Given I have a service running on "<envHost>" for a brand
   When I try to get response from the API "<JsonFileName>"
    And I should see the message as "<msg>"
    And I should see the mnemonic as "<mnemonics>"
    And I should see the eligiblity criteria is "<isEligible>"
    And I should see http statuscode as "<httpStatusCode>"

    Examples: 
       | envHost          | httpStatusCode |msg                                           | mnemonics |isEligible |JsonFileName		    |   
       | env.deci.halifax | 200            |Successfully fetched the eligibility details. | P_REWARD  |  true     |DECI_validRequest_CA |
     
 #################################################################################################################################    
     
   Scenario Outline: Validate the response if user is eligible for RCA by posting arrangement type as SA
   Given I have a service running on "<envHost>" for a brand
   When I try to get response from the API "<JsonFileName>"
    And I should see the message as "<msg>"
    And I should see the mnemonic as "<mnemonics>"
    And I should see the eligiblity criteria is "<isEligible>"
    And I should see http statuscode as "<httpStatusCode>"

    Examples: 
       | envHost          |  httpStatusCode  |msg                                          | mnemonics   |isEligible  |JsonFileName        |   
       | env.deci.halifax |  200             |Successfully fetched the eligibility details.| P_REWARD    |true        |DECI_validRequest_SA|
       
       
  #############################################################################################################################################

	Scenario Outline: Validate the error code and error messages if Customer belongs to High risk Country 
   Given I have a service running on "<envHost>" for a brand
   When I try to get response from the API "<JsonFileName>"
    And I should see the message as "<msg>"
    And I should see the mnemonic as "<mnemonics>"
    And I should see the eligiblity criteria is "<isEligible>"
    And I should see http statuscode as "<httpStatusCode>"
	And I should see the code as "<errorCode>"
    And I should see the description as "<errorDescription>"

     Examples: 
       | envHost          |httpStatusCode|msg                                           | errorCode| errorDescription            | mnemonics |isEligible |JsonFileName     				     |         
	   | env.deci.halifax |200           |Successfully fetched the eligibility details. | CR039    | nationality is not allowed  | P_REWARD  |false      |DECI_validRequest_CA_errorCodeCR039|     

#############################################################################################################################################	  
	   
   #Scenario Outline: Validate the response if the customer age is less than 18 and greater than 100
   #Given I have a service running on "<envHost>" for a brand
   #When I try to get response from the API "<JsonFileName>"
    #And I should see the message as "<msg>"
    #And I should see http statuscode as "<httpStatusCode>"

   #Examples: 
       #| envHost          | httpStatusCode | msg 				              |JsonFileName                             |   
       #| env.deci.halifax | 400            |The age must be between 18-100    | DECI_invalidRequest_CA_ageLessThan18    |
       #| env.deci.halifax | 400            |The age must be between 18-100    | DECI_invalidRequest_CA_ageGreaterThan100|
       
       #############################################################################################################################################	  
	   
   #Scenario Outline: Validate the error message if Customer Product holding Limit exceeds
   #Given I have a service running on "<envHost>" for a brand
   #When I try to get response from the API "<JsonFileName>"
    #And I should see the message as "<msg>"
    #And I should see the mnemonic as "<mnemonics>"
    #And I should see the eligiblity criteria is "<isEligible>"
    #And I should see http statuscode as "<httpStatusCode>"
	#And I should see the code as "<errorCode>"
    #And I should see the description as "<errorDescription>"

     #Examples: 
      # | envHost          |httpStatusCode|msg                                           | errorCode| errorDescription                       | mnemonics |isEligible |JsonFileName     				    |   
      # | env.deci.halifax |200           |Successfully fetched the eligibility details. | CR043    | Customer Product holding Limit exceeds | P_REWARD  |false      |DECI_validRequest_CA_errorCodeCR043|


 #############################################################################################################################################	

#Scenario Outline: Validate the error message if Customer Applied product holding Limit exceeds
   #Given I have a service running on "<envHost>" for a brand
   #When I try to get response from the API "<JsonFileName>"
    #And I should see the message as "<msg>"
    #And I should see the mnemonic as "<mnemonics>"
    #And I should see the eligiblity criteria is "<isEligible>"
    #And I should see http statuscode as "<httpStatusCode>"
	#And I should see the code as "<errorCode>"
    #And I should see the description as "<errorDescription>"

     #Examples: 
       #| envHost          |httpStatusCode|msg                                           | errorCode| errorDescription                              | mnemonics |isEligible |JsonFileName     				   |   
       #| env.deci.halifax |200           |Successfully fetched the eligibility details. | CR042    | Customer Applied product holding Limit exceeds| P_REWARD  |false      |DECI_validRequest_CA_errorCodeCR042|


 #############################################################################################################################################

#Scenario Outline: Validate the error code and error messages if Customer has Frozen Account
   #Given I have a service running on "<envHost>" for a brand
   #When I try to get response from the API "<JsonFileName>"
    #And I should see the message as "<msg>"
    #And I should see the mnemonic as "<mnemonics>"
    #And I should see the eligiblity criteria is "<isEligible>"
    #And I should see http statuscode as "<httpStatusCode>"
	#And I should see the code as "<errorCode>"
    #And I should see the description as "<errorDescription>"

     #Examples: 
       #| envHost          |httpStatusCode|msg                                           | errorCode| errorDescription             | mnemonics |isEligible |JsonFileName     				  |   
	   #| env.deci.halifax |200           |Successfully fetched the eligibility details. | CR041    | Customer has Frozen Account  | P_REWARD  |false      |DECI_validRequest_CA_errorCodeCR041|
	   
	   

 ############################################################################################################################################# 
	   
  Scenario Outline: Validate the response if the arrangement type is invalid
   Given I have a service running on "<envHost>" for a brand
   When I try to get response from the API "<JsonFileName>"
    And I should see the message as "<msg>"
    And I should see http statuscode as "<httpStatusCode>"

   Examples: 
       | envHost          | httpStatusCode |msg 				|JsonFileName			            	 |   
       | env.deci.halifax | 400            |Invalid JSON format |DECI_invalidRequest_CA_incorrectArrType |
       
  
	   
  #############################################################################################################################################	  
  
  Scenario Outline: Validate the response if the date of birth format is incorrect
   Given I have a service running on "<envHost>" for a brand
   When I try to get response from the API "<JsonFileName>"
    And I should see the message as "<msg>"
    And I should see http statuscode as "<httpStatusCode>"

   Examples: 
       | envHost          |  httpStatusCode| msg 				 |JsonFileName					      |   
       | env.deci.halifax |  400           |Invalid JSON format  |DECI_invalidRequest_CA_incorrectDOB |  
       
  #############################################################################################################################################	  
  
  Scenario Outline: Validate the response if other product mnemonic is passed
   Given I have a service running on "<envHost>" for a brand
   When I try to get response from the API "<JsonFileName>"
    And I should see the message as "<msg>"
    And I should see the mnemonic as "<mnemonics>"
    And I should see the eligiblity criteria is "<isEligible>"
    And I should see http statuscode as "<httpStatusCode>"

   Examples: 
       | envHost          | httpStatusCode |msg                                           | mnemonics   |isEligible |JsonFileName		                    |   
       | env.deci.halifax | 200            |Successfully fetched the eligibility details. | P_ULTIMATE  |  true     |DECI_validRequest_CA_otherProductMnemonic |  
       
       
  #############################################################################################################################################	  
  
  #Scenario Outline: Validate the response if multiple nationalities are passed and one is high risk country
   #Given I have a service running on "<envHost>" for a brand
   #When I try to get response from the API "<JsonFileName>"
    #And I should see the message as "<msg>"
    #And I should see the mnemonic as "<mnemonics>"
    #And I should see the eligiblity criteria is "<isEligible>"
    #And I should see http statuscode as "<httpStatusCode>"

   #Examples: 
      # | envHost          | httpStatusCode |msg                                           | mnemonics |isEligible |JsonFileName		                           |   
       #| env.deci.halifax | 200            |Successfully fetched the eligibility details. | P_REWARD  |  false     |DECI_validRequest_CA_multipleNationalities |      
       
  #############################################################################################################################################	  
  
  Scenario Outline: Validate the response by passing employer details, home phone number and structured address
   Given I have a service running on "<envHost>" for a brand
   When I try to get response from the API "<JsonFileName>"
    And I should see the message as "<msg>"
    And I should see the mnemonic as "<mnemonics>"
    And I should see the eligiblity criteria is "<isEligible>"
    And I should see http statuscode as "<httpStatusCode>"

   Examples: 
       | envHost          | httpStatusCode |msg                                           | mnemonics |isEligible |JsonFileName		                                |   
       | env.deci.halifax | 200            |Successfully fetched the eligibility details. | P_REWARD  |  true     |DECI_validRequest_CA_emplyerAndstructuredAddress |         
	   
  #############################################################################################################################################

  ###########################################################################################################################################
  @Negative
  Scenario Outline: Validate the response if the date of birth is Null
   		Given I have a service running on "<envHost>" for a brand
   		When I try to get response from the API "<JsonFileName>"
    	And I should see the message as "<msg>"
    	And I should see http statuscode as "<httpStatusCode>"

   Examples:
       | envHost          |  httpStatusCode| msg 				 						|JsonFileName					      |   
       | env.deci.halifax |  400           |'dob' required to be set but it was 'null'  |DECI_invalidRequest_CA_EmptyDOB |
       
       
  ###########################################################################################################################################
    @Negative
    Scenario Outline: Validate the response if the last name is more than 24 characters
   		Given I have a service running on "<envHost>" for a brand
   		When I try to get response from the API "<JsonFileName>"
    	And I should see the message as "<msg>"
    	And I should see http statuscode as "<httpStatusCode>"

   	Examples: 
       | envHost          |  httpStatusCode| msg 				 								  							   |JsonFileName					      |   
       | env.deci.halifax |  400           |'longercharacterthantwentyfour' is longer than '24' characters in field 'lastName' |DECI_invalidRequest_CA_LongerName |
    

   ##########################################################################################################################################
  @Negative
   Scenario Outline: Validate the response if the Postal Code is invalid
   		Given I have a service running on "<envHost>" for a brand
   		When I try to get response from the API "<JsonFileName>"
    	And I should see the message as "<msg>"
    	And I should see http statuscode as "<httpStatusCode>"

   Examples:
       | envHost          |  httpStatusCode| msg 				 								  	   |JsonFileName					  |   
       | env.deci.halifax |  400           |'EC1Y4XXWF' is not matching with format in field 'postcode' |DECI_invalidRequest_CA_InvalidPostCode |


###########################################################################################################################################

  #Scenario Outline: Validate the response if service is unavailable or timed out error

    #Given I have a service running on "<envHost>" for a brand
    #When I try to get response from the API "<JsonFileName>"
    #And I should see the message as "<msg>"
    #And I should see http statuscode as "<httpStatusCode>"

    #Examples:
      #| envHost          | httpStatusCode  | msg                         | mnemonics   |isEligible |JsonFileName                         |
      #| env.deci.halifax | 408             | The services has timed out  | P_REWARD    |           |DECI_validRequest_serviceTimedOut    |
      #| env.deci.halifax | 500             | No records found            | P_REWARD    |           |DECI_validRequest_noRecordFound      |
      #| env.deci.halifax | 503             | Service Unavailable         | P_REWARD    |           |DECI_validRequest_serviceUnavailable |
