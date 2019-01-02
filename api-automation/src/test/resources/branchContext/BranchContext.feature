#############################################################
##API Acceptance Tests for PCA-12 (RecordArrangementQuestionnaire)
#############################################################
@API @REGRESSION @PCA-162
Feature: BranchContext

  #################################################################################################
  
  Scenario Outline: Search Customer Details of a pending customer using valid arrangement ID after Branch Context
    Given Perform the branch context setting operation endpoint "<branchContextSetEndpoint>" with colleagueId "<colleagueId>" and domain "<domain>"
    Then Perform the branchContext operation using enpoint and Request "<branchContextEndpoint>" "<branchContextRequest>"
    Then verify colleagueId equals "<colleagueId>"
    Then verify authorized equals "<authorized>"
    
    Given for a  branch colleague, I have an arrangementId"<arrangementId>" of a customer
	When I make a call to the retrieveProductArrangement API at retrieveArrangement-endpoint "<retrieveArrangement-endpoint>"
	Then I should see the repsonse status as "<httpStatusCode>"
    And I should see customer details response as "<retrieveArrangement-validResponse>"

    Examples: 
     
     |  branchContextSetEndpoint  		 | branchContextEndpoint     		  | branchContextRequest      | colleagueId   | authorized |domain 		| brand   | product       | retrieveArrangement-endpoint       	      | httpStatusCode | arrangementId | retrieveArrangement-validResponse  |
     |  lloydsBranchContextSetEndpoint   | lloydsBranchContextEndpoint     	  | ValidBranchContextReq     | CT067484      | true       |GLOBAL 		| lloyds  | esaver        | env.retrievePendingProductLookUp.lloyds   |     200        | 121366    	   | lastName  						    |
     |  bosBranchContextSetEndpoint      | bosBranchContextEndpoint     	  | ValidBranchContextReq     | CT067484      | true       |GLOBAL 		| bos     |               | env.retrievePendingProductLookUp.bos      |     200        | 128517    	   | lastName  						    |
     |  halifaxBranchContextSetEndpoint  | halifaxBranchContextEndpoint       | ValidBranchContextReq     | CT067484      | true       |GLOBAL 		| halifax |               | env.retrievePendingProductLookUp.halifax  |     200        | 123377    	   | lastName  						    |

  Scenario Outline: Validate the response from Branch Context
    Given Perform the branch context setting operation endpoint "<branchContextSetEndpoint>" with colleagueId "<colleagueId>" and domain "<domain>"
    Then Perform the branchContext operation using enpoint and Request "<branchContextEndpoint>" "<branchContextRequest>"
    Then verify colleagueId equals "<colleagueId>"
    Then verify authorized equals "<authorized>"

    Examples: 
   |  branchContextSetEndpoint  		| branchContextEndpoint     		| branchContextRequest      | colleagueId   | authorized | domain 		|
   |  halifaxBranchContextSetEndpoint   | halifaxBranchContextEndpoint      | ValidBranchContextReq 	| CT067484      | true       | GLOBAL 		|
   |  lloydsBranchContextSetEndpoint    | lloydsBranchContextEndpoint       | ValidBranchContextReq 	| CT067484      | true       | GLOBAL 		|
   |  bosBranchContextSetEndpoint       | bosBranchContextEndpoint          | ValidBranchContextReq 	| CT067484      | true       | GLOBAL 		|   


  Scenario Outline: Validate the response from RetreiveProductArrangement after Branch Context
    Given Perform the branch context setting operation endpoint "<branchContextSetEndpoint>" with colleagueId "<colleagueId>" and domain "<domain>"
    Then Perform the branchContext operation using enpoint and Request "<branchContextEndpoint>" "<branchContextRequest>"
    Then verify colleagueId equals "<colleagueId>"
    Then verify authorized equals "<authorized>"
    
    Given product retrieve service is up and running "<retrieveEndpoint>" "<urlIdentifier>"
    Then verify product name "<productName>"
    Then verify product mnemonic "<productMnemonic>"
    Then verify service status "<httpStatusCode>"

    Examples: 
   |  branchContextSetEndpoint  		 | branchContextEndpoint     		  | branchContextRequest     | colleagueId   | authorized | domain 		| retrieveEndpoint     | urlIdentifier    | httpStatusCode | productName                     | productMnemonic |
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | isasavervariable |            200 | ISA Saver Variable              | P_ISA_VAR       |     
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint        | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | esaver           |            200 | eSavings                        | P_ESAVER        |   
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint           | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | accesssaver      |            200 | Access Saver                    | P_EASY_SVR      |   
   
      
  Scenario Outline: (Youth and Student All) Validate the response from RetreiveProductArrangement after Branch Context
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
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_STUDENT      | LydStudentAccept    		 | REFER     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS REFER JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_STUDENT      | BosStudentAccept    		 | REFER     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      ################################################ HALIFAX REFER JOURNEY ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_STUDENT      | HalifaxStudentAccept        | REFER     | Accept   | env.activate.halifax | ActivateRequest |1007				 |
      
      ################################################ LLOYDS APPLICATION BEING PROCESS JOURNEY ########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_STUDENT      | LydStudentAppBeingProcess	 | REFER     | REFER    |env.activate.lloyds  | ActivateRequest |1008				 |
      ################################################ BOS APPLICATION BEING PROCESS JOURNEY ########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_STUDENT      | BosStudentAppBeingProcess   | REFER     | REFER   | env.activate.bos     | ActivateRequest |1008				 |
      ################################################ HALIFAX APPLICATION BEING PROCESS JOURNEY ########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_STUDENT      | HalifaxStudentAppBeingProcess| REFER     | REFER   | env.activate.halifax | ActivateRequest |1008				 |

      ################################################ LLOYDS DOWNSELL CLASSIC JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_CLASSIC      | LydStudentDownsellClassic	 | REFER     | Accept   |env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS DOWNSELL CLASSIC JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_CLASSIC      | BosStudentDownsellClassic   | REFER     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      ################################################ HALIFAX DOWNSELL CLASSIC JOURNEY ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_STANDARD      | HalifaxStudentDownsellClassic| REFER    | Accept   | env.activate.halifax | ActivateRequest |1007				 |

      ################################################ LLOYDS DOWNSELL BASIC JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_NEW_BASIC    | LydStudentDownsellBasic	 | REFER     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 | 
      ################################################ BOS DOWNSELL BASIC JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_NEW_BASIC    | BosStudentDownsellBasic     | REFER     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      ################################################ HALIFAX DOWNSELL BASIC JOURNEY ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_NEW_BASIC    | HalifaxStudentDownsellBasic | REFER     | Accept   | env.activate.halifax | ActivateRequest |1007				 |
    
      ################################################ LLOYDS DOWNSELL YOUTH JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_UNDER19      | LydStudentDownsellYouth	 | REFER     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS DOWNSELL YOUTH JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_UNDER19      | BosStudentDownsellYouth     | REFER     | Accept   | env.activate.bos     | ActivateRequest |1007				 |

      ################################################ LLOYDS NON REFER ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB        |        200 | P_UNDER19  | P_UNDER19      | LydYouthAccept    		 | REFER    | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      
      ################################################ BOS  NORMAL ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos     |under19accountBOS        |        200 | P_UNDER19  | P_UNDER19      | BosYouthAccept    		 | REFER    | Accept   | env.activate.bos     | ActivateRequest |1007				 |

      ################################################ Halifax  Accept ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  | expresscashHLX         |        200 | P_EXPRESSCASH  | P_EXPRESSCASH | HalifaxYouthAccept   | REFER    | Accept   | env.activate.halifax | ActivateRequest |1007				 |


  Scenario Outline: (Youth and Student Decline ) Validate the response from RetreiveProductArrangement after Branch Context
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

    Examples: 
   |  branchContextSetEndpoint  		  | branchContextEndpoint     		  | branchContextRequest     | colleagueId   | authorized | domain 		| retrieveEndpoint     | offerEndpoint                  | urlIdentifier           |httpStatusCode| mnemonic   | offeredProduct | offerRequest                | eidvScore | asmScore |

      ################################################ LLOYDS NON DECLINE ###########################################################################################
    |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds  |under19accountLTB       |        200 | P_UNDER19  | P_UNDER19      | LydYouthDecline    		 | REFER    | DECLINE   |

      ################################################ BOS  DECLINE ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos     |under19accountBOS   |        200 | P_UNDER19  | P_UNDER19      | BosYouthDecline    		 | REFER    | DECLINE   |
          
      ################################################ Halifax  DECLINE ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax     | expresscashHLX     |        200 | P_EXPRESSCASH | P_EXPRESSCASH      | HalifaxYouthDecline    		 | REFER    | DECLINE   |

      ################################################ LLOYDS DECLINE JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_STUDENT      | LydStudentDecline	           | REFER     | DECLINE |
      ################################################ BOS DECLINE JOURNEY ###########################################################################################
   |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200   | P_STUDENT  | P_STUDENT      | BosStudentDecline             | REFER     | DECLINE |
      ################################################ HALIFAX DECLINE JOURNEY ###########################################################################################
   |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200   | P_STUDENT  | P_STUDENT      | HalifaxStudentDecline         | REFER     | DECLINE |


   
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
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellAccept    | Accept    | Accept   | env.activate.halifax | ActivateRequest |1010				 |
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellProcessed | Accept    | Refer    | env.activate.halifax | ActivateRequest |1008				 |
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellRefer     | Refer     | Accept   | env.activate.halifax | ActivateRequest |1007				 |

      ################################################ HALIFAX DOWNSELL ###########################################################################################
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellAccept       | Accept    | Accept   | env.activate.halifax | ActivateRequest |1010				 |
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellProcessed    | Accept    | Refer    | env.activate.halifax | ActivateRequest |1008				 |
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellRefer        | Refer     | Accept   | env.activate.halifax | ActivateRequest |1007				 |
      ################################################ LLOYDS NON DOWNSELL ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellAccept    | Accept    | Accept   | env.activate.lloyds  | ActivateRequest |1010				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellProcessed | Accept    | Refer    | env.activate.lloyds  | ActivateRequest |1008				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellRefer     | Refer     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ LLOYDS DOWNSELL ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellAccept       | Accept    | Accept   | env.activate.lloyds  | ActivateRequest |1010				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellProcessed    | Accept    | Refer    | env.activate.lloyds  | ActivateRequest |1008				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellRefer        | Refer     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |
      ################################################ BOS NON DOWNSELL ###########################################################################################
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellAccept    | Accept    | Accept   | env.activate.bos     | ActivateRequest |1010				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellProcessed | Accept    | Refer    | env.activate.bos     | ActivateRequest |1008				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellRefer     | Refer     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      ################################################ BOS DOWNSELL ###########################################################################################
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_NEW_BASIC    | BosDownsellAccept       | Accept    | Accept   | env.activate.bos     | ActivateRequest |1010				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 |P_CLASSIC   | P_NEW_BASIC    | BosDownsellProcessed    | Accept    | Refer    | env.activate.bos     | ActivateRequest |1008				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 |P_CLASSIC   | P_NEW_BASIC    | BosDownsellRefer        | Refer     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      

  @pca-01
  Scenario Outline: (AVA and EPIC-1 Products) Validate the response from RetreiveProductArrangement after Branch Context 
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
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | platinumaccountBOS      |        200 | P_PLAT     | P_PLAT         | BosPlatinumAccept       | Accept    | Accept   | env.activate.bos     | ActivateRequest |1010				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | platinumaccountBOS       |        200 | P_PLAT     | P_PLAT         | BosPlatinumProcess      | Accept    | Refer    | env.activate.bos     | ActivateRequest |1008				 |
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | platinumaccountBOS       |        200 | P_PLAT     | P_PLAT         | BosPlatinumRefer        | Refer     | Accept   | env.activate.bos     | ActivateRequest |1007				 |
      ################################################ AVA Products ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | clubaccountLTB          |        200 | P_CLUB    | P_CLUB      | LydClubAccept    | Accept    | Accept   | env.activate.lloyds  | ClubActivateRequest |1010				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | clubaccountLTB          |        200 | P_CLUB  | P_CLUB      | LydClubProcess   | Accept    | Refer    | env.activate.lloyds  | ClubActivateRequest |1008				 |
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | clubaccountLTB          |        200 | P_CLUB  | P_CLUB      | LydClubRefer     | Refer     | Accept   | env.activate.lloyds  | ClubActivateRequest |1007				 |
      