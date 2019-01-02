package com.lbg.ib.api.sales.paperless.Dao;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.paperless.dao.PaperlessDAOImpl;
import com.lbg.ib.api.sales.paperless.dto.PersonalDetailsDTO;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB298AUserSetPersDetails;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB298BUserSetPersDetails;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB818AUserInfoByOcisId;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB818BUserInfoByOcisId;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.UserPortType;

@RunWith(MockitoJUnitRunner.class)
public class PaperlessDAOImplTest {

    @InjectMocks
    private PaperlessDAOImpl        paperlessDAOImpl;

    /**
     * Object of UserPortType.
     */
    @Mock
    private UserPortType            userPort;

    /**
     * Object of FoundationServerUtil.
     */
    @Mock
    private FoundationServerUtil    foundationServerUtil;
    /**
     * Object of SessionManagementDAO.
     */
    @Mock
    private SessionManagementDAO    sessionManagementDAO;
    /**
     * Object of DAOExceptionHandler.
     */
    @Mock
    private DAOExceptionHandler     exceptionHandler;

    /**
     * Object of Galaxy Error Code.
     */
    @Mock
    private GalaxyErrorCodeResolver resolver;

    /**
     * Object of Logger.
     */
    @Mock
    private LoggerDAO               logger;

    private static final String     sessionId = "SOME_RANDOM_SESSION_ID";

    private static final BigInteger ocisId    = BigInteger.valueOf(1000);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        UserContext context = SessionServiceUtil.prepareUserContext("lloyds");
        when(sessionManagementDAO.getUserContext()).thenReturn(context);
        when(foundationServerUtil.createStHeader(any(UserContext.class))).thenReturn(stHeader());

    }

    public static UserContext prepareUserContext(String brand) {
        return new UserContext("871515", "192.168.1.1", sessionId, "+100000", "1204629", brand, "chansecMode", "Chrome",
                "En", "inboxIdClient", "host");
    }

    @Test(expected = Exception.class)
    public void testGetUserMandateDataExc() {
        paperlessDAOImpl.getUserMandateData(ocisId);
    }

    @Test
    public void testGetUserMandateData() throws RemoteException {
        StB818BUserInfoByOcisId b818bUserInfoByOcisId = new StB818BUserInfoByOcisId();
        b818bUserInfoByOcisId.setDateFirstLogon(Calendar.getInstance());
        b818bUserInfoByOcisId.setDateLastLogon(Calendar.getInstance());
        b818bUserInfoByOcisId.setUserregstatecode("0");
        b818bUserInfoByOcisId.setUserregstatedesc("Not Registered");
        b818bUserInfoByOcisId.setUserid("0");
        StError error = new StError();
        error.setErrorno(0);
        b818bUserInfoByOcisId.setSterror(error);
        when(userPort.b818UserInfoByOcisId(any(StB818AUserInfoByOcisId.class))).thenReturn(b818bUserInfoByOcisId);
        paperlessDAOImpl.getUserMandateData(ocisId);
    }

    @Test(expected = Exception.class)
    public void testGetUserMandateDataFail() throws RemoteException {
        StB818BUserInfoByOcisId b818bUserInfoByOcisId = new StB818BUserInfoByOcisId();
        StError error = new StError();
        error.setErrorno(1);
        error.setErrormsg("Error");
        b818bUserInfoByOcisId.setSterror(error);
        when(userPort.b818UserInfoByOcisId(any(StB818AUserInfoByOcisId.class))).thenReturn(b818bUserInfoByOcisId);
        paperlessDAOImpl.getUserMandateData(ocisId);
    }

    @Test
    public void testGetUserMandateDataSucc2() throws RemoteException {
        StB818BUserInfoByOcisId b818bUserInfoByOcisId = new StB818BUserInfoByOcisId();
        StError error = new StError();
        error.setErrorno(1);
        error.setErrormsg("Error");
        b818bUserInfoByOcisId.setDateFirstLogon(Calendar.getInstance());
        b818bUserInfoByOcisId.setSterror(null);
        when(userPort.b818UserInfoByOcisId(any(StB818AUserInfoByOcisId.class))).thenReturn(b818bUserInfoByOcisId);
        paperlessDAOImpl.getUserMandateData(ocisId);
    }

    @Test(expected = Exception.class)
    public void testupdatePersonalDetailsEx() {
        PersonalDetailsDTO details = new PersonalDetailsDTO();
        details.setMktgindMail(1);
        details.setOcisId("0");
        paperlessDAOImpl.updatePersonalDetails(details);
    }

    //@Test
    public void testupdatePersonalDetailsSucc() throws RemoteException {
        PersonalDetailsDTO details = new PersonalDetailsDTO();
        details.setMktgindMail(1);
        details.setOcisId("0");
        StB298BUserSetPersDetails b818bUserInfoByOcisId = new StB298BUserSetPersDetails();
        StError error = new StError();
        error.setErrorno(0);
        b818bUserInfoByOcisId.setSterror(error);
        when(userPort.b298UserSetPersonalDetails(any(StB298AUserSetPersDetails.class)))
                .thenReturn(b818bUserInfoByOcisId);
        paperlessDAOImpl.updatePersonalDetails(details);
    }

    @Test(expected = Exception.class)
    public void testupdatePersonalDetailsFail() throws RemoteException {
        PersonalDetailsDTO details = new PersonalDetailsDTO();
        details.setMktgindMail(1);
        details.setOcisId("0");
        StB298BUserSetPersDetails b818bUserInfoByOcisId = new StB298BUserSetPersDetails();
        StError error = new StError();
        error.setErrorno(1);
        error.setErrormsg("Error");
        b818bUserInfoByOcisId.setSterror(error);
        when(userPort.b298UserSetPersonalDetails(any(StB298AUserSetPersDetails.class)))
                .thenReturn(b818bUserInfoByOcisId);
        paperlessDAOImpl.updatePersonalDetails(details);
    }

    //@Test
    public void testupdatePersonalDetailsFail2() throws RemoteException {
        PersonalDetailsDTO details = new PersonalDetailsDTO();
        details.setMktgindMail(1);
        details.setOcisId("0");
        StB298BUserSetPersDetails b818bUserInfoByOcisId = new StB298BUserSetPersDetails();
        StError error = new StError();
        error.setErrorno(1);
        error.setErrormsg("Error");
        b818bUserInfoByOcisId.setSterror(null);
        b818bUserInfoByOcisId.setAlertid("Alert");
        when(userPort.b298UserSetPersonalDetails(any(StB298AUserSetPersDetails.class)))
                .thenReturn(b818bUserInfoByOcisId);
        paperlessDAOImpl.updatePersonalDetails(details);
    }

    @Test(expected = Exception.class)
    public void shouldUpdatePersonalDetailsFail() throws Exception {
        RemoteException remoteException = new RemoteException("Remote Exception Occurred");
        PersonalDetailsDTO details = new PersonalDetailsDTO();
        details.setMktgindMail(1);
        details.setOcisId("0");
        when(userPort.b298UserSetPersonalDetails(any(StB298AUserSetPersDetails.class))).thenThrow(remoteException);
        DAOResponse.DAOError error = new DAOError("errorCode", "errorMsg");
        when(exceptionHandler.handleException(remoteException, PaperlessDAOImpl.class, "DAO: updatePersonalDetails",
                details)).thenReturn(error);
        paperlessDAOImpl.updatePersonalDetails(details);
    }

    @Test(expected = Exception.class)
    public void shouldGetUserMandateFail() throws Exception {
        PersonalDetailsDTO details = new PersonalDetailsDTO();
        String methodName = "DAO: getUserMandateData";
        RemoteException remoteException = new RemoteException("Remote Exception Occurred");
        DAOResponse.DAOError error = new DAOError("errorCode", "errorMsg");
        details.setMktgindMail(1);
        details.setOcisId("0");
        when(userPort.b818UserInfoByOcisId(any(StB818AUserInfoByOcisId.class))).thenThrow(remoteException);
        when(exceptionHandler.handleException(remoteException, PaperlessDAOImpl.class, methodName, ocisId))
                .thenReturn(error);

        paperlessDAOImpl.getUserMandateData(ocisId);
    }

    private StHeader stHeader() {
        StHeader stHeader = new StHeader();
        StParty stParty = new StParty();
        stParty.setPartyid("1000");
        stParty.setOcisid(new BigInteger("10"));
        stHeader.setStpartyObo(stParty);
        return stHeader;
    }

}
