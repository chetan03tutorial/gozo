/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lloydstsb.ib.ea.cache.services.application.ApplicationCacheService;

/**
 * @author 8735182
 *
 */
@Component
public class DocUploadRefDataCachingComponent {

    /**
     * APP_CACHE_SERVICE used for putting and retrieving the values from
     * application cache.
     */
    @Autowired
    private ApplicationCacheService applicationCacheService;

    @Autowired
    private LoggerDAO               logger;

    @Autowired
    private ResponseErrorCodeMapper resolver;

    public ProcessDTO getRefDataFromCache(String processCd) {
        ProcessDTO processDto = null;
        try {
            processDto = (ProcessDTO) applicationCacheService
                    .getCachedObject(DocUploadConstant.CACHE_DOCUPLOAD_REFDATA_NAMESPACE, processCd);
        } catch (IllegalArgumentException anIllegalArgumentException) {
            logger.logException(this.getClass(), anIllegalArgumentException);
            throw new DocUploadServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_REF_DATA_SERVICE));
        } catch (ClassCastException castException) {
            logger.logException(this.getClass(), castException);
        }
        return processDto;
    }

    public void setRefDataInCache(ProcessDTO processDto) {
        try {
            if (null != processDto && null != processDto.getProcessCd()) {
                applicationCacheService.setCachedObject(DocUploadConstant.CACHE_DOCUPLOAD_REFDATA_NAMESPACE,
                        processDto.getProcessCd(), processDto);
            }
        } catch (IllegalArgumentException anIllegalArgumentException) {
            logger.logException(this.getClass(), anIllegalArgumentException);
            throw new DocUploadServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_REF_DATA_SERVICE));
        }

    }

}
