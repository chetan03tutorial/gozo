package com.lbg.ib.api.sales.dto.product.base;

import com.lbg.ib.api.sso.domain.user.UserContext;

public class BaseDTO {

    private UserContext userContext;

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

}
