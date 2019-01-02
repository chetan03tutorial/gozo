package com.lbg.ib.api.sales.user.service;

import java.io.Serializable;
import java.util.Comparator;

import com.lbg.ib.api.sales.product.domain.Product;

public class ProductHoldingComparator implements Comparator<Product>, Serializable {

    private static final long serialVersionUID = 1555915366824853628L;

    public int compare(Product account1, Product account2) {

        if (account1 == null) {
            return -1;
        }
        if (account2 == null) {
            return 1;
        }

        if ((account1.getDisplayOrder() == account2.getDisplayOrder()) && null != account1.getAccountOpenedDate()
                && null != account2.getAccountOpenedDate()) {
            return account1.getAccountOpenedDate().compareTo(account2.getAccountOpenedDate());
        }
        return account1.getDisplayOrder().compareTo(account2.getDisplayOrder());
    }

}
