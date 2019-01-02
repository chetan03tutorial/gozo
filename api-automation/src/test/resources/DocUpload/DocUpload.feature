#############################################################
##API Acceptance Tests for PCA-1579(Doc Upload API)
#############################################################
@api @regression @pca-1579
Feature: Validate the response of Create Case API and Document Upload API


	@pca-1579-1
	Scenario Outline: Validate response success response of Create Case API
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
 #   And validate the "<messageType>" response of Create Case Service for messageType

  
  Examples:     
     | createcaseEndpoint   | docUploadRequest			  | success |  failFlag |
     | env.createCase.lloyds| successCreateCaseRequest    |  true   |  false    |


	@pca-1579-2    
   Scenario Outline: Validate response success response of Create Case API
   
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
    And validate the "<code>" response of code in error section
    And validate the "<message>" response of Create Case Service for message
    And validate the "<messageType>" response of Create Case Service for messageType
    
    And validate the "<status>" response of status in error section
    
       
    Examples:     
    | createcaseEndpoint   | docUploadRequest          | success | status| code        | message                              | messageType | failFlag |
    |env.createCase.lloyds | successCreateCaseRequest  | false   | 200   | 600140      | EXTERNAL_SERVICE_UNAVAILABLE         | Failure     | true     |


  	@pca-1579-3
   Scenario Outline: Validate response of Create Case API when the request is made by removing the non mandatory party details parameters
   
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
    And validate the "<messageType>" response of Create Case Service for messageType
      
    Examples:     
      | createcaseEndpoint 	   | docUploadRequest  | success | messageType  | failFlag |
      | env.createCase.lloyds  | removeNonMandatory| true    |  Success     |  false   |

	@pca-1579-4     
    Scenario Outline: Validate response of Create Case API when the request is made by passing incorrect values for non mandatory parameters
   
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
    And validate the "<message>" response of Create Case Service for message
    And validate the "<messageType>" response of Create Case Service for messageType
    And validate the "<status>" response of status in error section
    And validate the "<code>" response of code in error section
   
     
    Examples:     
      | createcaseEndpoint   | docUploadRequest     | success | status| code  | message               | messageType |failFlag |
      | env.createCase.lloyds | invalidNonMandatory  | false   | 400   | 600036| INVALID_PARTY_DETAILS| Failure     | true    |
      
@pca-1579-5   
   Scenario Outline:  Validate response of Create Case API when the request is made by removing mandatory parameters
   
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
    And validate the "<message>" response of Create Case Service for message
    And validate the "<messageType>" response of Create Case Service for messageType
    And validate the "<status>" response of status in error section
    And validate the "<code>" response of code in error section
   
    
     
    Examples:     
      | createcaseEndpoint   | docUploadRequest  | success | status| code  | message              | messageType   |failFlag |
      |env.createCase.lloyds   | removeMandatory   | false   | 400   | 600036| INVALID_PARTY_DETAILS| Failure     | true    |  

      
   Scenario Outline:  Validate response of Create Case API when the request is made by passing invalid upload details
   
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
    And validate the "<message>" response of Create Case Service for message
    And validate the "<messageType>" response of Create Case Service for messageType
    And validate the "<status>" response of status in error section
    And validate the "<code>" response of code in error section
   
    
     
      Examples:     
      | createcaseEndpoint   | docUploadRequest      | success | status| code  | message               | messageType  |failFlag |
      | env.createCase.lloyds   |invalidPartyDetails  | false   | 400   | 600052| INVALID_UPLOAD_DETAILS| Failure     | true    | 
      
      
  Scenario Outline:  Validate response of Create Case API when the request is made by passing invalid case PSF details
   
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
    And validate the "<message>" response of Create Case Service for message
    And validate the "<messageType>" response of Create Case Service for messageType
    And validate the "<status>" response of status in error section
    And validate the "<code>" response of code in error section
   
    
     
      Examples:     
      | createcaseEndpoint   | docUploadRequest     | success | status| code  | message                | messageType  |failFlag |
     |env.createCase.lloyds   | invalidPSFDetails    | false   | 400   | 600051| INVALID_CASEPSF_DETAILS| Failure     | true    |    

     
Scenario Outline:  VValidate response of Create Case API when the request is made by passing blank Process code
   
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
    And validate the "<message>" response of Create Case Service for message
    And validate the "<messageType>" response of Create Case Service for messageType
    And validate the "<status>" response of status in error section
    And validate the "<code>" response of code in error section
   
    
     
      Examples:     
     | createcaseEndpoint     | docUploadRequest     | success | status| code        | message                     | messageType | failFlag |
     | env.createCase.lloyds  | blankProcessCode     | false   | 200   |  600140     | EXTERNAL_SERVICE_UNAVAILABLE| Failure     | true     |  
      


  