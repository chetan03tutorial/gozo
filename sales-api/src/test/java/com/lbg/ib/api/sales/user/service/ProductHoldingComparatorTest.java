package com.lbg.ib.api.sales.user.service;

import com.lbg.ib.api.sales.product.domain.Product;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class ProductHoldingComparatorTest {
    @Test
    public void returnMinus1WhenFirstProductInputIsNull() {
        Product product1 = null;
        Product product2 = new Product();
        ProductHoldingComparator productHoldingComparator = new ProductHoldingComparator();
        Assert.assertEquals(-1, productHoldingComparator.compare(product1, product2));
    }

    @Test
    public void return1WhenSecondProductInputIsNull() {
        Product product1 = new Product();
        Product product2 = null;
        ProductHoldingComparator productHoldingComparator = new ProductHoldingComparator();
        Assert.assertEquals(1, productHoldingComparator.compare(product1, product2));
    }

    @Test
    public void return1WhenFirstAndSecondProductDisplayOrderAreSameAndAccountOpenedDateOfFirstIsLaterWithSecondNullDate() {
        Product product1 = new Product();
        product1.setDisplayOrder(1);
        product1.setAccountOpenedDate(Calendar.getInstance());
        Product product2 = new Product();
        product2.setDisplayOrder(1);
        Calendar earlierDate = Calendar.getInstance();
        long millis = earlierDate.getTimeInMillis() - 20000;
        earlierDate.setTimeInMillis(millis);
        product2.setAccountOpenedDate(null);
        ProductHoldingComparator productHoldingComparator = new ProductHoldingComparator();
        Assert.assertEquals(0, productHoldingComparator.compare(product1, product2));
    }

    @Test
    public void return1WhenFirstAndSecondProductDisplayOrderAreSameAndAccountOpenedDateOfFirstIsLaterWithFirstNullDate() {
        Product product1 = new Product();
        product1.setDisplayOrder(1);
        product1.setAccountOpenedDate(null);
        Product product2 = new Product();
        product2.setDisplayOrder(1);
        Calendar earlierDate = Calendar.getInstance();
        long millis = earlierDate.getTimeInMillis() - 20000;
        earlierDate.setTimeInMillis(millis);
        product2.setAccountOpenedDate(earlierDate);
        ProductHoldingComparator productHoldingComparator = new ProductHoldingComparator();
        Assert.assertEquals(0, productHoldingComparator.compare(product1, product2));
    }

    @Test
    public void return1WhenFirstAndSecondProductDisplayOrderAreSameAndAccountOpenedDateOfFirstIsLater() {
        Product product1 = new Product();
        product1.setDisplayOrder(1);
        product1.setAccountOpenedDate(Calendar.getInstance());
        Product product2 = new Product();
        product2.setDisplayOrder(1);
        Calendar earlierDate = Calendar.getInstance();
        long millis = earlierDate.getTimeInMillis() - 20000;
        earlierDate.setTimeInMillis(millis);
        product2.setAccountOpenedDate(earlierDate);
        ProductHoldingComparator productHoldingComparator = new ProductHoldingComparator();
        Assert.assertEquals(1, productHoldingComparator.compare(product1, product2));
    }

    @Test
    public void returnMinus1WhenTheDisplayOrderOfFirstProductIsLowerThanTheSecondProducts() {
        Product product1 = new Product();
        product1.setDisplayOrder(1);
        Product product2 = new Product();
        product2.setDisplayOrder(2);
        ProductHoldingComparator productHoldingComparator = new ProductHoldingComparator();
        Assert.assertEquals(-1, productHoldingComparator.compare(product1, product2));
    }
}
