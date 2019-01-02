package com.lbg.ib.api.sales.dao.mapper;

import com.lbg.ib.api.sales.application.service.ApplicationService;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.domain.PegaSwitchTypeEnum;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.sales.switching.domain.AccountSwitchingRequest;
import com.lbg.ib.api.sales.switching.domain.SwitchingAccount;
import com.lbg.ib.api.sales.switching.domain.SwitchingParty;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lloydsbanking.xml.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static com.lbg.ib.api.sales.switching.constants.AccountSwitchingConstants.*;
import static com.lbg.ib.api.sales.utils.CommonUtils.convert;
import static com.lloydsbanking.xml.ActiveCurrencyCodeType.GBP;
import static org.apache.commons.lang3.StringUtils.substring;

@Component
public class CreateCaseAccountMapper {
    private static final Logger LOG = Logger.getLogger(CreateCaseAccountMapper.class);

    private SearchPartyService searchPartyService;

    private ApplicationService applicationService;

    private ConfigurationDAO configurationDAO;

    @Autowired
    public CreateCaseAccountMapper(SearchPartyService searchPartyService, ApplicationService applicationService, ConfigurationDAO configurationDAO){
        this.searchPartyService = searchPartyService;
        this.applicationService = applicationService;
        this.configurationDAO = configurationDAO;
    }

    public OldAccountType createOldAccountType(final AccountSwitchingRequest accountSwitchingRequest, String accountName) {
        LOG.info("Entering createOldAccountType@CreateCaseAccountMapper");
        SwitchingAccount oldAccountDetails = accountSwitchingRequest.getOldAccountDetails();
        final OldAccountType oldAccount = new OldAccountType();
        oldAccount.setSortCode(oldAccountDetails.getSortCode());
        oldAccount.setAccountNumber(oldAccountDetails.getAccountNumber());
        //Change for Sole to Joint-- Need to send based on owner indicator
        if(accountSwitchingRequest.getSwitchingType() == PegaSwitchTypeEnum.SOLE_TO_JOINT){
            oldAccount.setAccountName(createName(Arrays.asList(ownerParty(accountSwitchingRequest.getParties()))));
        }else{
            oldAccount.setAccountName(accountName);
        }

        if (!StringUtils.isEmpty(oldAccountDetails.getBankName())) {
            oldAccount.setBankName(oldAccountDetails.getBankName());
        }
        LOG.info("Exiting createOldAccountType@CreateCaseAccountMapper");
        return oldAccount;
    }

    public NewAccountType createNewAccount(final AccountSwitchingRequest accountSwitchingRequest, final PartyPostalAddressType postalAddress) {
        LOG.info("Entering createNewAccount@CreateCaseAccountMapper");
        final String sortCode = accountSwitchingRequest.getNewAccountDetails().getSortCode();
        final String accountNumber = accountSwitchingRequest.getNewAccountDetails().getAccountNumber();

        final NewAccountType newAccount = new NewAccountType();

        if(null != accountSwitchingRequest.getPayOdAmount()){
            final ActiveCurrencyAndAmountType amountType = new ActiveCurrencyAndAmountType();
            amountType.set_value(accountSwitchingRequest.getPayOdAmount());
            amountType.setCcy(GBP);
            newAccount.setBalanceTransferFundingLimit(amountType);
        }
        newAccount.setSortCode(sortCode);
        newAccount.setAccountNumber(accountNumber);

        final String bankName = accountSwitchingRequest.getNewAccountDetails().getBankName();
        if (StringUtils.isNotEmpty(bankName)) {
            newAccount.setBankName(accountSwitchingRequest.getNewAccountDetails().getBankName());
        }
        newAccount.setBrand(getBrand());

        final List<SwitchingParty> parties = accountSwitchingRequest.getParties();
        setPartyIdAndDOB(parties, sortCode, accountNumber, accountSwitchingRequest.getSwitchingType());

        newAccount.setAccountName(createName(parties.size()==1?parties:reArrangePartiesForName(parties)));
        newAccount.setAccountParty(new AccountPartyStructureType[accountSwitchingRequest.getParties().size()]);
        for(int index = 0; index < accountSwitchingRequest.getParties().size(); index++){
            final SwitchingParty party = parties.get(index);
            newAccount.setAccountParty(index, getAccountParty(party, party.getPrimary() ? PRIMARY : JOINT, postalAddress, createDebitCardDetailsType(party)));
        }

        LOG.info("Exiting createNewAccount@CreateCaseAccountMapper");
        return newAccount;
    }

    private String getBrand() {
        final String brand = applicationService.getCurrentBrand();
        final Map<String, Object> map = configurationDAO.getConfigurationItems("REVERSE_PRODUCT_BRAND");
        final Object value = map.get(brand);
        if (value != null) {
            return (String) value;
        }
        return null;
    }

    private String createName(final List<SwitchingParty> parties){
        int countParties = 1;
        StringBuilder accountNameBuilder = new StringBuilder();
        for(final SwitchingParty party: parties){
            if (StringUtils.isNotEmpty(party.getFirstName())) {
                accountNameBuilder.append(party.getFirstName());
                accountNameBuilder.append(SPACE);
            }
            if (StringUtils.isNotEmpty(party.getMiddleName())) {
                accountNameBuilder.append(party.getMiddleName());
                accountNameBuilder.append(SPACE);
            }
            if (StringUtils.isNotEmpty(party.getLastName())) {
                accountNameBuilder.append(party.getLastName());
            }
            if(parties.size() != countParties){
                accountNameBuilder.append(SPACE);
                accountNameBuilder.append(ACCOUNT_NAME_JOINER);
                accountNameBuilder.append(SPACE);
            }
            countParties++;
        }
        //PCA-5856 - Strip the accountName to 18 characters
        return substring(accountNameBuilder.toString(), 0, MAX_CHARACTER_ACCOUNT_NAME);

    }

    private List<SwitchingParty> reArrangePartiesForName(final List<SwitchingParty> parties){
        return Arrays.asList(primaryParty(parties), secondParty(parties));
    }

    private void setPartyIdAndDOB(final List<SwitchingParty> parties, final String sortCode, final String accountNumber, PegaSwitchTypeEnum switchType){
        final SearchPartyRequest partyDetails = new SearchPartyRequest(sortCode + accountNumber);
        final List<IBParties> partiesRetrievedFromOcis;
        partyDetails.setAgreementType(CURRENT_ACCOUNT);
        try{
            partiesRetrievedFromOcis = searchPartyService.retrieveParty(partyDetails);
        }catch(Exception exception){
            LOG.info(exception);
            throw new ServiceException(new ResponseError(ResponseErrorConstants.PEGA_ERROR_INVOKING_RETRIEVE_PARTY,"Error occured while invoking RetrievePartyDetails." +
                     exception.getMessage() + ". Cause: " + exception.getCause()));
        }
        if((partiesRetrievedFromOcis==null)||(partiesRetrievedFromOcis.size()==0)){
           throw new ServiceException(new ResponseError(ResponseErrorConstants.PEGA_NO_PARTY_DETAILS_OCIS,"No party details found for provided details"));
        }
        if((partiesRetrievedFromOcis.size()>2)){
            throw new ServiceException(new ResponseError(ResponseErrorConstants.OCIS_TOO_MANY_PARTIES_FOUND,"More than two parties found in OCIS response. Can not proceed"));
        }
        if(!isRequiredPartiesReceivedFromOcis(partiesRetrievedFromOcis, switchType)){
            LOG.trace("The parties retrieved from OCIS does not suits the type of switching journey.Please try again with right number of parties");
            throw new ServiceException(new ResponseError(ResponseErrorConstants.PEGA_CREATE_PARTIES_MISMATCH,"The parties retrieved from OCIS does not suits the type of switching request.Please try again with right number of parties and/or correct switching type"));
        }
        int partyMatchIndicator = 0;
        for(final SwitchingParty party: parties) {
            final ListIterator<IBParties> ocisPartiesListIterator = partiesRetrievedFromOcis.listIterator();
            while(ocisPartiesListIterator.hasNext()) {
                final IBParties ocisParty = ocisPartiesListIterator.next();
                if (isSameParty(ocisParty, party)) {
                    party.setDob(ocisParty.getBirthDate());
                    party.setPartyId(ocisParty.getPartyId());
                    partyMatchIndicator++;
                    ocisPartiesListIterator.remove();
                    break;
                }
            }
        }
        //Party Size zero or empty is handled at the resource level
        if(partyMatchIndicator != parties.size()){
            LOG.trace("RetrieveParty is not able to locate all parties of the switching request based on Name. Able to locate "+ partyMatchIndicator +"parties out of "+parties.size());
            throw new ServiceException(new ResponseError(ResponseErrorConstants.PEGA_PARTIES_NOT_LOCATE,"RetrieveParty is not able to locate all parties of the switching request based on Name. Able to locate only "+ partyMatchIndicator +" parties out of "+parties.size()));
        }
    }

    private boolean isSameParty(final IBParties party, final SwitchingParty switchingParty){
        return party.getFirstName().equalsIgnoreCase(switchingParty.getFirstName()) && party.getLastName().equalsIgnoreCase(switchingParty.getLastName());
    }

    private AccountPartyStructureType getAccountParty(final SwitchingParty switchingParty, final String accountPartyType, final PartyPostalAddressType postalAddress, DebitCardDetailsType debitCardDetails) {
        LOG.info("Entering getAccountParty@CreateCaseAccountMapper");
        final AccountPartyStructureType accountParty = new AccountPartyStructureType();

        try {
            //java.util.Calendar convert(java.util.Date convert(String))
            accountParty.setPartyBirthDate(convert(convert(switchingParty.getDob())));
        }catch (ParseException ex){
            LOG.error("Unable to parse dob = " + switchingParty.getDob(), ex);
        }

        if (StringUtils.isNotEmpty(switchingParty.getNationality())) {
            accountParty.setPartyNationality(switchingParty.getNationality());
        }
        accountParty.setSendSMSUpdatesIndicator(String.valueOf(switchingParty.getTextAlert()));
        if (StringUtils.isNotEmpty(switchingParty.getPartyId())) {
            accountParty.setPartyId(switchingParty.getPartyId());
        }
        final String last5PANDigits = switchingParty.getCardNumber();
        // PCA-7217 - Indicators are converted from boolean to string because ibm stubs is converting boolean
        // fields to 0 or 1 which PEGA is not able to support
        if (StringUtils.isNotEmpty(last5PANDigits)) {
            accountParty.setDebitCardIndicator(String.valueOf(true));
            accountParty.setLast5PANDigits(last5PANDigits.substring(last5PANDigits.length() - ACCOUNT_PARTY_5PAN_DIGITS_LENGHT));
        } else{
            accountParty.setDebitCardIndicator(String.valueOf(false));
        }
        accountParty.setAccountPartyType(accountPartyType);
        accountParty.setAuthorityIndicator(String.valueOf(true));
        accountParty.setPartyCountryOfResidence(COUNTRY);
        accountParty.setOldDebitCardDetails(debitCardDetails);
        accountParty.setPartyPostalAddress(postalAddress);
        accountParty.setPartyName(createPartyName(switchingParty));
        LOG.info("Exiting getAccountParty@CreateCaseAccountMapper");
        return accountParty;
    }

    private PartyNameType createPartyName(final SwitchingParty switchingParty) {
        LOG.info("Entering createPartyName@CreateCaseAccountMapper");
        final PartyNameType partyName = new PartyNameType();
        if (StringUtils.isNotEmpty(switchingParty.getNamePrefix())) {
            partyName.setNamePrefix(switchingParty.getNamePrefix());
        }
        if (StringUtils.isNotEmpty(switchingParty.getFirstName())) {
            partyName.setFirstName(switchingParty.getFirstName());
        }
        //PCA-5856 - Added middle name in the request
        if (StringUtils.isNotEmpty(switchingParty.getMiddleName())) {
            partyName.setSecondName(switchingParty.getMiddleName());
        }
        if (StringUtils.isNotEmpty(switchingParty.getLastName())) {
            partyName.setFamilyName(switchingParty.getLastName());
        }
        LOG.info("Exiting createPartyName@CreateCaseAccountMapper");
        return partyName;
    }

    private DebitCardDetailsType createDebitCardDetailsType(final SwitchingParty switchingParty) {
        LOG.info("Entering createDebitCardDetailsType@CreateCaseAccountMapper");
        DebitCardDetailsType debitCardDetails = null;
        LOG.info("Checking if unencryptedCardNumber is available in the request...");
        final String unencryptedFullCardNumber = switchingParty.getCardNumber();
        if (StringUtils.isNotEmpty(unencryptedFullCardNumber)) {
            debitCardDetails = new DebitCardDetailsType();
            final StringBuffer newFormattedCardNumber = new StringBuffer();
            newFormattedCardNumber.append("00000000000");
            LOG.info("unencryptedFullCardNumber found");
            LOG.info("Length of unencrypted card number:" + unencryptedFullCardNumber.length());
            LOG.info("Length of unformatted card number:" + newFormattedCardNumber.toString().length());
            newFormattedCardNumber.append(unencryptedFullCardNumber.substring(unencryptedFullCardNumber.length() - ACCOUNT_PARTY_5PAN_DIGITS_LENGHT));
            LOG.info("Length of formatted card number:" + newFormattedCardNumber.length());
            debitCardDetails.setPAN(newFormattedCardNumber.toString()); //Set unencrypted card number pefixed with 9 zeros in PEGA request object
            LOG.info("Formatting done for PEGA pan attribute");

            // set the expiry date for the debit card only if debit card details are provided
            if (StringUtils.isNotEmpty(switchingParty.getCardExpiryDate())) {
                String expiryDate = switchingParty.getCardExpiryDate();
                try {
                    expiryDate = expiryDate.substring(LOWER_DATE_LIMIT, UPPER_DATE_LIMIT).concat("-").concat("20").concat(expiryDate.substring(LOWER_YEAR_LIMIT, UPPER_YEAR_LIMIT));
                    final Calendar cal = convert(convert(expiryDate, "MM-yyyy"));
                    // PCA-7216: Expiry date should be the end date of the expiry month
                    if(null != cal) {
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
                    }
                    debitCardDetails.setDebitCardExpiryDate(cal);
                }catch (ParseException ex){
                    LOG.error("Unable to parse expiry date = " + expiryDate, ex);
                }
            }
        }
        LOG.info("Exiting createDebitCardDetailsType@CreateCaseAccountMapper");
        return debitCardDetails;
    }

    public SwitchingParty primaryParty(List<SwitchingParty> parties){
        for(SwitchingParty party: parties){
            if(party.getPrimary()){
                return party;
            }
        }
        LOG.trace("No Primary party found");
        throw new ServiceException(new ResponseError(ResponseErrorConstants.PEGA_NO_PRIMARY_PARTY_FOUND,"No Primary party found in the switching request"));
    }

    //This will be only called when there will be 2 parties in the request
    public SwitchingParty secondParty(List<SwitchingParty> parties){
        for(SwitchingParty party: parties){
            if((party.getPrimary()==null)||(!party.getPrimary())){
                return party;
            }
        }
        LOG.trace("No Second party found when expected");
        throw new ServiceException(new ResponseError(ResponseErrorConstants.PEGA_NO_SECOND_PARTY_FOUND,"No Second party found in the switching request but it is expected"));
    }

    public SwitchingParty ownerParty(List<SwitchingParty> parties){
        for(SwitchingParty party: parties){
            if(party.getOldAccountHolder()){
                return party;
            }
        }
        LOG.trace("No Old account owner party found");
        throw new ServiceException(new ResponseError(ResponseErrorConstants.OLD_OWNER_NOT_FOUND,"No Owner party for old account in Switching request"));
    }

    private boolean isRequiredPartiesReceivedFromOcis(List<IBParties> partiesReceivedfromOcis, PegaSwitchTypeEnum switchType){
        if((partiesReceivedfromOcis.size()==2)&&(switchType==PegaSwitchTypeEnum.SOLE_TO_SOLE)){
            return false;
        }else if((partiesReceivedfromOcis.size()==1)&&((switchType==PegaSwitchTypeEnum.SOLE_TO_JOINT)||(switchType==PegaSwitchTypeEnum.JOINT_TO_JOINT))){
            return false;
        }
     return true;
    }
}
