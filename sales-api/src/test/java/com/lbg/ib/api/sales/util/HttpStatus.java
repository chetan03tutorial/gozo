package com.lbg.ib.api.sales.util;

public enum HttpStatus {
    
    OK(200), BAD_REQUEST(400);
    
    private int status;
    
    private HttpStatus(int status) {
        this.status = status;
    }
    
    public int status() {
        return this.status;
    }
}
