package com.lbg.ib.api.sales.idassertion.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.idassertion.domain.IdentityAssertionResponse;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.saml.TokenIdValidated;
import com.lbg.ib.api.sso.domain.saml.TokenIdValidation;
import com.lbg.ib.api.sso.service.ValidateTokenIdService;

@Path("/saml")
public class IdentityAssertionResource {

	@Autowired
	private ValidateTokenIdService service;

	/**
	 * Mock API. Plz comment the resource in digital.
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@GET
	@Produces({ APPLICATION_JSON })
	@Path("/identity-assertion")
	@TraceLog
	public Response setColleagueToBranchContext(@Context HttpServletRequest req) throws Exception {

		IdentityAssertionResponse identityAssertionResponse = new IdentityAssertionResponse();

		identityAssertionResponse.setSamlToken("SAMLASSERTION6");
		return respond(Status.OK, identityAssertionResponse);

	}

	@GET
	@Produces({ APPLICATION_JSON })
	@Path("/identity-assertion/{id}")
	@TraceLog
	public Response setColleagueToBranchContext1(@Context HttpServletRequest req, @PathParam("id") String id)
			throws Exception {

		IdentityAssertionResponse identityAssertionResponse = new IdentityAssertionResponse();
		if (id != null) {
			identityAssertionResponse.setSamlToken(id.trim());
			return respond(Status.OK, callTokenService(identityAssertionResponse));
		} else {
			identityAssertionResponse.setSamlToken("SAMLASSERTION6");
			return respond(Status.OK, identityAssertionResponse);
		}

	}

	private TokenIdValidated callTokenService(IdentityAssertionResponse identityAssertionResponse) {
		TokenIdValidation tokenIdValidation = new TokenIdValidation(identityAssertionResponse.getSamlToken());
		return service.validate(tokenIdValidation);

	}

	private Response respond(Status status, Object content) {
		return new ResponseBuilderImpl().status(status).entity(content).build();
	}
}
