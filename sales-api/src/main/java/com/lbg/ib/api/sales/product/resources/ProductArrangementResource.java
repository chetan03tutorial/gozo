/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.lbg.ib.api.sales.common.util.DateUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.filter.RoleValidator;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.common.validation.ValidationException;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.mca.services.BranchContextService;
import com.lbg.ib.api.sales.product.domain.PackageAccountSessionInfo;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sales.product.domain.arrangement.OfferType;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.product.rules.ArrangementRuleValidator;
import com.lbg.ib.api.sales.product.rules.MarketingPreferenceValidator;
import com.lbg.ib.api.sales.product.service.ArrangementService;
import com.lbg.ib.api.sales.user.domain.AddParty;

@Path("/product")
public class ProductArrangementResource {

    private static final String Y_STRING = "Y";

    private static final String ICOBS = "ICOBS";

    private static final String ENVIRONMENT_NAME = "environmentName";

    @Autowired
    private ArrangementService       service;

    @Autowired
    private RequestBodyResolver      resolver;

    @Autowired
    private FieldValidator           fieldValidator;

    @Autowired
    private ArrangementRuleValidator ruleValidator;

    @Autowired
    private GalaxyErrorCodeResolver  errorResolver;

    @Autowired
    private LoggerDAO                logger;

    @Autowired
    private BranchContextService     branchContextService;

    @Autowired
    private SessionManagementDAO     session;

    @Autowired
    private MarketingPreferenceValidator marketingPreferenceValidator;

    @Autowired
    private ApiServiceProperties apiServiceProperties;

    @POST
    @Produces(APPLICATION_JSON)
    @Path("/{accountType}/arrangement")
    @RoleValidator
    // Warning do not add @TraceLog as we do not intend to print the Request as
    // the Request contains the DOB.
    public Response arrangement(String requestBody, @PathParam("accountType") String accountType)
            throws InvalidFormatException {
        Arrangement arrangement = resolver.resolve(requestBody, Arrangement.class);
        String townName=null;
		if (arrangement.getPrimaryInvolvedParty() != null
				&& arrangement.getPrimaryInvolvedParty().getCurrentAddress() != null) {
		    
		    PostalAddressComponent currentAddress = arrangement.getPrimaryInvolvedParty().getCurrentAddress();
		    
			
			
			if(currentAddress.getStructuredAddress()!=null) {
				townName=currentAddress.getStructuredAddress().getTown();
				logger.traceLog(this.getClass(), "StructuredAddress city is : " + townName);
			}
			
			if(currentAddress.getUnstructuredAddress() != null) {
				townName=currentAddress.getUnstructuredAddress().getAddressLine6();
				logger.traceLog(this.getClass(), "Un-StructuredAddress town is : " + townName);
			}
			
			
			logger.traceLog(this.getClass(), ":::Setting town name in session:::" + townName);
			session.setPartyCity(townName);
			
		}
		

        logger.traceLog(this.getClass(), "Inside the Arrangement Resource. Input Request is : " + arrangement);

        arrangement.setAccountType(AccountType.findAccountTypeFromCode(accountType));

        // We first check if request is having Colleague Id is coming in the
        // Request. Then checks if Branch context is present and then invoke
        // Service if not present
        if (session.getBranchContext() == null && arrangement.getColleagueId() != null
                && arrangement.getOriginatingSortCode() != null && arrangement.getDomain() != null) {
            BranchContext branchContext = new BranchContext();
            branchContext.setDomain(arrangement.getDomain());
            branchContext.setColleagueId(arrangement.getColleagueId());
            branchContext.setOriginatingSortCode(arrangement.getOriginatingSortCode());
            branchContextService.setBranchContextToSession(branchContext);
        }

        if (session.getAddPartyContext() != null) {
            session.setAddPartyContext(null);
        }

        if (arrangement.getRelatedInvolvedParty() != null) {

            PrimaryInvolvedParty primaryInvolvedParty = arrangement.getPrimaryInvolvedParty();
            if (primaryInvolvedParty != null) {
                AddParty addParty = new AddParty();
                if (primaryInvolvedParty.getPartyId() != null) {
                    addParty.setExistingParty(true);
                } else {
                    addParty.setNewParty(true);
                }
                session.setAddPartyContext(addParty);
            }
        }
        // End of Branch Context Code
        validate(arrangement);

        Arranged arranged = service.arrange(arrangement);
        if (arranged != null) {
            //PCA-6411 Adding current date in Offer response for switch date calculation
            arranged.setCurrentDate(DateUtil.getCurrentUKDateAsString(true));
            logger.traceLog(this.getClass(), "Inside the Arrangement Resource. Output Response is : " + arranged);

            //if the product offered is packaged then set the packaged account session info
            checkAndSetPackagedInfoInSession(arrangement, arranged);

            return respond(Status.OK, arranged);
        }

        logger.traceLog(this.getClass(), "Inside the Arrangement Resource. Output Response has errors.");
        return respond(Status.OK, errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }

    /**
     * This method checks and sets the packaged account info in session
     * @param arrangement
     * @param arranged
     */
    private void checkAndSetPackagedInfoInSession(Arrangement arrangement, Arranged arranged) {
        SelectedProduct selectedProduct = session.getSelectedProduct();
        if (selectedProduct != null && MapUtils.isNotEmpty(selectedProduct.getOptionsMap())) {
            Map<String, String> optionsMap = selectedProduct.getOptionsMap();

            if (StringUtils.equals(optionsMap.get(ICOBS), Y_STRING)) {
                final PackageAccountSessionInfo packageAccountSessionInfo = new PackageAccountSessionInfo();
                packageAccountSessionInfo.setOfferRequest(arrangement);
                packageAccountSessionInfo.setOfferResponse(arranged);

                Map<String, Object> applicationProperties = new HashMap<String, Object>(
                        apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES));
                if (applicationProperties != null) {
                    packageAccountSessionInfo.setEnvironmentName((String) applicationProperties.get(ENVIRONMENT_NAME));
                }

                session.setPackagedAccountSessionInfo(packageAccountSessionInfo);
            }
        }
    }

    private void validate(Arrangement arrangement) throws InvalidFormatException {
        if (arrangement.getAccountType() != null) {
            ValidationError validationError = null;
            try {
                fieldValidator.validateInstanceFields(arrangement, arrangement.getAccountType());
            } catch (ValidationException e) {
                logger.logException(this.getClass(), e);
                validationError = e.getValidationError();
            }
            if (validationError != null) {
                throw new InvalidFormatException(validationError.getMessage());
            }
            validationError = ruleValidator.validateRules(arrangement);
            if(validationError == null) {
                validationError = marketingPreferenceValidator.validateRules(arrangement);
            }
            if (validationError != null) {
                throw new InvalidFormatException(validationError.getMessage());
            }
        } else {
            throw new InvalidFormatException(
                    "The account type in the request url is not in the accepted set of values");
        }

    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
