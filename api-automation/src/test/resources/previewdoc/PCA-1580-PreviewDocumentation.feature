@PCA-1580 @api @p1 @mocks

Feature: Fetch document for a filenet reference id using PreviewDoc API 

#############################################
#####1
#############################################

  Scenario Outline: Retrieve document using valid filenet reference
	 Given I make a call to the Preview Document API "<previewdoc-endpoint>" "<fileNetReferenceId>" "<success>"
	 Then I should see the valid document response "<httpStatusCode>"

    Examples:
     | previewdoc-endpoint | httpStatusCode | fileNetReferenceId                     |success|
     | env.previewdoc      |     200        | A3C98E2E-F4E8-40A6-8A7F-AECC49F50A68	 |yes    |     

#############################################
#####4
#############################################

  Scenario Outline: Retrieve document using invalid filenet reference
	 Given I make a call to the Preview Document API "<previewdoc-endpoint>" "<fileNetReferenceId>" "<success>"
	 Then I should not see the valid document response "<httpStatusCode>"

    Examples: 
      | previewdoc-endpoint	| fileNetReferenceId                            | errorcode     | httpStatusCode  |errormsg                     |success|
      | env.previewdoc      | A3C98E2E-F4E8-40A6-8A7F-AECC49F50A69          | "600300"      | 200             | "FAILED_TO_RETRIEVE_FILE"   |no     |
     