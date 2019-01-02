#############################################################
##API Acceptance Tests for PCA-687 (Activate Product Features)
#############################################################
@api @regression @pca-687
Feature: Modify Activate Product Service

  Scenario Outline: Validate the response from Arrangement
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<productRequest>"
    Then validate the response of retrieve service "<defaultProduct>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then validate the response of offer service "<offeredProduct>" "<eidvScore>" "<asmScore>"
    Then fetch product value for from Offer Response "ocisId"
    Then fetch product value for from Offer Response "partyId"
    Then fetch product value for from Offer Request "title"
    Then fetch product value for from Offer Request "firstName"
    Then fetch product value for from Offer Request "middleName"
    Then fetch product value for from Offer Request "lastName"
    Then fetch product value for from Offer Request "email"
    Then fetch product value for from Offer Request "dob"
    Then fetch product value for from Offer Request "marketPrefPhone"
    Then fetch product value for from Offer Request "marketPrefEmail"
    Then fetch product value for from Offer Request "marketPrefText"
    Then fetch product value for from Offer Request "marketPrefPost"
    #Then fetch product value for from Offer Request "addressLine1"
    #Then fetch product value for from Offer Request "addressLine2"
    #Then fetch product value for from Offer Request "addressLine3"
    #Then fetch product value for from Offer Request "addressLine4"
    #Then fetch product value for from Offer Request "postcode"
    Then fetch product value for from Offer Request "countryCode"
    Then fetch product value for from Offer Request "number"
    #When we try to get response from the API "<JsonFileName>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"
	
	Then Generate the Saml Asssertion "<samlAssertion>" "<samlRequest>" "<brand>"
	
	Given Perform the branch context setting operation endpoint "<branchContextSetEndpoint>" with colleagueId "<colleagueId>" and domain "<domain>"
    Then Perform the branchContext operation using enpoint and Request "<branchContextEndpoint>" "<branchContextRequest>"
    Then verify colleagueId equals "<colleagueId>"
    Then verify authorized equals "<authorized>"
    
	And I Post the SAML token to Token Validate API as "<validateTokenHost>"
    And I should see http statuscode same as "<httpStatusCode>"
    And I should see request message with "<key>" having value "<value>"
    And I Get the response from userInfo from API "<userInfoUrl>"
    Then I should see response with following user details
      | title           |
      | firstName       |
      | lastName        |
      | dob             |
      | email           |
      | applicantType   |
      | occupnType      |
      | gender          |
      | nationalities   |
      | birthCountry    |
      | birthCity       |
      | currentAddress  |
	Given Product retrieve service is up and running for product with session "<retrieveEndpoint>" "<newProductRequest>"
    Then validate the response of retrieve service "<newDefaultProduct>"
    And we send a request for offer service using auth journey "<offerEndpoint>" "<authOfferRequest>"
    #And we send a request for offer service "<offerEndpoint>" "<authOfferRequest>"
    
    Then validate the response of offer service "<newOfferedProduct>" "<authEidvScore>" "<authAsmScore>"
    Then validate the ocisid of offer service
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<authapplicationStatus>"    	      
      
   Examples:   
   | No| branchContextSetEndpoint  		   | branchContextEndpoint     		   | branchContextRequest     | colleagueId   | authorized | domain 	| retrieveEndpoint     | offerEndpoint                  | urlIdentifier     |httpStatusCode  | offeredProduct| offerRequest      		 | eidvScore  | asmScore | activateEndpoint     | activateRequest   | applicationStatus  | samlRequest   | samlAssertion 		| defaultProduct | validateTokenHost           | key 		| value   | userInfoUrl          | brand  |  newProductRequest | newDefaultProduct | authOfferRequest            | productRequest          | newOfferedProduct	|	authEidvScore | authAsmScore  | authapplicationStatus |authactivateRequest |
     
	 ################################################ HALIFAX URCA Accept AND ABP ###########################################################################################
   |1 | halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX     | 200            | P_REWARD     | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_REWARD      | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | urcaccountHLX      | P_ULTIMATE       | AuthHlxNonDownsellAccept | rewardcurrentaccountHLX | P_ULTIMATE         | ACCEPT          | ACCEPT        | 1010                  |ActivateRequest     |
   |2 | halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX    	| 200            | P_REWARD     | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_REWARD      | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | urcaccountHLX      | P_ULTIMATE       | AuthHlxNonDownsellProcessed | rewardcurrentaccountHLX | P_ULTIMATE         | ACCEPT          | REFER         | 1008                  |ActivateRequest     |

   ################################################ HALIFAX URCA DOWNSELL BASIC Accept AND ABP ###########################################################################################
   |5 | halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | standardcurrentaccountHLX | 200    | P_STANDARD   | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_STANDARD    | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | urcaccountHLX      | P_ULTIMATE       | HlxNonDownsellAccept        | rewardcurrentaccountHLX | P_BASIC            | ACCEPT          | ACCEPT        | 1010                  |ActivateRequest     |
   |6 | halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | standardcurrentaccountHLX | 200    | P_STANDARD   | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_STANDARD    | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | urcaccountHLX      | P_ULTIMATE       | HlxNonDownsellAccept        | rewardcurrentaccountHLX | P_BASIC            | ACCEPT          | REFER         | 1008                  |ActivateRequest     |

	 ################################################ HALIFAX RCA Accept AND ABP ###########################################################################################
   |7 | halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | standardcurrentaccountHLX | 200    | P_STANDARD     | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_STANDARD    | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | rewardcurrentaccountHLX | P_REWARD    | AuthHlxNonDownsellProcessed | rewardcurrentaccountHLX | P_REWARD          | ACCEPT          | ACCEPT        | 1010                  |ActivateRequest     |
   |8 | halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | standardcurrentaccountHLX | 200    | P_STANDARD     | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_STANDARD    | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | rewardcurrentaccountHLX | P_REWARD    | AuthHlxNonDownsellProcessed | rewardcurrentaccountHLX | P_REWARD          | ACCEPT          | REFER         | 1008                  |ActivateRequest     |
   
   ################################################ HALIFAX RCA DOWNSELL BASIC Accept AND ABP ###########################################################################################
   |9 | halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | standardcurrentaccountHLX | 200    | P_STANDARD   | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_STANDARD    | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | rewardcurrentaccountHLX | P_REWARD    | HlxNonDownsellAccept        | rewardcurrentaccountHLX | P_BASIC            | ACCEPT          | ACCEPT        | 1010                  |ActivateRequest     |
   |10| halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | standardcurrentaccountHLX | 200    | P_STANDARD   | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_STANDARD    | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | rewardcurrentaccountHLX | P_REWARD    | HlxNonDownsellAccept        | rewardcurrentaccountHLX | P_BASIC            | ACCEPT          | REFER         | 1008                  |ActivateRequest     |

   ################################################ HALIFAX STUDENT REFER AND ABP ###########################################################################################
   |11| halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX     | 200            | P_ULTIMATE   | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_ULTIMATE   | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | studcurrentaccountHLX | P_STUDENT      | HlxNonDownsellAccept        | rewardcurrentaccountHLX | P_STUDENT          | REFER          | ACCEPT        | 1010                  |ActivateRequest     |
   |12| halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX    	| 200            | P_ULTIMATE   | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_ULTIMATE   | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | studcurrentaccountHLX | P_STUDENT      | HlxNonDownsellAccept        | rewardcurrentaccountHLX | P_STUDENT          | REFER          | REFER         | 1008                  |ActivateRequest     |
   
   ################################################ HALIFAX STUDENT DOWNSELL BASIC REFER AND ABP ###########################################################################################
   |13| halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX     | 200            | P_ULTIMATE   | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_ULTIMATE   | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | studcurrentaccountHLX | P_STUDENT      | HlxNonDownsellAccept        | rewardcurrentaccountHLX | P_BASIC            | REFER           | ACCEPT        | 1010                  |ActivateRequest     |
   |14| halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX    	| 200            | P_ULTIMATE   | HalifaxrewardcurrentAccept | Accept     | Accept   | env.activate.halifax | ActivateRequest   |1010			     | SamlRequest	  | env.samlAssertionGen  | P_ULTIMATE   | env.validateToken.halifax  | validated  | true    | env.userInfo.halifax | HALIFAX | studcurrentaccountHLX | P_STUDENT      | HlxNonDownsellAccept        | rewardcurrentaccountHLX | P_BASIC            | REFER           | REFER         | 1008                  |ActivateRequest     |


	################################################ LLOYDS CLUB  ACCOUNT ###########################################################################################
   |21| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB  | 200 			 | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | Accept    | env.activate.lloyds | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | clubaccountLTB         | P_CLUB        | AuthLLoydsclassicAccept 	 | clubplatinumaccountLTB  | P_CLUB             | ACCEPT         | ACCEPT        | 1010                  |    ActivateRequest  |
   |22| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB  | 200           | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | ACCEPT    | env.activate.lloyds  | ActivateRequest  |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | clubaccountLTB         | P_CLUB        | AuthLydNonDownsellProcessed | clubplatinumaccountLTB  | P_CLUB             | ACCEPT         | REFER         | 1008                 |    ActivateRequest  |

	################################################ LLOYDS CLUB  DOWNSELL CLASSIC ACCOUNT ###########################################################################################
   |23| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB  | 200 			 | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | Accept    | env.activate.lloyds | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | clubaccountLTB         | P_CLUB        | AuthLLoydsclassicAccept 	 | clubplatinumaccountLTB  | P_CLASSIC         | ACCEPT         | ACCEPT        | 1010                  |    ActivateRequest  |
   |24| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB  | 200           | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | ACCEPT    | env.activate.lloyds  | ActivateRequest  |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | clubaccountLTB         | P_CLUB        | AuthLydNonDownsellProcessed | clubplatinumaccountLTB  | P_CLASSIC         | ACCEPT         | REFER         | 1008                 |    ActivateRequest  |
   
	################################################ LLOYDS CLUB  DOWNSELL BASIC ACCOUNT ###########################################################################################
   |25| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB  | 200 			 | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | Accept    | env.activate.lloyds | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | clubaccountLTB         | P_CLUB        | AuthLLoydsclassicAccept 	 | clubplatinumaccountLTB  | P_BASIC           | ACCEPT         | ACCEPT        | 1010                  |    ActivateRequest  |
   |26| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB  | 200           | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | ACCEPT    | env.activate.lloyds  | ActivateRequest  |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | clubaccountLTB         | P_CLUB        | AuthLydNonDownsellProcessed | clubplatinumaccountLTB  | P_BASIC           | ACCEPT         | REFER         | 1008                 |    ActivateRequest  |



	################################################ LLOYDS CLASSIC  ACCOUNT ###########################################################################################
   |33| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | platinumaccountLTB | 200 			 | P_PLAT        | AuthJourneylloydsClassicAccept | Accept    | Accept    | env.activate.lloyds | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_PLAT       | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | platinumaccountLTB     | P_PLAT        | AuthLLoydsclassicAccept 	 | platinumaccountLTB      | P_CLUB             | ACCEPT         | ACCEPT        | 1010                  |    ActivateRequest  |
   |34| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | platinumaccountLTB | 200           | P_PLAT        | AuthJourneylloydsClassicAccept | Accept    | ACCEPT    | env.activate.lloyds  | ActivateRequest  |1010			 |SamlRequest	  | env.samlAssertionGen  | P_PLAT       | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | platinumaccountLTB     | P_PLAT        | AuthLydNonDownsellProcessed | platinumaccountLTB      | P_CLUB             | ACCEPT         | REFER         | 1008                 |    ActivateRequest  |

  
	################################################ LLOYDS CLASSIC  DOWNSELL BASIC ACCOUNT ###########################################################################################
   |35| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | platinumaccountLTB | 200 			 | P_PLAT        | AuthJourneylloydsClassicAccept | Accept    | Accept    | env.activate.lloyds | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_PLAT       | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | platinumaccountLTB     | P_PLAT        | AuthLLoydsclassicAccept 	 | platinumaccountLTB      | P_BASIC           | ACCEPT         | ACCEPT        | 1010                  |    ActivateRequest  |
   |36| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | platinumaccountLTB | 200           | P_PLAT        | AuthJourneylloydsClassicAccept | Accept    | ACCEPT    | env.activate.lloyds  | ActivateRequest  |1010			 |SamlRequest	  | env.samlAssertionGen  | P_PLAT       | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | platinumaccountLTB     | P_PLAT        | AuthLydNonDownsellProcessed | platinumaccountLTB      | P_BASIC           | ACCEPT         | REFER         | 1008                 |    ActivateRequest  |


	################################################ LLOYDS STUDENT  ACCOUNT ###########################################################################################
   |37| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | studentaccountLTB  | 200 			 | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | Accept    | env.activate.lloyds | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | studentaccountLTB     | P_STUDENT     | AuthLLoydsclassicAccept 	 | studentaccountLTB      | P_STUDENT          | REFER         | ACCEPT        | 1010                  |    ActivateRequest  |
   |38| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | studentaccountLTB  | 200           | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | ACCEPT    | env.activate.lloyds  | ActivateRequest  |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | studentaccountLTB     | P_STUDENT     | AuthLydNonDownsellProcessed  | studentaccountLTB      | P_STUDENT          | REFER         | REFER         | 1008                 |    ActivateRequest  |

	################################################ LLOYDS STUDENT  DOWNSELL CLASSIC ACCOUNT ###########################################################################################
   |39| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | studentaccountLTB  | 200 			 | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | Accept    | env.activate.lloyds | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | studentaccountLTB     | P_STUDENT     | AuthLLoydsclassicAccept 	 | studentaccountLTB      | P_CLASSIC         | REFER         | ACCEPT        | 1010                  |    ActivateRequest  |
   |40| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | studentaccountLTB  | 200           | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | ACCEPT    | env.activate.lloyds  | ActivateRequest  |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | studentaccountLTB     | P_STUDENT     | AuthLydNonDownsellProcessed  | studentaccountLTB      | P_CLASSIC         | REFER         | REFER         | 1008                 |    ActivateRequest  |
   
	################################################ LLOYDS STUDENT  DOWNSELL BASIC ACCOUNT ###########################################################################################
   |41| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | studentaccountLTB  | 200 			 | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | Accept    | env.activate.lloyds | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | studentaccountLTB     | P_STUDENT     | AuthLLoydsclassicAccept 	 | studentaccountLTB      | P_BASIC           | REFER          | ACCEPT        | 1010                  |    ActivateRequest  |
   |42| lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | studentaccountLTB  | 200           | P_CLASSIC      | AuthJourneylloydsClassicAccept | Accept    | ACCEPT    | env.activate.lloyds  | ActivateRequest  |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.lloyds   | validated   | true    | env.userInfo.lloyds  | LLOYDS | studentaccountLTB     | P_STUDENT     | AuthLydNonDownsellProcessed  | studentaccountLTB      | P_BASIC           | REFER          | REFER         | 1008                 |    ActivateRequest  |



  ################################################ BOS CLASSIC WITHOUT VANTAGE ###########################################################################################
   |43| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | classicaccountBOS  | 200              | P_CLASSIC      | BosNonDownsellAccept           | Accept    | Accept    | env.activate.bos     | ActivateRequest  |1010			| SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | classicaccountBOS      | P_CLASSIC    | AuthBOSClassicAccept          | classicaccountBOS     | P_CLASSIC         | ACCEPT         | ACCEPT      | 1010                 |ActivateRequest |
   |44| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | classicaccountBOS  | 200              | P_CLASSIC      | BosNonDownsellAccept           | Accept    | Accept    | env.activate.bos     | ActivateRequest  |1010          |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | classicaccountBOS      | P_CLASSIC    | AuthBosNonDownsellProcessed   | classicaccountBOS     | P_CLASSIC         | ACCEPT         | REFER       | 1008                 |ActivateRequest |

  ################################################ BOS CLASSIC WITH VANTAGE ###########################################################################################
   |45| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | classicaccountBOS  | 200              | P_CLASSIC      | BosNonDownsellAccept           | Accept    | Accept    | env.activate.bos     | ActivateRequest  |1010			| SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | classicaccountBOS      | P_CLASSIC    | AuthBOSClassicAccept          | classicaccountBOS     | P_CLASSIC         | ACCEPT         | ACCEPT      | 1010                 |ActivateRequest |
   |46| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | classicaccountBOS  | 200              | P_CLASSIC      | BosNonDownsellAccept           | Accept    | Accept    | env.activate.bos     | ActivateRequest  |1010          |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | classicaccountBOS      | P_CLASSIC    | AuthBosNonDownsellProcessed   | classicaccountBOS     | P_CLASSIC         | ACCEPT         | REFER       | 1008                 |ActivateRequest |


  ################################################ BOS CLASSIC WITHOUT VANTAGE DOWNSELL TO BASIC ###########################################################################################
   |47| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | classicaccountBOS  | 200              | P_CLASSIC      | BosNonDownsellAccept           | Accept    | Accept    | env.activate.bos     | ActivateRequest  |1010			| SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | classicaccountBOS      | P_CLASSIC    | AuthBOSClassicAccept          | classicaccountBOS     | P_BASIC           | ACCEPT         | ACCEPT      | 1010                 |ActivateRequest |
   |48| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | classicaccountBOS  | 200              | P_CLASSIC      | BosNonDownsellAccept           | Accept    | Accept    | env.activate.bos     | ActivateRequest  |1010          |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | classicaccountBOS      | P_CLASSIC    | AuthBosNonDownsellProcessed   | classicaccountBOS     | P_BASIC           | ACCEPT         | REFER       | 1008                 |ActivateRequest |

  ################################################ BOS CLASSIC WITH VANTAGE DOWNSELL TO BASIC ###########################################################################################
   |49| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | classicaccountBOS  | 200              | P_CLASSIC      | BosNonDownsellAccept           | Accept    | Accept    | env.activate.bos     | ActivateRequest  |1010			| SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | classicaccountBOS      | P_CLASSIC    | AuthBOSClassicAccept          | classicaccountBOS     | P_BASIC           | ACCEPT         | ACCEPT      | 1010                 |ActivateRequest |
   |50| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | classicaccountBOS  | 200              | P_CLASSIC      | BosNonDownsellAccept           | Accept    | Accept    | env.activate.bos     | ActivateRequest  |1010          |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | classicaccountBOS      | P_CLASSIC    | AuthBosNonDownsellProcessed   | classicaccountBOS     | P_BASIC           | ACCEPT         | REFER       | 1008                 |ActivateRequest |


	################################################ BOS STUDENT  ACCOUNT ###########################################################################################
   |63| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	| CT067484      | true       | GLOBAL 		| env.retrieve.bos  	   | env.productarrangement.bos  | studentaccountBOS  | 200 			 | P_CLASSIC      | AuthJourneybosClassicAccept    | Accept    | Accept    | env.activate.bos     | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | studentaccountBOS       | P_STUDENT   | AuthBosclassicAccept 	      | studentaccountBOS      | P_STUDENT        | REFER          | ACCEPT      | 1010                 |ActivateRequest  |
   |64| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	| CT067484      | true       | GLOBAL 		| env.retrieve.bos 		   | env.productarrangement.bos  | studentaccountBOS  | 200              | P_CLASSIC      | AuthJourneybosClassicAccept    | Accept    | ACCEPT    | env.activate.bos     | ActivateRequest   |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | studentaccountBOS       | P_STUDENT   | AuthBosNonDownsellProcessed   | studentaccountBOS      | P_STUDENT        | REFER          | REFER       | 1008                 |ActivateRequest  |

	################################################ BOS STUDENT  DOWNSELL CLASSIC ACCOUNT ###########################################################################################
   |65| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos  	   | env.productarrangement.bos  | studentaccountBOS  | 200 			 | P_CLASSIC      | AuthJourneybosClassicAccept    | Accept    | Accept    | env.activate.bos     | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | studentaccountBOS       | P_STUDENT   | AuthBosclassicAccept 	      | studentaccountBOS      | P_CLASSIC        | REFER          | ACCEPT      | 1010                 |ActivateRequest  |
   |66| bosBranchContextSetEndpoint    | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | studentaccountBOS  | 200              | P_CLASSIC      | AuthJourneybosClassicAccept    | Accept    | ACCEPT    | env.activate.bos     | ActivateRequest   |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | studentaccountBOS       | P_STUDENT   | AuthBosNonDownsellProcessed   | studentaccountBOS      | P_CLASSIC        | REFER          | REFER       | 1008                 |ActivateRequest  |
   
	################################################ BOS STUDENT  DOWNSELL BASIC ACCOUNT ###########################################################################################
   |67| bosBranchContextSetEndpoint    | bosBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | studentaccountBOS  | 200 			 | P_CLASSIC      | AuthJourneybosClassicAccept    | Accept    | Accept    | env.activate.bos     | ActivateRequest   |1010			 | SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | studentaccountBOS       | P_STUDENT   | AuthBosclassicAccept 	      | studentaccountBOS      | P_BASIC          | REFER          | ACCEPT      | 1010                 |ActivateRequest  |
   |68| bosBranchContextSetEndpoint    | bosBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos         | env.productarrangement.bos  | studentaccountBOS  | 200              | P_CLASSIC      | AuthJourneybosClassicAccept    | Accept    | ACCEPT    | env.activate.bos     | ActivateRequest   |1010			 |SamlRequest	  | env.samlAssertionGen  | P_CLASSIC    | env.validateToken.bos      | validated   | true    | env.userInfo.bos     | BOS    | studentaccountBOS       | P_STUDENT   | AuthBosNonDownsellProcessed   | studentaccountBOS      | P_BASIC          | REFER          | REFER       | 1008                 |ActivateRequest  |


   