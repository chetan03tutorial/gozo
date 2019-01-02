package com.lbg.ib.api.sales.product.domain.arrangement;

import org.apache.commons.lang.StringUtils;

public enum OfferType {

    NORMAL("2001"), UPSELL("2002"), DOWNSELL("2003"), TYPICAL("2004");

    private final String offerTypeCode;

    OfferType(String offerTypeCode) {
        this.offerTypeCode = offerTypeCode;
    }

    public String getOfferTypeCode() {
        return (this.offerTypeCode);
    }

    public static OfferType findOfferTypeFromCode(String codeOfRequiredOfferType) {
        if (StringUtils.isBlank(codeOfRequiredOfferType)) {
            return null;
        }

        for (OfferType offerType : values()) {
            if (offerType.getOfferTypeCode().equals(codeOfRequiredOfferType)) {
                return offerType;
            }
        }
        return null;
    }
}
