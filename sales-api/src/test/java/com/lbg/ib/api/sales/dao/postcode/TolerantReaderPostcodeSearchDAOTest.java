package com.lbg.ib.api.sales.dao.postcode;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dto.postcode.PostalAddressDTO;
import com.lbg.ib.api.sales.dto.postcode.PostcodeDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.postcodesearch.communication.SoapServiceCommunicator;
import com.lbg.ib.api.sales.soapapis.postcodesearch.communication.SoapServiceRequest;
import com.lbg.ib.api.sales.soapapis.postcodesearch.communication.SoapServiceResponse;
import com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.domain.*;
import com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.domain.LloydsPostcodeBackendResponse.PostcodeError;
import com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.transformers.LloydsPostcodeBackendRequestXmlMarshaller;
import com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.transformers.LloydsPostcodeBackendResponseXmlUnmarshaller;
import org.junit.Test;

import java.net.URI;
import java.util.List;

import static com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.domain.LloydsPostcodeBackendResponse.addresses;
import static com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.domain.LloydsPostcodeBackendResponse.error;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class TolerantReaderPostcodeSearchDAOTest {

    public static final String                           CODE            = "code";
    public static final String                           MESSAGE         = "message, Called Method: search, Method Call Params: PostcodeDTO{inPostcode='asd', outPostcode='ad'}";
    public static final String                           MARSHALLED_BODY = "marshalledBody";
    private LloydsPostcodeBackendRequestXmlMarshaller    marshaller      = mock(
            LloydsPostcodeBackendRequestXmlMarshaller.class);
    private LloydsPostcodeBackendResponseXmlUnmarshaller unmarshaller    = mock(
            LloydsPostcodeBackendResponseXmlUnmarshaller.class);
    private SoapServiceCommunicator                      communicator    = mock(SoapServiceCommunicator.class);
    private LoggerDAO                                    logger          = mock(LoggerDAO.class);
    private ApiServiceProperties                         properties      = mock(ApiServiceProperties.class);
    private GBOHeaderUtility                             headerUtility   = mock(GBOHeaderUtility.class);

    @Test
    public void shouldResponseWithPostalAddressesWhenPostcodeUnmarshallerReturnsWithPostalAddresses() throws Exception {
        URI ENDPOINT = new URI("http://example");
        TolerantReaderPostcodeSearchDAO dao = new TolerantReaderPostcodeSearchDAO(headerUtility, marshaller,
                unmarshaller, communicator, logger, properties);
        when(properties.getUriEndpointForPostcodeCheckAddress()).thenReturn(ENDPOINT);
        when(marshaller.marshal(expectedBody())).thenReturn(MARSHALLED_BODY);
        when(headerUtility.prepareSoapHeader(anyString(), anyString()))
                .thenReturn(asList(bapiHeader(), contactPointHeader()));
        when(communicator.execute(expectedRequestFor(ENDPOINT, MARSHALLED_BODY)))
                .thenReturn(new SoapServiceResponse("communicatorResponse"));
        when(unmarshaller.unmarshal("communicatorResponse")).thenReturn(addresses(
                asList(new PostalAddressExternal("d", "t", "p", "c", "o", "s", "b", "n", asList("l1", "l2"), "dps"))));

        DAOResponse<List<PostalAddressDTO>> search = dao.search(new PostcodeDTO("ad", "asd"));

        verify(logger, never()).logError(any(String.class), any(String.class), any(Class.class));
        assertThat(search.getError(), is(nullValue()));
        assertThat(search.getResult(),
                is(asList(new PostalAddressDTO("d", "t", "p", "c", "o", "s", "b", "n", asList("l1", "l2"), "dps"))));
    }

    private PostcodeSearchRequest expectedBody() {
        PostcodeExternal postcodeExternal = new PostcodeExternal("ad", "asd");
        ContactPointHeader contact = new ContactPointHeader("pointType", "pointId", "appId", "originatorType");
        BapiHeader bapi = new BapiHeader("chanId", "session", "host", "partyId", "ocis", "chansemode", "agent",
                "inboxId", "author");
        return new PostcodeSearchRequest(postcodeExternal, contact, bapi);
    }

    private SoapServiceRequest expectedRequestFor(URI endpoint, String marshalledBody) {
        return new SoapServiceRequest(endpoint, marshalledBody);
    }

    private SOAPHeader contactPointHeader() {
        SOAPHeader soapHeader = new SOAPHeader();
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setApplicationId("appId");
        contactPoint.setContactPointId("pointId");
        contactPoint.setContactPointType("pointType");
        contactPoint.setInitialOriginatorType("originatorType");
        soapHeader.setName(GBOHeaderUtility.CONTACT_POINT);
        soapHeader.setValue(contactPoint);
        return soapHeader;
    }

    private SOAPHeader bapiHeader() {
        SOAPHeader soapHeader = new SOAPHeader();
        BapiInformation bapiInformation = new BapiInformation();
        BAPIHeader bapiHeader = new BAPIHeader();
        bapiHeader.setChanid("chanId");
        bapiHeader.setChansecmode("chansemode");
        bapiHeader.setInboxidClient("inboxId");
        bapiHeader.setUserAgent("agent");
        bapiHeader.setSessionid("session");
        bapiHeader.setUseridAuthor("author");
        HostInformation stpartyObo = new HostInformation();
        stpartyObo.setPartyid("partyId");
        stpartyObo.setHost("host");
        stpartyObo.setOcisid("ocis");
        bapiHeader.setStpartyObo(stpartyObo);
        bapiInformation.setBAPIHeader(bapiHeader);
        soapHeader.setName(GBOHeaderUtility.BAPI_INFORMATION);
        soapHeader.setValue(bapiInformation);
        return soapHeader;
    }

    @Test
    public void shouldResponseWithErrorWhenPostcodeUnmarshallerReturnsWithErrorResponse() throws Exception {
        TolerantReaderPostcodeSearchDAO dao = new TolerantReaderPostcodeSearchDAO(headerUtility, marshaller,
                unmarshaller, communicator, logger, properties);
        when(communicator.execute(any(SoapServiceRequest.class))).thenReturn(new SoapServiceResponse(""));
        when(unmarshaller.unmarshal(any(String.class))).thenReturn(error(new PostcodeError(CODE, "message")));

        DAOResponse<List<PostalAddressDTO>> search = dao.search(new PostcodeDTO("ad", "asd"));

        verify(logger).logError(CODE, MESSAGE, TolerantReaderPostcodeSearchDAO.class);
        assertThat(search.getError().getErrorCode(), is(CODE));
        assertThat(search.getError().getErrorMessage(), is("message"));
    }
}