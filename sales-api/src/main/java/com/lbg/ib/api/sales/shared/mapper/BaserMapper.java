/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.shared.mapper;

import com.lloydstsb.lcsm.common.AlternateId;
import com.lloydstsb.lcsm.common.ObjectReference;

/**
 * Class to have common setters.
 * 
 * @author tkhann
 */
public class BaserMapper {
    /**
     * Method to add Alternate Id's
     * 
     * @param key
     * @param value
     * @return
     */
    public static AlternateId buildAlternateId(String key, String value) {
        AlternateId alternateId = new AlternateId();
        alternateId.setAttributeString(key);
        alternateId.setValue(value);
        return alternateId;
    }

    /**
     * Method to add objectReference.
     * 
     * @param keyGroupType
     * @param alternateIds
     * @return
     */
    public static ObjectReference buildObjectReference(String keyGroupType, AlternateId[] alternateIds) {
        ObjectReference objectReference = new ObjectReference();
        objectReference.setAlternateId(alternateIds);
        objectReference.setKeyGroupType(keyGroupType);
        return objectReference;
    }

}
