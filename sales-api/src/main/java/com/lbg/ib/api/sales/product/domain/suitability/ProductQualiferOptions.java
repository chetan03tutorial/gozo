package com.lbg.ib.api.sales.product.domain.suitability;

import java.util.List;

public class ProductQualiferOptions {

    private List<String> values;
    private String       type;

    public ProductQualiferOptions() {
        // default comments for Sonar violations avoidance.
    }

    public ProductQualiferOptions(List<String> values, String type) {
        this.values = values;
        this.type = type;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
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
        ProductQualiferOptions other = (ProductQualiferOptions) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (values == null) {
            if (other.values != null) {
                return false;
            }
        } else if (values != null && other.values != null) {
            for (String value : other.values) {
                if (!values.contains(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductQualiferOptions [values=" + values + ", type=" + type + "]";
    }

}