package com.lbg.ib.api.sales.party.service;


import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.common.rest.util.OcisRestHeaderUtil;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import com.lbg.ib.api.sales.party.domain.response.ElectronicAddress;
import com.lbg.ib.api.sales.party.domain.response.PostalAddressDetails;
import com.lbg.ib.api.sales.party.domain.response.RetrievePartyDetailsResponse;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for hitting the RetrievePartyDetails API from OCIS
 * @author 8903735
 *
 */
@Component
public class RetrievePartyDetailsServiceImpl implements RetrievePartyDetailsService {

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private ExternalRestApiClientDAO externalRestApiClientDAO;

    @Autowired
    private OcisRestHeaderUtil ocisRestHeaderUtil;

    @Autowired
    private UriResolver ocisUriResolver;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    private static final String ACTIVE = "ACTIVE";
    private static final String EMAIL = "EMAIL";
    private static final String PERSONAL = "PERSONAL";

    /**
     * Retrieve Party Details
     * @param ocisId
     * @return {@link RetrievePartyDetailsResponse}
     */
    @TraceLog
    public RetrievePartyDetailsResponse retrievePartyDetails(String ocisId) {
        logger.logDebug(RetrievePartyDetailsServiceImpl.class, "Get the party details from OCIS for party id %s", ocisId);
        // prepare the url
        String endpoint = ocisUriResolver.getEndpoint(SalsaEndpoints.RETRIEVE_PARTY_DETAILS_ENDPOINT);
        Map<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("partyId", ocisId);

        //Salsa in the name of the files is due to the use of existing apis
        RestContext externalRestContext = buildRequest(pathParams, endpoint);

        RetrievePartyDetailsResponse response = externalRestApiClientDAO.customGet(externalRestContext,
                RetrievePartyDetailsResponse.class);
        return response;
    }
    /**
     * Retrieve Party Details
     * @param ocisId
     * @return {@link RetrievePartyDetailsResponse}
     */
    @TraceLog
    public PartyDetails getPartyDetails(String ocisId) {
        PartyDetails partyDetails = new PartyDetails();
        RetrievePartyDetailsResponse response = retrievePartyDetails(ocisId);
        if (response == null || response.getParty() == null) {
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.FAILED_TO_GET_OTHER_PARTY_DETAIL));
        }
        partyDetails.setTitle(response.getParty().getTitle());
        partyDetails.setFirstName(response.getParty().getFirstName());
        partyDetails.setSurname(response.getParty().getLastName());
        partyDetails.setDob(response.getParty().getBirthDate());
        if (null != response.getPostalAddress()) {
            for (PostalAddressDetails postalAddressDetails : response.getPostalAddress()) {
                if (ACTIVE.equalsIgnoreCase(postalAddressDetails.getContactPointStatus()) && "RESIDENTIAL".equalsIgnoreCase(postalAddressDetails.getPostalAddressType())) {
                    partyDetails.setPostalCode(postalAddressDetails.getPostCode());
                    partyDetails.setAddressStartDate(postalAddressDetails.getStartDate());
                    partyDetails.setAddressEndDate(postalAddressDetails.getEndDate());
                    if(postalAddressDetails.getAddressLines() != null) {
                        String[] addressLines = new String[postalAddressDetails.getAddressLines().size()];
                        for( int i=0; i<postalAddressDetails.getAddressLines().size(); i++) {
                            addressLines[i] = postalAddressDetails.getAddressLines().get(i);
                        }
                        partyDetails.setAddressLines(addressLines);
                    }
                }
            }
        }
        partyDetails.setJoint(true); // Always true for other party
        partyDetails.setEmail(extractPartyEmail(response));
        return partyDetails;
    }

    /**
     * If more than 1 active email for the party then pick email labelled as 'PERSONAL'
     * Else pick active email irrespective of whether it is labelled as BUSINESS OR PERSONAL
     * @param response
     * @return
     */

    private String extractPartyEmail(RetrievePartyDetailsResponse response) {
        if (response.getElectronicAddress() != null) {
            List<ElectronicAddress> addresses = Lists.newArrayList();
            for (ElectronicAddress address : response.getElectronicAddress()) {
                if (ACTIVE.equalsIgnoreCase(address.getContactPointStatus()) &&
                        (EMAIL).equalsIgnoreCase(address.getElectronicAddressType())) {
                    addresses.add(address);
                }
            }
            if (addresses.isEmpty()) {
                return null;
            }
            if (addresses.size() == 1) {
                return addresses.get(0).getAddress();
            } else {
                for (ElectronicAddress address : addresses) {
                    if (PERSONAL.equalsIgnoreCase(address.getElectronicPartyAddressType())) {
                        return address.getAddress();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Builds the request for rest call
     * @param pathParameters
     * @param endpoint
     * @return {@link RestContext}
     */
    private RestContext buildRequest(Map<String, String> pathParameters, String endpoint) {
        Map<String, Object> headers = ocisRestHeaderUtil.getSalsaHeader();
        return RestContext.SalsaRestContextBuilder.getBuilder(endpoint).requestHeaders(headers).pathParameters(pathParameters)
                .build();
    }

}
