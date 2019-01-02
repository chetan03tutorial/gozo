/**
 * 
 */
package com.lbg.ib.api.paperless.productHolding;

import static org.junit.Assert.assertTrue;

import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.PathParameter;
import com.lbg.ib.api.alligator.annotations.RequestBody;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class UserProductHoldingFeatureStep extends BaseFeatures {

    @Given("^the user has selected a particular product \"([^\"]*)\" of a particular brand \"([^\"]*)\"$")
    @RestInvoker(service = "retrieveProduct", method = HttpMethod.GET)
    public void the_user_has_selected_a_particular_product_of_a_particular_brand(@PathParameter(param = "urlIdentifier") String product, @Brand String brand) {

    }

    @Given("^the user has been offered \"([^\"]*)\" a product of that particular brand \"([^\"]*)\"$")
    @RestInvoker(service = "offerProduct", method = HttpMethod.POST)
    public void the_user_has_been_offered_a_product_of_that_particular_brand(@RequestBody(file = true) String offerRequest, @Brand String brand) {

    }

    @Then("^retrieve the green status for product holdings \"([^\"]*)\"$")
    @RestInvoker(service = "retrievePaperfreePreferences", method = HttpMethod.GET)
    public void retrieve_the_green_status_for_product_holdings(@Brand String brand) {

    }

    @Then("^verify the green status \"([^\"]*)\"$")
    public void verify_the_green_status(String greenStatus) {
        assertTrue("Unexpected value of Email", greenStatus.equalsIgnoreCase((String) response.getPropValue("goGreenStatus")));
    }

}
