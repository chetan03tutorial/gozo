#############################################################
##API Acceptance Tests for PCA-1296 (UserIDValidation)
#############################################################
@API @REGRESSION @PCA1296
Feature: UserIDValidation by passing username and password

  
  
#################################################################################################

	
    Scenario Outline: Validate user name is available

    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we have a userid validation service running on "<envHost>" for a brand
	When we try to get response from the API "<UserIdValidationRequest>" for "<availability>"
   And we should check the availability as "<availability>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
	xamples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest                 | offeredProduct | eidvScore | asmScore |availability  |httpStatusCode |UserIdValidationRequest |envHost| 
      ###########################################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | OfferProductAcceptRequest     | P_ULTIMATE     | Accept    | Accept   |true		   |200			   |UserIdValidationSuccess |env.useridvalidation.halifax|
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept          | P_CLASSIC      | Accept    | Accept   |true		   |200			   |UserIdValidationSuccess |env.useridvalidation.lloyds |
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellAccept          | P_CLASSIC      | Accept    | Accept   |true		   |200			   |UserIdValidationSuccess |env.useridvalidation.bos    |
    
    
    
    Scenario Outline: validate failure scenario if password contains personal details
     
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we have a userid validation service running on "<envHost>" for a brand
	When we try to get response from the API "<UserIdValidationRequest>" for "<availability>"
    And we should see the error code "<errorCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
	xamples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest                 | offeredProduct | eidvScore | asmScore |errorCode         |httpStatusCode |UserIdValidationRequest |envHost| 
      ###########################################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | OfferProductAcceptRequest    | P_ULTIMATE     | Accept    | Accept   |92000020		   |200			   |UserIdValidationPwdContainsCustomerDetails |env.useridvalidation.halifax|
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept         | P_CLASSIC      | Accept    | Accept   |92000020		   |200			   |UserIdValidationPwdContainsCustomerDetails |env.useridvalidation.lloyds |
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellAccept         | P_CLASSIC      | Accept    | Accept   |92000020		   |200			   |UserIdValidationPwdContainsCustomerDetails |env.useridvalidation.bos    |
     
    
    Scenario Outline: validate failure scenario if password does not follow required password rule
     
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we have a userid validation service running on "<envHost>" for a brand
	When we try to get response from the API "<UserIdValidationRequest>" for "<availability>"
    And we should see the error code "<errorCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
	xamples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest                 | offeredProduct | eidvScore | asmScore |errorCode         |httpStatusCode |UserIdValidationRequest |envHost| 
      ###########################################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | OfferProductAcceptRequest    | P_ULTIMATE     | Accept    | Accept   |92000021		   |200			   |UserIdValidationPwdNotSatisfyingRules |env.useridvalidation.halifax|
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept         | P_CLASSIC       | Accept    | Accept   |92000021		   |200			   |UserIdValidationPwdNotSatisfyingRules |env.useridvalidation.lloyds |
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellAccept         | P_CLASSIC     | Accept    | Accept   |92000021		   |200			   |UserIdValidationPwdNotSatisfyingRules |env.useridvalidation.bos    |
    
    
    
      Scenario Outline: validate failure scenario if password does not contains integer
     
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we have a userid validation service running on "<envHost>" for a brand
	When we try to get response from the API "<UserIdValidationRequest>" for "<availability>"
    And we should see the error code "<errorCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
	xamples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest                 | offeredProduct | eidvScore | asmScore |errorCode         |httpStatusCode |UserIdValidationRequest |envHost| 
      ###########################################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | OfferProductAcceptRequest    | P_ULTIMATE     | Accept    | Accept   |92000022		   |200			   |UserIdValidationNumberLessPwd |env.useridvalidation.halifax|
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept         | P_CLASSIC       | Accept    | Accept   |92000022		   |200			   |UserIdValidationNumberLessPwd |env.useridvalidation.lloyds |
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellAccept         | P_CLASSIC     | Accept    | Accept   |92000022		   |200			   |UserIdValidationNumberLessPwd |env.useridvalidation.bos    |
      
    
    Scenario Outline: validate failure scenario if username and password are same
     
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we have a userid validation service running on "<envHost>" for a brand
	When we try to get response from the API "<UserIdValidationRequest>" for "<availability>"
    And we should see the error code "<errorCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
	xamples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest                 | offeredProduct | eidvScore | asmScore |errorCode         |httpStatusCode |UserIdValidationRequest |envHost| 
      ###########################################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | OfferProductAcceptRequest    | P_ULTIMATE     | Accept    | Accept   |92000023		   |200			   |UserIdValidationSameUsernameNPwd |env.useridvalidation.halifax|
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept         | P_CLASSIC      | Accept    | Accept   |92000023		   |200			   |UserIdValidationSameUsernameNPwd |env.useridvalidation.lloyds |
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellAccept         | P_CLASSIC      | Accept    | Accept   |92000023		   |200			   |UserIdValidationSameUsernameNPwd |env.useridvalidation.bos    |
     
     Scenario Outline: validate failure scenario if password is blacklisted
     
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we have a userid validation service running on "<envHost>" for a brand
	When we try to get response from the API "<UserIdValidationRequest>" for "<availability>"
    And we should see the error code "<errorCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
	xamples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest                 | offeredProduct | eidvScore | asmScore |errorCode         |httpStatusCode |UserIdValidationRequest |envHost| 
      ###########################################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | OfferProductAcceptRequest    | P_ULTIMATE     | Accept    | Accept   |92000024		   |200			   |UserIdValidationBlacklistedPwd |env.useridvalidation.halifax|
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept         | P_CLASSIC       | Accept    | Accept   |92000024		   |200			   |UserIdValidationBlacklistedPwd |env.useridvalidation.lloyds |
    
    
    Scenario Outline: validate failure scenario if password does not contains alphabets
     
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    And we have a userid validation service running on "<envHost>" for a brand
	When we try to get response from the API "<UserIdValidationRequest>" for "<availability>"
    And we should see the error code "<errorCode>"
    And we should see http statuscode as "<httpStatusCode>"


	Examples:
	xamples: 
      | retrieveEndpoint     | productRequest          | defaultProduct | offerEndpoint                  | offerRequest                 | offeredProduct | eidvScore | asmScore |errorCode         |httpStatusCode |UserIdValidationRequest |envHost| 
      ###########################################################################################################################################################################################################################################
      | env.retrieve.halifax | urcaccountHLX           | P_ULTIMATE     | env.productarrangement.halifax | OfferProductAcceptRequest    | P_ULTIMATE     | Accept    | Accept   |92000025		   |200			   |UserIdValidationAlphaLessPwd |env.useridvalidation.halifax|
      | env.retrieve.lloyds  | classicaccountLTB       | P_CLASSIC      | env.productarrangement.lloyds  | LydNonDownsellAccept         | P_CLASSIC       | Accept    | Accept   |92000025		   |200			   |UserIdValidationAlphaLessPwd |env.useridvalidation.lloyds |
      | env.retrieve.bos     | classicaccountBOS       | P_CLASSIC      | env.productarrangement.bos     | BosNonDownsellAccept         | P_CLASSIC     | Accept    | Accept   |92000025		   |200			   |UserIdValidationAlphaLessPwd |env.useridvalidation.bos    |
      