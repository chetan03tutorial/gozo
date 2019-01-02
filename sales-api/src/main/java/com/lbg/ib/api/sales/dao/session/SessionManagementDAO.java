package com.lbg.ib.api.sales.dao.session;

import com.lbg.ib.api.sales.common.session.dto.ArrangeToOfferParameters;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sales.dto.communicationparty.CommunicationPartyDetailsDTO;
import com.lbg.ib.api.sales.dto.party.TriadResultDTO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.PackageAccountSessionInfo;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DirectDebit;
import com.lbg.ib.api.sales.user.domain.AddParty;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.UserContext;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved. The SessionManagementDAO is the interface for the
 * Session Management DAO.
 * 
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 ***********************************************************************/
public interface SessionManagementDAO {
	void setSelectedProduct(SelectedProduct product);

	SelectedProduct getSelectedProduct();

	void setArrangeToActivateParameters(ArrangeToActivateParameters arrangement);

	ArrangeToActivateParameters getArrangeToActivateParameters();

	String getSessionId();

	UserContext getUserContext();

	void setSendCommunicationPartyDetails(CommunicationPartyDetailsDTO partyDetails);

	CommunicationPartyDetailsDTO getSendCommunicationPartyDetails();

	void clearSessionAttributeForPipelineChasing();

	void setOcisPartyIdInUserContext(String ocisId, String partyId);

	void setUserInfo(Arrangement arrangement);

	Arrangement getUserInfo();

	ArrangeToOfferParameters getArrangeToOfferParameters();

	void setArrangeToOfferParameters(ArrangeToOfferParameters offerArrangement);

	void setBranchContext(BranchContext branchContext);

	BranchContext getBranchContext();

	CustomerInfo getCustomerDetails();

	void setCustomerDetails(CustomerInfo cusInfo);

	CustomerInfo getRelatedCustomerDetails();

	void setRelatedCustomerDetails(CustomerInfo cusInfo);

	void setSwitchingDetails(DirectDebit directDebit);

	public DirectDebit getSwitchingDetails();

	void setAddPartyContext(AddParty addParty);

	AddParty getAddPartyContext();

	void setPackagedAccountSessionInfo(PackageAccountSessionInfo packageAccountSessionInfo);

	PackageAccountSessionInfo getPackagedAccountSessionInfo();

	void setAllPartyDetailsSessionInfo(Map<String, PartyDetails> allPartyDetails);

	Map<String, PartyDetails> getAllPartyDetailsSessionInfo();

	void setAvailableUpgradeOptions(Map<String, UpgradeOption> availableUpgradeOptions);

	Map<String, UpgradeOption> getAvailableUpgradeOptions();

	void setAccountToConvertInContext(SelectedAccount account);

	SelectedAccount getAccountToConvertInContext();

	void setTriadDetails(TriadResultDTO resultDTO);

	TriadResultDTO getTriadDetails();

	void setMaxOverDraftLimit(String maxOverDraftLimit);

	String getMaxOverDraftLimit();

	public void setInUserMap(Object key, Object value);

	public Object getFromUserMap(Object key);

	Map<String, String> getPrimaryPartyOcisIdMap();

	public void setPrimaryPartyOcisIdMap(Map<String, String> primaryPartyOcisIdMap);

	void setEmailRetryCount( AtomicInteger retryCount);

    AtomicInteger getEmailRetryCount();

	String getTraceRequestFlag();

	void setTraceRequestFlag(String value);
	
	String getPartyCity();
	
	void setPartyCity(String cityName);

	Double getDemandedOD();

	void setDemandedOD(Double demandedOD);
}
