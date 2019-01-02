package com.lbg.ib.api.sales.testing;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class TestingHelper {
    public static String contentOf(String file) {
        try {
            return IOUtils.toString(TestingHelper.class.getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
