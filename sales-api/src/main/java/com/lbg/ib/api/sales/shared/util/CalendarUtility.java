package com.lbg.ib.api.sales.shared.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.springframework.context.annotation.ComponentScan;

import com.ibm.icu.text.SimpleDateFormat;
import com.lbg.ib.api.sales.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.shared.util.domain.TimeDifference;

@ComponentScan
public class CalendarUtility {

	public static final String DD_MM_YYYY_FORMAT = "ddMMYYYY";
	public static final String DD_MM_YY_HH_MM = "ddMMyyHHmm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static Date getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		return currentDate;
	}

	public static Date getRelativeDate(int date, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DATE, date);
		return calendar.getTime();
	}

	public static String formatDate(String format, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static Date parseDate(String format, String date) {
		validateDateString(date);
		Date formattedDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			formattedDate = formatter.parse(date);
		} catch (ParseException e) {
			throw new InvalidFormatException("Invalid Date Format", e);
		}
		return formattedDate;
	}

	public static TimeDifference getDifference(Date startDate, Date endDate) {
		LocalDate start = new LocalDate(startDate);
		LocalDate end = new LocalDate(endDate);
		Period period = new Period(end, start);
		TimeDifference timeDiff = new TimeDifference();
		long months = Math.abs(period.getMonths());
		long years = Math.abs(period.getYears());
		long days = Math.abs(period.getDays());
		timeDiff.setDays(days);
		timeDiff.setMonths(months);
		timeDiff.setYears(years);
		return timeDiff;
	}

	public static void validateDateString(String date) {
		if (StringUtils.isEmpty(date)) {
			throw new InvalidFormatException("Invalid Date Format");
		}
	}

}
