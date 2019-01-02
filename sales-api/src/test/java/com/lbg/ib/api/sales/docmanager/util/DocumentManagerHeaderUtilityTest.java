package com.lbg.ib.api.sales.docmanager.util;
/*
Created by Rohit.Soni at 25/06/2018 14:26
*/

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.service.DocGenAndSaveServiceImplTest;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.www.DocumentationManagerServiceLocator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentManagerHeaderUtilityTest {

    private static final String brand = "LTB";

    @Mock
    private LoggerDAO logger;

    @Mock
    private SessionManagementDAO session;

    @Mock
    private GBOHeaderUtility gboHeaderUtility;

    @Mock
    private MCAHeaderUtility mcaHeaderUtility;

    @Mock
    private DocumentationManagerServiceLocator documentationManagerServiceLocator;

    @Mock
    private HandlerRegistry handlerRegistry;

    @InjectMocks
    DocumentManagerHeaderUtility classUnderTest = new DocumentManagerHeaderUtility();

    @BeforeClass
    public static void setup(){
        MockitoAnnotations.initMocks(DocumentManagerHeaderUtilityTest.class);
    }

    @Before
    public void setupForEach(){
        when(gboHeaderUtility.prepareSoapHeader(anyString(), anyString()))
                .thenReturn(HeaderServiceUtil.prepareSoapHeaders());
        when(mcaHeaderUtility.prepareSoapHeader(anyString(), anyString()))
                .thenReturn(HeaderServiceUtil.prepareSoapHeaders());
        when(documentationManagerServiceLocator.getServiceName()).thenReturn(new QName("temp"));
    }

    @Test
    public void testSuccessInDigital(){
        when(documentationManagerServiceLocator.getHandlerRegistry()).thenReturn(handlerRegistry);
        when(session.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(documentationManagerServiceLocator.getDocumentationManagerSOAPPortWSDDPortName()).thenReturn("temp");
        classUnderTest.setupAndGetDataHandler(documentationManagerServiceLocator, "DocumentManagerService");
        verify(gboHeaderUtility,times(1)).prepareSoapHeader(anyString(), anyString());
    }
    @Test
    public void testSuccessInMCA(){
        when(session.getBranchContext()).thenReturn(new BranchContext());
        when(documentationManagerServiceLocator.getHandlerRegistry()).thenReturn(handlerRegistry);
        when(session.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(documentationManagerServiceLocator.getDocumentationManagerSOAPPortWSDDPortName()).thenReturn("temp");
        classUnderTest.setupAndGetDataHandler(documentationManagerServiceLocator, "DocumentManagerService");
        verify(mcaHeaderUtility,times(1)).prepareSoapHeader(anyString(), anyString());
    }
}
