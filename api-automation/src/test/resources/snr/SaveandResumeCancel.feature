@api @snr-api
Feature: Terminate arrangement


########################  Save and Resume- Cancel - POST Method  ##################################

  Scenario Outline: Cancel api|
    
	Given I perform the cancel operation for save and resume enpoint and Request "<endpoint>" "<request>"
    Then I should see httpstatuscode as "<ResponseCode>"
    Then I should see assignedArrangementId as "<arrangementId>"
  
    
 Examples:
     | brand   | endpoint       	    |request			| ResponseCode   | arrangementId|
     | lloyds  | env.SnRCancel.lloyds   |SaveandResumeCancel|     200        | 	59146		|

 

########################  Save and Resume- Cancel - POST Method  ##################################

  Scenario Outline: Cancel api| Cross brand testing
    
    Given I perform the snr cancel api validation using enpoint and Request "<endpoint>" "<request>" "<CrossBrandarrangementId>"
    And I should validate error message for CrossBrand "<message>"
    
 Examples:
     | brand   | endpoint       	    |request			| ResponseCode   | CrossBrandarrangementId   |message|
     | lloyds  | env.SnRCancel.lloyds   |SaveandResumeCancel|     200        | 654321    	             |  Exception occured while cancel application. Either the application is not valid for the Channel or Arrangement Id is not valid.     |
 	
 
 