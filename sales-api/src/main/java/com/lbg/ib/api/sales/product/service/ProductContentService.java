/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.service;

import java.util.Map;

import com.lloydstsb.ea.referencedata.exceptions.ReferenceDataException;

/**
 * @author ssama1
 */
public interface ProductContentService {
    
    /**
     * @return returns
     * @throws ReferenceDataException
     */
    public Map<String, String> getAllProductContent(String path) throws ReferenceDataException;
    
}
