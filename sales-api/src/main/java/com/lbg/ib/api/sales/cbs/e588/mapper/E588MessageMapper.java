package com.lbg.ib.api.sales.cbs.e588.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.cbs.e588.domain.E588Request;
import com.lbg.ib.api.sales.cbs.e588.domain.E588Response;
import com.lbg.ib.api.sales.cbs.e588.dto.E588RequestDto;
import com.lbg.ib.api.sales.cbs.e588.dto.E588ResponseDto;
import com.lbg.ib.api.sales.gozo.mappers.MessageMapper;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.ProductFamilyIdentifier;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lloydsbanking.xml.CBSRequestGp2;
import com.lloydsbanking.xml.E588Req;
import com.lloydsbanking.xml.E588Resp;

@Component
public class E588MessageMapper implements MessageMapper {

	@Autowired
	private ChannelBrandingDAO channelService;

	@Autowired
	private ServiceErrorValidator serviceErrorValidator;

	public Object buildRequest(Object... clientRequest) {
		E588RequestDto request = (E588RequestDto) clientRequest[0];
		E588Req serviceRequest = new E588Req();
		CBSRequestGp2 cbsRequestGroup2 = new CBSRequestGp2();
		cbsRequestGroup2.setSourceId(request.getSourceId());
		cbsRequestGroup2.setInputOfficerFlagStatusCd(request.getInputOfficerFlag());
		cbsRequestGroup2.setOverrideDetailsCd(request.getOverrideFlag());
		serviceRequest.setCBSRequestGp2(cbsRequestGroup2);
		serviceRequest.setMaxRepeatGroupQy(request.getMaxGroupQuantity());
		serviceRequest.setCBSAccountNoId(request.getCbsAccountNumber());
		serviceRequest.setCBSProdNoFlagId(request.getProductFlag());
		serviceRequest.setCBSProdNoId(request.getProductNumber());
		serviceRequest.setTariffFlagId(request.getTariffFlag());
		serviceRequest.setTariffId(request.getTariffId());
		return serviceRequest;
	}

	public Object buildResponse(Object... serviceResponse) {
		E588ResponseDto response = (E588ResponseDto)serviceResponse[0];
		E588Response clientResponse = new E588Response();
		clientResponse.setCbsCustomerNumber(response.getCbsCustomerNumber());
		return clientResponse;
	}

	public Object buildRequestDto(Object... request) {
		E588Request clientRequest = (E588Request) request[0];
		E588RequestDto requestDto = new E588RequestDto();
		requestDto.setCbsAccountNumber(clientRequest.getAccountNumber());
		requestDto.setInputOfficerFlag(0);
		requestDto.setMaxGroupQuantity(0);
		requestDto.setOverrideFlag(1);
		requestDto.setProductFlag(1);
		requestDto.setProductNumber(findProductFamilyId());
		requestDto.setSourceId("777505");
		requestDto.setTariffFlag(1);
		requestDto.setTariffId(clientRequest.getTariff());
		return requestDto;
	}

	public Object buildResponseDto(Object... serviceResponse) {
		E588Resp response = (E588Resp) serviceResponse[0];
		serviceErrorValidator.validateResponseError(response.getE588Result().getResultCondition());
		E588ResponseDto responseDto = new E588ResponseDto();
		responseDto.setCbsCustomerNumber(response.getCustomerNumberGp().getCBSCustNo());
		return responseDto;
	}

	private Integer findProductFamilyId() {
		String channelId = channelService.getChannelBrand().getResult().getBrand();
		ProductFamilyIdentifier productFamilyIdentifier = OverdraftConstants.ProductFamilyIdentifier.valueOf(channelId);
		return Integer.valueOf(productFamilyIdentifier.stringValue());
	}
}
