package com.lbg.ib.api.sales.docgen.util;
/*
Created by Rohit.Soni at 08/05/2018 18:35
*/

public class UniqueReferenceNumber {
    static long current = System.currentTimeMillis();
    static public synchronized long get(){
        return current++;
    }
}
