#############################################################
##API Acceptance Tests for PCA-12 (RecordArrangementQuestionnaire)
#############################################################
@API @REGRESSION @PCA-162
Feature: BranchContext without Email

  #################################################################################################

  Scenario Outline: (Student All) Validate the response from RetreiveProductArrangement after Branch Context
    Given Perform the branch context setting operation endpoint "<branchContextSetEndpoint>" with colleagueId "<colleagueId>" and domain "<domain>"
    Then Perform the branchContext operation using enpoint and Request "<branchContextEndpoint>" "<branchContextRequest>"
    Then verify colleagueId equals "<colleagueId>"
    Then verify authorized equals "<authorized>"
    
    Given product retrieve service is up and running "<retrieveEndpoint>" "<urlIdentifier>"
    Then verify product mnemonic "<mnemonic>"
    Then verify service status "<httpStatusCode>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then verify product mnemonic of offer "<offeredProduct>"    
    Then verify eidv score "<eidvScore>"
    Then verify asm score "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"

    Examples: 
   |  branchContextSetEndpoint  		  | branchContextEndpoint     		  | branchContextRequest     | colleagueId   | authorized | domain 		| retrieveEndpoint     | offerEndpoint                  | urlIdentifier           |httpStatusCode| mnemonic   | offeredProduct | offerRequest                | eidvScore | asmScore | activateEndpoint     | activateRequest |applicationStatus |
      ################################################ LLOYDS REFER JOURNEY ###########################################################################################
  # |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_STUDENT      | LydStudentAcceptNoEmail	 | REFER     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS REFER JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_STUDENT      | BosStudentAcceptNoEmail	 | REFER     | Accept   | env.activate.bos  | ActivateRequest |1007				 |
      ################################################ HALIFAX REFER JOURNEY ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_STUDENT      | HalifaxStudentAcceptNoEmail | REFER     | Accept   | env.activate.halifax  | ActivateRequest |1007				 |
      
      ################################################ LLOYDS APPLICATION BEING PROCESS JOURNEY ########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_STUDENT      | LydStudentAppBeingProcessNoEmail | REFER     | REFER   | env.activate.lloyds  | ActivateRequest |1008				 |
      ################################################ BOS APPLICATION BEING PROCESS JOURNEY ########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_STUDENT      | BosStudentAppBeingProcessNoEmail   | REFER     | REFER   | env.activate.bos  | ActivateRequest |1008				 |
      ################################################ HALIFAX APPLICATION BEING PROCESS JOURNEY ########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_STUDENT      | HalifaxStudentAppBeingProcessNoEmail| REFER     | REFER   | env.activate.halifax  | ActivateRequest |1008				 |

      ################################################ LLOYDS DOWNSELL CLASSIC JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_CLASSIC      | LydStudentDownsellClassicNoEmail | REFER     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS DOWNSELL CLASSIC JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_CLASSIC      | BosStudentDownsellClassicNoEmail | REFER     | Accept   | env.activate.bos  | ActivateRequest |1007				 |
      ################################################ HALIFAX DOWNSELL CLASSIC JOURNEY ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_STANDARD      | HalifaxStudentDownsellClassicNoEmail| REFER    | Accept   | env.activate.halifax  | ActivateRequest |1007				 |

      ################################################ LLOYDS DOWNSELL BASIC JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_NEW_BASIC    | LydStudentDownsellBasicNoEmail | REFER     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS DOWNSELL BASIC JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_NEW_BASIC    | BosStudentDownsellBasicNoEmail | REFER     | Accept   | env.activate.bos  | ActivateRequest |1007				 |
      ################################################ HALIFAX DOWNSELL BASIC JOURNEY ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_NEW_BASIC    | HalifaxStudentDownsellBasicNoEmail | REFER     | Accept   | env.activate.halifax  | ActivateRequest |1007				 |
    
      ################################################ LLOYDS DOWNSELL YOUTH JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_UNDER19      | LydStudentDownsellYouthNoEmail	 | REFER     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS DOWNSELL YOUTH JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_UNDER19      | BosStudentDownsellYouthNoEmail  | REFER     | Accept   | env.activate.bos  | ActivateRequest |1007				 |

      ################################################ LLOYDS NON REFER ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19          | LydYouthAcceptNoEmail   		 | REFER    | Accept   | env.activate.bos  | ActivateRequest |1007				 |

    ################################################ BOS  NORMAL ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos     |under19accountBOS       |        200 | P_UNDER19  | P_UNDER19          | BosYouthAcceptNoEmail    		 | REFER    | Accept   | env.activate.bos  | ActivateRequest |1007				 |

      ################################################ Halifax  Accept ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax     | expresscashHLX     |        200 | P_EXPRESSCASH  | P_EXPRESSCASH      | HalifaxYouthAcceptNoEmail	 | REFER    | Accept   | env.activate.bos  | ActivateRequest |1007				 |


  Scenario Outline: (URCA RCA and Classic) Validate the response from RetreiveProductArrangement after Branch Context 
    Given Perform the branch context setting operation endpoint "<branchContextSetEndpoint>" with colleagueId "<colleagueId>" and domain "<domain>"
    Then Perform the branchContext operation using enpoint and Request "<branchContextEndpoint>" "<branchContextRequest>"
    Then verify colleagueId equals "<colleagueId>"
    Then verify authorized equals "<authorized>"
    
    Given product retrieve service is up and running "<retrieveEndpoint>" "<urlIdentifier>"
    Then verify product mnemonic "<mnemonic>"
    Then verify service status "<httpStatusCode>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then verify product mnemonic of offer "<offeredProduct>"    
    Then verify eidv score "<eidvScore>"
    Then verify asm score "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"

    Examples: 
      |  branchContextSetEndpoint  		  | branchContextEndpoint     		  | branchContextRequest     | colleagueId   | authorized | domain 		| retrieveEndpoint     | offerEndpoint                      | urlIdentifier           |httpStatusCode| mnemonic   | offeredProduct | offerRequest                | eidvScore | asmScore | activateEndpoint     | activateRequest |applicationStatus |
      ################################################ HALIFAX NON DOWNSELL ###########################################################################################
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellAcceptNoEmail    | Accept    | Accept   | env.activate.halifax | ActivateRequest |1010				 |
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellProcessedNoEmail | Accept    | Refer    | env.activate.halifax | ActivateRequest |1008				 |
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellReferNoEmail     | Refer     | Accept   | env.activate.halifax | ActivateRequest |1007				 |

      ################################################ HALIFAX DOWNSELL ###########################################################################################
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellAcceptNoEmail       | Accept    | Accept   | env.activate.halifax | ActivateRequest |1010				 |
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellProcessedNoEmail    | Accept    | Refer    | env.activate.halifax | ActivateRequest |1008				 |
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellReferNoEmail        | Refer     | Accept   | env.activate.halifax | ActivateRequest |1007				 |
      ################################################ LLOYDS NON DOWNSELL ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellAcceptNoEmail    | Accept    | Accept   | env.activate.lloyds  | ActivateRequest |1010				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellProcessedNoEmail | Accept    | Refer    | env.activate.lloyds  | ActivateRequest |1008				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellReferNoEmail     | Refer     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ LLOYDS DOWNSELL ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellAcceptNoEmail       | Accept    | Accept   | env.activate.lloyds  | ActivateRequest |1010				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellProcessedNoEmail    | Accept    | Refer    | env.activate.lloyds  | ActivateRequest |1008				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellReferNoEmail        | Refer     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS NON DOWNSELL ###########################################################################################
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellAcceptNoEmail    | Accept    | Accept   | env.activate.bos     | ActivateRequest |1010				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellProcessedNoEmail | Accept    | Refer    | env.activate.bos     | ActivateRequest |1008				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellReferNoEmail     | Refer     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      ################################################ BOS DOWNSELL ###########################################################################################
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_NEW_BASIC    | BosDownsellAcceptNoEmail       | Accept    | Accept   | env.activate.bos     | ActivateRequest |1010				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 |P_CLASSIC   | P_NEW_BASIC    | BosDownsellProcessedNoEmail    | Accept    | Refer    | env.activate.bos     | ActivateRequest |1008				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 |P_CLASSIC   | P_NEW_BASIC    | BosDownsellReferNoEmail        | Refer     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      

  Scenario Outline: (AVA Products) Validate the response from RetreiveProductArrangement after Branch Context 
    Given Perform the branch context setting operation endpoint "<branchContextSetEndpoint>" with colleagueId "<colleagueId>" and domain "<domain>"
    Then Perform the branchContext operation using enpoint and Request "<branchContextEndpoint>" "<branchContextRequest>"
    Then verify colleagueId equals "<colleagueId>"
    Then verify authorized equals "<authorized>"
    
    Given product retrieve service is up and running "<retrieveEndpoint>" "<urlIdentifier>"
    Then verify product mnemonic "<mnemonic>"
    Then verify service status "<httpStatusCode>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then verify product mnemonic of offer "<offeredProduct>"    
    Then verify eidv score "<eidvScore>"
    Then verify asm score "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"
    
    Examples: 
      |  branchContextSetEndpoint  		  | branchContextEndpoint     		  | branchContextRequest     | colleagueId   | authorized | domain 		| retrieveEndpoint     | offerEndpoint                      | urlIdentifier           |httpStatusCode| mnemonic   | offeredProduct | offerRequest                | eidvScore | asmScore | activateEndpoint     | activateRequest |applicationStatus |
      ################################################ AVA Products ###########################################################################################
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | platinumaccountBOS      |        200 | P_PLAT     | P_PLAT         | BosPlatinumAcceptNoEmail       | Accept    | Accept   | env.activate.bos     | ActivateRequest |1010				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | platinumaccountBOS       |        200 | P_PLAT     | P_PLAT         | BosPlatinumProcessNoEmail      | Accept    | Refer    | env.activate.bos     | ActivateRequest |1008				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | platinumaccountBOS       |        200 | P_PLAT     | P_PLAT         | BosPlatinumReferNoEmail        | Refer     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      ################################################ AVA Products ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | clubaccountLTB          |        200 | P_CLUB    | P_CLUB      | LydClubAccept    | Accept    | Accept   | env.activate.lloyds  | ClubActivateRequest |1010				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | clubaccountLTB          |        200 | P_CLUB  | P_CLUB      | LydClubProcess   | Accept    | Refer    | env.activate.lloyds  | ClubActivateRequest |1008				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | clubaccountLTB          |        200 | P_CLUB  | P_CLUB      | LydClubRefer     | Refer     | Accept   | env.activate.lloyds  | ClubActivateRequest |1007				 |
      
   ###################################################################################################################################################################

  
   Scenario Outline: Validate the response from RetreiveProductArrangement after Branch Context | error code
    Given Perform the branch context setting operation endpoint "<branchContextSetEndpoint>" with colleagueId "<colleagueId>" and domain "<domain>"
  	Then verify error code is "<code>"
  	
    Examples: 
   |  branchContextSetEndpoint  		  | colleagueId   | domain 		|code 	| httpStatusCode|
      ################################################ LLOYDS REFER JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint   | 		     	  | GLOBAL 		|9310002| 200			 |
   |  bosBranchContextSetEndpoint      | 		     	  | GLOBAL 		|9310002| 200			 |
   |  halifaxBranchContextSetEndpoint  | 		     	  | GLOBAL 		|9310002| 200			 |
   
   