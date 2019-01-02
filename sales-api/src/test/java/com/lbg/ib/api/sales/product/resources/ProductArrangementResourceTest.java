/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.resources;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sales.common.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.common.validation.ValidationException;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.mca.services.BranchContextService;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sales.product.domain.arrangement.MarketingPreference;
import com.lbg.ib.api.sales.product.domain.arrangement.OfferType;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.product.rules.ArrangementRuleValidator;
import com.lbg.ib.api.sales.product.rules.MarketingPreferenceValidator;
import com.lbg.ib.api.sales.product.service.ArrangementService;

@RunWith(MockitoJUnitRunner.class)
public class ProductArrangementResourceTest {

    @InjectMocks
    private ProductArrangementResource resource;

    @Mock
    private ArrangementService         service;

    @Mock
    private RequestBodyResolver        resolver;

    @Mock
    private FieldValidator             fieldValidator;

    @Mock
    private ArrangementRuleValidator   ruleValidator;

    @Mock
    private GalaxyErrorCodeResolver    errorResolver;

    @Mock
    private SessionManagementDAO       session;

    @Mock
    private BranchContextService       branchContextService;

    @Mock
    private MarketingPreferenceValidator marketingRuleValidator;

    @Mock
    private ApiServiceProperties apiServiceProperties;

    private LoggerDAO                  logger                    = mock(LoggerDAO.class);

    public static final Arrangement    ARRANGEMENT               = new Arrangement(new PrimaryInvolvedParty(), null,
            null);

    public static final Arranged       HAPPY_PRODUCT_ARRANGEMENT = new Arranged();
    public static final Arranged       HAPPY_ARRANGED            = new Arranged();
    public static final String         REQUEST                   = "request";

    @Before
    public void setup(){
        HAPPY_PRODUCT_ARRANGEMENT.setCurrentDate(DateUtil.getCurrentUKDateAsString(true));
        HAPPY_ARRANGED.setCurrentDate(DateUtil.getCurrentUKDateAsString(true));
    }

    @Test
    public void shouldPostProductArrangementsToTheService() throws Exception {
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(ARRANGEMENT);
        when(service.arrange(ARRANGEMENT)).thenReturn(HAPPY_ARRANGED);

        Arranged arranged = (Arranged) resource.arrangement(REQUEST, "pca").getEntity();

        assertThat(arranged, is(HAPPY_PRODUCT_ARRANGEMENT));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldPostProductArrangementsToTheServiceWithAccountTypeNull() throws Exception {

        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(ARRANGEMENT);
        when(service.arrange(ARRANGEMENT)).thenReturn(HAPPY_ARRANGED);
        resource.arrangement(REQUEST, null).getEntity();
    }

    @Test
    public void shouldReturnServiceUnavailableResponseWhenServiceReturnNull() throws Exception {
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(ARRANGEMENT);
        when(service.arrange(ARRANGEMENT)).thenReturn(null);

        ResponseError arranged = (ResponseError) resource.arrangement(REQUEST, "pca").getEntity();

        assertThat(arranged, is(errorResolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE)));
    }

    @Test()
    public void shouldReturnInvalidFormatWhenValidationErrorExists() throws Exception {
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(ARRANGEMENT);
        // when(service.arrange(ARRANGEMENT)).thenReturn(null);
        ValidationError error = new ValidationError("");
        when(fieldValidator.validateInstanceFields(any(Arrangement.class), any(AccountType.class))).thenReturn(false);
        resource.arrangement(REQUEST, "pca").getEntity();
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturnInvalidFormatWhenRuleValidationExists() throws Exception {
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(ARRANGEMENT);
        // when(service.arrange(ARRANGEMENT)).thenReturn(null);
        when(ruleValidator.validateRules(any(Arrangement.class))).thenReturn(new ValidationError(""));
        resource.arrangement(REQUEST, "pca").getEntity();
    }

    @Test
    public void shouldPostProductArrangementForCrossSellRequest() throws Exception {
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(ARRANGEMENT);
        when(service.arrange(ARRANGEMENT)).thenReturn(HAPPY_ARRANGED);

        Arranged arranged = (Arranged) resource.arrangement(REQUEST, "sav").getEntity();

        assertThat(arranged, is(HAPPY_PRODUCT_ARRANGEMENT));
    }

    @Test
    public void shouldPostProductArrangementForCrossSellRequestWithColleagueId() throws Exception {
        Arrangement arrangement = new Arrangement(new PrimaryInvolvedParty(), null, null);
        arrangement.setColleagueId("dummyColleagueId");
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(arrangement);
        when(service.arrange(arrangement)).thenReturn(HAPPY_ARRANGED);

        Arranged arranged = (Arranged) resource.arrangement(REQUEST, "sav").getEntity();

        assertThat(arranged, is(HAPPY_PRODUCT_ARRANGEMENT));
    }

    @Test
    public void shouldPostProductArrangementForCrossSellRequestWithColleagueIdSortCode() throws Exception {
        Arrangement arrangement = new Arrangement(new PrimaryInvolvedParty(), null, null);
        arrangement.setColleagueId("dummyColleagueId");
        arrangement.setOriginatingSortCode("dummySortCode");
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(arrangement);
        when(service.arrange(arrangement)).thenReturn(HAPPY_ARRANGED);

        Arranged arranged = (Arranged) resource.arrangement(REQUEST, "sav").getEntity();

        assertThat(arranged, is(HAPPY_PRODUCT_ARRANGEMENT));
    }

    @Test
    public void shouldPostProductArrangementForCrossSellRequestWithColleagueIdSortCodeDomain() throws Exception {
        Arrangement arrangement = new Arrangement(new PrimaryInvolvedParty(), null, null);
        arrangement.setColleagueId("dummyColleagueId");
        arrangement.setOriginatingSortCode("dummySortCode");
        arrangement.setDomain("dummyDomain");
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(arrangement);
        when(service.arrange(arrangement)).thenReturn(HAPPY_ARRANGED);

        Arranged arranged = (Arranged) resource.arrangement(REQUEST, "sav").getEntity();

        assertThat(arranged, is(HAPPY_PRODUCT_ARRANGEMENT));
    }

    @Test
    public void shouldPostProductArrangementForCrossSellRequestWithColleagueIdSortCodeDomainBranchContext()
            throws Exception {
        Arrangement arrangement = new Arrangement(new PrimaryInvolvedParty(), null, null);
        arrangement.setColleagueId("dummyColleagueId");
        arrangement.setOriginatingSortCode("dummySortCode");
        arrangement.setDomain("dummyDomain");
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(arrangement);
        when(session.getBranchContext()).thenReturn(new BranchContext());
        when(service.arrange(arrangement)).thenReturn(HAPPY_ARRANGED);

        Arranged arranged = (Arranged) resource.arrangement(REQUEST, "sav").getEntity();

        assertThat(arranged, is(HAPPY_PRODUCT_ARRANGEMENT));
    }

    @Test
    public void shouldPostProductArrangementForCrossSellRequestWithColleagueIdSortCodeDomainNullBranchContext()
            throws Exception {
        Arrangement arrangement = new Arrangement(new PrimaryInvolvedParty(), null, null);
        arrangement.setColleagueId("dummyColleagueId");
        arrangement.setOriginatingSortCode("dummySortCode");
        arrangement.setDomain("dummyDomain");
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(arrangement);
        when(session.getBranchContext()).thenReturn(null);
        when(service.arrange(arrangement)).thenReturn(HAPPY_ARRANGED);

        Arranged arranged = (Arranged) resource.arrangement(REQUEST, "sav").getEntity();

        assertThat(arranged, is(HAPPY_PRODUCT_ARRANGEMENT));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldPostProductArrangementForCrossSellRequestWithValidationException() throws Exception {
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(ARRANGEMENT);
        when(service.arrange(ARRANGEMENT)).thenReturn(HAPPY_ARRANGED);
        when(fieldValidator.validateInstanceFields(any(Arrangement.class), any(AccountType.class)))
                .thenThrow(new ValidationException(new ValidationError("DUMMY")));

        Arranged arranged = (Arranged) resource.arrangement(REQUEST, "sav").getEntity();

        assertThat(arranged, is(HAPPY_PRODUCT_ARRANGEMENT));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturnInvalidFormatWhenMarketingPreferencesDoNotHaveTheCorrectFormat() throws Exception {
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        MarketingPreference marketingPreference = new MarketingPreference();
        List<MarketingPreference> listMarketingPref = new ArrayList<MarketingPreference>();
        marketingPreference.setEntitlementId("1001");
        marketingPreference.setConsentOption(null);
        listMarketingPref.add(marketingPreference);

        primaryInvolvedParty.setMarketingPreferences(listMarketingPref);
        Arrangement ARRANGEMENT = new Arrangement(
                primaryInvolvedParty, null, null);

        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(ARRANGEMENT);
        when(marketingRuleValidator.validateRules(any(Arrangement.class))).thenReturn(new ValidationError(""));
        resource.arrangement(REQUEST, "pca").getEntity();
    }

    @Test
    public void shouldSetPackagedAccountInfoInSession() throws Exception {
        Map<String, Object> appProperties = new HashMap<String, Object>();
        appProperties.put("environmentName", "Test");
        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(appProperties);
        SelectedProduct selectedProduct = mock(SelectedProduct.class);
        Map<String, String> optionsMap = new HashMap<String, String>();
        optionsMap.put("ICOBS", "Y");
        when(selectedProduct.getOptionsMap()).thenReturn(optionsMap);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        Arrangement arrangement = mock(Arrangement.class);
        PrimaryInvolvedParty primaryInvolvedParty = mock(PrimaryInvolvedParty.class);
        when(arrangement.getPrimaryInvolvedParty()).thenReturn(primaryInvolvedParty);
        when(arrangement.getAccountType()).thenReturn(AccountType.CA);
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(arrangement);

        Arranged arranged = mock(Arranged.class);
        when(arranged.getOfferType()).thenReturn(OfferType.NORMAL.toString());
        when(service.arrange(arrangement)).thenReturn(arranged);

        Arranged arrangedResponse = (Arranged) resource.arrangement(REQUEST, "pca").getEntity();

        assertNotNull(arrangedResponse);
    }

    @Test
    public void shouldNotSetPackagedAccountInfoInSession() throws Exception {
        Map<String, Object> appProperties = new HashMap<String, Object>();
        appProperties.put("environmentName", "Test");
        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(appProperties);
        SelectedProduct selectedProduct = mock(SelectedProduct.class);
        Map<String, String> optionsMap = new HashMap<String, String>();
        optionsMap.put("ICOBS", "N");
        when(selectedProduct.getOptionsMap()).thenReturn(optionsMap);
        when(session.getSelectedProduct()).thenReturn(selectedProduct);
        Arrangement arrangement = mock(Arrangement.class);
        PrimaryInvolvedParty primaryInvolvedParty = mock(PrimaryInvolvedParty.class);
        when(arrangement.getPrimaryInvolvedParty()).thenReturn(primaryInvolvedParty);
        when(arrangement.getAccountType()).thenReturn(AccountType.CA);
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(arrangement);

        Arranged arranged = mock(Arranged.class);
        when(arranged.getOfferType()).thenReturn(OfferType.NORMAL.toString());
        when(service.arrange(arrangement)).thenReturn(arranged);

        Arranged arrangedResponse = (Arranged) resource.arrangement(REQUEST, "pca").getEntity();

        assertNotNull(arrangedResponse);
    }

    @Test
    public void shouldNotSetPackagedAccountInfoInSession_1() throws Exception {
        Map<String, Object> appProperties = new HashMap<String, Object>();
        appProperties.put("environmentName", "Test");
        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(appProperties);

        when(session.getSelectedProduct()).thenReturn(null);
        Arrangement arrangement = mock(Arrangement.class);
        PrimaryInvolvedParty primaryInvolvedParty = mock(PrimaryInvolvedParty.class);
        when(arrangement.getPrimaryInvolvedParty()).thenReturn(primaryInvolvedParty);
        when(arrangement.getAccountType()).thenReturn(AccountType.CA);
        when(resolver.resolve(any(String.class), eq(Arrangement.class))).thenReturn(arrangement);

        Arranged arranged = mock(Arranged.class);
        when(arranged.getOfferType()).thenReturn(OfferType.NORMAL.toString());
        when(service.arrange(arrangement)).thenReturn(arranged);

        Arranged arrangedResponse = (Arranged) resource.arrangement(REQUEST, "pca").getEntity();

        assertNotNull(arrangedResponse);
    }
}