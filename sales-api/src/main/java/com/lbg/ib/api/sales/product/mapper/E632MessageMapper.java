/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.mapper;

import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lloydsbanking.xml.CBSRequestGp2;
import com.lloydsbanking.xml.E632Req;

/**
 * E632MessageMapper.
 * @author cshar8
 */
public class E632MessageMapper {
    /**
     * Build request for E632.
     * @param fromAccount
     * @param toProduct
     * @return
     */
    public static E632Req buildE632Request(Account fromAccount, UpgradeOption toProduct, PcciOverdraftRequest overdraftOption) {
        E632Req e632Request = new E632Req();
        e632Request.setMaxRepeatGroupQy(1);
        CBSRequestGp2 cbsGroup = new CBSRequestGp2();
        cbsGroup.setInputOfficerFlagStatusCd(0);
        cbsGroup.setInputOfficerStatusCd(0);
        cbsGroup.setOverrideDetailsCd(0);
        cbsGroup.setOverridingOfficerStaffNo("0");
        e632Request.setCBSRequestGp2(cbsGroup);
        e632Request.setCurrencyCd(fromAccount.getCurrencyCode());
        e632Request.setCurrencyFlagCd(1);
        e632Request.setSortCd(fromAccount.getSortCode());

        if (overdraftOption != null && overdraftOption.getOverDraftAmount() != null) {
            e632Request.setOverdraftLimit2Am(overdraftOption.getOverDraftAmount());
        } else {
            e632Request.setOverdraftLimit2Am(String.valueOf(fromAccount.getOverdraftLimit().longValue()));
        }

        if (toProduct != null) {
            e632Request.setTariffFlagId(1);
            e632Request.setTariffId(Integer.valueOf(toProduct.getTariff()));
            e632Request.setCBSProdNoFlagId(1);
            e632Request.setCBSProdNoId(Integer.valueOf(toProduct.getCbsProductIds().get(0).substring(0, 4)));
        } else {
            e632Request.setAccountStemCBSId(fromAccount.getAccountNumber());
        }
        return e632Request;
    }
}
