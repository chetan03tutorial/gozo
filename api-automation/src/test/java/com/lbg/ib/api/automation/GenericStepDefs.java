package com.lbg.ib.api.automation;

public interface GenericStepDefs {

	public void givenIhaveServiceAvailableForFeatureAndBrand(String feature, String brand);

	public void whenITryToInvokeTheService();

	public void thenIshouldGetResponseFromTheService(String responseCode);

	public void iShouldSeeResponseWithResponseCode(String responseCode) throws Throwable;

	public void iShouldSeeMessage(String message) throws Throwable;

}
