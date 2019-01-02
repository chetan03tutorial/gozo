#############################################################
##API Acceptance Tests for PCA-12 (RecordArrangementQuestionnaire)
#############################################################
@API @REGRESSION @PCA-162
Feature: ApplicationVersion

  #################################################################################################

  Scenario Outline: Validate the response for Application Version
    Given Perform the application version operation using enpoint "<applicationVersionEndpoint>"
    Then verify ApplicationName equals "<applicationName>"
    Then verify ActiveXFlag equals "<activeXFlag>"

    Examples: 
      | applicationVersionEndpoint     		 | applicationName       | activeXFlag |
      | halifaxApplicationVersionEndpoint    | PCA-SALES      	     | false       |


