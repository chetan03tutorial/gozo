package com.lbg.ib.api.sales.overdraft.service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants;
import com.lbg.ib.api.sales.overdraft.domain.IeCustomerDetails;
import com.lbg.ib.api.sales.overdraft.domain.Q122Request;
import com.lbg.ib.api.sales.overdraft.domain.Q122Response;
import com.lbg.ib.api.sales.overdraft.dto.IeCustomerDetailsDto;
import com.lbg.ib.api.sales.overdraft.dto.Q122DTO;
import com.lbg.ib.api.sales.overdraft.mapper.Q122MessageMapper;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.PartyInformation;
import com.lbg.ib.api.sales.user.dto.AddressDto;
import com.lbg.ib.api.sales.user.dto.NameDto;
import com.lbg.ib.api.sales.user.dto.PartyDto;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydsbanking.xml.Q122Req;
import com.lloydsbanking.xml.Q122Resp;
import com.lloydsbanking.xml.Q122_FunOvrdrtDecisnReq_PortType;
import com.lloydsbanking.xml.Q122_FunOvrdrtDecisnReq_ServiceLocator;

@Component
public class Q122ServiceImpl extends SoaAbstractService implements Q122Service {

    @Autowired
    private AccountInContextUtility contextUtility;

    @Autowired
    private ServiceErrorValidator serviceErrorMapper;

    @Autowired
    private ChannelBrandingDAO channelService;

    @Autowired
    private LoggerDAO logger;

    @Override
    public Class<?> getPort() {
        return Q122_FunOvrdrtDecisnReq_PortType.class;
    }

    @HeaderRegistry(portNameMethod = "getQ122_FunOvrdrtDecisnReqWSDDPortName", serviceLocator = Q122_FunOvrdrtDecisnReq_ServiceLocator.class)
    @PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class,
            SecurityHeader.class }, serviceName = "Q122", serviceAction = "Q122")
    public Q122Response invokeQ122(Q122Request q122Request, String creditScoreSourceSystemCd) {
        Q122MessageMapper messageMapper = new Q122MessageMapper();
        Q122DTO q122Dto = buildDto(q122Request);
        Q122Req serviceRequest = messageMapper.buildQ122Request(q122Dto,creditScoreSourceSystemCd);
        logger.traceLog(this.getClass(),
                "Unique CreditScoreReferenceNumber is " + serviceRequest.getRequestDetails().getCreditScoreRequestNo());
        Q122Resp serviceResponse = (Q122Resp) invoke("q122", serviceRequest);
        serviceErrorMapper.validateResponseError(serviceResponse.getQ122Result().getResultCondition());
        verifyAndUpdateMaximumOverdraftLimit(serviceRequest, serviceResponse);
        Q122Response q122Response = messageMapper.buildQ122Response(serviceRequest, serviceResponse);
        String existingOverdraft = String.valueOf(contextUtility.getAvailedOverdraft());
        logger.traceLog(this.getClass(),
                "Q122 Result: Decision=" + q122Response.getOverdraftDecision()
                        + " Maximum Overdraft="+ contextUtility.getMaximumOverdraftLimit()
                        + " Upfront Eligible Amount=" + contextUtility.getMaxOfferedByAsm()
                        + " Existing Overdraft="+ existingOverdraft);
        return q122Response;
    }

    private void verifyAndUpdateMaximumOverdraftLimit(Q122Req q122Request, Q122Resp q122Response) {
        Integer creditResult = Integer.valueOf(q122Response.getASMCreditScoreResultCd());
        logger.traceLog(this.getClass(), "Credit Result offered is " + creditResult);
        // Force maxLimit to ZERO when overdraft is declined or any other value
        // is sent
        if (creditResult >= 3) {
            contextUtility.setMaximumOverdraftLimit(OverdraftConstants.ZERO);
            return;
        }
        // Force maxLimit to what customer has demanded in case the OD amount
        // has been
        // accepted
        if (creditResult == 1) {
            contextUtility.setMaximumOverdraftLimit(
                    Double.valueOf(q122Request.getApplicationDetails().getReqstdOvrdrtLimitAm()));
            return;
        }
        Double maximumAllowedOverdraft = Double.valueOf(q122Response.getAffordableAm());
        if(maximumAllowedOverdraft != null) {
            maximumAllowedOverdraft = maximumAllowedOverdraft/100;
        }
        logger.traceLog(this.getClass(), "Affordable Amount offered is " + maximumAllowedOverdraft);
        contextUtility.setMaximumOverdraftLimit(maximumAllowedOverdraft);
    }

    private Q122DTO buildDto(Q122Request q122Request) {
        Q122DTO q122Dto = new Q122DTO();
        q122Dto.setCreditScoreSourceSystemCd(contextUtility.getCreditScoreSourceSystemCd());
        q122Dto.setOverdraftLimitType(q122Request.getOverdraftLimitType());
        q122Dto.setOverdraftPurpose(q122Request.getOverdraftPurpose());
        q122Dto.setSelectedAccount(contextUtility.getSelectedAccountDetail());
        q122Dto.setChannelDto(channelService.getChannelBrand().getResult());
        logger.traceLog(this.getClass(), "ProductId is " + channelService.getChannelBrand().getResult().getBrand());
        q122Dto.setDemandedOd(q122Request.getDemandedOd());
        q122Dto.setPartiesInformation(buildPartyDto(q122Request));
        return q122Dto;
    }

    private List<PartyDto> buildPartyDto(Q122Request q122Request) {
        List<PartyDto> partyList = new LinkedList<PartyDto>();
        if (CollectionUtils.isEmpty(q122Request.getParties())) {
            PartyInformation partyInformation = new PartyInformation();
            partyInformation.setDurationOfStay(q122Request.getDurationOfStay());
            partyInformation.setEmploymentStatus(q122Request.getEmploymentStatusCode());
            partyInformation.setIeIndicator(q122Request.isIeIndicator());
            partyInformation.setPrimary(true);
            if (q122Request.isIeIndicator()) {
                IeCustomerDetails ieDetails = new IeCustomerDetails();
                ieDetails.setAccommodationExpenses(q122Request.getAccommodationExpenses());
                ieDetails.setIncomeFrequency(q122Request.getIncomeFrequency());
                ieDetails.setMonthlyExpenses(q122Request.getMonthlyExpenses());
                ieDetails.setMonthlyIncome(q122Request.getMonthlyIncome());
                partyInformation.setIeDetails(ieDetails);
            }
            partyList.add(0, buildParty(partyInformation));
        } else {
            for (PartyInformation partyInformation : q122Request.getParties()) {
                if (partyInformation.isPrimary()) {
                    partyList.add(0, buildParty(partyInformation));
                } else {
                    partyList.add(1, buildParty(partyInformation));
                }
            }
        }
        return partyList;
    }

    private PartyDto buildParty(PartyInformation partyInformation) {
        PartyDetails party = null;
        String ocisId = null;
        String cidPersId = null;
        String cbsCustomerNumber = null;
        if (partyInformation.isPrimary()) {
            party = contextUtility.getPrimaryPartyDetails();
            ocisId = contextUtility.getPrimaryPartyOcisId();
            cidPersId = contextUtility.getPrimaryPartyCidPersonalId();
            cbsCustomerNumber = contextUtility.getPrimaryPartyCbsCustomerNumber();
        } else {
            party = contextUtility.getJointPartyDetails();
            ocisId = contextUtility.getJointPartyOcisId();
        }
        PartyDto partyDto = new PartyDto();
        partyDto.setOcisId(ocisId);
        partyDto.setPartyId(cidPersId);
        partyDto.setCbsCustomerNumber(cbsCustomerNumber);
        partyDto.setBirthDate(party.getDob());
        partyDto.setEmailAddress(party.getEmail());
        partyDto.setName(buildName(party));
        AddressDto address = buildAddress(party);
        address.setDurationOfStay(partyInformation.getDurationOfStay());
        partyDto.setAddress(address);
        partyDto.setIeIndicator(partyInformation.isIeIndicator());
        if (partyInformation.isIeIndicator()) {
            IeCustomerDetails ieDetails = partyInformation.getIeDetails();
            partyDto.setIeDetails(buildIeDetailDto(ieDetails));
        }
        partyDto.setEmploymentStatus(partyInformation.getEmploymentStatus());
        return partyDto;
    }

    private IeCustomerDetailsDto buildIeDetailDto(IeCustomerDetails ieDetails) {
        IeCustomerDetailsDto dto = new IeCustomerDetailsDto();
        dto.setAccommodationExpenses(ieDetails.getAccommodationExpenses());
        dto.setIncomeFrequency(ieDetails.getIncomeFrequency());
        dto.setMonthlyExpenses(ieDetails.getMonthlyExpenses());
        dto.setMonthlyIncome(ieDetails.getMonthlyIncome());
        return dto;
    }

    private NameDto buildName(PartyDetails party) {
        NameDto name = new NameDto();
        name.setFirstName(party.getFirstName());
        name.setLastName(party.getSurname());
        return name;
    }

    private AddressDto buildAddress(PartyDetails party) {
        AddressDto address = new AddressDto();
        address.setAddressStartDate(party.getAddressStartDate());
        address.setAddressEndDate(party.getAddressEndDate());
        address.setAddressLines(Arrays.asList(party.getAddressLines()));
        address.setPostCode(party.getPostalCode());
        return address;
    }

}
