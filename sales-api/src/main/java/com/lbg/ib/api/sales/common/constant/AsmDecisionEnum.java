package com.lbg.ib.api.sales.common.constant;

public enum AsmDecisionEnum {
    ACCEPT("1", "ACCEPT"),
    REFERED("2", "REFER"),
    DECLINED("3", "DECLINE"),
    UNSCORED("4", "UNSCORED");

    String asmDecisionResultCd;
    String asmDecision;

    AsmDecisionEnum(String asmDecisionResultCd, String asmDecision) {
        this.asmDecisionResultCd = asmDecisionResultCd;
        this.asmDecision = asmDecision;
    }

    public String getAsmDecision() {
        return asmDecision;
    }

    public String getAsmDecisionResultCd() {
        return asmDecisionResultCd;
    }
}
