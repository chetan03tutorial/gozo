/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.arrangement;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OfferTypeTest {

    @Test
    public void findsOfferTypeFromCode() {
        OfferType normal = OfferType.findOfferTypeFromCode("2001");
        assertThat(normal, is(OfferType.NORMAL));
    }

    @Test
    public void findsOfferType_returnsNullWhenNotFound() {
        OfferType normal = OfferType.findOfferTypeFromCode("unknown");
        assertThat(normal, CoreMatchers.nullValue());
    }

    @Test
    public void findsOfferType_returnsNullWhenNullEntry() {
        OfferType normal = OfferType.findOfferTypeFromCode(null);
        assertThat(normal, CoreMatchers.nullValue());
    }
}