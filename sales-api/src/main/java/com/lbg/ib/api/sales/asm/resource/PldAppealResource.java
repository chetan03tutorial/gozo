package com.lbg.ib.api.sales.asm.resource;
/*
Created by Rohit.Soni at 22/05/2018 10:54
*/

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.asm.service.PldAppealService;
import com.lbg.ib.api.sales.common.resource.BaseResource;
import com.lbg.ib.api.sales.pld.request.PldAppealRequest;
import com.lbg.ib.api.sales.product.domain.features.PldProductInfo;
import com.lbg.ib.api.shared.util.logger.TraceLog;

@Path("/pldAppeal")
public class PldAppealResource extends BaseResource {
	@Autowired
	private PldAppealService service;

	/**
	 * API to enquire c078 for app score
	 * 
	 * @param appScoreRequest
	 * @return
	 */
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@TraceLog
	public Response pldAppeal(PldAppealRequest pldAppealRequest) {
		PldProductInfo pldProductInfo = service
				.fetchAppScoreResultsAndUpdateDecision(pldAppealRequest);
		return respond(Response.Status.OK, pldProductInfo);
	}
}
