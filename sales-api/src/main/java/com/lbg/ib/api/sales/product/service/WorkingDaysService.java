package com.lbg.ib.api.sales.product.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 09thApr2018
 ***********************************************************************/
public interface WorkingDaysService {

    /**
     * Fetches the  Bank Holidays
     *
     * @response Set of Bank Holidays
     */
    public Set<String> fetchBankHolidays();
    /**
     * Fetches the Next Working Days After Week
     *
     *
     * @param bankHolidays set of Bank Holidays
     * @param range set of Upto which the working days are to be fetched
     * @param fromDate fromDate from where the Dates are to be fetched
     * @response List of Working Days after a week's time
     */
    public List<String> fetchNextWorkingDaysAfterWeek(Set<String> bankHolidays,int range, Date fromDate);
    /**
     * Fetches the Next Working Days
     *
     *
     * @param bankHolidays set of Bank Holidays
     * @param range set of Upto which the working days are to be fetched
     * @param fromDate fromDate from where the Dates are to be fetched
     * @response List of Working Days
     */
    public List<String> fetchNextWorkingDays(Set<String> bankHolidays,int range,Date fromDate);

    /**
     * Identifies if Day is a weekend
     *
     *
     * @param date To check if Date is current Date
     * @response boolean
     */
    public boolean isWeekend(Calendar cal);
    /**
     * Identifies if Day is a bankHoliday
     *
     *
     * @param date To check if Date is current Date
     * @param bankHolidays Set of BankHolidays
     * @response boolean
     */
    public  boolean isBankHoliday(Calendar cal,Set<String> bankHolidays);
}

