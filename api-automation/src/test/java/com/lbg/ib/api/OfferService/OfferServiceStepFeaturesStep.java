/**
 *
 */
package com.lbg.ib.api.OfferService;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.product.RetrieveProductFeaturesStep;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.OfferProductFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class OfferServiceStepFeaturesStep extends BaseEnvSetUp {

    private final OfferProductFeaturesImpl    offerService;
    private final RetrieveProductFeaturesStep retrieveService;
    public Map<String, String>            actualResult;

    public OfferServiceStepFeaturesStep() throws Exception {
    super();
    offerService = new OfferProductFeaturesImpl();
    retrieveService = new RetrieveProductFeaturesStep();
    }

    public String readRequest(final String fileName) {
    return readRequestData("jsonRequestFiles/offer", fileName);
    }

    @Given("^product retrieve service is up and running for product \"([^\"]*)\" \"([^\"]*)\"$")
    public void retrieveProduct(final String retrieveEndpoint, final String urlIdentifier) throws Throwable {
    retrieveService.retrieveProduct(retrieveEndpoint, urlIdentifier);
    }

    @Then("^validate the response of retrieve service \"([^\"]*)\"$")
    public void validateProductResponse(final String productMnemonic) throws Throwable {
    retrieveService.verifyProductMnemonic(productMnemonic);
    }

    @Given("^we send a request for offer service \"([^\"]*)\" \"([^\"]*)\"$")
    public Map<String, String> offerProduct(final String endpoint, final String requestData) throws Throwable {
    final String url = (String) this.getEnvProperty().get(endpoint);
    actualResult = offerService.getResponseFromAPIWithCookieMap(url, readRequest(requestData), getCookieMap());
    assertNotNull(actualResult);
    return actualResult;
    }

    public String offerProductAndReturnReq(final String endpoint, final String requestData) throws Throwable {
    final String url = (String) this.getEnvProperty().get(endpoint);
    final String request = readRequest(requestData);
    actualResult = offerService.getResponseFromAPIWithCookieMap(url, request, getCookieMap());
    assertNotNull(actualResult);
    return request;
    }

    public Map<String, String> offerProductAndReturnReqWithFullText(final String endpoint, final String requestData) throws Throwable {
    actualResult = offerService.getResponseFromAPI(endpoint, requestData);
    assertNotNull(actualResult);
    return actualResult;
    }

    public void offerProductAndReturnReqFullTextBody(final String endpoint, final String requestData) throws Throwable {
    System.out.println(requestData);
    final String url = (String) this.getEnvProperty().get(endpoint);
    actualResult = offerService.getResponseFromAPIWithCookieMap(url, requestData, getCookieMap());
    assertNotNull(actualResult);
    }

    @Then("^verify product mnemonic \"([^\"]*)\"$")
    public void validateOfferedProductMnemonic(final String mnemonic) throws Throwable {
    final String messageBody = actualResult.get("StatusMessage");
    final String productMnemonic = JsonUtils.parseJsonRecursive(messageBody, "mnemonic");
    assertNotNull(productMnemonic);
    assertThat(productMnemonic, equalToIgnoringCase(mnemonic));
    }

    @Then("^fetch product value for \"([^\"]*)\"$")
    public String fetchOfferedProductFromOfferResponse(final String argument) throws Throwable {
    final String messageBody = actualResult.get("StatusMessage");
    final String value = JsonUtils.parseJsonRecursive(messageBody, argument);
    return value;
    }

    @Then("^verify eidv score \"([^\"]*)\"$")
    public void validateEidvScore(final String eidvScore) throws Throwable {
    final String messageBody = actualResult.get("StatusMessage");
    final String eidv = JsonUtils.parseJsonRecursive(messageBody, "eidvScore");
    assertNotNull(eidv);
    assertThat(eidv, equalToIgnoringCase(eidvScore));
    }

    @Then("^verify asm score \"([^\"]*)\"$")
    public void validateAsmScore(final String asmScore) throws Throwable {
    final String messageBody = actualResult.get("StatusMessage");
    final String asm = JsonUtils.parseJsonRecursive(messageBody, "asmScore");
    assertNotNull(asm);
    assertThat(asm, equalToIgnoringCase(asmScore));
    }

    public void validateOcis(final String ocisId) throws Throwable {
    final String messageBody = actualResult.get("StatusMessage");
    final String newOcisId = JsonUtils.parseJsonRecursive(messageBody, "ocisId");
    assertNotNull(newOcisId);
    assertThat(newOcisId, equalToIgnoringCase(ocisId));
    }

    public String getArrangementId() {
    final String statusMessage = actualResult.get("StatusMessage");
    final String arrangementId = JsonUtils.parseJson(statusMessage, "arrangementId").replaceAll("\"", "");
    assertNotNull(arrangementId);
    return arrangementId;
    }

    public String getMessageBody() {
    final String messageBody = actualResult.get("StatusMessage");
    return messageBody;
    }

    @Then("^verify GDPR marketing preferences$")
    public void validateGDPRMarketingPreferences() throws Throwable {
    final String messageBody = actualResult.get("StatusMessage");
    final JsonArray marketingPreferencesJsonArray = JsonUtils.parseJson(messageBody).getAsJsonArray("marketingPreferences");
    assertNotNull(marketingPreferencesJsonArray);
    assertThat(marketingPreferencesJsonArray.size(), equalTo(2));
    for (final JsonElement marketingPreferenceJson : marketingPreferencesJsonArray) {
        assertNotNull(JsonUtils.parseJsonRecursive(marketingPreferenceJson.getAsJsonObject(), "entitlementId"));
        assertNotNull(JsonUtils.parseJsonRecursive(marketingPreferenceJson.getAsJsonObject(), "consentOption"));
    }
    }

    @Then("^verify validation error codes \\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$")
    public void validateErrorResponse(final String httpStatusCode, final String errorCode) throws Throwable {
    final String httpStatusActual = actualResult.get("StatusCode").replaceAll("\"", "");
    assertNotNull(httpStatusActual);
    assertThat(httpStatusActual, equalToIgnoringCase(httpStatusCode));
    final String messageBody = actualResult.get("StatusMessage");
    final String code = JsonUtils.parseJsonRecursive(messageBody, "code");
    assertNotNull(code);
    assertThat(code, equalToIgnoringCase(errorCode));
    }

    @Then("^verify error code is \"([^\"]*)\"$")
    public void verifyErrorcode(String code) {
        String messageBody = actualResult.get("StatusMessage");
        String errorcode = JsonUtils.parseJsonRecursive(messageBody, "code");
        assertTrue(code.equalsIgnoreCase(errorcode));
    }

    @Then("^verify error msg is \"([^\"]*)\"$")
    public void verifyErrorMessage(String msg) {
        String messageBody = actualResult.get("StatusMessage");
        String errorMessage = JsonUtils.parseJsonRecursive(messageBody, "message");
        assertTrue(msg.equalsIgnoreCase(errorMessage));
    }
}
