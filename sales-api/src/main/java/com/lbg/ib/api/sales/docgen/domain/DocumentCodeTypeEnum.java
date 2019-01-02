package com.lbg.ib.api.sales.docgen.domain;
/*
Created by Rohit.Soni at 08/05/2018 18:12
*/

public enum DocumentCodeTypeEnum {
    SODN("1001"),
    WELCOME_EMAIL("1002"),
    OVERDRAFT_LETTER("1003");

    String documentCodeValue;

    DocumentCodeTypeEnum(String documentCodeVal){
        this.documentCodeValue = documentCodeVal;
    }

    public String getDocumentCodeValue(){
        return documentCodeValue;
    }

}
