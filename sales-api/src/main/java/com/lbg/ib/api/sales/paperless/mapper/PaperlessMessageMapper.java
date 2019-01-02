package com.lbg.ib.api.sales.paperless.mapper;

import static com.lbg.ib.api.sales.paperless.constants.PaperlessConstants.FULL_PAPER;
import static com.lbg.ib.api.sales.paperless.constants.PaperlessConstants.PAPER_LESS;
import static com.lbg.ib.api.sales.paperless.constants.PaperlessConstants.SOME_PAPER;
import static com.lbg.ib.api.sales.paperless.constants.PaperlessConstants.NO_DATA;

import java.util.List;

import com.lbg.ib.api.sales.paperless.dto.Account;
import com.lbg.ib.api.sales.paperless.dto.PaperlessResult;

public class PaperlessMessageMapper {

    private PaperlessMessageMapper() {

    }

    public static PaperlessResult buildPaperlessStatus(List<Account> accountList) {
        PaperlessResult result = new PaperlessResult();
        result.setGoGreenStatus(goGreenStatus(accountList));
        result.setAccounts(accountList);
        return result;
    }

    private static String goGreenStatus(List<Account> accountList) {
        int paperfreeStatementCount = 0;
        int nonPaperfreeCount = 0;
        for (Account account : accountList) {
            if (account.getGreenStatus().equalsIgnoreCase(FULL_PAPER)) {
                nonPaperfreeCount++;
            }
            if (account.getGreenStatus().equalsIgnoreCase(PAPER_LESS)) {
                paperfreeStatementCount++;
            }
        }
        return buildGreenStatus(paperfreeStatementCount, nonPaperfreeCount);
    }

    private static String buildGreenStatus(int paperfreeCount, int paperCount) {
        if (paperfreeCount + paperCount == 0) { // naAccount == totalAccounts
            return NO_DATA;
        } else if (paperCount == 0) { // paperfreeStatementCount + naAccount ==
                                      // totalAccounts
            return PAPER_LESS;
        } else if (paperfreeCount == 0) { // nonPaperfreeCount + naAccount ==
                                          // totalAccounts
            return FULL_PAPER;
        } else {
            return SOME_PAPER;
        }
    }
}
