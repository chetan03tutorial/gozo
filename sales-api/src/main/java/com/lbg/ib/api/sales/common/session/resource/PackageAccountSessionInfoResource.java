/**
 * 
 */
package com.lbg.ib.api.sales.common.session.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.PackageAccountSessionInfo;

/**
 * PackagedAccoountSession Info resource
 * @author 8903735
 *
 */
@Path("/session/getPBAInfo")
public class PackageAccountSessionInfoResource extends Base {
    
    @Autowired
    private SessionManagementDAO session;
    
    @GET
    @Produces(APPLICATION_JSON)
    public Response getPackagedAccountSessionInfo() throws Exception {
        logger.traceLog(this.getClass(), "Getting the Packaged Account Info from session");
        PackageAccountSessionInfo packageAccountSessionInfo = session.getPackagedAccountSessionInfo();
        if (packageAccountSessionInfo == null) {
            packageAccountSessionInfo = new PackageAccountSessionInfo();
        }
        packageAccountSessionInfo.setUserInfo(session.getUserInfo());
        logger.traceLog(this.getClass(), "Completed getting the Packaged Account Info from session");
        return respond(Status.OK, packageAccountSessionInfo);
    }
    
    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
    
    
}
