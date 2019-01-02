package com.lbg.ib.api.sales.product.service;


import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 09thApr2018
 ***********************************************************************/
@Component
public class WorkingDaysServiceImpl implements WorkingDaysService {


    public static final String      HOLIDAY_DATE_FORMAT         = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final SimpleDateFormat HOLIDAY_DATE_FORMATTER = new SimpleDateFormat(HOLIDAY_DATE_FORMAT);

    public static final String      HOLIDAY_DATE_RESPONSE_FORMAT         = "dd/MM/yyyy";
    public static final SimpleDateFormat HOLIDAY_DATE_RESPONSE_FORMATTER = new SimpleDateFormat(HOLIDAY_DATE_RESPONSE_FORMAT);

    @Autowired
    private ReferenceDataServiceDAO referenceData;

    @Autowired
    private LoggerDAO logger;

    private static final int SEVENTH_WORKING_DAY = 6;

    public Set<String> fetchBankHolidays(){
        final Map<String, String> holidayMap = referenceData.getBankHolidays();
        Set<String> bankHolidays = null;
        if(holidayMap != null && !holidayMap.isEmpty()){
            bankHolidays = holidayMap.keySet();
        }

        return bankHolidays;
    }

    public List<String> fetchNextWorkingDaysAfterWeek(Set<String> bankHolidays,int range, Date fromDate){
        List<String> workingDays = fetchNextWorkingDays(bankHolidays,range,fromDate);
        if(workingDays.size()<SEVENTH_WORKING_DAY){
            return new ArrayList<String>();
        }
        workingDays = workingDays.subList(SEVENTH_WORKING_DAY,workingDays.size());
        return workingDays;
    }

    public List<String> fetchNextWorkingDays(Set<String> bankHolidays,int range, Date fromDate){
        Calendar c = Calendar.getInstance();
        c.setTime(fromDate);
        int count=0;
        List<String> workingDaysList = new ArrayList<String>();
        while(count<range) {
            c.add(Calendar.DATE, 1);  // number of days to add
            if(!isHoliday(c,bankHolidays)){
                workingDaysList.add(HOLIDAY_DATE_RESPONSE_FORMATTER.format(c.getTime()));
            }
            count++;
        }
        return workingDaysList;
    }

    public boolean isHoliday(Calendar calendar,Set<String> bankHolidays){
        boolean isHoliday = false;
        if(isWeekend(calendar)){
            isHoliday = true;
        }else{
            isHoliday = isBankHoliday(calendar,bankHolidays);
        }
        return isHoliday;
    }

    public boolean isWeekend(Calendar cal){
        if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)  || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
            return true;
        } else {
            return false;
        }
    }

    public  boolean isBankHoliday(Calendar cal,Set<String> bankHolidays){
        // Bank Holidays are of This Format [2018-05-07 00:00:00.0, 2018-05-28 00:00:00.0]
        Calendar holCal = Calendar.getInstance();

        if(bankHolidays!=null) {
            for (String holidayDateStr : bankHolidays) {
                try {
                    Date holidayDate = HOLIDAY_DATE_FORMATTER.parse(holidayDateStr);
                    holCal.setTime(holidayDate);
                    if (cal.get(Calendar.YEAR) == holCal.get(Calendar.YEAR) &&
                            cal.get(Calendar.MONTH) == holCal.get(Calendar.MONTH) &&
                            cal.get(Calendar.DAY_OF_MONTH) == holCal.get(Calendar.DAY_OF_MONTH)) {
                        return true;
                    }
                } catch (ParseException exception) {
                    logger.logException(this.getClass(), exception);
                }
            }
        }
        return false;
    }
}
