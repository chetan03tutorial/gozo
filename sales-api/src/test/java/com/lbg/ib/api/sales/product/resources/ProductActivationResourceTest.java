/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.resources;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.mca.services.BranchContextService;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.activate.AccountSwitching;
import com.lbg.ib.api.sales.product.domain.activate.Activated;
import com.lbg.ib.api.sales.product.domain.activate.Activation;
import com.lbg.ib.api.sales.product.domain.activate.ArrangementId;
import com.lbg.ib.api.sales.product.domain.activate.CustomerDocument;
import com.lbg.ib.api.sales.product.domain.activate.CustomerPendingDetialsJsonReponse;
import com.lbg.ib.api.sales.product.domain.activate.InvolvedPartyRole;
import com.lbg.ib.api.sales.product.domain.activate.Location;
import com.lbg.ib.api.sales.product.domain.arrangement.AccountDetails;
import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.Overdraft;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;
import com.lbg.ib.api.sales.product.rules.ActivationRuleValidator;
import com.lbg.ib.api.sales.product.service.ActivateProductService;

@RunWith(MockitoJUnitRunner.class)
public class ProductActivationResourceTest {
    private static final ArrangementId     ARRANGEMENT_ID                 = new ArrangementId("87546");
    private static Overdraft               overdraftFeatures              = new Overdraft();
    private static final AccountSwitching  accountSwitchingDetails        = new AccountSwitching();

    private static final Activation        ACTIVATION                     = new Activation(
            new InvolvedPartyRole("uname", "passwd"), new Condition[] { new Condition("n", 1, "v") },
            new Location("10", "20"), overdraftFeatures, accountSwitchingDetails, false, "productId", null, null, null,
            null);
    private static final Activated         ACTIVATED                      = new Activated("pname", "mnemonic",
            "appStatus", "appSubStatus", ARRANGEMENT_ID.getValue(), new AccountDetails("sc", "ano"), null, "1234567890",
            null);
    private static final Activated         ACTIVATED_CROSS_SELL           = new Activated("pname", "mnemonic",
            "appStatus", "appSubStatus", ARRANGEMENT_ID.getValue(), new AccountDetails("sc", "ano"),
            "clubexclusivesaver", "1234567890", null);

    private ActivateProductService         service                        = mock(ActivateProductService.class);
    private RequestBodyResolver            resolver                       = mock(RequestBodyResolver.class);
    private FieldValidator                 validator                      = mock(FieldValidator.class);
    private GalaxyErrorCodeResolver        errorResolver                  = mock(GalaxyErrorCodeResolver.class);
    private ActivationRuleValidator        ruleValidator                  = mock(ActivationRuleValidator.class);
    private SessionManagementDAO           session                        = mock(SessionManagementDAO.class);
    @Mock
    private LoggerDAO                      logger                         = mock(LoggerDAO.class);
    private ResumeProductActivationService resumeProductActivationService = mock(ResumeProductActivationService.class);

    private BranchContextService           branchContextService           = mock(BranchContextService.class);
    private ProductActivationResource      productActivationResource      = new ProductActivationResource(service,
            resolver, validator, errorResolver, ruleValidator, session, logger, resumeProductActivationService,
            branchContextService);
    static Arranged                        arranged                       = new Arranged();

    @Test
    public void shouldReturnActivatedWhenTheServiceReturns() throws Exception {
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(ACTIVATION);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");

        // assertThat((Activated)response.getEntity(), is(ACTIVATED));
        // assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWhenTheServiceReturnsWithValidRequest() throws Exception {
        ProductActivationResource dummmy = new ProductActivationResource();
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(ACTIVATION);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED);
        when(session.getArrangeToActivateParameters()).thenReturn(new ArrangeToActivateParameters());
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");

        // assertThat((Activated)response.getEntity(), is(ACTIVATED));
        assertThat(response.getStatus(), is(200));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturnActivatedWhenTheServiceReturnsWithInValidRequest() throws Exception {
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(ACTIVATION);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED);
        when(session.getArrangeToActivateParameters()).thenReturn(new ArrangeToActivateParameters());

        when(validator.validateInstanceFields(any(ArrangementId.class)))
                .thenReturn(new ValidationError("Validation Error"));
        productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");

        // assertThat((Activated)response.getEntity(), is(ACTIVATED));

    }

    @Test
    public void shouldReturnActivatedWhenTheServiceReturnsWithValidRequestWithResponseNull() throws Exception {
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(ACTIVATION);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(null);
        when(session.getArrangeToActivateParameters()).thenReturn(new ArrangeToActivateParameters());
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");

        // assertThat((Activated)response.getEntity(), is(ACTIVATED));
        assertThat(response.getStatus(), is(200));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturnInvalidFormatExceptionWhenResolverThrows() throws Exception {
        when(resolver.resolve(any(String.class), eq(Activation.class)))
                .thenThrow(new InvalidFormatException("Bad activate request body"));
        productActivationResource.activateProductArrangement(new ArrangementId("aid"), "body");
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldThrowInvalidFormatExceptionWhenValidationFails() throws Exception {
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(ACTIVATION);
        when(validator.validateInstanceFields(ACTIVATION)).thenReturn(new ValidationError(""));
        productActivationResource.activateProductArrangement(new ArrangementId("aid"), "body");
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldThrowInvalidFormatExceptionWhenValidationFail() throws Exception {
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(ACTIVATION);
        when(validator.validateInstanceFields(ACTIVATION)).thenReturn(new ValidationError(""));
        productActivationResource.activateProductArrangement(new ArrangementId("aid"), "body");
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductName() throws Exception {
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(ACTIVATION);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithCustomerPendingDetails() throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        activation.setCustomerPendingDetails(new CustomerPendingDetialsJsonReponse());
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithCustomerPendingDetailsValid()
            throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithColleagueIdValid() throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        activation.setColleagueId("dummyColleagueId");
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithColleagueIdDomainValid() throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        activation.setColleagueId("dummyColleagueId");
        activation.setDomain("dummyDomain");
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithColleagueIdDomainSortCodeValid()
            throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        activation.setColleagueId("dummyColleagueId");
        activation.setDomain("dummyDomain");
        activation.setOriginatingSortCode("dummySortCode");
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithColleagueIdValidSessionValid()
            throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        activation.setColleagueId("dummyColleagueId");
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        when(session.getBranchContext()).thenReturn(new BranchContext());
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithColleagueIdDomainValidSessionValid()
            throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        activation.setColleagueId("dummyColleagueId");
        activation.setDomain("dummyDomain");
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        when(session.getBranchContext()).thenReturn(new BranchContext());
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithColleagueIdDomainSortCodeValidSessionValid()
            throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        activation.setColleagueId("dummyColleagueId");
        activation.setDomain("dummyDomain");
        activation.setOriginatingSortCode("dummySortCode");
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        when(session.getBranchContext()).thenReturn(new BranchContext());
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithCustomerDocuments() throws Exception {
        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        List<CustomerDocument> customerDocumentsList = new ArrayList<CustomerDocument>();
        CustomerDocument customerDocument = new CustomerDocument();
        customerDocumentsList.add(customerDocument);
        activation.setCustomerDocuments(customerDocumentsList);
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithCustomerDocumentsEmpty() throws Exception {
        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        List<CustomerDocument> customerDocumentsList = new ArrayList<CustomerDocument>();
        activation.setCustomerDocuments(customerDocumentsList);
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturnActivatedWithCrossSellProductNameWithCustomerDocumentsEmptyRuleValidatorError()
            throws Exception {
        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        List<CustomerDocument> customerDocumentsList = new ArrayList<CustomerDocument>();
        activation.setCustomerDocuments(customerDocumentsList);
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        when(ruleValidator.validateRules(any(Activation.class), any(ArrangeToActivateParameters.class)))
                .thenReturn(new ValidationError("Exception in Field Validation"));
        productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithColleagueIdDomainSortCodeValidWithCurrentValidColleagueId()
            throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        activation.setColleagueId("dummyColleagueId");
        activation.setDomain("dummyDomain");
        activation.setOriginatingSortCode("dummySortCode");
        activation.setCurrentColleagueId("dummyColleagueId");
        activation.setIsSnr(true);
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnActivatedWithCrossSellProductNameWithResumeWithColleagueIdDomainSortCodeValidWithCurrentValidColleagueIdSnrFalse()
            throws Exception {

        Activation activation = new Activation(new InvolvedPartyRole("uname", "passwd"),
                new Condition[] { new Condition("n", 1, "v") }, new Location("10", "20"), overdraftFeatures,
                accountSwitchingDetails, false, "productId", null, null, null, null);
        CustomerPendingDetialsJsonReponse respone = new CustomerPendingDetialsJsonReponse();
        List<CustomerPendingDetails> listCustomerPendingDetails = respone.getCustomerPendingDetails();
        listCustomerPendingDetails.add(new CustomerPendingDetails());
        respone.setCustomerPendingDetails(listCustomerPendingDetails);
        activation.setCustomerPendingDetails(respone);
        activation.setColleagueId("dummyColleagueId");
        activation.setDomain("dummyDomain");
        activation.setOriginatingSortCode("dummySortCode");
        activation.setCurrentColleagueId(null);
        activation.setIsSnr(true);
        when(resolver.resolve(any(String.class), eq(Activation.class))).thenReturn(activation);
        when(service.activateProduct(ARRANGEMENT_ID, ACTIVATION)).thenReturn(ACTIVATED_CROSS_SELL);
        Response response = productActivationResource.activateProductArrangement(ARRANGEMENT_ID, "body");
        assertThat(response.getStatus(), is(200));
    }
}