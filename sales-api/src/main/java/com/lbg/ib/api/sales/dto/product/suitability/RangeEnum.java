package com.lbg.ib.api.sales.dto.product.suitability;

import java.util.Arrays;
import java.util.List;

public enum RangeEnum {

    LESS_THAN_TWELVE("12MonthsOrLess",
            Arrays.asList(new Range[] { new Range("0-03"), new Range("0-06"), new Range("0-09"), new Range("0-12") })),

    MORE_THAN_TWELVE("MoreThan12Months", Arrays.asList(new Range[] { new Range("0-18"), new Range("0-24"),
            new Range("0-36"), new Range("0-48"), new Range("0-60") }));

    private String      range;

    private List<Range> rangeList;

    public String getRange() {
        return range;
    }

    public List<Range> getRangeList() {
        return rangeList;
    }

    private RangeEnum(String range, List<Range> rangeList) {
        this.range = range;
        this.rangeList = rangeList;
    }

    public static RangeEnum getRangeByName(List<String> values) {

        if (values.isEmpty()) {
            return null;
        }
        for (RangeEnum rangeEnum : values()) {
            if (rangeEnum.getRange().equalsIgnoreCase(values.get(0))) {
                return rangeEnum;
            }

        }
        return null;

    }
}