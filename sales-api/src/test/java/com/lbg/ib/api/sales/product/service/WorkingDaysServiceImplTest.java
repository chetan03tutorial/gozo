package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Debashish on 09/04/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class WorkingDaysServiceImplTest {

    public static final String      HOLIDAY_DATE_FORMAT         = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final SimpleDateFormat HOLIDAY_DATE_FORMATTER = new SimpleDateFormat(HOLIDAY_DATE_FORMAT);

    public static final String      HOLIDAY_DATE_RESPONSE_FORMAT         = "dd/MM/yyyy";
    public static final SimpleDateFormat HOLIDAY_DATE_RESPONSE_FORMATTER = new SimpleDateFormat(HOLIDAY_DATE_RESPONSE_FORMAT);

    @Mock
    LoggerDAO logger;

    @InjectMocks
    WorkingDaysServiceImpl fetchNextWorkingDaysServiceImpl;

    @Test
    public void testIsBankHolidayWhenDatePassedIsBankHoliday() throws ParseException {

        Set<String> bankHolidays = new HashSet<String>();
        bankHolidays.add("2018-04-11 00:00:00.0");
        Date date = HOLIDAY_DATE_FORMATTER.parse("2018-04-11 00:00:00.0");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertTrue(fetchNextWorkingDaysServiceImpl.isBankHoliday(cal , bankHolidays));
    }


    @Test
    public void testIsBankHolidayWhenDatePassedNotIsBankHoliday() throws ParseException {
        Set<String> bankHolidays = new HashSet<String>();
        bankHolidays.add("2018-04-11 00:00:00.0");
        Date date = HOLIDAY_DATE_FORMATTER.parse("2018-04-12 00:00:00.0");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertFalse(fetchNextWorkingDaysServiceImpl.isBankHoliday(cal , bankHolidays));
    }

    @Test
    public void testIsWeekendWhenDatePassedNotIsWeekend() throws ParseException {
        Date date = HOLIDAY_DATE_FORMATTER.parse("2018-04-08 00:00:00.0");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertTrue(fetchNextWorkingDaysServiceImpl.isWeekend(cal));
    }


    @Test
    public void testIsWeekendWhenDatePassedNotIsNotWeekend() throws ParseException {
        Date date = HOLIDAY_DATE_FORMATTER.parse("2018-04-09 00:00:00.0");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertFalse(fetchNextWorkingDaysServiceImpl.isWeekend(cal));
    }

    @Test
    public void testfetchNextWorkingDaysWithGivenRange() throws ParseException {
        Set<String> bankHolidays = new HashSet<String>();
        bankHolidays.add("2018-04-25 00:00:00.0");
        List<String> workingDays = fetchNextWorkingDaysServiceImpl.fetchNextWorkingDays(bankHolidays,30,HOLIDAY_DATE_FORMATTER.parse("2018-04-22 00:00:00.0"));
        assertTrue(workingDays.size()<30);
        for(String workingDay : workingDays){
            Calendar cal = Calendar.getInstance();
            cal.setTime(HOLIDAY_DATE_RESPONSE_FORMATTER.parse(workingDay));
            assertFalse(fetchNextWorkingDaysServiceImpl.isWeekend(cal));
            cal.setTime(HOLIDAY_DATE_RESPONSE_FORMATTER.parse(workingDay));
            assertFalse(fetchNextWorkingDaysServiceImpl.isBankHoliday(cal,bankHolidays));
        }
    }

    @Test
    public void testFetchNextWorkingDaysAfterWeekWithGivenRange() throws ParseException{
        Set<String> bankHolidays = new HashSet<String>();
        bankHolidays.add("2018-04-25 00:00:00.0");
        List<String> workingDays = fetchNextWorkingDaysServiceImpl.fetchNextWorkingDays(bankHolidays,30,HOLIDAY_DATE_FORMATTER.parse("2018-04-22 00:00:00.0"));
        assertTrue(workingDays.size()<30);
        for(String workingDay : workingDays){
            Calendar cal = Calendar.getInstance();
            cal.setTime(HOLIDAY_DATE_RESPONSE_FORMATTER.parse(workingDay));
            assertFalse(fetchNextWorkingDaysServiceImpl.isWeekend(cal));
            cal.setTime(HOLIDAY_DATE_RESPONSE_FORMATTER.parse(workingDay));
            assertFalse(fetchNextWorkingDaysServiceImpl.isBankHoliday(cal,bankHolidays));
        }

        List<String> workingDaysAfterWeek = fetchNextWorkingDaysServiceImpl.fetchNextWorkingDaysAfterWeek(bankHolidays,30,HOLIDAY_DATE_FORMATTER.parse("2018-04-22 00:00:00.0"));
        assertTrue(workingDays.size()-workingDaysAfterWeek.size()==6);
    }


    @Test
    public void testFetchNextWorkingDaysAfterWeekWithGivenRangeLessThanSeven() throws ParseException{
        Set<String> bankHolidays = new HashSet<String>();
        bankHolidays.add("2018-04-25 00:00:00.0");
        List<String> workingDays = fetchNextWorkingDaysServiceImpl.fetchNextWorkingDays(bankHolidays,10,HOLIDAY_DATE_FORMATTER.parse("2018-04-22 00:00:00.0"));
        assertTrue(workingDays.size()<30);
        for(String workingDay : workingDays){
            Calendar cal = Calendar.getInstance();
            cal.setTime(HOLIDAY_DATE_RESPONSE_FORMATTER.parse(workingDay));
            assertFalse(fetchNextWorkingDaysServiceImpl.isWeekend(cal));
            cal.setTime(HOLIDAY_DATE_RESPONSE_FORMATTER.parse(workingDay));
            assertFalse(fetchNextWorkingDaysServiceImpl.isBankHoliday(cal,bankHolidays));
        }

        List<String> workingDaysAfterWeek = fetchNextWorkingDaysServiceImpl.fetchNextWorkingDaysAfterWeek(bankHolidays,10,HOLIDAY_DATE_FORMATTER.parse("2018-04-22 00:00:00.0"));
        assertTrue(workingDaysAfterWeek.size()==1);
    }
}
