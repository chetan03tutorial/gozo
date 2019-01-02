package com.lbg.ib.api.sales.asm.domain;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018.
 */
public enum ApplicationType {
    CAAS("012"),
    QWEL("025"),
	A034("034"),
	A008("008");

    private String input;

    private ApplicationType(String input) {
        this.input = input;
    }

    public String getValue(){
        return input;
    }
}
