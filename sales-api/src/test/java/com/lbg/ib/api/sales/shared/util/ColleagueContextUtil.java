package com.lbg.ib.api.sales.shared.util;

import com.lbg.ib.api.sales.mca.domain.ColleagueContext;

public class ColleagueContextUtil {

    public static ColleagueContext prepareColleagueContext(boolean authorizedFlag) {
        return new ColleagueContext("871515", authorizedFlag);
    }
}
