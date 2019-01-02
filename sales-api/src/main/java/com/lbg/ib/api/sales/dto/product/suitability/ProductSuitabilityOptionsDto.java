package com.lbg.ib.api.sales.dto.product.suitability;

public class ProductSuitabilityOptionsDto {
    private String value;
    private String type;
    private String name;

    public ProductSuitabilityOptionsDto() {
        /* jackson */}

    public ProductSuitabilityOptionsDto(String value, String type, String name) {
        this.value = value;
        this.type = type;
        this.name = name;
    }

    public ProductSuitabilityOptionsDto(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProductSuitabilityOptionsDto other = (ProductSuitabilityOptionsDto) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductOptions [value=" + value + ", type=" + type + "]";
    }

}