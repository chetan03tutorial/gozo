package com.lbg.ib.api.sales.dto.postcode;

import com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.domain.PostcodeExternal;

public class DTOtoExternalMapper {
    public PostcodeExternal map(PostcodeDTO postcode) {
        return new PostcodeExternal(postcode.getOutPostcode(), postcode.getInPostcode());
    }
}
