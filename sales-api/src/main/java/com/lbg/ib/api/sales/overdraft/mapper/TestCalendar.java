package com.lbg.ib.api.sales.overdraft.mapper;

import java.util.Calendar;
import java.util.Date;

public class TestCalendar {

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		System.out.println(currentDate);
	}
}
