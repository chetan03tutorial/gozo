package com.lbg.ib.api.sales.communication.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.communication.dao.CommunicationDAO;
import com.lbg.ib.api.sales.communication.dto.CommunicationRequestDTO;
import com.lbg.ib.api.sales.communication.dto.CommunicationResponseDTO;
import com.lbg.ib.api.sales.communication.dto.PartyCommunicationDetails;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;

/**
 * Created by rabaja on 18/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommunicationServiceImplTest implements CommunicationConstants{
    @Mock
    private LoggerDAO logger;
    @Mock
    private CommunicationDAO communicationDao;
    @Mock
    private GalaxyErrorCodeResolver resolver;

    @Mock
    CommunicationRequestDTO requestDTO;
    @Mock
    private SessionManagementDAO session;
    @InjectMocks
    private CommunicationServiceImpl communicationService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldReturnSucessfullResult()throws ServiceException{
        CommunicationResponseDTO responseDTO = new CommunicationResponseDTO();
        responseDTO.setIsSuccessful(new Boolean("true"));
        when(communicationDao.sendEmailCommunictaion(any(CommunicationRequestDTO.class))).thenReturn(DAOResponse.withResult(responseDTO));
        Boolean result = communicationService.sendEmailCommunication(createCommunicationDeatilObject());
        Assert.assertTrue(result);
    }

     private PartyCommunicationDetails createCommunicationDeatilObject(){
        PartyCommunicationDetails communicationDetails = new PartyCommunicationDetails();
        communicationDetails.setTemplateId("NEXT_BEST_ACTION_ITEM_EMAIL");
        communicationDetails.setRecipientEmail(new String[]{"dummyUser@test.llyods.com"});
        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("Best Action Item Header 1", "Best Action Item Value 1");
        tokenMap.put("Best Action Item Header 2", "Best Action Item Value 2");
        communicationDetails.setTokenMap(tokenMap);
        return communicationDetails;
    }


}
