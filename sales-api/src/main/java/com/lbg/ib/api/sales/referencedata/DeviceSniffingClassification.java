/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.  
 *   
 * All Rights Reserved.   
 *  
 * Class Name: DeviceSniffingClassification   
 *   
 * Author(s): CT100704
 *  
 * Date: 18 Feb 2014  
 *  
 ***********************************************************************/

package com.lbg.ib.api.sales.referencedata;

import com.lloydstsb.ea.referencedata.ClassificationCfgAdapter;
import com.lloydstsb.ea.referencedata.ReferenceDataGroups;

/**
 * Implementation of a DeviceSniffingClassification reference data
 * classification.
 *
 * @author
 * @version 1.0
 */
public class DeviceSniffingClassification extends ClassificationCfgAdapter {

    /**
     * default classification key.
     */
    public static final String DEFAULT_CLASSIFICATION_KEY = "DeviceSniffingClassification";

    /**
     * Constructor for the DeviceSniffingClassification.
     * 
     * @param aClassificationKey
     *            classification ID
     * @param aLoaderCfgSection
     *            Section Name of the loader associated with reference data
     *            classification
     * @param aGroupCfgSection
     *            Section name of the reference data group associated with
     *            reference data classification
     * @pram aChannelName channel name associated with reference data
     *       classification
     */
    public DeviceSniffingClassification(String aClassificationKey, String aLoaderCfgSection, String aGroupCfgSection,
            String aChannelName) {
        super(aClassificationKey, aLoaderCfgSection, aGroupCfgSection, aChannelName);
    }

    /**
     * Get the key.
     * 
     * @param aValue
     *            Object
     * @return String object
     * @see com.lloydstsb.framework.ea.referencedata.ClassificationAdapter#getKey(java.lang.Object)
     */
    public String getKey(Object aValue) {
        return ((ReferenceDataGroups) aValue).getKey();
    }

    /**
     * Returns a reference data group associated with the name.
     * 
     * @param aName
     *            name of the reference data group
     * @return ReferenceDataGroups reference data group
     */
    public ReferenceDataGroups get(String aName) {
        ReferenceDataGroups retVal = (ReferenceDataGroups) super.get(aName);
        return (retVal);
    }

    /**
     * Test the type of object.
     * 
     * @param anObject
     *            type
     * @return boolean flag
     * 
     */
    public boolean testObjectType(Object anObject) {
        return (anObject instanceof ReferenceDataGroups);
    }

    /**
     * Get the type.
     * 
     * @return String object
     */
    public String getObjectTypeName() {
        return (ReferenceDataGroups.class.getName());
    }

}
