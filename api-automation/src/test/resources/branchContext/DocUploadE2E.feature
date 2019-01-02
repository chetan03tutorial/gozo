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
    
    When product retrieve service is up and running "<retrieveEndpoint>" "<urlIdentifier>"
    Then verify product mnemonic "<mnemonic>"
    Then verify service status "<httpStatusCode>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then verify product mnemonic of offer "<offeredProduct>"    
    Then verify eidv score "<eidvScore>"
    Then verify asm score "<asmScore>"
    And we send a request for activate service "<activateEndpoint>" "<activateRequest>"
    Then validate the response of activate service "<applicationStatus>"
    
    #When we send a request for createCase service "<createcaseEndpoint>" with request "<docUploadRequest>" and failFlag "<failFlag>"
    #Then validate the "<success>" response of Create Case Service for Success
    #Then Update "<docUploadRequest>"
    #Then we send a request for Document Upload service "<DocumentCaptureEndpoint>" with request "<DocumentJson>"
    #Then validate the "<DocumentSuccess>" response of Document Upload
    
    #And we send a request for second activate service "<activateEndpoint>" "<secondActivateRequest>"
    #Then validate the response of activate service "<secondApplicationStatus>"
    
    
    
    

    Examples: 
   |  branchContextSetEndpoint  		  | branchContextEndpoint     		  | branchContextRequest     | colleagueId   | authorized | domain 		| retrieveEndpoint     | offerEndpoint                  | urlIdentifier           |httpStatusCode| mnemonic   | offeredProduct | offerRequest                | eidvScore | asmScore | activateEndpoint     | activateRequest |applicationStatus |  createcaseEndpoint   | docUploadRequest			  | DocumentCaptureEndpoint    | DocumentJson        |Success|DocumentSuccess| failFlag |  secondActivateRequest      | secondApplicationStatus |
      ################################################ LLOYDS REFER JOURNEY ###########################################################################################
   |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200   | P_STUDENT  | P_STUDENT      | LydStudentAcceptNoEmail	 | REFER     | Accept   | env.activate.lloyds  | ActivateRequest |1007				|  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					|
   

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
      |  branchContextSetEndpoint  		    | branchContextEndpoint     		| branchContextRequest       | colleagueId   | authorized | domain 		| retrieveEndpoint     | offerEndpoint                  | urlIdentifier           |httpStatusCode| mnemonic   | offeredProduct | offerRequest                | eidvScore | asmScore | activateEndpoint     | activateRequest |applicationStatus | createcaseEndpoint   | docUploadRequest			   | DocumentCaptureEndpoint    | DocumentJson        |Success|DocumentSuccess| failFlag |  secondActivateRequest      | secondApplicationStatus |
      ################################################ HALIFAX NON DOWNSELL ###########################################################################################
      #|  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | urcaccountHLX           |        200 | P_ULTIMATE | P_ULTIMATE     | HlxNonDownsellReferNoEmail     | Refer     | Accept   | env.activate.halifax | ActivateRequest |1007				 |  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					|
      ################################################ HALIFAX DOWNSELL ###########################################################################################
      |  halifaxBranchContextSetEndpoint    | halifaxBranchContextEndpoint      | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.halifax | env.productarrangement.halifax | rewardcurrentaccountHLX |        200 | P_REWARD   | P_NEW_BASIC    | HlxDownsellReferNoEmail        | Refer     | Accept   | env.activate.halifax | ActivateRequest |1007				 |  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					|
      ################################################ LLOYDS NON DOWNSELL ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_CLASSIC      | LydNonDownsellReferNoEmail     | Refer     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					|
      ################################################ LLOYDS DOWNSELL ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | classicaccountLTB       |        200 | P_CLASSIC  | P_NEW_BASIC    | LydDownsellReferNoEmail        | Refer     | Accept   | env.activate.lloyds  | ActivateRequest |1007				 |  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					|
      ################################################ BOS NON DOWNSELL ###########################################################################################
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 | P_CLASSIC  | P_CLASSIC      | BosNonDownsellReferNoEmail     | Refer     | Accept   | env.activate.bos     | ActivateRequest |1007				 |  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					|
      ################################################ BOS DOWNSELL ###########################################################################################
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | classicaccountBOS       |        200 |P_CLASSIC   | P_NEW_BASIC    | BosDownsellReferNoEmail        | Refer     | Accept   | env.activate.bos     | ActivateRequest |1007				 |  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					|
      

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
      |  branchContextSetEndpoint  		  | branchContextEndpoint     		    | branchContextRequest       | colleagueId   | authorized | domain 		| retrieveEndpoint     | offerEndpoint                  | urlIdentifier            |httpStatusCode| mnemonic   | offeredProduct | offerRequest                | eidvScore | asmScore | activateEndpoint     | activateRequest     |applicationStatus | createcaseEndpoint    | docUploadRequest			   | DocumentCaptureEndpoint    | DocumentJson        |Success|DocumentSuccess| failFlag |  secondActivateRequest      | secondApplicationStatus |
      ################################################ AVA Products ###########################################################################################
      |  bosBranchContextSetEndpoint        | bosBranchContextEndpoint          | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.bos     | env.productarrangement.bos     | platinumaccountBOS       |        200   | P_PLAT     | P_PLAT         | BosPlatinumReferNoEmail     | Refer     | Accept   | env.activate.bos     | ActivateRequest     |1007				 |  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					 |
      ################################################ AVA Products ###########################################################################################
      |  lloydsBranchContextSetEndpoint     | lloydsBranchContextEndpoint       | ValidBranchContextReq 	 | CT067484      | true       | GLOBAL 		| env.retrieve.lloyds  | env.productarrangement.lloyds  | clubaccountLTB           |        200   | P_CLUB     | P_CLUB         | LydClubRefer                | Refer     | Accept   | env.activate.lloyds  | ClubActivateRequest |1007			     |  env.createCase.lloyds| successCreateCaseRequest    |  env.DocumentCapture.lloyds|SuccessUploadDocument|true   |true           | false    |  DocActivateRequestStudent  | 1010					 |
   
      