package com.lbg.ib.api.sales.user.mapper;

import java.util.LinkedList;
import java.util.List;

import com.lbg.ib.api.sales.paperless.constants.PaperlessConstants;
import com.lbg.ib.api.sales.paperless.dto.Account;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StAccount;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StAccountListDetail;

public class UserProductHoldingMessageMapper {

    private UserProductHoldingMessageMapper() {

    }

    public static List<Account> buildResponse(List<StAccountListDetail> productList) {
        List<Account> accountList = new LinkedList<Account>();
        for (StAccountListDetail productDetail : productList) {

            StAccount accountDetails = productDetail.getStacc();
            Account account = new Account();
            account.setAccountNumber(accountDetails.getAccno());
            account.setSortCode(accountDetails.getSortcode());
            account.setCardNumber(accountDetails.getCardno());

            account.setExternalSystemProductId(productDetail.getOcisProductHeldId());
            account.setProductType(accountDetails.getProdtype());
            account.setHost(accountDetails.getHost());
            account.setExternalSystem(productDetail.getExternalsystem());
            account.setExternalSystemProductHeldId(productDetail.getExtsysprodheldid());
            account.setName(productDetail.getAccname());
            account.setBrand(productDetail.getBrandcode());
            account.setCategory(productDetail.getAccountcategory());
            account.setOpeningDate(productDetail.getDateOpened());
            account.setType(productDetail.getAcctype());
            account.setExternalPartyIdentifierText(productDetail.getPartyid());
            account.setStatus(productDetail.getAccstatus());
            String statementType = productDetail.getStcommprefdata().getCommOptTx();
            String correspondanceType = productDetail.getStcorrspondncdata().getCommOptTx();
            account.setStatementType(statementType);
            account.setCorrespondanceType(correspondanceType);
            boolean mayBePaper = PaperlessConstants.PAPER.equalsIgnoreCase(statementType)
                    || PaperlessConstants.PAPER.equalsIgnoreCase(correspondanceType);
            boolean mayBePaperless = PaperlessConstants.PAPER_LESS.equalsIgnoreCase(statementType)
                    || PaperlessConstants.PAPER_LESS.equalsIgnoreCase(correspondanceType);
            account.setGreenStatus(greenStatus(mayBePaper, mayBePaperless));
            accountList.add(account);
        }
        return accountList;
    }

    private static String greenStatus(boolean paperFlag, boolean paperlessFlag) {
        String paperlessConstant = PaperlessConstants.NO_DATA;
        if (paperFlag && paperlessFlag) {
            paperlessConstant = PaperlessConstants.SOME_PAPER;
        } else if (!paperFlag && paperlessFlag) {
            paperlessConstant = PaperlessConstants.PAPER_LESS;
        } else if (paperFlag && !paperlessFlag) {
            paperlessConstant = PaperlessConstants.FULL_PAPER;
        }
        return paperlessConstant;
    }
}
