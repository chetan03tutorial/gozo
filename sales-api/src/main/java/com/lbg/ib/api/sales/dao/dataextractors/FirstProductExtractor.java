/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.dataextractors;

import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;

import static org.apache.commons.lang.ArrayUtils.isNotEmpty;

public class FirstProductExtractor {
    private final Product[]  products;

    private ProductExtractor productExtractor;

    public FirstProductExtractor(Product... offeredProducts) {
        products = offeredProducts;
    }

    public FirstProductExtractor extract() {
        Product product = null;
        if (isNotEmpty(products)) {
            product = products[0];
        }
        productExtractor = new ProductExtractor(product).extract();

        return this;
    }

    public ProductExtractor getProductExtractor() {
        return productExtractor;
    }
}
