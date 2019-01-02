#############################################################
##API Acceptance Tests for PCA-687 (Activate Product Features)
#############################################################
@api @regression @pca-687
Feature: Modify Activate Product Service

  Scenario Outline: Validate the user sms mandate
  Given the saml token is generated "<brand>"
  Then verify the user session with valid session Id
  Then request the user sms mandate "<brand>"
  Then verify the user mandate
  
  Examples:
  |brand|
  |lloyds|
   








      
      
      
      
      
      
      