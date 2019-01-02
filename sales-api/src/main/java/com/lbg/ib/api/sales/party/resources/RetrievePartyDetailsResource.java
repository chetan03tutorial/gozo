package com.lbg.ib.api.sales.party.resources;

import com.lbg.ib.api.sales.party.domain.response.RetrievePartyDetailsResponse;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsService;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsServiceImpl;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Resource class for RetrievePartyDetails
 * @author 8903735
 *
 */
@Path("/party/")
public class RetrievePartyDetailsResource {

	@Autowired
	private RetrievePartyDetailsService retrievePartyDetailsService;

	@Autowired
	private LoggerDAO logger;

	/**
	 * Returns the partyDetails response
	 * @param ocisId
	 * @return {@link Response}
	 */
	@GET
	@Produces("application/json")
	@Path("/{ocisId}")
	@TraceLog
	public Response retrievePartyDetails(@PathParam("ocisId") String ocisId) throws InvalidFormatException {
		logger.logDebug(RetrievePartyDetailsResource.class, "Retrieve party details for ocis id - %s", ocisId);
		validate(ocisId);

		RetrievePartyDetailsResponse partyDetails = retrievePartyDetailsService.retrievePartyDetails(ocisId);

		logger.logDebug(RetrievePartyDetailsServiceImpl.class, "Response from OCIS - %s", partyDetails);

		return respond(Status.OK, partyDetails);
	}

	/**
	 * Validates the param
	 * @param partyId
	 */
	private void validate(String partyId) throws InvalidFormatException {
		if (StringUtils.isBlank(partyId)) {
			throw new InvalidFormatException("Invalid Party Identifier");
		}
	}

	/**
	 * Creates and returns response
	 *
	 * @param status
	 * @param content
	 * @return response
	 */
	private Response respond(Status status, Object content) {
		return new ResponseBuilderImpl().status(status).entity(content).build();
	}
}
