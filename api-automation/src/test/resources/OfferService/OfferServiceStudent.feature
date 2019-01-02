#############################################################
##API Acceptance Tests for PCA-12 (RecordArrangementQuestionnaire)
#############################################################
@API @REGRESSION @PCA-162
Feature: Student - OfferService

  Scenario Outline: Validate the response from Arrangement
    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<product>"
    Then validate the response of retrieve service "<mnemonic>"
    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
    Then verify product mnemonic "<offeredProduct>"
    Then verify eidv score "<eidvScore>"
    Then verify asm score "<asmScore>"

    Examples:
      | retrieveEndpoint     | offerEndpoint                  | product                 | httpStatus | mnemonic   | offeredProduct | offerRequest                | eidvScore | asmScore |
      ################################################ LLOYDS REFER JOURNEY ###########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_STUDENT      | LydStudentAccept    		 | REFER     | Accept   |
      ################################################ BOS REFER JOURNEY ###########################################################################################
      | env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200 | P_STUDENT  | P_STUDENT      | BosStudentAccept    		 | REFER     | Accept   |
      ################################################ HALIFAX REFER JOURNEY ###########################################################################################
      | env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200 | P_STUDENT  | P_STUDENT      | HalifaxStudentAccept        | REFER     | Accept   |
      
      ################################################ LLOYDS APPLICATION BEING PROCESS JOURNEY ########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_STUDENT      | LydStudentAppBeingProcess	 | REFER     | REFER   |
      ################################################ BOS APPLICATION BEING PROCESS JOURNEY ########################################################################################
      | env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200 | P_STUDENT  | P_STUDENT      | BosStudentAppBeingProcess   | REFER     | REFER   |
      ################################################ HALIFAX APPLICATION BEING PROCESS JOURNEY ########################################################################################
      | env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200 | P_STUDENT  | P_STUDENT      | HalifaxStudentAppBeingProcess| REFER     | REFER   |

      ################################################ LLOYDS DOWNSELL CLASSIC JOURNEY ###########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_CLASSIC      | LydStudentDownsellClassic	 | REFER     | Accept   |
      ################################################ BOS DOWNSELL CLASSIC JOURNEY ###########################################################################################
      | env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200 | P_STUDENT  | P_CLASSIC      | BosStudentDownsellClassic   | REFER     | Accept   |
      ################################################ HALIFAX DOWNSELL CLASSIC JOURNEY ###########################################################################################
      | env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200 | P_STUDENT  | P_STANDARD      | HalifaxStudentDownsellClassic| REFER    | Accept   |

      ################################################ LLOYDS DOWNSELL BASIC JOURNEY ###########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_NEW_BASIC    | LydStudentDownsellBasic	 | REFER     | Accept   |
      ################################################ BOS DOWNSELL BASIC JOURNEY ###########################################################################################
      | env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200 | P_STUDENT  | P_NEW_BASIC    | BosStudentDownsellBasic     | REFER     | Accept   |
      ################################################ HALIFAX DOWNSELL BASIC JOURNEY ###########################################################################################
      | env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200 | P_STUDENT  | P_NEW_BASIC    | HalifaxStudentDownsellBasic | REFER     | Accept   |
    
      ################################################ LLOYDS DOWNSELL YOUTH JOURNEY ###########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_UNDER19      | LydStudentDownsellYouth	 | REFER     | Accept   |
      ################################################ BOS DOWNSELL YOUTH JOURNEY ###########################################################################################
      | env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200 | P_STUDENT  | P_UNDER19      | BosStudentDownsellYouth     | REFER     | Accept   |

      ################################################ LLOYDS DECLINE JOURNEY ###########################################################################################
      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_STUDENT      | LydStudentDecline	           | REFER     | DECLINE |
      ################################################ BOS DECLINE JOURNEY ###########################################################################################
      | env.retrieve.bos     |env.productarrangement.bos      |studentaccountBOS        |        200 | P_STUDENT  | P_STUDENT      | BosStudentDecline             | REFER     | DECLINE |
      ################################################ HALIFAX DECLINE JOURNEY ###########################################################################################
      | env.retrieve.halifax |env.productarrangement.halifax  |studcurrentaccountHLX    |        200 | P_STUDENT  | P_STUDENT      | HalifaxStudentDecline         | REFER     | DECLINE |
      
      

      ############################################################# Negative Scenarios ##########################################################
      
      
      @NEGATIVE
      Scenario Outline: Validate the response from Arrangement
	    Given product retrieve service is up and running for product "<retrieveEndpoint>" "<product>"
	    Then validate the response of retrieve service "<mnemonic>"
	    And we send a request for offer service "<offerEndpoint>" "<offerRequest>"
	    Then verify error msg is "<message>"

	    Examples:
	      | retrieveEndpoint     | offerEndpoint                  | product                 | httpStatus | mnemonic   | offeredProduct | offerRequest                	|message													   |
	      ################################################ LLOYDS REFER JOURNEY ###########################################################################################
	      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_STUDENT      | LydStudentMissingName    		 |'firstName' required to be set but it was ''				   |
	      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_STUDENT      | LydStudentMissingEmail    		 |'email' required to be set but it was ''    				   	|
	      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_STUDENT      | LydStudentWrongEmailFormat    	 |'adam@gmail' is not matching with format in field 'email'    |
	      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_STUDENT      | LydStudentMissingDateOfBirth    |'dob' required to be set but it was 'null'    |
	      | env.retrieve.lloyds  |env.productarrangement.lloyds   |studentaccountLTB        |        200 | P_STUDENT  | P_STUDENT      | LydStudentMissingAddress	    |Either 'buildingName' or 'buildingNumber' must be specified    |
     
      
      