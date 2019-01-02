/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 * All Rights Reserved.
 * Class Name: GetCaseService
 * Author(s):8768724
 * Date: 04 Jan 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import com.lbg.ib.api.sales.docupload.domain.Case;
import com.lbg.ib.api.sales.docupload.domain.ServiceResponse;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

import java.io.InputStream;

/**
 * @author 8768724
 * 
 */
@Path("/docUpload/cases")
public interface CaseDetailsService {
    
    /**
     * @param caseRef
     * @return
     * @throws DocUploadServiceException
     */
    
    /**
     * @param requestBody
     * @return
     * @throws DocUploadServiceException
     */
    @POST
    @Descriptions({ @Description(target = DocTarget.METHOD, value = "Create the case for the given case details"),
            @Description(target = DocTarget.RESPONSE, value = "CaseDTO"),
            @Description(target = DocTarget.REQUEST, value = "The case details as a JSON string -caseDTO") })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    ServiceResponse<Case> createCase(String requestBody) throws DocUploadServiceException;
    
    /**
     * @param requestBody
     * @return
     * @throws DocUploadServiceException
     *//*
       * @Path("/files")
       * 
       * @PUT
       * 
       * @Descriptions({
       * 
       * @Description(target = DocTarget.METHOD, value =
       * "Preview the file for given caseId"),
       * 
       * @Description(target = DocTarget.RESPONSE, value =
       * "MultipartBody - The case details as a JSON string -caseDTO and  file attachment"
       * ),
       * 
       * @Description(target = DocTarget.REQUEST, value =
       * "The case details as a JSON string -caseDTO") })
       * 
       * @Consumes(MediaType.APPLICATION_JSON)
       * 
       * @Produces(MediaType.MULTIPART_FORM_DATA) MultipartBody
       * previewDocument(String requestBody) throws DocUploadServiceException;
       */
    
    /**
     * @param requestBody
     * @param multipartBody
     * @return
     * @throws DocUploadServiceException
     */
    @Path("/files")
    @POST
    @Descriptions({ @Description(target = DocTarget.METHOD, value = "Upload the file with caseDetails"),
            @Description(target = DocTarget.RESPONSE, value = "CaseDTO"),
            @Description(target = DocTarget.REQUEST, value = "The caseDetails as a JSON string of caseDTO and filesegment(s)") })
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    ServiceResponse<Case> uploadDocument(
            @Description(value = "Case Request parameter", target = DocTarget.PARAM) @Multipart("caseDetails") String requestBody,
            @Description(value = "File segments", target = DocTarget.PARAM) @Multipart(required = false) MultipartBody multipartBody)
            throws DocUploadServiceException;
    
    /**
     * @param requestBody
     * @return
     * @throws DocUploadServiceException
     */
    @GET
    @Path("/preview/{fileNetRefId}")
    @Produces({ "image/jpg", "image/jpeg", "application/json" })
    
    Response previewDocument(@PathParam("fileNetRefId") String fileNetRefId) throws DocUploadServiceException;

    ServiceResponse<Case> createCase(CaseDTO caseDto);

    public CaseDTO uploadProcess(CaseDTO caseDto, CaseDTO getCaseResponse, InputStream file, int actualFileSize);
    
}
