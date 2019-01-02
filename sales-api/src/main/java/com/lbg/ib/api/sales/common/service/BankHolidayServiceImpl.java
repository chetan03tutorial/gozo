package com.lbg.ib.api.sales.common.service;

import com.lbg.ib.api.sales.common.domain.BankHolidaysHolder;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Rohit Soni on 12/03/2018.
 * PCA-6522
 * Service Implementation for getting Bank Holidays
 */

@Component
public class BankHolidayServiceImpl implements BankHolidayService {

    @Autowired
    private ReferenceDataServiceDAO referenceData;

    public BankHolidaysHolder getBankHolidaysList() throws ServiceException{

        BankHolidaysHolder bankHolidaysHolder = new BankHolidaysHolder();

        final Map<String, String> holidayMap = referenceData.getBankHolidays();
        if (holidayMap != null && !holidayMap.isEmpty()) {
            bankHolidaysHolder.setBankHolidays(holidayMap.keySet());
        }

        return bankHolidaysHolder;

    }
}
