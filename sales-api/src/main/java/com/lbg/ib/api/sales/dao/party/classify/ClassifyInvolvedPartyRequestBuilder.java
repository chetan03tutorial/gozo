/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.party.classify;

import static com.lbg.ib.api.sales.party.domain.TaxResidencyType.findTaxResidencyTypeFromCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.SalsaGBOHeaderUtility;
import com.lbg.ib.api.sales.dao.SalsaMCAHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyRequestDTO;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lloydstsb.ib.salsa.crs.messages.BaseRequest;
import com.lloydstsb.ib.salsa.crs.messages.BusinessDescription;
import com.lloydstsb.ib.salsa.crs.messages.ClassificationCondition;
import com.lloydstsb.ib.salsa.crs.messages.ClassifyInvolvedPartyRequest;
import com.lloydstsb.ib.salsa.crs.messages.InvolvedParty;
import com.lloydstsb.ib.salsa.crs.messages.OnlineIndiCiaCheck;
import com.lloydstsb.ib.salsa.crs.messages.RequestHeader;
import com.lloydstsb.ib.salsa.crs.messages.SOAPHeader;
import com.lloydstsb.ib.salsa.crs.messages.Scheme;
import com.lloydstsb.ib.salsa.crs.messages.SubRules;
import com.lloydstsb.ib.salsa.crs.messages._ClassifyInvolvedPartyRequest_requestHeader;
import com.lloydstsb.ib.salsa.crs.messages.__ClassifyInvolvedPartyRequest_involvedParty_objectReference;
import com.lloydstsb.ib.salsa.crs.messages.___ClassifyInvolvedPartyRequest_involvedParty_objectReference_alternateId;

@Component
public class ClassifyInvolvedPartyRequestBuilder {
    private static final String   UNITED_KINGDOM_CODE            = "GBR";
    private static final String   SALSA_SERVICE_NAME             = "CharacterEvaluationService";
    private static final String   MODE                           = "ONLINEINDICIACHECK";
    private static final String   MODE_TYPE                      = "AdditionalClassificationData";
    private static final String   PARTY_EXTERNAL_SYSTEM_ID_VALUE = "19";
    private static final String   SALSA_SERVICE_ACTION           = "classifyInvolvedParty";
    public static final String    FATCA_VALUE                    = "1";
    public static final String    CRS_VALUE                      = "4";

    @Autowired
    private SessionManagementDAO  session;
    @Autowired
    private SalsaGBOHeaderUtility gboHeaderUtility;
    @Autowired
    private SalsaMCAHeaderUtility mcaheaderUtility;

    public ClassifyInvolvedPartyRequest build(ClassifyPartyRequestDTO classifyPartyRequestDTO) {
        ClassifyInvolvedPartyRequest rq = new ClassifyInvolvedPartyRequest();
        _ClassifyInvolvedPartyRequest_requestHeader reqHeader = new _ClassifyInvolvedPartyRequest_requestHeader();
        reqHeader.setDataSourceName(PARTY_EXTERNAL_SYSTEM_ID_VALUE);
        rq.setRequestHeader(reqHeader);
        rq.setHeader(prepareSoapHeaders().getHeader());
        rq.setClassificationCondition(populateClassificationcondition(classifyPartyRequestDTO));
        rq.setInvolvedParty(populateInvolvedParty());
        rq.setScheme(populateScheme());
        return rq;
    }

    private Scheme[] populateScheme() {
        Scheme[] scheme = new Scheme[2];
        scheme[0] = new Scheme();
        scheme[0].setDescription(findTaxResidencyTypeFromCode(FATCA_VALUE).toString());
        scheme[0].setName(FATCA_VALUE);
        scheme[1] = new Scheme();
        scheme[1].setDescription(findTaxResidencyTypeFromCode(CRS_VALUE).toString());
        scheme[1].setName(CRS_VALUE);
        return scheme;
    }

    private InvolvedParty populateInvolvedParty() {
        InvolvedParty involvedParty = new InvolvedParty();
        __ClassifyInvolvedPartyRequest_involvedParty_objectReference objectReference = new __ClassifyInvolvedPartyRequest_involvedParty_objectReference();
        ___ClassifyInvolvedPartyRequest_involvedParty_objectReference_alternateId alternateId = new ___ClassifyInvolvedPartyRequest_involvedParty_objectReference_alternateId();
        alternateId.setExternalSystemIdentifier(PARTY_EXTERNAL_SYSTEM_ID_VALUE);
        objectReference.setAlternateId(alternateId);
        involvedParty.setObjectReference(objectReference);
        return involvedParty;
    }

    private ClassificationCondition populateClassificationcondition(ClassifyPartyRequestDTO classifyPartyRequestDTO) {
        ClassificationCondition classificationCondition = new ClassificationCondition();
        classificationCondition.setInputParameters(MODE);
        SubRules subrules = new SubRules();
        BusinessDescription businessDescription = new BusinessDescription();
        OnlineIndiCiaCheck onlineIndiCiaCheck = new OnlineIndiCiaCheck();
        onlineIndiCiaCheck.setCountryOfBirth(classifyPartyRequestDTO.getBirthCountry());
        onlineIndiCiaCheck.setCountryOfNationality(classifyPartyRequestDTO.getNationalities()
                .toArray(new String[classifyPartyRequestDTO.getNationalities().size()]));
        onlineIndiCiaCheck
                .setCountryOfTaxResidency(populateCountryOfTaxResidency(classifyPartyRequestDTO.getTaxResidencies()));
        String[] curraddress = new String[1];
        curraddress[0] = UNITED_KINGDOM_CODE;
        onlineIndiCiaCheck.setCountryForAnyCurrentAddress(curraddress);
        businessDescription.setOnlineIndiCiaCheck(onlineIndiCiaCheck);
        subrules.setBusinessDescription(businessDescription);
        subrules.setName(MODE_TYPE);
        classificationCondition.setSubRules(subrules);
        return classificationCondition;
    }

    private String[] populateCountryOfTaxResidency(Set<TaxResidencyDetails> taxResidencies) {
        List<String> taxCountries = new ArrayList<String>();
        for (TaxResidencyDetails t : taxResidencies) {
            taxCountries.add(t.getTaxResidency());
        }
        return taxCountries.toArray(new String[taxCountries.size()]);
    }

    private BaseRequest prepareSoapHeaders() {
        RequestHeader requestHeader = new RequestHeader();

        List<SOAPHeader> soapHeaders = null;

        if (null != session.getBranchContext()) {
            soapHeaders = mcaheaderUtility.prepareSoapHeader(SALSA_SERVICE_ACTION, SALSA_SERVICE_NAME);
        } else {
            soapHeaders = gboHeaderUtility.prepareSoapHeader(SALSA_SERVICE_ACTION, SALSA_SERVICE_NAME);
        }
        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));

        requestHeader.setBusinessTransaction(SALSA_SERVICE_NAME);
        UserContext userContext = session.getUserContext();
        if (userContext != null) {
            requestHeader.setChannelId(userContext.getChannelId());
        }
        requestHeader.setInteractionId(session.getSessionId());
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setHeader(requestHeader);
        return baseRequest;
    }

    public void setSession(SessionManagementDAO session) {
        this.session = session;
    }

    public void setGboHeaderUtility(SalsaGBOHeaderUtility gboHeaderUtility) {
        this.gboHeaderUtility = gboHeaderUtility;
    }

}
