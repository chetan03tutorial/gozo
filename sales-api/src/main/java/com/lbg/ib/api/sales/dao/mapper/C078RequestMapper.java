package com.lbg.ib.api.sales.dao.mapper;

import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.APPLICATION_SOURCE_CD_BRANCH;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.APPLICATION_SOURCE_CD_DIGITAL;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.CS_ORGANISATION_CD;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.MAX_REPEAT_GROUP_QTY;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.formatter;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.dto.C078RequestDto;
import com.lbg.ib.api.sales.utils.CommonUtils;
import com.lloydstsb.www.C078Req;

/**
 * Created by Debashish Bhattacharjee on 25/05/2018. The mapper covers the
 * mapping of the Request
 */
@Component
public class C078RequestMapper {
	@Autowired
	private CommonUtils commonUtils;
	/*
	 * The invokeC078 is used to invoke the CO78 to fetch the App Score response
	 * 
	 * @param AppScoreRequest
	 * 
	 * @return The C078Resp
	 */

	public C078Req create(AppScoreRequest appScoreRequest) {
		C078Req request = new C078Req();
		request.setMaxRepeatGroupQy(MAX_REPEAT_GROUP_QTY);
		request.setCSProcessDt(formatter.format(new Date()));
		request.setCSOrganisationCd(CS_ORGANISATION_CD);
		if (commonUtils.isBranchContext()) {
			request.setApplicationSourceCd(APPLICATION_SOURCE_CD_BRANCH);
		} else {
			request.setApplicationSourceCd(APPLICATION_SOURCE_CD_DIGITAL);
		}
		request.setCreditScoreSourceSystemCd(appScoreRequest.getApplicationType().getValue());
		request.setCreditScoreRequestNo(appScoreRequest.getCreditScoreRequestNo());
		return request;
	}

	public static C078Req buildC078Request(C078RequestDto appScoreRequest) {
		C078Req request = new C078Req();
		request.setMaxRepeatGroupQy(appScoreRequest.getMaxRepeatGroupQuantity());
		request.setCSProcessDt(appScoreRequest.getProcessDate());
		request.setCSOrganisationCd(appScoreRequest.getOrganisationCode());
		request.setApplicationSourceCd(appScoreRequest.getApplicationSourceCode());
		request.setCreditScoreSourceSystemCd(appScoreRequest.getCreditScoreSourceSystemCode());
		request.setCreditScoreRequestNo(appScoreRequest.getCreditScoreRequestNo());
		return request;
	}
}
