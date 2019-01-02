package com.lbg.ib.api.sales.dto.product.content;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductContentDTO {

    @JsonProperty("productKey")
    private String         productKey;

    @JsonProperty("productData")
    private ProductDataDTO productDataDTO;

    public ProductContentDTO(String productKey, ProductDataDTO productDataDTO) {
        this.productKey = productKey;
        this.productDataDTO = productDataDTO;
    }

    @JsonProperty("productKey")
    public String getProductKey() {
        return productKey;
    }

    @JsonProperty("productKey")
    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @JsonProperty("productData")
    public ProductDataDTO getProductDataDTO() {
        return productDataDTO;
    }

    @JsonProperty("productData")
    public void setProductDataDTO(ProductDataDTO productDataDTO) {
        this.productDataDTO = productDataDTO;
    }

    @Override
    public String toString() {
        return "ProductContentDTO [productKey=" + productKey + ", productDataDTO=" + productDataDTO + "]";
    }

}
