package com.lbg.ib.api.sales.shared.context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RequestContextTest {

	@Test
	public void testGet() {
		RequestContext.setInRequestContext("a", "a");
		RequestContext.getInRequestContext("a");
		RequestContext.getInRequestContext("b");
		RequestContext.remove("a");
		RequestContext.clear();
		RequestContext.getRequestContext();
	}
}
