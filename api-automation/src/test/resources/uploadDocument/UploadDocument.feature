 #############################################################
##API Acceptance Tests for PCA-1579(Doc Upload API)
#############################################################
@api @regression @pca-1579
Feature: Validate the response of Create Case API and Document Upload APIScenario Outline: Validate that the user is successfully able to upload the file
    
     Scenario Outline: Validate that the user is successfully able to upload the file
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then validate the "<success>" response of Create Case Service for Success
    Then Update "<docUploadRequest>"
    Then we send a request for Document Upload service "<DocumentCaptureEndpoint>" with request "<DocumentJson>"
    Then validate the "<DocumentSuccess>" response of Document Upload
  
  	Examples:     
     | createcaseEndpoint   | docUploadRequest			  | DocumentCaptureEndpoint    | DocumentJson        |Success|DocumentSuccess| failFlag |
     | env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |
     
     
    Scenario Outline: Validate that the user is not successfully able to upload as the file is with invalid Format
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    Then  validate the "<success>" response of Create Case Service for Success
    Then  we send a request for Document Upload service "<DocumentCaptureEndpoint>" with request "<DocumentJson>"
    Then  validate the "<DocumentSuccess>" response of Document Upload
  
     Examples:     
     | createcaseEndpoint   | docUploadRequest			 | DocumentCaptureEndpoint    | DocumentJson              |Success|DocumentSuccess| failFlag |
     | env.createCase.lloyds| successCreateCaseRequest   |  env.DocumentCapture.lloyds|InvalidContentTypeUploadDoc| true  |false          | false    |  
     
     
         
    Scenario Outline: Validate that the user is not successfully able to upload as the file is with Large File size.
    Given we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>"
    Then  Update "<docUploadRequest>"
    Then  we send a request for Document Upload service "<DocumentCaptureEndpoint>" with request "<DocumentJson>"
    Then  validate the "<DocumentSuccess>" response of Document Upload
  
     Examples:     
     | createcaseEndpoint   | docUploadRequest			 | DocumentCaptureEndpoint    | DocumentJson           |Success|DocumentSuccess| failFlag |
     | env.createCase.lloyds| successCreateCaseRequest   |  env.DocumentCapture.lloyds|LargeFileDocumentCapture| true  |false          |  false   |
