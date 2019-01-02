package com.lbg.ib.api.sales.docgen.service;
/*
Created by Rohit.Soni at 11/06/2018 17:06
*/

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.docgen.mapper.GenerateDocumentMapper;
import com.lbg.ib.api.sales.docupload.domain.ServiceResponse;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DocumentationItem;
import com.lbg.ib.api.sales.soapapis.gendoc.IA_GenerateDocument;
import com.lbg.ib.api.sales.soapapis.gendoc.reqrsp.GenerateDocumentRequest;
import com.lbg.ib.api.sales.soapapis.gendoc.reqrsp.GenerateDocumentResponse;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import java.rmi.RemoteException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenerateDocumentServiceImplTest {

    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private ConfigurationDAO configManager;

    @Mock
    private ModuleContext beanLoader;

    @Mock
    private LoggerDAO logger;

    @Mock
    private GenerateDocumentMapper generateDocumentMapper;

    @Mock
    private GalaxyErrorCodeResolver resolver;

    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;

    @Mock
    private IA_GenerateDocument generateDocument;

    private DocGenAndSaveRequest docGenAndSaveRequest;
    private GenerateDocumentRequest generateDocumentRequest ;
    private GenerateDocumentResponse generateDocumentResponse ;
    private static String DOCUMENT_AS_STRING = "JVBERi0xLjQKJeLjz9MKMiAwIG9iago8PC9MZW5ndGggMzY2L0ZpbHRlci9GbGF0ZURlY29kZT4+c3RyZWFtCnicrZS9bsMgFIV3PwVjOhCDnTjO0KFVf5YOqWSpIyJw49IYcAyulbcvThM1SV1VarMAMuae7/ges4luiyjNUE4yVMiIIEx3i/iBIpqgYhWN0FXxFjbIyf6oBk1JyspWSWDeMvHKTQmO1XJ1HXe1izuhY2GNAeFjD87XDbwr6JgHrp3ywKrKbmU4AI2zhldsyc1aDWqZMubOgXeHSbYNX1agQapWx49nDHgcIAYLAaHMtVrzZsvsiknQ3ASEMDADIP9GP6h0ZOkX+r46Drq4roA7wA3UFReAK+6hwXRK0zydzLPkR1P7TnRQCavhvw34oh0UG7SwCKzKtJrdCGFb49nLnmXBxZqR9A4Ee9qJYTrJyYwkWZr+5mfXVW8vlCtTDmpdKFd7ZB+CJC7XgDANp/gb6XmFINHoz1wHAKm8sqb/9tksmWdpnh+MnPzb90X0HG36+4Ci/hFFyZyM6QxNUaGPLwRMcX9s9/4HxvZo8AplbmRzdHJlYW0KZW5kb2JqCjQgMCBvYmoKPDwvUGFyZW50IDMgMCBSL0NvbnRlbnRzIDIgMCBSL1R5cGUvUGFnZS9SZXNvdXJjZXM8PC9Gb250PDwvRjEgMSAwIFI+Pj4+L01lZGlhQm94WzAgMCA1OTUgODQyXT4+CmVuZG9iagoxIDAgb2JqCjw8L0Jhc2VGb250L0hlbHZldGljYS9UeXBlL0ZvbnQvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nL1N1YnR5cGUvVHlwZTE+PgplbmRvYmoKMyAwIG9iago8PC9UeXBlL1BhZ2VzL0NvdW50IDEvS2lkc1s0IDAgUl0+PgplbmRvYmoKNSAwIG9iago8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMyAwIFI+PgplbmRvYmoKNiAwIG9iago8PC9Qcm9kdWNlcihpVGV4dK4gNS41LjggqTIwMDAtMjAxNSBpVGV4dCBHcm91cCBOViBcKEFHUEwtdmVyc2lvblwpKS9Nb2REYXRlKEQ6MjAxODA1MDgxMjUxNTlaKS9DcmVhdGlvbkRhdGUoRDoyMDE4MDUwODEyNTE1OVopPj4KZW5kb2JqCnhyZWYKMCA3CjAwMDAwMDAwMDAgNjU1MzUgZiAKMDAwMDAwMDU2MCAwMDAwMCBuIAowMDAwMDAwMDE1IDAwMDAwIG4gCjAwMDAwMDA2NDggMDAwMDAgbiAKMDAwMDAwMDQ0OCAwMDAwMCBuIAowMDAwMDAwNjk5IDAwMDAwIG4gCjAwMDAwMDA3NDQgMDAwMDAgbiAKdHJhaWxlcgo8PC9Sb290IDUgMCBSL0lEIFs8ZDdmY2YzOWMwOWNlYzBlZDY0ZjQ3NzExMmYyMmE1ZjI+PGQ3ZmNmMzljMDljZWMwZWQ2NGY0NzcxMTJmMjJhNWYyPl0vSW5mbyA2IDAgUi9TaXplIDc+PgolaVRleHQtNS41LjgKc3RhcnR4cmVmCjg4OQolJUVPRgo=";
    private byte[] documentAsByteArray  = DOCUMENT_AS_STRING.getBytes();

    @InjectMocks
    private GenerateDocumentService classUnderTest = new GenerateDocumentServiceImpl();

    @BeforeClass
    public static void setup(){
        MockitoAnnotations.initMocks(GenerateDocumentServiceImplTest.class);
    }

    @Before
    public void beforeEachTestSetup(){
        docGenAndSaveRequest = new DocGenAndSaveRequest();
        generateDocumentRequest = new GenerateDocumentRequest();
        generateDocumentResponse = new GenerateDocumentResponse();
    }

    @Test
    public void testGenerateDocumentSuccess() throws RemoteException {
        DocumentationItem documentationItem = new DocumentationItem();
        documentationItem.setDocument(documentAsByteArray);
        generateDocumentResponse.setDocumentationItem(documentationItem);
        when(generateDocumentMapper.creatGenerateDocumentRequest(any(DocGenAndSaveRequest.class),any(PartyDetails.class))).thenReturn(generateDocumentRequest);
        when(generateDocument.generateDocument(generateDocumentRequest)).thenReturn(generateDocumentResponse);
        byte[] result= classUnderTest.generateDocument(docGenAndSaveRequest,new PartyDetails());
        Assert.assertEquals(documentAsByteArray,result);
    }

    @Test(expected = ServiceException.class)
    public void testGenerateDocumentThrowsException() throws RemoteException {
        DocumentationItem documentationItem = new DocumentationItem();
        documentationItem.setDocument(documentAsByteArray);
        generateDocumentResponse.setDocumentationItem(documentationItem);
        when(generateDocumentMapper.creatGenerateDocumentRequest(any(DocGenAndSaveRequest.class),any(PartyDetails.class))).thenReturn(generateDocumentRequest);
        when(generateDocument.generateDocument(generateDocumentRequest)).thenThrow(Exception.class);
        byte[] result= classUnderTest.generateDocument(docGenAndSaveRequest,new PartyDetails());
        Assert.assertEquals(documentAsByteArray,result);
    }

    @Test(expected = ServiceException.class)
    public void testGenerateDocumentWhenDocumentationItemIsNull() throws RemoteException {
        generateDocumentResponse.setDocumentationItem(null);
        when(generateDocumentMapper.creatGenerateDocumentRequest(any(DocGenAndSaveRequest.class),any(PartyDetails.class))).thenReturn(generateDocumentRequest);
        when(generateDocument.generateDocument(generateDocumentRequest)).thenReturn(generateDocumentResponse);
        byte[] result= classUnderTest.generateDocument(docGenAndSaveRequest,new PartyDetails());
        Assert.assertEquals(documentAsByteArray,result);
    }

}
