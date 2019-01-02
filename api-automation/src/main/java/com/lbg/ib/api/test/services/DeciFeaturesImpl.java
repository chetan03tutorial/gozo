package com.lbg.ib.api.test.services;

import com.jayway.restassured.RestAssured;

public class DeciFeaturesImpl extends GenericFeaturesImpl {

	static {
        RestAssured.useRelaxedHTTPSValidation();
	}
}

