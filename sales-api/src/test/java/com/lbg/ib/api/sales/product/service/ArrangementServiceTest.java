package com.lbg.ib.api.sales.product.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ArrangementServiceTest {

    @Test
    public void testGetTariffKeyFromStudentYearFirstYear() {
        String tariff = ArrangementService.Tariffs.getTariffKeyFromStudentYear(1);
        assertEquals(tariff, "Student 1st Year Tariff");
    }

    @Test
    public void testGetTariffKeyFromStudentYearSecondThirdFourthFifthSixthYear() {
        String tariff = ArrangementService.Tariffs.getTariffKeyFromStudentYear(2);
        assertEquals(tariff, "Student 2nd Year Tariff");
        tariff = ArrangementService.Tariffs.getTariffKeyFromStudentYear(3);
        assertEquals(tariff, "Student 3rd Year Tariff");

        tariff = ArrangementService.Tariffs.getTariffKeyFromStudentYear(4);
        assertEquals(tariff, "Student 4th Year Tariff");

        tariff = ArrangementService.Tariffs.getTariffKeyFromStudentYear(5);
        assertEquals(tariff, "Student 5h Year Tariff");

        tariff = ArrangementService.Tariffs.getTariffKeyFromStudentYear(6);
        assertEquals(tariff, "Student 6th Year Tariff");

        tariff = ArrangementService.Tariffs.getTariffKeyFromStudentYear(7);
        assertEquals(tariff, "Student No Year Tariff");
    }
}
