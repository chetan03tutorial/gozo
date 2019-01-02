package com.lbg.ib.api.sales.overdraft.dto;

import java.util.List;

import com.lbg.ib.api.sales.user.dto.PartyDto;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sso.domain.user.Account;

public class Q122DTO {

	private Account selectedAccount;
	private String overdraftPurpose;
	private String overdraftLimitType;
	private String creditScoreSourceSystemCd;
	private String demandedOd;
	private ChannelBrandDTO channelDto;
	private List<PartyDto> partiesInformation;

	public Account getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(Account selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public String getDemandedOd() {
		return demandedOd;
	}

	public void setDemandedOd(String demandedOd) {
		this.demandedOd = demandedOd;
	}

	public ChannelBrandDTO getChannelDto() {
		return channelDto;
	}

	public void setChannelDto(ChannelBrandDTO channelDto) {
		this.channelDto = channelDto;
	}

	public List<PartyDto> getPartiesInformation() {
		return partiesInformation;
	}

	public void setPartiesInformation(List<PartyDto> partiesInformation) {
		this.partiesInformation = partiesInformation;
	}

	public String getOverdraftLimitType() {
		return overdraftLimitType;
	}

	public void setOverdraftLimitType(String overdraftLimitType) {
		this.overdraftLimitType = overdraftLimitType;
	}

	public String getOverdraftPurpose() {
		return overdraftPurpose;
	}

	public void setOverdraftPurpose(String overdraftPurpose) {
		this.overdraftPurpose = overdraftPurpose;
	}

    public String getCreditScoreSourceSystemCd() {
        return creditScoreSourceSystemCd;
    }

    public void setCreditScoreSourceSystemCd(String creditScoreSourceSystemCd) {
        this.creditScoreSourceSystemCd = creditScoreSourceSystemCd;
    }
}
