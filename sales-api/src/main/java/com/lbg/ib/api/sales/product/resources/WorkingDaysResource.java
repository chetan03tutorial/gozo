package com.lbg.ib.api.sales.product.resources;


import com.lbg.ib.api.sales.product.service.WorkingDaysService;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.*;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 09thApr2018
 ***********************************************************************/
@Path("/fetch/")
public class WorkingDaysResource {

    public static final int THIRTY_DAYS_RANGE = 30;
    @Autowired
    private WorkingDaysService fetchNextWorkingDaysService;



    /**
     * API to return The working Days after a weeks time Based on the Range.
     * @return Response
     */

    @GET
    @Produces("application/json")
    @Path("/workingDays/{path:.*}")
    @TraceLog
    public Response fetchNextWorkingDaysAfterWeek(@PathParam("daysRange") Integer daysRange){
        daysRange = daysRange!=null?daysRange:THIRTY_DAYS_RANGE;
        return respond(Response.Status.OK, fetchNextWorkingDaysService.fetchNextWorkingDaysAfterWeek(fetchNextWorkingDaysService.fetchBankHolidays(),daysRange,new Date()));
    }

    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

}
