package com.lbg.ib.api.sales.docgen.mapper;
/*
Created by Rohit.Soni at 12/06/2018 10:07
*/

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.docgen.domain.DocumentCodeTypeEnum;
import com.lbg.ib.api.sales.docgen.domain.DocumentInfo;
import com.lbg.ib.api.sales.docgen.domain.EvidenceInfo;
import com.lbg.ib.api.sales.docupload.domain.ServiceResponse;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InformationContent;
import com.lbg.ib.api.sales.soapapis.gendoc.reqrsp.GenerateDocumentRequest;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
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

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenerateDocumentMapperTest {

    @Mock
    private LoggerDAO logger;

    @Mock
    private GBOHeaderUtility gboHeaderUtility;

    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;

    @Mock
    private ModuleContext beanLoader;

    @InjectMocks
    GenerateDocumentMapper classUnderTest = new GenerateDocumentMapper();

    private static final String TEMPLATE_NAME =  "CA_WELCOME_MSG_PDF";
    private static final String PRODUCT_MNEMONIC =  "P_PLAT";
    private static final String EVIDENCE_TYPE_CODE =  "POI";
    private static final String PROCESS_CODE =  "AOOIDV-ON-LTB";
    private static final String ADDR_LINE_1 =  "ADDR_LINE_1";
    private static final String ADDR_LINE_2 =  "ADDR_LINE_2";
    private static final String ADDR_LINE_3 =  "ADDR_LINE_3";
    private static final String ADDR_LINE_4 =  null;
    private static final String ADDR_LINE_5 =  "ADDR_LINE_5";
    private static final String ADDR_LINE_6 =  "";
    private static final String ADDR_LINE_7 =  "ADDR_LINE_7";
    private static final String ADDR_LINE_8 =  "ADDR_LINE_8";


    private static String[] addressArray = {ADDR_LINE_1,ADDR_LINE_2,ADDR_LINE_3,ADDR_LINE_4,ADDR_LINE_5,ADDR_LINE_6,ADDR_LINE_7,ADDR_LINE_8};

    @BeforeClass
    public static void setup(){
        MockitoAnnotations.initMocks(GenerateDocumentMapperTest.class);
    }

    @Before
    public void beforeEachTestSetup(){
    }


    @Test
    public void testGenerateDocumentMapper(){
        SelectedAccount accountInContext = createSelectedAccount();
        DocGenAndSaveRequest docGenAndSaveRequest = createDocGenAndSaveRequest();
        when(sessionManager.getAccountToConvertInContext()).thenReturn(accountInContext);
        final GenerateDocumentRequest generateDocumentRequest = classUnderTest.creatGenerateDocumentRequest(docGenAndSaveRequest, new PartyDetails());
        final InformationContent hasSpecifiedContent = generateDocumentRequest.getDocumentationItem().getHasContent().getHasSpecifiedContent(0);
        Assert.assertEquals(TEMPLATE_NAME,generateDocumentRequest.getDocumentationItem().getHasContent().getContentTemplateId());
        Assert.assertEquals(PRODUCT_MNEMONIC, hasSpecifiedContent.getValue());
        checkForAllBlankAddressLines(generateDocumentRequest);
    }

    @Test
    public void testGenerateDocumentMapperWithPartyAddress(){
        SelectedAccount accountInContext = createSelectedAccount();
        DocGenAndSaveRequest docGenAndSaveRequest = createDocGenAndSaveRequest();
        when(sessionManager.getAccountToConvertInContext()).thenReturn(accountInContext);
        final PartyDetails partyDetails = new PartyDetails();
        partyDetails.setAddressLines(addressArray);
        final GenerateDocumentRequest generateDocumentRequest = classUnderTest.creatGenerateDocumentRequest(docGenAndSaveRequest, partyDetails);
        final InformationContent hasSpecifiedContent = generateDocumentRequest.getDocumentationItem().getHasContent().getHasSpecifiedContent(0);
        Assert.assertEquals(TEMPLATE_NAME,generateDocumentRequest.getDocumentationItem().getHasContent().getContentTemplateId());
        Assert.assertEquals(PRODUCT_MNEMONIC, hasSpecifiedContent.getValue());
        checkForPopulatedAddressLines(generateDocumentRequest);
    }

    private void checkForAllBlankAddressLines(GenerateDocumentRequest generateDocumentRequest){
        final InformationContent[] hasSpecifiedContent = generateDocumentRequest.getDocumentationItem().getHasContent().getHasSpecifiedContent();
        for(InformationContent informationContent:hasSpecifiedContent){
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+1)){
                Assert.assertEquals("",informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+2)){
                Assert.assertEquals("",informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+3)){
                Assert.assertEquals("",informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+4)){
                Assert.assertEquals("",informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+5)){
                Assert.assertEquals("",informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+6)){
                Assert.assertEquals("",informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+7)){
                Assert.assertEquals("",informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+8)){
                Assert.assertEquals("",informationContent.getValue());
            }
        }

    }

    private void checkForPopulatedAddressLines(GenerateDocumentRequest generateDocumentRequest){
        final InformationContent[] hasSpecifiedContent = generateDocumentRequest.getDocumentationItem().getHasContent().getHasSpecifiedContent();
        for(InformationContent informationContent:hasSpecifiedContent){
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+1)){
                Assert.assertEquals(ADDR_LINE_1,informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+2)){
                Assert.assertEquals(ADDR_LINE_2,informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+3)){
                Assert.assertEquals(ADDR_LINE_3,informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+4)){
                Assert.assertEquals(ADDR_LINE_5,informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+5)){
                Assert.assertEquals(ADDR_LINE_7,informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+6)){
                Assert.assertEquals(ADDR_LINE_8,informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+7)){
                Assert.assertEquals("",informationContent.getValue());
            }
            if(informationContent.getKey().equalsIgnoreCase("IB.Product.Addr.AddressLine"+8)){
                Assert.assertEquals("",informationContent.getValue());
            }
        }

    }

    private SelectedAccount createSelectedAccount(){
        SelectedAccount accountToConvert = new SelectedAccount();
        accountToConvert.setAccountNumber("11111111");
        accountToConvert.setSortCode("303030");
        accountToConvert.setOverdraft(300.0);
        return accountToConvert;
    }

    private DocGenAndSaveRequest createDocGenAndSaveRequest(){
        DocGenAndSaveRequest docGenAndSaveRequest = new DocGenAndSaveRequest();
        docGenAndSaveRequest.setTemplateName(TEMPLATE_NAME);
        docGenAndSaveRequest.setProductMnemonic(PRODUCT_MNEMONIC);
        EvidenceInfo evidenceInfo = new EvidenceInfo();
        evidenceInfo.setEvidenceTypeCode(EVIDENCE_TYPE_CODE);
        evidenceInfo.setProcessCode(PROCESS_CODE);
        docGenAndSaveRequest.setEvidenceInfo(evidenceInfo);
        DocumentInfo documentInfo = new DocumentInfo();
        documentInfo.setDocumentCode(DocumentCodeTypeEnum.SODN);
        documentInfo.setDocumentPurpose("identity verification");
        return docGenAndSaveRequest;
    }
}
