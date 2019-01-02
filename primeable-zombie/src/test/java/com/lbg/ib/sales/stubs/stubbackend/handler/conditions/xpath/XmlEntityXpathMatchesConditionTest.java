package com.lbg.ib.sales.stubs.stubbackend.handler.conditions.xpath;

import com.lbg.ib.sales.stubs.stubbackend.handler.conditions.Condition;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static com.lbg.ib.sales.stubs.stubbackend.handler.conditions.xpath.XmlEntityXpathMatchesCondition.xpath;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class XmlEntityXpathMatchesConditionTest {

    private HttpExchange exchange = mock(HttpExchange.class);

    @Before
    public void setup(){
        when(exchange.getRequestBody())
                .thenReturn(new ByteArrayInputStream("<ns1:foo><bar>rob</bar><car></car></ns1:foo>".getBytes()));
    }

    @Test
    public void shouldReturnTrueWhenTheXpathValueMatches() throws Exception {
        Condition condition = xpath("/foo/bar").valueIs("rob");
        assertThat(condition.matches(exchange), is(true));
    }

    @Test
    public void shouldReturnFalseWhenTheXpathValueDoesNotMatch() throws Exception {
        Condition condition = xpath("/foo/bar").valueIs("bob");
        assertThat(condition.matches(exchange), is(false));
    }

    @Test
    public void shouldReturnFalseWhenTheXpathDoesNotExist() throws Exception {
        Condition condition = xpath("/coo").valueIs("rob");
        assertThat(condition.matches(exchange), is(false));
    }

    @Test
    public void shouldReturnTrueWhenThePathExist() throws Exception {
        Condition condition = xpath("/foo/bar").exist();
        assertThat(condition.matches(exchange), is(true));
    }

    @Test
    public void shouldReturnFalseWhenThePathDoesNotExist() throws Exception {
        Condition condition = xpath("/foo/paul").exist();
        assertThat(condition.matches(exchange), is(false));
    }

    @Test
    public void shouldReturnTrueWhenThePathDoesExistWithNoSubElement() throws Exception {
        Condition condition = xpath("/foo/car").exist();
        assertThat(condition.matches(exchange), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenNoValueIsSpecified() throws Exception {
        Condition condition = xpath("/coo");
        condition.matches(exchange);
    }
}