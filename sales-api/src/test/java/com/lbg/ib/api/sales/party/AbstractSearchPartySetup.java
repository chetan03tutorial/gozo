package com.lbg.ib.api.sales.party;

import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.dto.SearchPartiesSalsaResponse;
import com.lbg.ib.api.sales.util.FileUtil;

import static com.lbg.ib.api.sales.util.JsonConverter.resolve;

abstract public class AbstractSearchPartySetup {

    protected static IBParties[]                  successResponseWithMultipleParties;
    protected static SearchPartiesSalsaResponse[] searchPartySalsaResponse;

    protected final String                        validSixteenDigitAgreementIdentifier  = "SOMESIXTEENDIGIT";
    protected final String                        validFourteenDigitAgreementIdentifier = "FOURTEENDIGITS";
    protected final String                        invalidAgreementIdentifier            = "SOMERANDOM";

    public AbstractSearchPartySetup() {
        prepareSearchPartyResponse();
        
    }
    
    public static void prepareSearchPartyResponse() {
        successResponseWithMultipleParties = resolve(FileUtil.loadFile("./party/AdapaResponseWithPartyDetails.json"),
                IBParties[].class);
        searchPartySalsaResponse = resolve(FileUtil.loadFile("./party/SearchPartySalsaResponse.json"),
                SearchPartiesSalsaResponse[].class);
    }
}
