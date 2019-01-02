package com.lbg.ib.api.sales.content.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.content.service.ContentService;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.TraceLog;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Path("/manager")
public class ContentManagerResource {

    private ContentService service;

    public ContentManagerResource() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    @Autowired
    public ContentManagerResource(ContentService service) {
        this.service = service;
    }

    @GET
    @Produces({ TEXT_HTML, APPLICATION_JSON })
    @TraceLog
    public Response content(@QueryParam("path") String path) throws ServiceException {
        return  service.contentManager(path);
    }
}
