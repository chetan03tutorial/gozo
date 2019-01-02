/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.dataextractors;

import static com.lbg.ib.api.sales.product.domain.arrangement.OfferType.findOfferTypeFromCode;
import static org.apache.commons.lang.ArrayUtils.isNotEmpty;

import java.util.HashMap;
import java.util.Map;

import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdFamilyIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductFamily;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOffer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;

public class ProductExtractor {
    private static final String CBS_PROD_NUM_ID         = "00004";
    private final Product       product;

    private String              productName             = null;
    private String              offerTypeString         = null;
    private Map<String, String> mapOfProductOptions     = new HashMap<String, String>();
    private String              mnemonic                = null;
    private String              cbsProductNumberTrimmed = null;
    private String              productIdentifier       = null;
    private String              productFamilyIdentifier = null;
    private String              cbsProductNumber        = null;
    private static final String MNEMONIC_ID             = "00010";

    public ProductExtractor(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public String getOfferTypeString() {
        return offerTypeString;
    }

    public Map<String, String> getMapOfProductOptions() {
        return mapOfProductOptions;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getCbsProductNumberTrimmed() {
        return cbsProductNumberTrimmed;
    }

    public void setCbsProductNumberTrimmed(String cbsProductNumberTrimmed) {
        this.cbsProductNumberTrimmed = cbsProductNumberTrimmed;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public String getProductFamilyIdentifier() {
        return productFamilyIdentifier;
    }

    public String getCbsProductNumber() {
        return cbsProductNumber;
    }

    public void setCbsProductNumber(String cbsProductNumber) {
        this.cbsProductNumber = cbsProductNumber;
    }

    public ProductExtractor extract() {
        if (product == null) {
            return this;
        }
        deriveProductName(product);
        deriveOfferType(product);
        deriveProductOptions(product);
        deriveMnemonic(product);
        deriveCBSProductNumber(product);
        deriveProductIdentifier(product);
        deriveProductFamilyIdentifier(product);
        return this;
    }

    private void deriveProductName(Product firstOfferedProduct) {
        productName = firstOfferedProduct.getProductName();
    }

    private void deriveMnemonic(Product product) {
        for (ExtSysProdIdentifier identifier : product.getExternalSystemProductIdentifier()) {
            if (MNEMONIC_ID.equals(identifier.getSystemCode())) {
                mnemonic = identifier.getProductIdentifier();
                break;
            }

        }
    }

    private void deriveCBSProductNumber(Product product) {
        for (ExtSysProdIdentifier identifier : product.getExternalSystemProductIdentifier()) {

            if (CBS_PROD_NUM_ID.equals(identifier.getSystemCode())) {
                cbsProductNumber = identifier.getProductIdentifier();
                cbsProductNumberTrimmed = identifier.getProductIdentifier().substring(0, 4);
                break;
            }
        }
    }

    private void deriveProductOptions(Product product) {
        ProductOptions[] productOptions = product.getProductoptions();
        if (productOptions != null) {
            for (ProductOptions pdtOption : productOptions) {
                mapOfProductOptions.put(pdtOption.getOptionsName(), pdtOption.getOptionsValue());
            }
        }
    }

    private void deriveOfferType(Product product) {
        ProductOffer[] productOffers = product.getProductoffer();
        if (isNotEmpty(productOffers) && productOffers[0] != null) {
            offerTypeString = findOfferTypeFromCode(productOffers[0].getOfferType()).toString();
        }
    }

    private void deriveProductIdentifier(Product product) {
        if (product.getProductIdentifier() != null) {
            productIdentifier = product.getProductIdentifier();
        }
    }

    private void deriveProductFamilyIdentifier(Product product) {
        ProductFamily[] pdtFamily = product.getAssociatedFamily();
        // We're expecting only one element under ProductFamily[]
        if (isNotEmpty(pdtFamily) && pdtFamily[0] != null) {
            ExtSysProdFamilyIdentifier[] extSysProdFamilyIdentifiers = pdtFamily[0].getExtsysprodfamilyidentifier();
            if (isNotEmpty(extSysProdFamilyIdentifiers) && extSysProdFamilyIdentifiers[0] != null) {
                productFamilyIdentifier = extSysProdFamilyIdentifiers[0].getProductFamilyIdentifier();
            }
        }
    }
}
