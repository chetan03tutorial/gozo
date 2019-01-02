package com.lbg.ib.api.sales.header.mapper;

import static java.net.URI.create;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.ColleagueDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.OperationalVariables;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.TransactionDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.UNPMechanismTypeEnum;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.CBSAppGrp;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.SecurityHeaderType;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.UsernameToken;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.UserContext;

public abstract class AbstractLBGHeaderMapper {

	public static final String CONTACT_POINT_ID = "CONTACT_POINT_ID";
	public static final String CONTACT_POINT_TYPE = "CONTACT_POINT_TYPE";
	public static final String CONTACT_POINT_HEADER = "ContactPointHeaderData";
	public static final String CONTACT_POINT_DETAILS = "ContactPointDetails";
	public static final String CONTACT_POINT_PORTFOLIO = "Cnt_Pnt_Prtflio";
	public static final String APPLICATION_ID = "APPLICATION_ID";
	public static final String INITIAL_ORIGINATOR_TYPE = "INITIAL_ORIGINATOR_TYPE";
	public static final String CONTACT_POINT = "ContactPoint";
	public static final String BAPI_INFORMATION = "bapiInformation";
	public static final String HOST_IP = "10.245.211.214";

	public abstract ContactPoint prepareContactPointHeader();

	@Autowired
	private SessionManagementDAO sessionManager;

	public ServiceRequest prepareServiceRequestHeader(String serviceAction, String serviceName) {

		BranchContext branchContext = sessionManager.getBranchContext();
		UserContext userContext = sessionManager.getUserContext();
		String messageId = null;
		if (branchContext != null) {
			messageId = "...";
		} else {
			messageId = userContext.getSessionId();
		}
		ServiceRequest serviceRequestHeader = new ServiceRequest();
		serviceRequestHeader.setAction(serviceAction);
		serviceRequestHeader.setFrom(create(HOST_IP));
		serviceRequestHeader.setMessageId(messageId);
		serviceRequestHeader.setMustReturn(false);
		serviceRequestHeader.setServiceName(create(serviceName));
		return serviceRequestHeader;
	}

	public SecurityHeaderType prepareSecurityRequestHeader() {
		String username = null;
		BranchContext branchContext = sessionManager.getBranchContext();
		if (branchContext != null && branchContext.getColleagueId() != null) {
			username = branchContext.getColleagueId();
		} else {
			username = "UNAUTHSALE";
		}
		UsernameToken usernameToken = new UsernameToken();
		usernameToken.setId("LloydsTSBSecurityToken");
		usernameToken.setUsername(username);
		usernameToken.setUserType("013");
		usernameToken.setUNPMechanismType(UNPMechanismTypeEnum.value1);

		SecurityHeaderType securityHeaderType = new SecurityHeaderType();
		securityHeaderType.setMustReturn(false);
		securityHeaderType.setUsernameToken(usernameToken);
		return securityHeaderType;
	}

	public BapiInformation prepareBAPIHeader() {
		UserContext userContext = sessionManager.getUserContext();
		BranchContext branchContext = sessionManager.getBranchContext();
		Arrangement arrangement = sessionManager.getUserInfo();
		BapiInformation bapiInfo = getBapiInformation(userContext, branchContext, arrangement);
		return bapiInfo;
	}

	private BapiInformation getBapiInformation(UserContext userContext, BranchContext branchContext,
			Arrangement arrangement) {
		BapiInformation bapiInfo = new BapiInformation();
		bapiInfo.setBAPIId("B001");

		final OperationalVariables operationalVariables = new OperationalVariables();
		operationalVariables.setBForceHostCall(Boolean.FALSE);
		operationalVariables.setBPopulateCache(Boolean.FALSE);
		operationalVariables.setBBatchRetry(Boolean.FALSE);
		bapiInfo.setBAPIOperationalVariables(operationalVariables);

		if (null != userContext) {

			HostInformation hostInformation = new HostInformation();
			hostInformation.setHost(userContext.getHost());
			hostInformation.setPartyid(userContext.getPartyId());
			hostInformation.setOcisid(userContext.getOcisId());

			BAPIHeader bapiHeader = new BAPIHeader();
			bapiHeader.setStpartyObo(hostInformation);
			bapiHeader.setChanid(userContext.getChannelId().replace("I", "B"));
			bapiHeader.setChansecmode(userContext.getChansecmode());
			bapiHeader.setSessionid(userContext.getSessionId());
			bapiHeader.setUserAgent(userContext.getUserAgent());
			bapiHeader.setInboxidClient(userContext.getInboxIdClient());
			bapiHeader.setChanctxt(BigInteger.valueOf(1L));
			bapiHeader.setUseridAuthor(userContext.getUserId());

			if (null != arrangement && null != arrangement.getInternalUserIdentifier()) {
				bapiHeader.setUseridAuthor(arrangement.getInternalUserIdentifier());
			}

			if (sessionManager.getBranchContext() != null) {
				if (null != branchContext.getColleagueRoles()) {
					ColleagueDetails[] stcolleaguedetails = new ColleagueDetails[branchContext.getColleagueRoles()
							.size()];
					int i = 0;
					for (String role : branchContext.getColleagueRoles()) {
						ColleagueDetails colleagueDetails = new ColleagueDetails();
						colleagueDetails.setRoleColleague(role);
						colleagueDetails.setColleagueid(branchContext.getColleagueId());
						stcolleaguedetails[i++] = colleagueDetails;
					}
					bapiHeader.setStcolleaguedetails(stcolleaguedetails);
				}

				TransactionDetails[] transactionDetailsArray = new TransactionDetails[1];
				TransactionDetails transactionDetails = new TransactionDetails();
				transactionDetails.setInputofcflgstatuscd(BigInteger.ZERO);
				transactionDetails.setInputofcstatuscd(BigInteger.ZERO);
				transactionDetails.setOverridedtlscd(BigInteger.ONE);
				transactionDetails.setSkllvlacqdcd(BigInteger.ZERO);
				transactionDetails.setHostapplicationid("Digital Branch");
				transactionDetails.setExtsysid(94);
				transactionDetailsArray[0] = transactionDetails;
				bapiHeader.setSttransactiondetails(transactionDetailsArray);
			}

			bapiInfo.setBAPIHeader(bapiHeader);
		}
		return bapiInfo;
	}

	public CBSAppGrp prepareCbsAppGroupHeader() {
		CBSAppGrp cbsAppGrp = new CBSAppGrp();
		cbsAppGrp.setCBS_Applcn_Group_Lookup_Id(sessionManager.getBranchContext().getOriginatingSortCode());
		return cbsAppGrp;
	}
}
