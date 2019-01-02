package com.lbg.ib.api.sales.asm.domain;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018.
 */
public enum ModifyArrangementOperationType {
    EIDV_OPERATION("updateEidvSubmission"),
    OD_OPERATION("updateODOperation");

    private String input;

    private ModifyArrangementOperationType(String input) {
        this.input = input;
    }

    public String getValue(){
        return input;
    }
}
