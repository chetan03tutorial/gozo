package com.lbg.ib.api.sales.dto.product.suitability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;

public enum SuitabilityProductOptions {

    LLOYDS_PRODUCT("LLOYDS", "PRODUCTS_LLOYDS_ED", "PRODUCTS_LLOYDS_YOUTH", "PRODUCTS_LLOYDS_STUDENT",
            "BANKRUPTCY_PRODUCT_KEY"),

    BOS_PRODUCT("BOS", "PRODUCTS_BOS_ED", "PRODUCTS_BOS_YOUTH", "PRODUCTS_BOS_STUDENT", "BANKRUPTCY_PRODUCT_KEY"),

    HALIFAX_PRODUCT("HALIFAX", "PRODUCTS_HALIFAX_ED", "PRODUCTS_HALIFAX_YOUTH", "PRODUCTS_HALIFAX_STUDENT",
            "HALIFAX_BANKRUPTCY_PRODUCT_KEY");

    private String            everyDayKey;
    private String            youthKey;
    private String            studentKey;
    private String            brand;
    private String            bankruptcyProductKey;

    private ConfigurationDAO  configuration;

    Map<String, List<Object>> brandProductMap = new HashMap<String, List<Object>>();

    SuitabilityProductOptions(String brand, String everyDayKey, String youthKey, String studentKey,
            String bankruptcyProductKey) {
        this.brand = brand;
        this.everyDayKey = everyDayKey;
        this.youthKey = youthKey;
        this.studentKey = studentKey;
        this.bankruptcyProductKey = bankruptcyProductKey;
    }

    public void populateProductBrandMap() {

        Map<String, Object> map = configuration.getConfigurationItems(everyDayKey);
        List<Object> mnemonics = new ArrayList<Object>(map.values());

        brandProductMap.put(everyDayKey, mnemonics);

        map = configuration.getConfigurationItems(youthKey);
        mnemonics = new ArrayList<Object>(map.values());

        brandProductMap.put(youthKey, mnemonics);

        map = configuration.getConfigurationItems(studentKey);
        mnemonics = new ArrayList<Object>(map.values());

        brandProductMap.put(studentKey, mnemonics);

        map = configuration.getConfigurationItems(bankruptcyProductKey);
        mnemonics = new ArrayList<Object>(map.values());

        brandProductMap.put(bankruptcyProductKey, mnemonics);
    }

    public static SuitabilityProductOptions getProductSuitabilityOption(String brand) {
        for (SuitabilityProductOptions optionEnum : SuitabilityProductOptions.values()) {
            if (optionEnum.brand.equalsIgnoreCase(brand)) {
                return optionEnum;
            }
        }
        return null;
    }

    public List<String> getSuitabilityProduct(SuitabilityProductOptions suitabilityProductOptions,
            List<EligibilityDetails> eligibilityDetailsList, String brand, String optionValue,
            boolean bankruptcyIndicator) {
        List<String> productList = new ArrayList<String>();
        if (brandProductMap.isEmpty()) {
            populateProductBrandMap();
        }
        List<Object> brandProductList = null;
        if ("STUDENT".equals(optionValue)) {
            if (bankruptcyIndicator) {
                brandProductList = brandProductMap.get(bankruptcyProductKey);
            } else {
                brandProductList = brandProductMap.get(studentKey);
            }
        } else if ("YOUTH".equals(optionValue)) {
            if (bankruptcyIndicator) {
                brandProductList = brandProductMap.get(bankruptcyProductKey);
            } else {
                brandProductList = brandProductMap.get(youthKey);
            }
        } else if ("EVERYDAY".equals(optionValue)) {
            if (bankruptcyIndicator) {
                brandProductList = brandProductMap.get(bankruptcyProductKey);
            } else {
                brandProductList = brandProductMap.get(everyDayKey);
            }
        }

        for (EligibilityDetails eligibilityDetail : eligibilityDetailsList) {
            if (brandProductList != null && brandProductList.contains(eligibilityDetail.getMnemonic())) {
                productList.add(eligibilityDetail.getMnemonic());
            }
        }

        return productList;
    }

    public void setConfiguration(ConfigurationDAO configuration) {
        this.configuration = configuration;
    }

}
