/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.mapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InformationContent;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InstructionDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Organisation;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OrganisationUnit;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.determinepromotional.reqres.DeterminePromotionalCustomerInstructionsRequest;

@Component
public class PromotionalCustomerInstructionRequestMapper {

    private static final String  SERVICE_NAME   = "DeterminePromotionalCustomerInstructions";

    private static final String  SERVICE_ACTION = "determinePromotionalCustomerInstructions";

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private GBOHeaderUtility     gboHeaderUtility;

    public DeterminePromotionalCustomerInstructionsRequest mapRequest(EligibilityRequestDTO requestDTO) {
        DeterminePromotionalCustomerInstructionsRequest rq = new DeterminePromotionalCustomerInstructionsRequest();
        rq.setHeader(prepareSoapHeaders());
        rq.setCustomerDetails(mapCustomerDeatils(requestDTO));
        rq.setInstructionDetails(mapInstructionDeatils(requestDTO));
        ProductArrangement existingProductArrangement = new ProductArrangement();
        Product associatedProducts = new Product();
        associatedProducts.setProductIdentifier(requestDTO.getProductIdentifier());
        existingProductArrangement.setAssociatedProduct(associatedProducts);
        Organisation financialInstitution = new Organisation();
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setSortCode(requestDTO.getSortCode());
        financialInstitution.setHasOrganisationUnits(new OrganisationUnit[] { organisationUnit });
        existingProductArrangement.setFinancialInstitution(financialInstitution);
        existingProductArrangement.setArrangementStartDate(Calendar.getInstance());
        List<ProductArrangement> existingProductArrangementList = new ArrayList<ProductArrangement>();
        existingProductArrangementList.add(existingProductArrangement);
        if (requestDTO.existingProductArrangements() != null) {
            ProductArrangement[] existingProductArrangementArray = (ProductArrangement[]) requestDTO
                    .existingProductArrangements();
            for (ProductArrangement existingProductArrangements : existingProductArrangementArray) {
                existingProductArrangementList.add(existingProductArrangements);
            }
        }

        rq.setExistingProductArrangments(
                existingProductArrangementList.toArray(new ProductArrangement[existingProductArrangementList.size()]));

        return rq;
    }

    private InstructionDetails mapInstructionDeatils(
            EligibilityRequestDTO determinePromotionalCustomerInstructionsRequest) {
        InstructionDetails instructionDetails = new InstructionDetails();
        String[] candidateInstructions = determinePromotionalCustomerInstructionsRequest.candidateInstructions();
        instructionDetails.setInstructionMnemonic(candidateInstructions[0]);
        instructionDetails.setInstructionState("007");
        return instructionDetails;
    }

    private Customer mapCustomerDeatils(EligibilityRequestDTO determinePromotionalCustomerInstructionsRequest) {
        Customer customerDetails = new Customer();
        Individual isPlayedBy = new Individual();
        isPlayedBy.setBirthDate(calendar(determinePromotionalCustomerInstructionsRequest.birthDate()));
        customerDetails.setIsPlayedBy(isPlayedBy);
        customerDetails.setCustomerIdentifier(determinePromotionalCustomerInstructionsRequest.customerIdentifier());
        customerDetails.setCustomerSegment("3");
        return customerDetails;
    }

    private RequestHeader prepareSoapHeaders() {
        RequestHeader requestHeader = new RequestHeader();

        List<SOAPHeader> soapHeaders = gboHeaderUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);
        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));
        requestHeader.setBusinessTransaction(SERVICE_NAME);
        requestHeader.setInteractionId(session.getSessionId());
        return requestHeader;
    }

    private Calendar calendar(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    public void setSession(SessionManagementDAO session) {
        this.session = session;
    }

    public void setGboHeaderUtility(GBOHeaderUtility gboHeaderUtility) {
        this.gboHeaderUtility = gboHeaderUtility;
    }
}
