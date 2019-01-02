package com.lbg.ib.api.sales.product.domain.suitability;

import java.util.List;

public class SuitableProduct {

    private List<String>  suitableProducts;
    private List<Product> products;

    public SuitableProduct(List<String> suitableProducts, List<Product> products) {
        this.suitableProducts = suitableProducts;
        this.products = products;
    }

    public SuitableProduct() {
        /* default constructor */
    }

    public List<String> getSuitableProducts() {
        return suitableProducts;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "SuitableProducts [products=" + products + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((products == null) ? 0 : products.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SuitableProduct other = (SuitableProduct) obj;
        if (products == null) {
            if (other.products != null) {
                return false;
            }
        } else if (!products.equals(other.products)) {
            return false;
        }
        return true;
    }

}
