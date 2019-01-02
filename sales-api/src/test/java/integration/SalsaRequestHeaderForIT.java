/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package integration;

import static java.net.URI.create;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.BAPIHeader;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.BapiInformation;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.HostInformation;
import com.lloydstsb.ib.salsa.common.schema.enterprise.lcsm.OperationalVariables;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.OperatorTypeEnum;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.UNPMechanismTypeEnum;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.soap.ContactPoint;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.soap.security.SecurityHeaderType;
import com.lloydstsb.ib.salsa.common.schema.infrastructure.soap.security.UsernameToken;
import com.lloydstsb.ib.salsa.crs.messages.SOAPHeader;

public class SalsaRequestHeaderForIT {
    public List<SOAPHeader> populateRequestHeader() throws Exception {

        List<SOAPHeader> SOAP_HEADERS = new ArrayList<SOAPHeader>();
        SOAPHeader soapHeader = new SOAPHeader();
        final OperationalVariables operationalVariables = new OperationalVariables();
        final SecurityHeaderType securityHeaderType = new SecurityHeaderType();
        final UsernameToken usernameToken = new UsernameToken();
        BAPIHeader bapiHeader = new BAPIHeader();
        HostInformation stParty = new HostInformation();

        soapHeader.setNameSpace(new URI("http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        soapHeader.setPrefix("ns4");
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setApplicationId("Internet Banking");
        contactPoint.setContactPointId("0000805121");
        contactPoint.setContactPointType("003");
        contactPoint.setInitialOriginatorType("Browser");
        contactPoint.setInitialOriginatorId("10.245.224.125");
        contactPoint.setOperatorType(OperatorTypeEnum.Customer);
        soapHeader.setName(GBOHeaderUtility.CONTACT_POINT);
        soapHeader.setValue(contactPoint);
        SOAP_HEADERS.add(soapHeader);

        soapHeader = new SOAPHeader();
        usernameToken.setId("LloydsTSBSecurityToken");
        usernameToken.setUsername("UNAUTHSALE");
        usernameToken.setUserType("013");
        usernameToken.setUNPMechanismType(UNPMechanismTypeEnum.value1);
        securityHeaderType.setMustReturn(false);
        securityHeaderType.setUsernameToken(usernameToken);
        soapHeader.setName("Security");
        soapHeader.setNameSpace(new URI("http://LB_GBO_Sales/Messages"));
        soapHeader.setPrefix("lgsm");
        soapHeader.setValue(securityHeaderType);
        SOAP_HEADERS.add(soapHeader);

        soapHeader = new SOAPHeader();
        soapHeader.setName("bapiInformation");
        soapHeader.setNameSpace(create("http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        soapHeader.setPrefix("ns5");
        operationalVariables.setBForceHostCall(Boolean.FALSE);
        operationalVariables.setBPopulateCache(Boolean.FALSE);
        operationalVariables.setBBatchRetry(Boolean.FALSE);
        BapiInformation bapiInfo = new BapiInformation();
        bapiInfo.setBAPIId("B001");
        bapiInfo.setBAPIOperationalVariables(operationalVariables);
        bapiHeader.setUseridAuthor("UNAUTHSALE");
        stParty.setHost("I");
        stParty.setPartyid("AAGATEWAY");
        stParty.setOcisid("0");
        bapiHeader.setStpartyObo(stParty);
        bapiHeader.setChanid("IBL");
        bapiHeader.setChansecmode("PWD");
        bapiHeader.setSessionid("y6VPwmrbk6iG7WhmWb6o4L");
        bapiHeader.setUserAgent("Mozilla/5.0 (Windows NT 5.1; rv:24.0)Gecko/20100101 Firefox/24.0");
        bapiHeader.setInboxidClient("GX");
        bapiHeader.setChanctxt(new BigInteger("1"));
        bapiInfo.setBAPIHeader(bapiHeader);
        soapHeader.setValue(bapiInfo);
        SOAP_HEADERS.add(soapHeader);

        return SOAP_HEADERS;
    }

}
