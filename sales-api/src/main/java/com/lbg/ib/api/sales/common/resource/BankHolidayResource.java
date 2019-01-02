package com.lbg.ib.api.sales.common.resource;

import com.lbg.ib.api.sales.common.domain.BankHolidaysHolder;
import com.lbg.ib.api.sales.common.service.BankHolidayService;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by Rohit Soni on 12/03/2018.- PCA-6522 Resource controller for
 * getting Bank Holidays
 */

@Path("/bankHolidays")
public class BankHolidayResource {

	@Autowired
	private BankHolidayService bankHolidayService;
	@Autowired
	private LoggerDAO logger;

	@GET
	@Produces(APPLICATION_JSON)
	public Response getBankHolidays() {
		logger.traceLog(this.getClass(), "Inside the Bank holidays Resource");
		BankHolidaysHolder bankHolidaysHolder = bankHolidayService.getBankHolidaysList();
		if ((bankHolidaysHolder.getBankHolidays() != null) && (!(bankHolidaysHolder.getBankHolidays().isEmpty()))) {
			logger.traceLog(this.getClass(),
					"Bank Holidays response returned : " + bankHolidaysHolder.getBankHolidays());
		} else {
			logger.traceLog(this.getClass(), "No or Error response returned from BankHolidays service.");
		}
		return respond(Response.Status.OK, bankHolidaysHolder);
	}

	private Response respond(Response.Status status, Object content) {
		return new ResponseBuilderImpl().status(status).entity(content).build();
	}
}
