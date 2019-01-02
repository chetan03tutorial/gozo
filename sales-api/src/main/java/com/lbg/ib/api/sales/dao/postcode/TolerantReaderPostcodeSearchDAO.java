package com.lbg.ib.api.sales.dao.postcode;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dto.postcode.DTOtoExternalMapper;
import com.lbg.ib.api.sales.dto.postcode.ExternalToDTOMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.GBOHeaderUtility.BAPI_INFORMATION;
import static com.lbg.ib.api.sales.dao.GBOHeaderUtility.CONTACT_POINT;
import static com.lbg.ib.api.shared.util.logger.ServiceLogger.formatMessage;

@Component
public class TolerantReaderPostcodeSearchDAO implements PostcodeSearchDAO {
    private final GBOHeaderUtility                             headerUtility;
    private final LoggerDAO                                    loggerDAO;
    private final LloydsPostcodeBackendRequestXmlMarshaller    marshaller;
    private final LloydsPostcodeBackendResponseXmlUnmarshaller unmarshaller;
    private final SoapServiceCommunicator                      communicator;
    private final ApiServiceProperties                         properties;

    @Autowired
    public TolerantReaderPostcodeSearchDAO(GBOHeaderUtility headerUtility, LoggerDAO loggerDAO,
            ApiServiceProperties properties) {
        this(headerUtility, new LloydsPostcodeBackendRequestXmlMarshaller(),
                new LloydsPostcodeBackendResponseXmlUnmarshaller(),
                new SoapServiceCommunicator(properties.soapCommunicatorTimeoutInMillis()), loggerDAO, properties);
    }

    TolerantReaderPostcodeSearchDAO(GBOHeaderUtility headerUtility,
            LloydsPostcodeBackendRequestXmlMarshaller marshaller,
            LloydsPostcodeBackendResponseXmlUnmarshaller unmarshaller, SoapServiceCommunicator communicator,
            LoggerDAO loggerDAO, ApiServiceProperties properties) {
        this.headerUtility = headerUtility;
        this.loggerDAO = loggerDAO;
        this.properties = properties;
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
        this.communicator = communicator;
    }

    @TraceLog
    public DAOResponse<List<PostalAddressDTO>> search(PostcodeDTO postcode) throws Exception {
        SoapServiceResponse result = soapCommunicatorCall(properties.getUriEndpointForPostcodeCheckAddress(), postcode);
        LloydsPostcodeBackendResponse response = unmarshaller.unmarshal(result.responseBody());
        PostcodeError postcodeError = response.getPostcodeError();
        if (postcodeError != null) {
            loggerDAO.logError(postcodeError.getCode(), formatMessage(postcodeError.getMessage(), "search", postcode),
                    TolerantReaderPostcodeSearchDAO.class);
            return withError(new DAOResponse.DAOError(postcodeError.getCode(), postcodeError.getMessage()));
        }
        return withResult(postalAddresses(response));
    }

    private List<PostalAddressDTO> postalAddresses(LloydsPostcodeBackendResponse response) {
        List<PostalAddressDTO> dtos = new ArrayList<PostalAddressDTO>();
        ExternalToDTOMapper mapper = new ExternalToDTOMapper();
        for (PostalAddressExternal external : response.getPostalAddressExternals()) {
            dtos.add(mapper.map(external));
        }
        return dtos;
    }

    private SoapServiceResponse soapCommunicatorCall(URI endpointUri, PostcodeDTO postcode) throws IOException {
        DTOtoExternalMapper mapper = new DTOtoExternalMapper();
        return communicator.execute(new SoapServiceRequest(endpointUri, marshaller
                .marshal(new PostcodeSearchRequest(mapper.map(postcode), contactPointHeader(), bapiHeader()))));

    }

    private BapiHeader bapiHeader() {
        BapiInformation bapiInformation = getHeader(BAPI_INFORMATION, BapiInformation.class);
        if (bapiInformation == null || bapiInformation.getBAPIHeader() == null) {
            return null;
        }
        BAPIHeader bapiHeader = bapiInformation.getBAPIHeader();
        HostInformation host = bapiHeader.getStpartyObo();
        return new BapiHeader(bapiHeader.getChanid(), bapiHeader.getSessionid(), host == null ? null : host.getHost(),
                host == null ? null : host.getPartyid(), host == null ? null : host.getOcisid(),
                bapiHeader.getChansecmode(), bapiHeader.getUserAgent(), bapiHeader.getInboxidClient(),
                bapiHeader.getUseridAuthor());
    }

    private ContactPointHeader contactPointHeader() {
        ContactPoint header = getHeader(CONTACT_POINT, ContactPoint.class);
        if (header == null) {
            return null;
        } else {
            return new ContactPointHeader(header.getContactPointType(), header.getContactPointId(),
                    header.getApplicationId(), header.getInitialOriginatorType());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getHeader(String name, Class<T> type) {
        List<SOAPHeader> headers = headerUtility.prepareSoapHeader("retrievePostalAddressDetails",
                "retrievePostalAddressService");
        for (SOAPHeader header : headers) {
            if (name.equals(header.getName())) {
                return (T) header.getValue();
            }
        }
        return null;
    }
}
