package com.lbg.ib.api.sales.common.service;

import com.lbg.ib.api.sales.common.domain.BankHolidaysHolder;
import com.lbg.ib.api.shared.exception.ServiceException;

/**
 * Created by Rohit Soni on 12/03/2018.
 * PCA-6522
 * Service interface for getting Bank Holidays
 */

public interface BankHolidayService {

    BankHolidaysHolder getBankHolidaysList() throws ServiceException;

}
