/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: DocUploadServiceHelper
 *
 * Author(s):Parameshwaran Kangamuthu(1146728)
 *
 * Date: 22 Dec 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.mapper;

import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.docupload.cache.DocUploadRefDataCachingComponent;
import com.lbg.ib.api.sales.docupload.delegate.DocUploadDelegate;
import com.lbg.ib.api.sales.docupload.dto.refdata.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 8735182
 *
 */
@Component
public class DocUploadRefDataServiceMapper {

    private DocUploadDelegate                docUploadProcessDelegate;

    private ResponseErrorCodeMapper          resolver;

    private HttpServletRequest               httRequest;

    private DocUploadRefDataCachingComponent docUploadRefDataCachingComponent;

    /**
     * @param docUploadProcessDelegate
     * @param resolver
     * @param httRequest
     * @param docUploadRefDataCachingDelegate
     */
    @Autowired
    public DocUploadRefDataServiceMapper(DocUploadDelegate docUploadProcessDelegate, ResponseErrorCodeMapper resolver,
            @Context HttpServletRequest httRequest, DocUploadRefDataCachingComponent docUploadRefDataCachingComponent) {
        super();
        this.docUploadProcessDelegate = docUploadProcessDelegate;
        this.resolver = resolver;
        this.httRequest = httRequest;
        this.docUploadRefDataCachingComponent = docUploadRefDataCachingComponent;
    }

    /**
     * Methods to get process dto Checks in cache if not present will get it
     * from salsa service
     *
     * @return ProcessDto
     *
     */
    public ProcessDTO getProcessDto() throws DocUploadServiceException {
        String processCd = getProcessCode();
        ProcessDTO processDto = docUploadRefDataCachingComponent.getRefDataFromCache(processCd);
        if (processDto == null) {
            // processDto =
            // docUploadProcessDelegate.retrieveDocUploadProcessRefData(processCd);
            processDto = fetchHardCodedRefData();
            docUploadRefDataCachingComponent.setRefDataInCache(processDto);
        }
        return processDto;
    }

    private ProcessDTO fetchHardCodedRefData() {
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setProcessCd("AOOIDV-ON-LTB");
        processDTO.setBrand("LTB");
        processDTO.setBusinessProcessName("Account Opening Identity Verfication");
        processDTO.setBusinessProcessTypeName("Online");
        processDTO.setDefaultExpiryPeriod(3653);
        processDTO.setDocUploadFileLimit(210);
        processDTO.setTargetApplication("");
        processDTO.setTempStoragePath("E2E Simplification FileNet--------------------------------------------");
        processDTO.setUploadSizeLimit(10240);
        processDTO.setAllowUndefinedDocument("Y");

        DocumentDTO docDTO = new DocumentDTO();
        docDTO.setCode("BST");
        docDTO.setName("Bank Statement");
        docDTO.setUploadFileLimit(14);
        List<DocumentDTO> docDTOList = new ArrayList<DocumentDTO>();
        docDTOList.add(docDTO);

        EvidenceTypeDTO evidenceTypeDTO = new EvidenceTypeDTO();
        evidenceTypeDTO.setCode("POI");
        evidenceTypeDTO.setLongDescription("abc");
        evidenceTypeDTO.setName("Proof of income");
        evidenceTypeDTO.setDocument(docDTOList);
        List<EvidenceTypeDTO> listEvTypeDTO = new ArrayList<EvidenceTypeDTO>();
        listEvTypeDTO.add(evidenceTypeDTO);

        FileFormatsDTO fileFormatsDTOBMP = new FileFormatsDTO();
        fileFormatsDTOBMP.setCode("BMP");
        fileFormatsDTOBMP.setContentType("image/bmp");

        FileFormatsDTO fileFormatsDTOJPEG = new FileFormatsDTO();
        fileFormatsDTOJPEG.setCode("JPEG");
        fileFormatsDTOJPEG.setContentType("image/jpeg");

        FileFormatsDTO fileFormatsDTOJPG = new FileFormatsDTO();
        fileFormatsDTOJPG.setCode("JPG");
        fileFormatsDTOJPG.setContentType("image/jpg");

        FileFormatsDTO fileFormatsDTOPDF = new FileFormatsDTO();
        fileFormatsDTOPDF.setCode("PDF");
        fileFormatsDTOPDF.setContentType("application/pdf");

        FileFormatsDTO fileFormatsDTOTIFF = new FileFormatsDTO();
        fileFormatsDTOTIFF.setCode("TIFF");
        fileFormatsDTOTIFF.setContentType("image/tiff");

        FileFormatsDTO fileFormatsDTOTIF = new FileFormatsDTO();
        fileFormatsDTOTIF.setCode("TIF");
        fileFormatsDTOTIF.setContentType("image/tif");

        FileFormatsDTO fileFormatsDTOPNG = new FileFormatsDTO();
        fileFormatsDTOPNG.setCode("PNG");
        fileFormatsDTOPNG.setContentType("image/png");

        FileFormatsDTO fileFormatsDTOGIF = new FileFormatsDTO();
        fileFormatsDTOGIF.setCode("GIF");
        fileFormatsDTOGIF.setContentType("image/gif");

        List<FileFormatsDTO> listFFDTO = new ArrayList<FileFormatsDTO>();
        listFFDTO.add(fileFormatsDTOBMP);
        listFFDTO.add(fileFormatsDTOJPEG);
        listFFDTO.add(fileFormatsDTOPDF);
        listFFDTO.add(fileFormatsDTOJPG);
        listFFDTO.add(fileFormatsDTOTIFF);
        listFFDTO.add(fileFormatsDTOTIF);
        listFFDTO.add(fileFormatsDTOPNG);
        listFFDTO.add(fileFormatsDTOGIF);

        processDTO.setFileFormat(listFFDTO);

        ProcessPsfDTO processPSFDTO = new ProcessPsfDTO();
        processPSFDTO.setFormat("\\d+");
        processPSFDTO.setMandatoryFlag("Y");
        processPSFDTO.setName("sort_code_account_number");
        processPSFDTO.setSize(14);

        List<ProcessPsfDTO> processPSFDToList = new ArrayList<ProcessPsfDTO>();
        processPSFDToList.add(processPSFDTO);

        processDTO.setProcessSpecificField(processPSFDToList);
        return processDTO;
    }

    /**
     * Methods to get process code If process code is not set in JWT filter/
     * empty then
     *
     * @throws DocUploadServiceException
     *
     * @return ProcessCd
     *
     */
    public String getProcessCode() throws DocUploadServiceException {
        return "AOOIDV-ON-LTB";
    }
}
