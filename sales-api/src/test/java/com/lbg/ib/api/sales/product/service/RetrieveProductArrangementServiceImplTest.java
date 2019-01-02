
package com.lbg.ib.api.sales.product.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author  Atul Choudhary
 * @version 1.0
 * @since   8thSeptember2016
 ***********************************************************************/
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import com.google.common.collect.Lists;
import org.junit.Test;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.product.retrieve.RetrieveProductDAO;
import com.lbg.ib.api.sales.dao.product.suspended.RetrieveProductArrangementDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.product.ProductDTO;
import com.lbg.ib.api.sales.dto.product.pendingarrangement.ProductArrangementDTO;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import com.lbg.ib.api.sales.product.domain.pending.ArrangementId;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.IndividualName;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InstructionDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.StructuredAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.TelephoneNumber;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.UnstructuredAddress;

public class RetrieveProductArrangementServiceImplTest {
    private static final ProductReference         REFERENCE             = new ProductReference("erf");
    // private static final ProductIdDTO PRODUCT_ID = new ProductIdDTO("",
    // "");
    private static final ChannelBrandDTO          CHANNEL               = new ChannelBrandDTO("ch", "br", "cid");
    private static final ArrangementId            ARR_ID                = new ArrangementId("121366");
    private static final String                   MNEMONIC              = "mnemonic";
    private static final String                   PDT_FAMILY_IDENTIFIER = "1501";
    private static HashMap<String, String>        optionsList           = new HashMap<String, String>();
    private static final ProductDTO               PRODUCT               = new ProductDTO("name", "id", MNEMONIC,
            PDT_FAMILY_IDENTIFIER, optionsList, new HashMap<String, List<String>>() {
                                                                                    {
                                                                                        put("extn", Lists.newArrayList("extv"));
                                                                                    }
                                                                                },
            null);
    private static final ResponseError            IB_ERROR              = new ResponseError();
    private RetrieveProductArrangementDAO         retProdArrDAO         = mock(RetrieveProductArrangementDAO.class);
    private RetrieveProductDAO                    retrieveFeatures      = mock(RetrieveProductDAO.class);
    private ChannelBrandingDAO                    branding              = mock(ChannelBrandingDAO.class);
    private GalaxyErrorCodeResolver               errorResolver         = mock(GalaxyErrorCodeResolver.class);
    private LoggerDAO                             logger                = mock(LoggerDAO.class);
    private SessionManagementDAO                  session               = mock(SessionManagementDAO.class);
    private RetrieveProductArrangementServiceImpl service               = new RetrieveProductArrangementServiceImpl(
            retProdArrDAO, errorResolver, session, branding, logger);
    private ProductArrangementDTO                 prodArrDTO            = new ProductArrangementDTO();

    @Test
    public void shouldReturnCustomerPendingWhenRetrieveFeaturesReturnResponse() throws Exception {

        CustomerDocument[] customerDocuments = new CustomerDocument[1];
        CustomerDocument doc = new CustomerDocument();
        doc.setDocumentReferenceIndex("123");
        customerDocuments[0] = doc;
        prodArrDTO.setCustomerDocuments(customerDocuments);
        Customer cust = new Customer();
        cust.setEmailAddress("a@b.com");
        Individual indi = new Individual();
        indi.setGender("Male");
        IndividualName[] individualNames = new IndividualName[1];

        IndividualName individualName = new IndividualName();
        individualName.setPrefixTitle("Mr");
        individualName.setFirstName("abc");
        individualName.setLastName("def");
        String[] middleNames = { "ghi" };
        individualName.setMiddleNames(middleNames);
        individualNames[0] = individualName;
        indi.setIndividualName(individualNames);
        ;
        Calendar calendar = new GregorianCalendar(2013, 0, 31);
        indi.setBirthDate(calendar);
        indi.setPlaceOfBirth("Delhi");
        indi.setNationality("India");
        indi.setCountryOfBirth("India");
        cust.setIsPlayedBy(indi);
        PostalAddress[] postalAddress = new PostalAddress[1];
        PostalAddress address = new PostalAddress();
        StructuredAddress structuredAddress = new StructuredAddress();
        structuredAddress.setDistrict("district");
        structuredAddress.setPostTown("postTown");
        structuredAddress.setCountry("country");
        structuredAddress.setCounty("county");
        structuredAddress.setOrganisation("organisation");
        structuredAddress.setSubBuilding("subBuilding");
        structuredAddress.setBuilding("building");
        structuredAddress.setBuildingNumber("buildingNumber");
        String[] addressLinePAFData = { "addressLinePAFData" };
        structuredAddress.setAddressLinePAFData(addressLinePAFData);
        structuredAddress.setPostCodeOut("postCodeOut");
        structuredAddress.setPostCodeIn("postCodeIn");
        structuredAddress.setPointSuffix("pointSuffix");
        address.setStructuredAddress(structuredAddress);
        postalAddress[0] = address;
        cust.setPostalAddress(postalAddress);
        TelephoneNumber[] telephoneNumber = new TelephoneNumber[1];
        TelephoneNumber tele = new TelephoneNumber();
        tele.setPhoneNumber("1234567");
        telephoneNumber[0] = tele;
        cust.setTelephoneNumber(telephoneNumber);
        prodArrDTO.setPrimaryInvolvedParty(cust);
        Product product = new Product();
        product.setProductName("esaver");
        product.setProductIdentifier("esaver");
        InstructionDetails instructionDetail = new InstructionDetails();
        instructionDetail.setInstructionMnemonic("ESAVER");
        product.setInstructionDetails(instructionDetail);
        prodArrDTO.setAssociatedProduct(product);
        when(retProdArrDAO.retrieveProductArrangementPending(any(ArrangementId.class)))
                .thenReturn(withResult(prodArrDTO));
        prodArrDTO.setArrangementType("SA");
        prodArrDTO.setArrangementId("121366");
        CustomerPendingDetails custPendingDetails = service.retrieveProductArrangement(ARR_ID);
        assertThat(custPendingDetails.getArrangementId(), is("121366"));
    }

    @Test
    public void shouldReturnCustomerPendingWhenRetrieveFeaturesReturnResponseWithCaseHistory() throws Exception {
        CustomerDocument[] customerDocuments = new CustomerDocument[1];
        CustomerDocument doc = new CustomerDocument();
        doc.setDocumentReferenceIndex("123");
        customerDocuments[0] = doc;
        prodArrDTO.setCustomerDocuments(customerDocuments);
        Customer cust = new Customer();
        cust.setEmailAddress("a@b.com");
        Individual indi = new Individual();
        indi.setGender("Male");
        Calendar calendar = new GregorianCalendar(2013, 0, 31);
        indi.setBirthDate(calendar);
        indi.setPlaceOfBirth("Delhi");
        indi.setNationality("India");
        indi.setCountryOfBirth("India");
        cust.setIsPlayedBy(indi);

        PostalAddress[] postalAddress = new PostalAddress[1];
        PostalAddress address = new PostalAddress();
        UnstructuredAddress unStructuredAddress = new UnstructuredAddress();
        unStructuredAddress.setAddressLine1("district");
        unStructuredAddress.setAddressLine2("postTown");
        unStructuredAddress.setAddressLine3("country");
        unStructuredAddress.setAddressLine4("county");
        unStructuredAddress.setAddressLine5("organisation");
        unStructuredAddress.setAddressLine6("subBuilding");
        unStructuredAddress.setAddressLine7("building");
        unStructuredAddress.setAddressLine8("buildingNumber");
        address.setUnstructuredAddress(unStructuredAddress);
        postalAddress[0] = address;
        cust.setPostalAddress(postalAddress);
        TelephoneNumber[] telephoneNumber = new TelephoneNumber[1];
        TelephoneNumber tele = new TelephoneNumber();
        tele.setPhoneNumber("1234567");
        telephoneNumber[0] = tele;
        cust.setTelephoneNumber(telephoneNumber);
        prodArrDTO.setPrimaryInvolvedParty(cust);
        Product product = new Product();
        product.setProductName("esaver");
        product.setProductIdentifier("esaver");
        InstructionDetails instructionDetail = new InstructionDetails();
        instructionDetail.setInstructionMnemonic("ESAVER");
        product.setInstructionDetails(instructionDetail);
        prodArrDTO.setAssociatedProduct(product);

        when(retProdArrDAO.retrieveProductArrangementPending(any(ArrangementId.class)))
                .thenReturn(withResult(prodArrDTO));
        prodArrDTO.setArrangementType("SA");
        prodArrDTO.setArrangementId("121366");

        CustomerPendingDetails custPendingDetails = service.retrieveProductArrangement(ARR_ID);
        assertThat(custPendingDetails.getArrangementId(), is("121366"));
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionIncaseOfAnyException() throws Exception {
        when(retProdArrDAO.retrieveProductArrangementPending(any(ArrangementId.class)))
                .thenThrow(new Exception("error"));
        assertThat(service.retrieveProductArrangement(ARR_ID), is(nullValue()));
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenAppNotFound() throws Exception {
        DAOResponse<ProductArrangementDTO> daoResponse = withError(
                new DAOError(BUSSINESS_ERROR, "application not found"));
        when(retProdArrDAO.retrieveProductArrangementPending(any(ArrangementId.class))).thenReturn(daoResponse);
        assertThat(service.retrieveProductArrangement(ARR_ID), is(nullValue()));
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenIncorrectBrand() throws Exception {
        DAOResponse<ProductArrangementDTO> daoResponse = withError(
                new DAOError(BUSSINESS_ERROR, "Application does not belong to specified brand"));
        when(retProdArrDAO.retrieveProductArrangementPending(any(ArrangementId.class))).thenReturn(daoResponse);
        assertThat(service.retrieveProductArrangement(ARR_ID), is(nullValue()));
    }

    @Test(expected = ServiceException.class)
    public void shouldReturnServiceExceptionWhenOtherBusinessException() throws Exception {
        DAOResponse<ProductArrangementDTO> daoResponse = withError(
                new DAOError(BUSSINESS_ERROR, "something went wrong"));
        when(retProdArrDAO.retrieveProductArrangementPending(any(ArrangementId.class))).thenReturn(daoResponse);
        assertThat(service.retrieveProductArrangement(ARR_ID), is(nullValue()));
    }

    @Test(expected = Exception.class)
    public void shouldReturnNullWhenReferenceDataDaoDoesNotMatchForReference() throws Exception {
        when(retProdArrDAO.retrieveProductArrangementPending(any(ArrangementId.class))).thenReturn(null);
        assertThat(service.retrieveProductArrangement(ARR_ID), is(nullValue()));
    }
}
