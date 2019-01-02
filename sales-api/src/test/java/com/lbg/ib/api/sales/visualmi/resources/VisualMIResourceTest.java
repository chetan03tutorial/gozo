package com.lbg.ib.api.sales.visualmi.resources;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.Test;

public class VisualMIResourceTest {
    VisualMIResource visualMIResource = new VisualMIResource();

    @Test
    public void shouldReturnOKForAGetRequst() {
        Response response = visualMIResource.getRequest();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnOKForAPostRequst() {
        Response response = visualMIResource.postRequest("request body");
        assertThat(response.getStatus(), is(200));
    }
}
