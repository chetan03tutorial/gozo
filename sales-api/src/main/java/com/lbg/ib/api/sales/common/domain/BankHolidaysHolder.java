package com.lbg.ib.api.sales.common.domain;

import java.util.Set;

/**
 * Created by Rohit Soni on 12/03/2018.
 * PCA-6522
 * Holder class for bank holidays
 */

public class BankHolidaysHolder {

    private Set<String> bankHolidays = null;

    public Set<String> getBankHolidays() {
        return bankHolidays;
    }

    public void setBankHolidays(Set<String> bankHolidays) {
        this.bankHolidays = bankHolidays;
    }
}
