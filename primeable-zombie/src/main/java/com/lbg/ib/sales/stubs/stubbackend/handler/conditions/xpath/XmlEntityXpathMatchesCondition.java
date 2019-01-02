package com.lbg.ib.sales.stubs.stubbackend.handler.conditions.xpath;

import com.lbg.ib.sales.stubs.stubbackend.handler.conditions.Condition;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class XmlEntityXpathMatchesCondition implements Condition {
    private String xpath;
    private String value;
    private Boolean checkXpathExist = false;

    private XmlEntityXpathMatchesCondition(String xpath) {
        this.xpath = xpath;
    }

//    @Override
    public boolean matches(HttpExchange httpExchange) {
        SAXReader reader = new SAXReader();
        try {
            if(!checkXpathExist && value==null) {
                throw new IllegalStateException("No value specified to match with");
            }
            String raw = IOUtils.toString(httpExchange.getRequestBody());
            String stripped =stripNamespace(raw);
            Document dom = reader.read(new StringReader(stripped));
            Node node = dom.selectSingleNode(xpath);
            if(node==null){return false;}
            String pathValue = node.getText();
            if(checkXpathExist){
                return pathValue!=null;
            }
            if(value.equals(pathValue)){
                return true;
            }
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private String stripNamespace(String s) {
        return s.replaceAll("</([a-zA-Z0-9]*):", "</").replaceAll("<([a-zA-Z0-9]*):","<");
    }

    public static XmlEntityXpathMatchesCondition xpath(String xpath) {
        return new XmlEntityXpathMatchesCondition(xpath);
    }

    public Condition valueIs(String value) {
        this.value=value;
        return this;
    }

    public Condition exist() {
        checkXpathExist=true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        XmlEntityXpathMatchesCondition that = (XmlEntityXpathMatchesCondition) o;

        return new EqualsBuilder()
                .append(xpath, that.xpath)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(xpath)
                .toHashCode();
    }
}
