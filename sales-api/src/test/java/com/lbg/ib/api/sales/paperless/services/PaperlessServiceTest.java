/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.services;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import java.math.BigInteger;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.paperless.dao.PaperlessDAO;
import com.lbg.ib.api.sales.paperless.dto.PersonalDetails;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoDTO;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoResult;
import com.lbg.ib.api.sales.paperless.service.PaperlessServiceImpl;
import com.lbg.ib.api.sso.domain.user.Arrangement;

@RunWith(MockitoJUnitRunner.class)
public class PaperlessServiceTest {

    private static final String     ocisId = "1000";

    @InjectMocks
    private PaperlessServiceImpl    paperlessService; // = new
                                                      // PaperlessServiceImpl();

    @Mock
    private PaperlessDAO            paperlessDAO;

    @Mock
    private GalaxyErrorCodeResolver resolver;
    @Mock
    private Arrangement             userArrangement;

    @Mock
    private LoggerDAO               logger;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        /*
         * when(resolver.resolve(ResponseErrorConstants.
         * DETAILS_NOT_FOUND_IN_SESSION)).thenReturn(
         * DETAILS_NOT_FOUND_IN_SESSION);
         * when(resolver.resolve(ResponseErrorConstants.PRODUCT_NOT_FOUND)).
         * thenReturn( PRODUCT_NOT_FOUND);
         */
    }

    @Test
    public void testUserMandateInfoReg() {
        Calendar dateLastLogon = Calendar.getInstance();
        dateLastLogon.add(Calendar.DATE, -5);
        UserMandateInfoDTO mandateInfoDTO = new UserMandateInfoDTO();
        mandateInfoDTO.setUserid(ocisId);
        mandateInfoDTO.setDateFirstLogon(Calendar.getInstance());
        mandateInfoDTO.setUserregstatecode("4");
        mandateInfoDTO.setUserregstatedesc("IB Auth");
        mandateInfoDTO.setDateLastLogon(dateLastLogon);
        DAOResponse<UserMandateInfoDTO> infoDTO = DAOResponse.withResult(mandateInfoDTO);
        when(paperlessDAO.getUserMandateData(any(BigInteger.class))).thenReturn(infoDTO);
        UserMandateInfoResult infoResult = paperlessService.getUserMandateInfo(ocisId);
        assertEquals("4", infoResult.getUserRegStateCode());
        assertNotNull(infoResult.getUserRegStateCode());
    }

    @Test
    public void testUserMandateInfoReg1() {
        Calendar dateLastLogon = Calendar.getInstance();
        dateLastLogon.add(Calendar.DATE, -5);
        UserMandateInfoDTO mandateInfoDTO = new UserMandateInfoDTO();
        mandateInfoDTO.setUserid(ocisId);
        mandateInfoDTO.setDateFirstLogon(Calendar.getInstance());
        mandateInfoDTO.setUserregstatecode("4");
        mandateInfoDTO.setUserregstatedesc("IB Auth");
        mandateInfoDTO.setDateLastLogon(dateLastLogon);
        when(paperlessDAO.getUserMandateData(any(BigInteger.class))).thenReturn(null);
        UserMandateInfoResult infoResult = paperlessService.getUserMandateInfo(ocisId);
        assertEquals(null, infoResult.getUserRegStateCode());
    }

    @Test
    public void testUserMandateInfoNotReg() {
        UserMandateInfoDTO mandateInfoDTO = new UserMandateInfoDTO();
        mandateInfoDTO.setUserid(ocisId);
        mandateInfoDTO.setUserregstatecode("1");
        mandateInfoDTO.setUserregstatedesc("Not IB Auth");
        DAOResponse<UserMandateInfoDTO> infoDTO = DAOResponse.withResult(mandateInfoDTO);
        when(paperlessDAO.getUserMandateData(any(BigInteger.class))).thenReturn(infoDTO);
        UserMandateInfoResult infoResult = paperlessService.getUserMandateInfo(ocisId);
        assertEquals("1", infoResult.getUserRegStateCode());
        assertNotNull(infoResult.getUserRegStateCode());
    }

    @Test
    public void testUserMandateInfoNull() {
        UserMandateInfoDTO mandateInfoDTO = null;
        DAOResponse<UserMandateInfoDTO> infoDTO = DAOResponse.withResult(mandateInfoDTO);
        when(paperlessDAO.getUserMandateData(any(BigInteger.class))).thenReturn(infoDTO);
        UserMandateInfoResult infoResult = paperlessService.getUserMandateInfo(ocisId);
        assertNotNull(infoResult.getLastLoginInMins());
    }

    @Test
    public void testUserMandateInfoCalNull() {
        UserMandateInfoDTO mandateInfoDTO = new UserMandateInfoDTO();
        mandateInfoDTO.setUserid(ocisId);
        mandateInfoDTO.setUserregstatecode("1");
        mandateInfoDTO.setUserregstatedesc("Not IB Auth");
        mandateInfoDTO.setDateLastLogon(null);
        DAOResponse<UserMandateInfoDTO> infoDTO = DAOResponse.withResult(mandateInfoDTO);
        when(paperlessDAO.getUserMandateData(any(BigInteger.class))).thenReturn(infoDTO);
        UserMandateInfoResult infoResult = paperlessService.getUserMandateInfo(ocisId);
        assertEquals(0, infoResult.getLastLoginInMins());
    }

    @Test
    public void updateEmail() {
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setEmailAddress("test@gmail.com");
        paperlessService.updateEmail(personalDetails,"1174655984","+00695296958a");
    }

    @Test
    public void updateEmail1() {
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setEmailAddress("");
        paperlessService.updateEmail(personalDetails,"1174655984","+00695296958a");
    }

    @Test
    public void updateEmail2() {
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setEmailAddress(null);
        paperlessService.updateEmail(personalDetails,"1174655984","+00695296958a");
    }
}
