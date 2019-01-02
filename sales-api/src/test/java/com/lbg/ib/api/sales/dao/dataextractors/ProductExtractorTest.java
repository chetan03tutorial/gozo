package com.lbg.ib.api.sales.dao.dataextractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdFamilyIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductFamily;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOffer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;

@RunWith(MockitoJUnitRunner.class)
public class ProductExtractorTest {

    @Test
    public void testExtractForSuccess() throws Exception {
        Product product = new Product();
        product.setProductName("productName");
        ProductOffer[] productoffers = new ProductOffer[1];
        ProductOffer prodOffer = new ProductOffer();
        prodOffer.setOfferType("2001");
        productoffers[0] = prodOffer;
        product.setProductoffer(productoffers);
        ProductOptions[] productoptions = new ProductOptions[1];
        ProductOptions prodOption = new ProductOptions();
        prodOption.setOptionsName("optionsName");
        prodOption.setOptionsValue("optionsValue");
        productoptions[0] = prodOption;
        product.setProductoptions(productoptions);
        ExtSysProdIdentifier[] externalSystemProductIdentifier = new ExtSysProdIdentifier[1];
        ExtSysProdIdentifier extSysProdId = new ExtSysProdIdentifier();
        extSysProdId.setSystemCode("00010");
        extSysProdId.setProductIdentifier("productIdentifier");
        externalSystemProductIdentifier[0] = extSysProdId;
        product.setExternalSystemProductIdentifier(externalSystemProductIdentifier);
        product.setProductIdentifier("productIdentifier");
        ProductFamily[] associatedFamily = new ProductFamily[1];
        ProductFamily assFamily = new ProductFamily();
        ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier[1];
        ExtSysProdFamilyIdentifier extsysprodfamID = new ExtSysProdFamilyIdentifier();
        extsysprodfamID.setProductFamilyIdentifier("productFamilyIdentifier");
        extsysprodfamilyidentifier[0] = extsysprodfamID;
        assFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifier);
        associatedFamily[0] = assFamily;
        product.setAssociatedFamily(associatedFamily);
        ProductExtractor productExtractor = new ProductExtractor(product).extract();
        assertEquals(productExtractor.getProductName(), "productName");
        assertEquals(productExtractor.getOfferTypeString(), "NORMAL");
        assertEquals(productExtractor.getMnemonic(), "productIdentifier");

    }

    @Test
    public void testExtractForSuccessWhenCBSIdentifier() throws Exception {
        Product product = new Product();
        product.setProductName("productName");
        ProductOffer[] productoffers = new ProductOffer[1];
        ProductOffer prodOffer = new ProductOffer();
        prodOffer.setOfferType("2004");
        productoffers[0] = prodOffer;
        product.setProductoffer(productoffers);
        ProductOptions[] productoptions = new ProductOptions[1];
        ProductOptions prodOption = new ProductOptions();
        prodOption.setOptionsName("optionsName");
        prodOption.setOptionsValue("optionsValue");
        productoptions[0] = prodOption;
        product.setProductoptions(productoptions);
        ExtSysProdIdentifier[] externalSystemProductIdentifier = new ExtSysProdIdentifier[1];
        ExtSysProdIdentifier extSysProdId = new ExtSysProdIdentifier();
        extSysProdId.setSystemCode("00004");
        extSysProdId.setProductIdentifier("productIdentifier");
        externalSystemProductIdentifier[0] = extSysProdId;
        product.setExternalSystemProductIdentifier(externalSystemProductIdentifier);
        product.setProductIdentifier("productIdentifier");
        ProductFamily[] associatedFamily = new ProductFamily[1];
        ProductFamily assFamily = new ProductFamily();
        ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier[1];
        ExtSysProdFamilyIdentifier extsysprodfamID = new ExtSysProdFamilyIdentifier();
        extsysprodfamID.setProductFamilyIdentifier("productFamilyIdentifier");
        extsysprodfamilyidentifier[0] = extsysprodfamID;
        assFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifier);
        associatedFamily[0] = assFamily;
        product.setAssociatedFamily(associatedFamily);
        ProductExtractor productExtractor = new ProductExtractor(product).extract();
        assertEquals(productExtractor.getProductName(), "productName");
        assertEquals(productExtractor.getOfferTypeString(), "TYPICAL");
        assertEquals(productExtractor.getCbsProductNumber(), "productIdentifier");
        assertEquals(productExtractor.getCbsProductNumberTrimmed(), "prod");

    }

    @Test
    public void testExtractwhenProductIsNull() throws Exception {

        ProductExtractor productExtractor = new ProductExtractor(null).extract();
        assertNull(productExtractor.getProductName());

    }

    @Test(expected = NullPointerException.class)
    public void testExtractWhenOfferTypeIsNull() throws Exception {
        Product product = new Product();
        product.setProductName("productName");
        ProductOffer[] productoffers = new ProductOffer[1];
        ProductOffer prodOffer = new ProductOffer();
        prodOffer.setOfferType(null);
        productoffers[0] = prodOffer;
        product.setProductoffer(productoffers);
        ProductExtractor productExtractor = new ProductExtractor(product).extract();

    }

    @Test(expected = NullPointerException.class)
    public void testExtractWhenOfferTypeIsNotValid() throws Exception {
        Product product = new Product();
        product.setProductName("productName");
        ProductOffer[] productoffers = new ProductOffer[1];
        ProductOffer prodOffer = new ProductOffer();
        prodOffer.setOfferType("2005");
        productoffers[0] = prodOffer;
        product.setProductoffer(productoffers);
        ProductExtractor productExtractor = new ProductExtractor(product).extract();

    }

    @Test
    public void testExtractWhenProductOptionNProductIdentifierIsNull() throws Exception {
        Product product = new Product();
        product.setProductName("productName");
        ProductOffer[] productoffers = new ProductOffer[1];
        ProductOffer prodOffer = new ProductOffer();
        prodOffer.setOfferType("2001");
        productoffers[0] = prodOffer;
        product.setProductoffer(productoffers);
        ExtSysProdIdentifier[] externalSystemProductIdentifier = new ExtSysProdIdentifier[1];
        ExtSysProdIdentifier extSysProdId = new ExtSysProdIdentifier();
        extSysProdId.setSystemCode("00010");
        extSysProdId.setProductIdentifier("productIdentifier");
        externalSystemProductIdentifier[0] = extSysProdId;
        product.setExternalSystemProductIdentifier(externalSystemProductIdentifier);
        ProductFamily[] associatedFamily = new ProductFamily[1];
        ProductFamily assFamily = new ProductFamily();
        ExtSysProdFamilyIdentifier[] extsysprodfamilyidentifier = new ExtSysProdFamilyIdentifier[1];
        ExtSysProdFamilyIdentifier extsysprodfamID = new ExtSysProdFamilyIdentifier();
        extsysprodfamID.setProductFamilyIdentifier("productFamilyIdentifier");
        extsysprodfamilyidentifier[0] = extsysprodfamID;
        assFamily.setExtsysprodfamilyidentifier(extsysprodfamilyidentifier);
        associatedFamily[0] = assFamily;
        product.setAssociatedFamily(associatedFamily);
        ProductExtractor productExtractor = new ProductExtractor(product).extract();
        assertEquals(productExtractor.getProductName(), "productName");
        assertEquals(productExtractor.getOfferTypeString(), "NORMAL");
        assertNull(productExtractor.getProductIdentifier());
        assertEquals(productExtractor.getMapOfProductOptions().size(), 0);

    }

    @Test
    public void testExtractWhenExtSysProdFamilyIdIsNull() throws Exception {
        Product product = new Product();
        product.setProductName("productName");
        ProductOffer[] productoffers = new ProductOffer[1];
        ProductOffer prodOffer = new ProductOffer();
        prodOffer.setOfferType("2001");
        productoffers[0] = prodOffer;
        product.setProductoffer(productoffers);
        ExtSysProdIdentifier[] externalSystemProductIdentifier = new ExtSysProdIdentifier[1];
        ExtSysProdIdentifier extSysProdId = new ExtSysProdIdentifier();
        extSysProdId.setSystemCode("00010");
        extSysProdId.setProductIdentifier("productIdentifier");
        externalSystemProductIdentifier[0] = extSysProdId;
        product.setExternalSystemProductIdentifier(externalSystemProductIdentifier);
        ProductFamily[] associatedFamily = new ProductFamily[1];
        ProductFamily assFamily = new ProductFamily();
        assFamily.setExtsysprodfamilyidentifier(null);
        associatedFamily[0] = assFamily;
        product.setAssociatedFamily(associatedFamily);
        ProductExtractor productExtractor = new ProductExtractor(product).extract();
        assertNull(productExtractor.getProductFamilyIdentifier());

    }

    @Test
    public void testExtractWhenAssociatedFamilyIsNull() throws Exception {
        Product product = new Product();
        product.setProductName("productName");
        ProductOffer[] productoffers = new ProductOffer[1];
        ProductOffer prodOffer = new ProductOffer();
        prodOffer.setOfferType("2001");
        productoffers[0] = prodOffer;
        product.setProductoffer(productoffers);
        ExtSysProdIdentifier[] externalSystemProductIdentifier = new ExtSysProdIdentifier[1];
        ExtSysProdIdentifier extSysProdId = new ExtSysProdIdentifier();
        extSysProdId.setSystemCode("00010");
        extSysProdId.setProductIdentifier("productIdentifier");
        externalSystemProductIdentifier[0] = extSysProdId;
        product.setExternalSystemProductIdentifier(externalSystemProductIdentifier);
        ProductExtractor productExtractor = new ProductExtractor(product).extract();
        assertNull(productExtractor.getProductFamilyIdentifier());

    }

}
