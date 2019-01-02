@ApiTesting @Regression @LFL-146 @smokeTest @TinRequest
Feature: As a bank we need to establish the Nationality, country of origin and Tax residency so that we can protect the bank and customer from financial crime
 
  Scenario Outline: Checking for TIN number when Tax country is United States
    Given I have a "<endpoint>" for Nationality "<nationality>"
    And country of Birth "<countryOfBirth>" Tax Country "<taxCountry>"
    When I send a request
    Then I should see http status code as "<httpStatusCode>"
    And I should see the response for asking TIN number
 
    Examples: 
      | endpoint             | nationality | countryOfBirth | taxCountry | httpStatusCode |
      | llyodtinServiceURL   | USA         | USA            | ISL        | 200            |
      | halifaxtinServiceURL | IND         | IND            | AUS        | 200            |
 
  Scenario Outline: Checking for TIN number when Country of Birth is 'United States', Nationality not United states and Tax country is United Kingdom
    Given I have a "<endpoint>" for Nationality "<nationality>"
    And country of Birth "<countryOfBirth>" Tax Country "<taxCountry>"
    When I send a request
    Then I should see http status code as "<httpStatusCode>"
    And I should not see the response for asking TIN number
 
    Examples: 
      | endpoint             | nationality | countryOfBirth | taxCountry | httpStatusCode |
      | bostinServiceURL     | IND         | IND            | CHN        | 200            |
      
   Scenario Outline: Checking for TIN number when Tax country is All Possible Countries
    Given I have a "<endpoint>" for Nationality "<nationality>"
    And country of Birth "<countryOfBirth>" Tax Country "<taxCountry>"
    When I send a request
    Then I should see http status code as "<httpStatusCode>"
    And I should see the response for asking TIN number
 
    Examples: 
      	| endpoint             | nationality | countryOfBirth |taxCountry| httpStatusCode |
      	| llyodtinServiceURL   | USA         | USA            |GBR 		| 200 |	
		| llyodtinServiceURL   | USA         | USA            |AFG 		| 200 |
		| llyodtinServiceURL   | USA         | USA            |ALA 		| 200 |
		| llyodtinServiceURL   | USA         | USA            |ALB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |DZA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ASM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |AND      | 200 |
		| llyodtinServiceURL   | USA         | USA            |AGO      | 200 |
		| llyodtinServiceURL   | USA         | USA            |AIA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ATA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ATG      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ARG      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ARM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ABW      | 200 |
		| llyodtinServiceURL   | USA         | USA            |AUS      | 200 |
		| llyodtinServiceURL   | USA         | USA            |AUT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |AZE      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BHS      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BHR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BGD      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BRB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BLR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BEL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BLZ      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BEN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BMU      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BTN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BOL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BES      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BIH      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BWA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BVT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BRA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |IOT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BRN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BGR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BFA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BDI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |KHM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CMR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CAN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CPV      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CYM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CAF      | 200 |
		| llyodtinServiceURL   | USA         | USA            |TCD      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CHL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CHN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CXR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CCK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |COL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |COM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |COG      | 200 |
		| llyodtinServiceURL   | USA         | USA            |COD      | 200 |
		| llyodtinServiceURL   | USA         | USA            |COK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CRI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CIV      | 200 |
		| llyodtinServiceURL   | USA         | USA            |HRV      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CUB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CUW      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CYP      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CZE      | 200 |
		| llyodtinServiceURL   | USA         | USA            |DNK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |DJI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |DMA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |DOM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ECU      | 200 |
		| llyodtinServiceURL   | USA         | USA            |EGY      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SLV      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GNQ      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ERI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |EST      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ETH      | 200 |
		| llyodtinServiceURL   | USA         | USA            |FLK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |FRO      | 200 |
		| llyodtinServiceURL   | USA         | USA            |FJI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |FIN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |CSK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |TMP      | 200 |
		| llyodtinServiceURL   | USA         | USA            |FXX      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PCI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SUN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |YUG      | 200 |
		| llyodtinServiceURL   | USA         | USA            |FRA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GUF      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PYF      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ATF      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GAB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GMB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GEO      | 200 |
		| llyodtinServiceURL   | USA         | USA            |DEU      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GHA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GIB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GRC      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GRL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GRD      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GLP      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GUM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GTM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GGY      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GIN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GNB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |GUY      | 200 |
		| llyodtinServiceURL   | USA         | USA            |HTI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |HMD      | 200 |
		| llyodtinServiceURL   | USA         | USA            |VAT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |HND      | 200 |
		| llyodtinServiceURL   | USA         | USA            |HKG      | 200 |
		| llyodtinServiceURL   | USA         | USA            |HUN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ISL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |IND      | 200 |
		| llyodtinServiceURL   | USA         | USA            |IDN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |IRN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |IRQ      | 200 |
		| llyodtinServiceURL   | USA         | USA            |IRL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |IOM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ISR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ITA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |JAM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |JPN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |JEY      | 200 |
		| llyodtinServiceURL   | USA         | USA            |JOR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |KAZ      | 200 |
		| llyodtinServiceURL   | USA         | USA            |KEN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |KIR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PRK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |KOR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |KWT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |KGZ      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LAO      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LVA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LBN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LSO      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LBR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LBY      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LIE      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LTU      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LUX      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MAC      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MKD      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MDG      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MWI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MYS      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MDV      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MLI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MLT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MHL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MTQ      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MRT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MUS      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MYT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MEX      | 200 |
		| llyodtinServiceURL   | USA         | USA            |FSM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MDA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MCO      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MNG      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MNE      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MSR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MAR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MOZ      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MMR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NAM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NRU      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NPL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NLD      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NCL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NZL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NIC      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NER      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NGA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NIU      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NFK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MNP      | 200 |
		| llyodtinServiceURL   | USA         | USA            |NOR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |OMN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PAK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PLW      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PSE      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PAN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PNG      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PRY      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PER      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PHL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PCN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |POL      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PRT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |PRI      | 200 |
		| llyodtinServiceURL   | USA         | USA            |QAT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |REU      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ROM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |RUS      | 200 |
		| llyodtinServiceURL   | USA         | USA            |RWA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |BLM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |KNA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LCA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |MAF      | 200 |
		| llyodtinServiceURL   | USA         | USA            |VCT      | 200 |
		| llyodtinServiceURL   | USA         | USA            |WSM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SMR      | 200 |
		| llyodtinServiceURL   | USA         | USA            |STP      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SAU      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SEN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SRB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SYC      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SLE      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SGP      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SXM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SVK      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SVN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SLB      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SOM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ZAF      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SGS      | 200 |
		| llyodtinServiceURL   | USA         | USA            |ESP      | 200 |
		| llyodtinServiceURL   | USA         | USA            |LKA      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SHN      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SPM      | 200 |
		| llyodtinServiceURL   | USA         | USA            |SDN       | 200 |
		| llyodtinServiceURL   | USA         | USA            |SUR       | 200 |
		| llyodtinServiceURL   | USA         | USA            |SJM       | 200 |
		| llyodtinServiceURL   | USA         | USA            |SWZ       | 200 |
		| llyodtinServiceURL   | USA         | USA            |SWE       | 200 |
		| llyodtinServiceURL   | USA         | USA            |CHE       | 200 |
		| llyodtinServiceURL   | USA         | USA            |SYR       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TWN       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TJK       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TZA       | 200 |
		| llyodtinServiceURL   | USA         | USA            |THA       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TLS       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TGO       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TKL       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TON       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TTO       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TUN       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TUR       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TKM       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TCA       | 200 |
		| llyodtinServiceURL   | USA         | USA            |TUV       | 200 |
		| llyodtinServiceURL   | USA         | USA            |UGA       | 200 |
		| llyodtinServiceURL   | USA         | USA            |UKR       | 200 |
		| llyodtinServiceURL   | USA         | USA            |ARE       | 200 |
		| llyodtinServiceURL   | USA         | USA            |USA       | 200 |
		| llyodtinServiceURL   | USA         | USA            |UMI       | 200 |
		| llyodtinServiceURL   | USA         | USA            |URY       | 200 |
		| llyodtinServiceURL   | USA         | USA            |UZB       | 200 |
		| llyodtinServiceURL   | USA         | USA            |VUT       | 200 |
		| llyodtinServiceURL   | USA         | USA            |VEN       | 200 |
		| llyodtinServiceURL   | USA         | USA            |VNM       | 200 |
		| llyodtinServiceURL   | USA         | USA            |VGB       | 200 |
		| llyodtinServiceURL   | USA         | USA            |VIR       | 200 |
		| llyodtinServiceURL   | USA         | USA            |WLF       | 200 |
		| llyodtinServiceURL   | USA         | USA            |ESH       | 200 |
		| llyodtinServiceURL   | USA         | USA            |YEM       | 200 |
		| llyodtinServiceURL   | USA         | USA            |ZMB       | 200 |
		| llyodtinServiceURL   | USA         | USA            |ZWE       | 200 |
           