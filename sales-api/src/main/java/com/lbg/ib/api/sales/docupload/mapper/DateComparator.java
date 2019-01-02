package com.lbg.ib.api.sales.docupload.mapper;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.icu.text.SimpleDateFormat;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docupload.domain.Case;

public class DateComparator implements Comparator<Case> {

    @Autowired
    private LoggerDAO logger;

    String            pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public int compare(Case c1, Case c2) {
        Date c1Date = null, c2Date = null;
        try {
            if (c1.getCreationDate() != null && c2.getCreationDate() != null) {
                c1Date = new SimpleDateFormat(pattern).parse(c1.getCreationDate());
                c2Date = new SimpleDateFormat(pattern).parse(c2.getCreationDate());
                return c1Date.compareTo(c2Date);
            }

        } catch (ParseException e) {
            logger.logException(this.getClass(), e);
        }
        return -1;
    }
}
