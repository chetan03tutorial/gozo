package com.lbg.ib.api.sales.common.validation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.lbg.ib.api.shared.validation.FieldValidation;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.util.ApplicationProperties;

@Component
public class ProductOptionValidation implements FieldValidation {
    
    private static List<String> arrangementIds;
    
    public ProductOptionValidation() throws IOException {
        arrangementIds = Arrays.asList(StringUtils
                .split(ApplicationProperties.getPropValue("sales-api.icobs.urca.arrangement.question.ids"), ","));
    }
    
    public boolean doesFail(Object obj) {
        boolean status = false;
        if (obj != null) {
            return !(arrangementIds.contains(obj));
        }
        return status;
    }
    
    public String message() {
        return "Invalid Question Code";
    }
    
}
