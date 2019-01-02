package com.lbg.ib.api.sales.header.builder;

import java.util.Set;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPPart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.header.mapper.AbstractLBGHeaderMapper;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.CBSAppGrp;

@Component
public class CbsAppGroupHeader extends AbstractBaseHeader {

	private static final String HEADER_NAME = "CBSAppGrp";
	private static final String CBS_APPLCN_GROUP_LOOKUP_ID = "CBS_Applcn_Group_Lookup_Id";

	@Autowired
	private SessionManagementDAO sessionManager;

	public String name() {
		return HEADER_NAME;
	}

	public boolean handle(SOAPPart soapPart, Object ... args) throws SOAPException {
		AbstractLBGHeaderMapper lbgHeaderBuilder = getLBGHeaderBuilder();
		CBSAppGrp cbsAppGroup = lbgHeaderBuilder.prepareCbsAppGroupHeader();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		SOAPHeader soapHeader = soapEnvelope.getHeader();
		SOAPElement cbsGroup = soapHeader.addHeaderElement(
				soapEnvelope.createName(HEADER_NAME, "soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
		SOAPElement contactPointType = cbsGroup.addChildElement(soapEnvelope.createName(CBS_APPLCN_GROUP_LOOKUP_ID,
				"soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
		contactPointType.setValue(cbsAppGroup.getCBS_Applcn_Group_Lookup_Id());
		return true;
	}

	@Override
	public void registerHandler(Set<AbstractBaseHeader> headerSet) {
		if (sessionManager.getBranchContext() != null) {
			super.registerHandler(headerSet);
		}
	}
}
