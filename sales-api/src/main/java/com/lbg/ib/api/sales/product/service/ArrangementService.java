package com.lbg.ib.api.sales.product.service;

import java.util.HashMap;
import java.util.Map;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface ArrangementService {
    Arranged arrange(Arrangement arrangement) throws ServiceException;

    static class Tariffs {

        public static final Map<Integer, String> TARIFF_MAP = new HashMap<Integer, String>();

        private Tariffs() {
            // not called
        }

        static {
            TARIFF_MAP.put(1, "Student 1st Year Tariff");
            TARIFF_MAP.put(2, "Student 2nd Year Tariff");
            TARIFF_MAP.put(3, "Student 3rd Year Tariff");
            TARIFF_MAP.put(4, "Student 4th Year Tariff");
            TARIFF_MAP.put(5, "Student 5h Year Tariff");
            TARIFF_MAP.put(6, "Student 6th Year Tariff");

        }

        public static String getTariffKeyFromStudentYear(int studentYearOfStudy) {
            if (TARIFF_MAP.containsKey(studentYearOfStudy)) {
                return TARIFF_MAP.get(studentYearOfStudy);
            }

            return "Student No Year Tariff";
        }
    }

}
