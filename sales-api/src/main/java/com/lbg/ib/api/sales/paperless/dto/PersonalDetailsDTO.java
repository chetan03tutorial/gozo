/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.dto;

import java.math.BigInteger;

import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StAddressLine;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StCntrlData;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StIBTelephone;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StNotifyMobile;

/**
 * DTO to have user details information.
 * @author tkhann
 */
public class PersonalDetailsDTO {

    private boolean calledAtLogon;
    private boolean changeSurname;
    private String surname;
    private boolean changeFirstname;
    private String firstname;
    private boolean changeSecondname;
    private String secondforename;
    private boolean changeThirdname;
    private String thirdforename;
    private boolean changeNiNumber;
    private String ninumber;
    private String personalptytitle;
    private boolean changeAddress;
    private String postcode;
    private StAddressLine[] astaddressline;
    private boolean changePhone;
    private StIBTelephone[] astTelephoneOld;
    private StIBTelephone[] astTelephone;
    private boolean changeEmail;
    private String emailaddr;
    private boolean changeEmailInd;
    private int mktgindEmail;
    private boolean changeOnlineInd;
    private int mktgindOnline;
    private boolean changeSWProdsInd;
    private boolean prodSuppressed;
    private int mktgindSMS;
    private StNotifyMobile stnotifymobileOld;
    private StNotifyMobile stnotifymobile;
    private boolean changeSMSTelephone;
    private boolean changeSMSInd;
    private boolean changeMailInd;
    private int mktgindMail;
    private boolean changePhoneInd;
    private int mktgindPhone;
    private String cctmSessionId;
    private BigInteger uniqueid;
    private String cctmTransId;
    private StCntrlData[] astCntrlData;
    private String authmechCOT;
    private String partyId;
    private String ocisId;

    /**
     * @return the partyId
     */
    public String getPartyId() {
        return partyId;
    }

    /**
     * @param partyId the partyId to set
     */
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    /**
     * @return the ocisId
     */
    public String getOcisId() {
        return ocisId;
    }

    /**
     * @param ocisId the ocisId to set
     */
    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }

    /**
     * @return the calledAtLogon
     */
    public boolean isCalledAtLogon() {
        return calledAtLogon;
    }

    /**
     * @param calledAtLogon the calledAtLogon to set
     */
    public void setCalledAtLogon(boolean calledAtLogon) {
        this.calledAtLogon = calledAtLogon;
    }

    /**
     * @return the changeSurname
     */
    public boolean isChangeSurname() {
        return changeSurname;
    }

    /**
     * @param changeSurname the changeSurname to set
     */
    public void setChangeSurname(boolean changeSurname) {
        this.changeSurname = changeSurname;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the changeFirstname
     */
    public boolean isChangeFirstname() {
        return changeFirstname;
    }

    /**
     * @param changeFirstname the changeFirstname to set
     */
    public void setChangeFirstname(boolean changeFirstname) {
        this.changeFirstname = changeFirstname;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the changeSecondname
     */
    public boolean isChangeSecondname() {
        return changeSecondname;
    }

    /**
     * @param changeSecondname the changeSecondname to set
     */
    public void setChangeSecondname(boolean changeSecondname) {
        this.changeSecondname = changeSecondname;
    }

    /**
     * @return the secondforename
     */
    public String getSecondforename() {
        return secondforename;
    }

    /**
     * @param secondforename the secondforename to set
     */
    public void setSecondforename(String secondforename) {
        this.secondforename = secondforename;
    }

    /**
     * @return the changeThirdname
     */
    public boolean isChangeThirdname() {
        return changeThirdname;
    }

    /**
     * @param changeThirdname the changeThirdname to set
     */
    public void setChangeThirdname(boolean changeThirdname) {
        this.changeThirdname = changeThirdname;
    }

    /**
     * @return the thirdforename
     */
    public String getThirdforename() {
        return thirdforename;
    }

    /**
     * @param thirdforename the thirdforename to set
     */
    public void setThirdforename(String thirdforename) {
        this.thirdforename = thirdforename;
    }

    /**
     * @return the changeNiNumber
     */
    public boolean isChangeNiNumber() {
        return changeNiNumber;
    }

    /**
     * @param changeNiNumber the changeNiNumber to set
     */
    public void setChangeNiNumber(boolean changeNiNumber) {
        this.changeNiNumber = changeNiNumber;
    }

    /**
     * @return the ninumber
     */
    public String getNinumber() {
        return ninumber;
    }

    /**
     * @param ninumber the ninumber to set
     */
    public void setNinumber(String ninumber) {
        this.ninumber = ninumber;
    }

    /**
     * @return the personalptytitle
     */
    public String getPersonalptytitle() {
        return personalptytitle;
    }

    /**
     * @param personalptytitle the personalptytitle to set
     */
    public void setPersonalptytitle(String personalptytitle) {
        this.personalptytitle = personalptytitle;
    }

    /**
     * @return the changeAddress
     */
    public boolean isChangeAddress() {
        return changeAddress;
    }

    /**
     * @param changeAddress the changeAddress to set
     */
    public void setChangeAddress(boolean changeAddress) {
        this.changeAddress = changeAddress;
    }

    /**
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @return the astaddressline
     */
    public StAddressLine[] getAstaddressline() {
        return astaddressline;
    }

    /**
     * @param astaddressline the astaddressline to set
     */
    public void setAstaddressline(StAddressLine[] astaddressline) {
        this.astaddressline = astaddressline;
    }

    /**
     * @return the changePhone
     */
    public boolean isChangePhone() {
        return changePhone;
    }

    /**
     * @param changePhone the changePhone to set
     */
    public void setChangePhone(boolean changePhone) {
        this.changePhone = changePhone;
    }

    /**
     * @return the astTelephoneOld
     */
    public StIBTelephone[] getAstTelephoneOld() {
        return astTelephoneOld;
    }

    /**
     * @param astTelephoneOld the astTelephoneOld to set
     */
    public void setAstTelephoneOld(StIBTelephone[] astTelephoneOld) {
        this.astTelephoneOld = astTelephoneOld;
    }

    /**
     * @return the astTelephone
     */
    public StIBTelephone[] getAstTelephone() {
        return astTelephone;
    }

    /**
     * @param astTelephone the astTelephone to set
     */
    public void setAstTelephone(StIBTelephone[] astTelephone) {
        this.astTelephone = astTelephone;
    }

    /**
     * @return the changeEmail
     */
    public boolean isChangeEmail() {
        return changeEmail;
    }

    /**
     * @param changeEmail the changeEmail to set
     */
    public void setChangeEmail(boolean changeEmail) {
        this.changeEmail = changeEmail;
    }

    /**
     * @return the emailaddr
     */
    public String getEmailaddr() {
        return emailaddr;
    }

    /**
     * @param emailaddr the emailaddr to set
     */
    public void setEmailaddr(String emailaddr) {
        this.emailaddr = emailaddr;
    }

    /**
     * @return the changeEmailInd
     */
    public boolean isChangeEmailInd() {
        return changeEmailInd;
    }

    /**
     * @param changeEmailInd the changeEmailInd to set
     */
    public void setChangeEmailInd(boolean changeEmailInd) {
        this.changeEmailInd = changeEmailInd;
    }

    /**
     * @return the mktgindEmail
     */
    public int getMktgindEmail() {
        return mktgindEmail;
    }

    /**
     * @param mktgindEmail the mktgindEmail to set
     */
    public void setMktgindEmail(int mktgindEmail) {
        this.mktgindEmail = mktgindEmail;
    }

    /**
     * @return the changeOnlineInd
     */
    public boolean isChangeOnlineInd() {
        return changeOnlineInd;
    }

    /**
     * @param changeOnlineInd the changeOnlineInd to set
     */
    public void setChangeOnlineInd(boolean changeOnlineInd) {
        this.changeOnlineInd = changeOnlineInd;
    }

    /**
     * @return the mktgindOnline
     */
    public int getMktgindOnline() {
        return mktgindOnline;
    }

    /**
     * @param mktgindOnline the mktgindOnline to set
     */
    public void setMktgindOnline(int mktgindOnline) {
        this.mktgindOnline = mktgindOnline;
    }

    /**
     * @return the changeSWProdsInd
     */
    public boolean isChangeSWProdsInd() {
        return changeSWProdsInd;
    }

    /**
     * @param changeSWProdsInd the changeSWProdsInd to set
     */
    public void setChangeSWProdsInd(boolean changeSWProdsInd) {
        this.changeSWProdsInd = changeSWProdsInd;
    }

    /**
     * @return the mktgindSMS
     */
    public int getMktgindSMS() {
        return mktgindSMS;
    }

    /**
     * @param mktgindSMS the mktgindSMS to set
     */
    public void setMktgindSMS(int mktgindSMS) {
        this.mktgindSMS = mktgindSMS;
    }

    /**
     * @return the stnotifymobileOld
     */
    public StNotifyMobile getStnotifymobileOld() {
        return stnotifymobileOld;
    }

    /**
     * @param stnotifymobileOld the stnotifymobileOld to set
     */
    public void setStnotifymobileOld(StNotifyMobile stnotifymobileOld) {
        this.stnotifymobileOld = stnotifymobileOld;
    }

    /**
     * @return the stnotifymobile
     */
    public StNotifyMobile getStnotifymobile() {
        return stnotifymobile;
    }

    /**
     * @param stnotifymobile the stnotifymobile to set
     */
    public void setStnotifymobile(StNotifyMobile stnotifymobile) {
        this.stnotifymobile = stnotifymobile;
    }

    /**
     * @return the changeSMSTelephone
     */
    public boolean isChangeSMSTelephone() {
        return changeSMSTelephone;
    }

    /**
     * @param changeSMSTelephone the changeSMSTelephone to set
     */
    public void setChangeSMSTelephone(boolean changeSMSTelephone) {
        this.changeSMSTelephone = changeSMSTelephone;
    }

    /**
     * @return the changeSMSInd
     */
    public boolean isChangeSMSInd() {
        return changeSMSInd;
    }

    /**
     * @param changeSMSInd the changeSMSInd to set
     */
    public void setChangeSMSInd(boolean changeSMSInd) {
        this.changeSMSInd = changeSMSInd;
    }

    /**
     * @return the changeMailInd
     */
    public boolean isChangeMailInd() {
        return changeMailInd;
    }

    /**
     * @param changeMailInd the changeMailInd to set
     */
    public void setChangeMailInd(boolean changeMailInd) {
        this.changeMailInd = changeMailInd;
    }

    /**
     * @return the mktgindMail
     */
    public int getMktgindMail() {
        return mktgindMail;
    }

    /**
     * @param mktgindMail the mktgindMail to set
     */
    public void setMktgindMail(int mktgindMail) {
        this.mktgindMail = mktgindMail;
    }

    /**
     * @return the changePhoneInd
     */
    public boolean isChangePhoneInd() {
        return changePhoneInd;
    }

    /**
     * @param changePhoneInd the changePhoneInd to set
     */
    public void setChangePhoneInd(boolean changePhoneInd) {
        this.changePhoneInd = changePhoneInd;
    }

    /**
     * @return the mktgindPhone
     */
    public int getMktgindPhone() {
        return mktgindPhone;
    }

    /**
     * @param mktgindPhone the mktgindPhone to set
     */
    public void setMktgindPhone(int mktgindPhone) {
        this.mktgindPhone = mktgindPhone;
    }

    /**
     * @return the cctmSessionId
     */
    public String getCctmSessionId() {
        return cctmSessionId;
    }

    /**
     * @param cctmSessionId the cctmSessionId to set
     */
    public void setCctmSessionId(String cctmSessionId) {
        this.cctmSessionId = cctmSessionId;
    }

    /**
     * @return the uniqueid
     */
    public BigInteger getUniqueid() {
        return uniqueid;
    }

    /**
     * @param uniqueid the uniqueid to set
     */
    public void setUniqueid(BigInteger uniqueid) {
        this.uniqueid = uniqueid;
    }

    /**
     * @return the cctmTransId
     */
    public String getCctmTransId() {
        return cctmTransId;
    }

    /**
     * @param cctmTransId the cctmTransId to set
     */
    public void setCctmTransId(String cctmTransId) {
        this.cctmTransId = cctmTransId;
    }

    /**
     * @return the astCntrlData
     */
    public StCntrlData[] getAstCntrlData() {
        return astCntrlData;
    }

    /**
     * @param astCntrlData the astCntrlData to set
     */
    public void setAstCntrlData(StCntrlData[] astCntrlData) {
        this.astCntrlData = astCntrlData;
    }

    /**
     * @return the authmechCOT
     */
    public String getAuthmechCOT() {
        return authmechCOT;
    }

    /**
     * @param authmechCOT the authmechCOT to set
     */
    public void setAuthmechCOT(String authmechCOT) {
        this.authmechCOT = authmechCOT;
    }

    /**
     * @return the prodSuppressed
     */
    public boolean isProdSuppressed() {
        return prodSuppressed;
    }

    /**
     * @param prodSuppressed the prodSuppressed to set
     */
    public void setProdSuppressed(boolean prodSuppressed) {
        this.prodSuppressed = prodSuppressed;
    }

}
