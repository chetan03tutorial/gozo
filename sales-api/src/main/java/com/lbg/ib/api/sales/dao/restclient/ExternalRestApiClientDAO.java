/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ExternalRestApiClientDAOImpl
 *
 * Author(s):8735182
 *
 * Date: 29 Dec 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.restclient;

import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author 8735182
 */
public interface ExternalRestApiClientDAO {

    /**
     * GET request
     * @param url Url for the request as String
     * @param responseType Response Object Type
     * @param isDefHeaderReq true if it is Salsa or false to add httpheader
     * @return <code>ExternalRestApiResponse</code> class containing the response body or a DAOError
     */
    <X> X get(String url, Class<X> responseType, boolean isDefHeaderReq) throws DocUploadServiceException;

    /**
     * GET request
     * @param context Rest Context
     * @param responseType Response Object Type
     * @return <code>ExternalRestApiResponse</code> class containing the response body or a DAOError
     */
    <X> X customGet(RestContext context, Class<X> responseType) throws DocUploadServiceException;

    /**
     * GET request
     * @param url Url for the request as String
     * @param responseType Response Object Type
     * @param isDefHeaderReq true if it is Salsa or false to add httpheader
     * @return <code>ExternalRestApiResponse</code> class containing the response body or a DAOError
     */
    <X> X getRefDataValue(String url, Class<X> responseType, boolean isDefHeaderReq, String brandFromCache)
            throws DocUploadServiceException;

    /**
     * POST request
     * @param url Url for the request
     * @param responseType Response Object Type
     * @param requestBody The payload for the request body
     * @param isDefHeaderReq true if it is Salsa or false to add httpheader
     * @return <code>ExternalRestApiResponse</code> class containing the resp onse body or a
     * DAOError
     */
    <X, Y> X post(String url, Class<X> responseType, Y requestBody, boolean isDefHeaderReq)
            throws DocUploadServiceException;

    /**
     * Custom POST request
     * @param url Url for the request
     * @param responseType Response Object Type
     * @param requestBody The payload for the request body
     * @param isDefHeaderReq true if it is Salsa or false to add httpheader
     * @return <code>ExternalRestApiResponse</code> class containing the resp onse body or a
     * DAOError
     */
    <X> X customPost(RestContext context, Class<X> responseType)
            throws DocUploadServiceException;

}
