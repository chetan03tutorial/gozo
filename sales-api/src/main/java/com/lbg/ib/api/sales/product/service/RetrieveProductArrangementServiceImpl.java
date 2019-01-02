
package com.lbg.ib.api.sales.product.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.product.suspended.RetrieveProductArrangementDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.pendingarrangement.ProductArrangementDTO;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.pending.ArrangementId;
import com.lbg.ib.api.sales.product.domain.pending.CustomerName;
import com.lbg.ib.api.sales.product.domain.pending.CustomerPendingDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ArrangementHistory;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.IndividualName;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Organisation;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.StructuredAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.TelephoneNumber;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.UnstructuredAddress;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 8thSeptember2016
 ***********************************************************************/
@Component
public class RetrieveProductArrangementServiceImpl implements RetrieveProductArrangementService {
    private RetrieveProductArrangementDAO retrieveProductArrangementDAO;
    private GalaxyErrorCodeResolver       resolver;
    private ChannelBrandingDAO            branding;
    private SessionManagementDAO          session;
    private LoggerDAO                     logger;

    @Autowired
    public RetrieveProductArrangementServiceImpl(RetrieveProductArrangementDAO retrieveProductArrangementDAO,
            GalaxyErrorCodeResolver resolver, SessionManagementDAO session, ChannelBrandingDAO branding,
            LoggerDAO logger) {
        this.retrieveProductArrangementDAO = retrieveProductArrangementDAO;
        this.resolver = resolver;
        this.session = session;
        this.branding = branding;
        this.logger = logger;
    }

    public CustomerPendingDetails retrieveProductArrangement(ArrangementId reference) throws ServiceException {
        DAOResponse<ProductArrangementDTO> responseDTO = null;
        try {
            responseDTO = retrieveProductArrangementDAO.retrieveProductArrangementPending(reference);
        } catch (Exception e) {
            logger.logException(RetrieveProductArrangementServiceImpl.class, e);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
        }
        if (responseDTO.getError() != null) {
            logger.logError(responseDTO.getError().getErrorCode(), responseDTO.getError().getErrorMessage(),
                    this.getClass());
            if (responseDTO.getError().getErrorMessage().contains("application not found")) {
                throw new ServiceException(resolver.resolve(ResponseErrorConstants.APPLICATION_NOT_FOUND));
            } else if (responseDTO.getError().getErrorMessage().contains("Application")
                    && responseDTO.getError().getErrorMessage().contains("does not belong to")) {
                throw new ServiceException(resolver.resolve(ResponseErrorConstants.INTERNAL_SERVICE_WRONG_BRAND));
            } else {
                throw new ServiceException(
                        resolver.resolve(ResponseErrorConstants.RETRIEVE_PENDING_ARRANGEMENT_SERVICE_EXCEPTION));
            }
        }
        return mapToResource(responseDTO);
    }

    private CustomerPendingDetails mapToResource(DAOResponse<ProductArrangementDTO> responseDTO) {

        CustomerPendingDetails custPendingDetails = new CustomerPendingDetails();
        ProductArrangementDTO productArrangement = responseDTO.getResult();
        Customer cust = productArrangement.getPrimaryInvolvedParty();
        CustomerName custName = new CustomerName();
        Individual individual = cust.getIsPlayedBy();
        if (individual != null && individual.getIndividualName() != null && individual.getIndividualName().length > 0) {
            IndividualName individualName = individual.getIndividualName()[0];
            custName.setPrefixTitle(individualName.getPrefixTitle());
            custName.setFirstName(individualName.getFirstName());
            custName.setLastName(individualName.getLastName());
            custName.setMiddleNames(individualName.getMiddleNames());
        }
        custPendingDetails.setCustomerName(custName);
        custPendingDetails.setGender(individual.getGender());
        custPendingDetails.setArrangementId(productArrangement.getArrangementId());
        custPendingDetails.setArrangementType(productArrangement.getArrangementType());
        custPendingDetails.setArrangementStatus(productArrangement.getApplicationStatus());
        custPendingDetails.setAccountNumber(productArrangement.getAccountNumber());
        custPendingDetails.setAccountPurpose(productArrangement.getAccountPurpose());
        Product associatedPdt = productArrangement.getAssociatedProduct();
        com.lbg.ib.api.sales.product.domain.features.Product pdt = new com.lbg.ib.api.sales.product.domain.features.Product(
                associatedPdt.getProductName(), associatedPdt.getProductIdentifier(),
                associatedPdt.getInstructionDetails().getInstructionMnemonic(), null, null, null);
        custPendingDetails.setAssociatedProduct(pdt);
        custPendingDetails.setCountryOfBirth(individual.getCountryOfBirth());
        Calendar birthDate = individual.getBirthDate();
        if (birthDate != null) {
            custPendingDetails.setDob(formatDate(birthDate));
        }
        List<ArrangementHistory> histories = productArrangement.getArrangementHistory();
        if (histories != null) {
            Calendar appSubmissionDate = null;
            for (ArrangementHistory history : histories) {
                if ("1001".equals(history.getStatus())) {
                    appSubmissionDate = history.getDateModified();
                    break;
                }

            }
            if (appSubmissionDate != null) {
                custPendingDetails.setAppSubmissionDate(formatDate(appSubmissionDate));
            }
        }
        custPendingDetails.setEmailAddress(cust.getEmailAddress());
        custPendingDetails.setTown(individual.getPlaceOfBirth());
        custPendingDetails.setNationality(individual.getNationality());
        custPendingDetails.setPostalAddComp(getPostalAddress(cust));
        TelephoneNumber[] telNoList = cust.getTelephoneNumber();
        if (telNoList != null) {
            ArrayList<TelephoneNumber> telNoCommonList = new ArrayList<TelephoneNumber>(Arrays.asList(telNoList));
            custPendingDetails.setTelephones(telNoCommonList);
        }
        Organisation organisation = productArrangement.getFinancialInstitution();
        if (organisation != null && organisation.getHasOrganisationUnits() != null
                && organisation.getHasOrganisationUnits().length > 0) {
            custPendingDetails.setSortCode(organisation.getHasOrganisationUnits()[0].getSortCode());
        }
        CustomerDocument[] customerDocuments = productArrangement.getCustomerDocuments();
        if (customerDocuments != null) {
            ArrayList<CustomerDocument> customerDocumentsList = new ArrayList<CustomerDocument>(
                    Arrays.asList(customerDocuments));
            custPendingDetails.setCustomerDocuments(customerDocumentsList);
        }
        return custPendingDetails;
    }

    public PostalAddressComponent getPostalAddress(Customer cust) {
        String stayDuration = null;
        PostalAddress structuredAddress = null;
        UnstructuredPostalAddress unStructuredPostalAddress = null;
        PostalAddressComponent postalAdd = null;
        com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress postAddress[] = cust.getPostalAddress();
        boolean isPAFAddress = false;
        // Postal Address Creation
        if (postAddress != null && postAddress.length > 0) {

            stayDuration = postAddress[0].getDurationofStay();
            StructuredAddress stAddress = postAddress[0].getStructuredAddress();
            UnstructuredAddress unstAddress = postAddress[0].getUnstructuredAddress();
            if (stAddress != null) {
                if (stAddress.getAddressLinePAFData() != null) {
                    isPAFAddress = true;
                }
                structuredAddress = new PostalAddress(stAddress.getDistrict(), stAddress.getPostTown(),
                        stAddress.getCounty(), stAddress.getOrganisation(), stAddress.getSubBuilding(),
                        stAddress.getBuilding(), stAddress.getBuildingNumber(),
                        new ArrayList<String>(Arrays.asList(stAddress.getAddressLinePAFData())),
                        stAddress.getPostCodeOut() + stAddress.getPostCodeIn(), stAddress.getPointSuffix());
                postalAdd = new PostalAddressComponent(stayDuration, isPAFAddress, !isPAFAddress, structuredAddress);
            } else if (unstAddress != null) {

                unStructuredPostalAddress = new UnstructuredPostalAddress(unstAddress.getAddressLine1(),
                        unstAddress.getAddressLine2(), unstAddress.getAddressLine3(), unstAddress.getAddressLine4(),
                        unstAddress.getAddressLine5(), unstAddress.getAddressLine6(), unstAddress.getAddressLine7(),
                        unstAddress.getAddressLine8(), unstAddress.getPostCode(), unstAddress.getPointSuffix());
                postalAdd = new PostalAddressComponent(stayDuration, isPAFAddress, !isPAFAddress,
                        unStructuredPostalAddress);
            }

        }

        return postalAdd;
    }

    public String formatDate(Calendar date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date.getTime());
    }
}
