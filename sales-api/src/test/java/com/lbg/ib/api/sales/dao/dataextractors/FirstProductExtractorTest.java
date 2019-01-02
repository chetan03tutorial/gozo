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
public class FirstProductExtractorTest {

    @Test
    public void testExtractForSuccess() throws Exception {
        Product[] products = new Product[1];
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
        products[0] = product;
        FirstProductExtractor firstProductExtractor = new FirstProductExtractor(products).extract();
        assertEquals(firstProductExtractor.getProductExtractor().getProductName(), "productName");
        assertEquals(firstProductExtractor.getProductExtractor().getOfferTypeString(), "NORMAL");
        assertEquals(firstProductExtractor.getProductExtractor().getMnemonic(), "productIdentifier");

    }

    @Test
    public void testExtractWhenProductIsNull() throws Exception {
        FirstProductExtractor firstProductExtractor = new FirstProductExtractor(null).extract();
        assertNull(firstProductExtractor.getProductExtractor().getProductName());

    }

}
