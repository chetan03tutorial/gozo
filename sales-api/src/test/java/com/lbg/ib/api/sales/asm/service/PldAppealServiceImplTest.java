package com.lbg.ib.api.sales.asm.service;

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.pld.request.PldAppealRequest;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.PackageAccountSessionInfo;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.sales.product.domain.features.PldProductInfo;
import com.lbg.ib.api.sales.product.service.ModifyProductArrangementService;
import com.lbg.ib.api.sales.product.service.RetrieveProductFeaturesService;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.www.C078Resp;
import com.lloydstsb.www.FacilitiesOffered;
import com.lloydstsb.www.ProductsOffered;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.MNEMONIC_SYSTEM_CODE;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by 8796528 on 11/06/2018.
 */


@RunWith(MockitoJUnitRunner.class)
public class PldAppealServiceImplTest {

    @Mock
    private C078Service service;

    @Mock
    private SessionManagementDAO sessionManager;

    @InjectMocks
    PldAppealServiceImpl serviceImpl;


    @Mock
    C078Resp response;

    @Mock
    LoggerDAO loggerDAO;

    @Mock
    RetrieveProductFeaturesService retrieveProductFeaturesService;

    @Mock
    B274Service b274Service;

    @Mock
    ModifyProductArrangementService modifyProductArrangementService;

    @Before
    public void setup() {
        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setProductFamilyIdentifier("0502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        retrieveProductConditionsResponse.setProduct(new Product[]{new Product()});
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);

        SelectedProduct selectedProduct = new SelectedProduct();
        selectedProduct.setPdtFamilyIdentifier("501");
        when(sessionManager.getSelectedProduct()).thenReturn(selectedProduct);

        response = new C078Resp();
    }

    @Test
    public void testFetchAppScoreResultsAndUpdateDecisionWithIncompleteInput(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getProduct().getMnemonic()==null);
    }


    @Test
    public void testFetchAppScoreResultsAndUpdateDecisionWithIncompleteInputWithOverDraftDetails(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[1];
        facilitiesOffered[0] = new FacilitiesOffered();
        facilitiesOffered[0].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[0].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getAmtOverdraft()!=null);
    }

    @Test
    public void testFetchAppScoreResultsAndUpdateDecisionWithIncompleteInputWithOverDraftDetailsWithNull(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setProductFamilyIdentifier("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);

        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getAmtOverdraft()!=null);
    }



    @Test(expected=ServiceException.class)
    public void testFetchAppScoreResultsAndUpdateDecisionWithIncompleteInputWithOverDraftDetailsWithNullProductsOffered(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);

        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getAmtOverdraft()!=null);
    }


    @Test
    public void testFetchAppScoreResultsAndUpdateDecisionWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductNotNull(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);

        SelectedProduct selectedProduct = new SelectedProduct();
        selectedProduct.setPdtFamilyIdentifier("501");
        when(sessionManager.getSelectedProduct()).thenReturn(selectedProduct);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getAmtOverdraft()!=null);
    }


    @Test
    public void testFetchAppScoreResultsAndUpdateDecisionWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductNull(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
    }



    @Test
    public void testFetchAppScoreWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductNoOptions(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();
        retrieveProductConditionsResponse.setProduct(products);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);


        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getAmtOverdraft()!=null);
    }


    @Test
    public void testFetchAppScoreWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductOptionsNoMnemonic(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();

        ProductOptions[] options = new ProductOptions[2];
        options[0] = new ProductOptions();
        products[0].setProductoptions(options);

        retrieveProductConditionsResponse.setProduct(products);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);

        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getAmtOverdraft()!=null);
    }


    @Test
    public void testFetchAppScoreWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductOptionsMnemonic(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();

        ProductOptions[] options = new ProductOptions[2];
        options[0] = new ProductOptions();
        products[0].setProductoptions(options);

        ExtSysProdIdentifier[] extSysProdIdentifiers = new ExtSysProdIdentifier[1];
        products[0].setExternalSystemProductIdentifier(extSysProdIdentifiers);

        retrieveProductConditionsResponse.setProduct(products);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);

        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getAmtOverdraft()!=null);

        extSysProdIdentifiers = new ExtSysProdIdentifier[1];
        extSysProdIdentifiers[0] = new ExtSysProdIdentifier();
        extSysProdIdentifiers[0].setSystemCode(MNEMONIC_SYSTEM_CODE);
        products[0].setExternalSystemProductIdentifier(extSysProdIdentifiers);
        products[0].setProductIdentifier("5002");
        retrieveProductConditionsResponse.setProduct(products);

        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);
        pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getAmtOverdraft()!=null);

    }


    @Test(expected = ServiceException.class)
    public void testFetchAppScoreWithIncompleteInputWithOverDraftDetailsWhenRetriveProductDetailsThrowsException(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();

        ProductOptions[] options = new ProductOptions[2];
        options[0] = new ProductOptions();
        products[0].setProductoptions(options);

        ExtSysProdIdentifier[] extSysProdIdentifiers = new ExtSysProdIdentifier[1];
        products[0].setExternalSystemProductIdentifier(extSysProdIdentifiers);

        retrieveProductConditionsResponse.setProduct(products);
        ResponseError error = new ResponseError();

        ResponseError.Error e = error.new Error("Error Code","Error Msg");

        ResponseError updatedError = new ResponseError(e);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenThrow(new ServiceException(updatedError));

        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
    }



    @Test
    public void testFetchAppScoreWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductOptionsMnemonicPBAInfo() {
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();

        ProductOptions[] options = new ProductOptions[2];
        options[0] = new ProductOptions();
        products[0].setProductoptions(options);

        ExtSysProdIdentifier[] extSysProdIdentifiers = new ExtSysProdIdentifier[1];
        products[0].setExternalSystemProductIdentifier(extSysProdIdentifiers);

        retrieveProductConditionsResponse.setProduct(products);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);

        PackageAccountSessionInfo packageAccountSessionInfo = new PackageAccountSessionInfo();
        when(sessionManager.getPackagedAccountSessionInfo()).thenReturn(packageAccountSessionInfo);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);

    }

    @Test
    public void testFetchAppScoreWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductOptionsMnemonicPBAInfoFull() {
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();

        ProductOptions[] options = new ProductOptions[2];
        options[0] = new ProductOptions();
        products[0].setProductoptions(options);

        ExtSysProdIdentifier[] extSysProdIdentifiers = new ExtSysProdIdentifier[1];
        products[0].setExternalSystemProductIdentifier(extSysProdIdentifiers);

        retrieveProductConditionsResponse.setProduct(products);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);

        PackageAccountSessionInfo packageAccountSessionInfo = new PackageAccountSessionInfo();
        packageAccountSessionInfo.setOfferResponse(new Arranged());
        when(sessionManager.getPackagedAccountSessionInfo()).thenReturn(packageAccountSessionInfo);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
    }


    @Test
    public void testFetchAppScoreWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductOptionsMnemonicPBAInfoFullProdId() {
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOfferedProductId("12345");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();

        ProductOptions[] options = new ProductOptions[2];
        options[0] = new ProductOptions();
        products[0].setProductoptions(options);

        ExtSysProdIdentifier[] extSysProdIdentifiers = new ExtSysProdIdentifier[1];
        products[0].setExternalSystemProductIdentifier(extSysProdIdentifiers);

        retrieveProductConditionsResponse.setProduct(products);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);

        PackageAccountSessionInfo packageAccountSessionInfo = new PackageAccountSessionInfo();
        packageAccountSessionInfo.setOfferResponse(new Arranged());
        when(sessionManager.getPackagedAccountSessionInfo()).thenReturn(packageAccountSessionInfo);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);

    }

    @Test
    public void testFetchAppScoreWithIncompleteInputWithOverDraftDetailsProductsOfferedWithSelectedProductOptionsMnemonicPBAInfoFullProdIdDiff() {
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOfferedProductId("1234");
        arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();

        products[0].setProductIdentifier("12345");
        ProductOptions[] options = new ProductOptions[2];
        options[0] = new ProductOptions();
        products[0].setProductoptions(options);

        ExtSysProdIdentifier[] extSysProdIdentifiers = new ExtSysProdIdentifier[1];
        products[0].setExternalSystemProductIdentifier(extSysProdIdentifiers);

        retrieveProductConditionsResponse.setProduct(products);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);

        PackageAccountSessionInfo packageAccountSessionInfo = new PackageAccountSessionInfo();
        packageAccountSessionInfo.setOfferResponse(new Arranged());
        when(sessionManager.getPackagedAccountSessionInfo()).thenReturn(packageAccountSessionInfo);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);

    }

    public void testFetchAppScoreResultsAndUpdateDecisionWithDecline(){
        AppScoreRequest appScoreRequest = new AppScoreRequest();
        appScoreRequest.setCreditScoreRequestNo("1234");
        appScoreRequest.setApplicationType(ApplicationType.QWEL);
        response.setASMCreditScoreResultCd("3");
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        pldAppealRequest.setApplicationStatus("1002");
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
        assertTrue(pldProductInfo.getIsAsmDecisionDecline());
    }
    
    
    @Test
    public void testSkipB274() {
        AppScoreRequest appScoreRequest = new AppScoreRequest();

        FacilitiesOffered[] facilitiesOffered = new FacilitiesOffered[2];
        facilitiesOffered[1] = new FacilitiesOffered();
        facilitiesOffered[1].setCSFacilityOfferedAm("00000000300100");
        facilitiesOffered[1].setCSFacilityOfferedCd("0102");

        response.setFacilitiesOffered(facilitiesOffered);

        ProductsOffered[] productsOffered = new ProductsOffered[2];
        productsOffered[0] = new ProductsOffered();
        productsOffered[0].setCSProductsOfferedCd("501");
        response.setProductsOffered(productsOffered);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setArrangementId("12345");
        arrangeToActivateParameters.setOfferedProductId("12345");
        //arrangeToActivateParameters.setOverdraftIntrestRates(new OverdraftIntrestRates());
        arrangeToActivateParameters.setProductFamilyIdentifier("502");
        when(sessionManager.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        RetrieveProductConditionsResponse retrieveProductConditionsResponse = new RetrieveProductConditionsResponse();
        Product[] products = new Product[2];
        products[0] = new Product();

        ProductOptions[] options = new ProductOptions[2];
        options[0] = new ProductOptions();
        products[0].setProductoptions(options);

        ExtSysProdIdentifier[] extSysProdIdentifiers = new ExtSysProdIdentifier[1];
        products[0].setExternalSystemProductIdentifier(extSysProdIdentifiers);

        retrieveProductConditionsResponse.setProduct(products);
        when(retrieveProductFeaturesService.retrieveProductFromFamily(any(RetrieveProductDTO.class))).thenReturn(retrieveProductConditionsResponse);

        PackageAccountSessionInfo packageAccountSessionInfo = new PackageAccountSessionInfo();
        packageAccountSessionInfo.setOfferResponse(new Arranged());
        when(sessionManager.getPackagedAccountSessionInfo()).thenReturn(packageAccountSessionInfo);
        PldAppealRequest pldAppealRequest=new PldAppealRequest();
        pldAppealRequest.setSkipOverdraft(true);
        PldProductInfo pldProductInfo = serviceImpl.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);

    }
}

