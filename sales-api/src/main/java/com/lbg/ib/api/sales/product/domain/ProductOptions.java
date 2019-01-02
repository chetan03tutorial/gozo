package com.lbg.ib.api.sales.product.domain;

import java.util.List;

public class ProductOptions {

    private String                      id;
    private String                      desc;
    private String                      code;
    private String                      name;
    private String                      type;
    private String                      value;
    private List<ProductRelatedOptions> relatedOptions;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     *            the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the relatedOptions
     */
    public List<ProductRelatedOptions> getRelatedOptions() {
        return relatedOptions;
    }

    /**
     * @param relatedOptions
     *            the relatedOptions to set
     */
    public void setRelatedOptions(List<ProductRelatedOptions> relatedOptions) {
        this.relatedOptions = relatedOptions;
    }

}
