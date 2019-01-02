package com.lbg.ib.api.sales.dto.postcode;

public class PostcodeDTO {
    private String inPostcode;
    private String outPostcode;

    public PostcodeDTO(String outPostcode, String inPostcode) {
        this.outPostcode = outPostcode;
        this.inPostcode = inPostcode;
    }

    public String getInPostcode() {
        return inPostcode;
    }

    public String getOutPostcode() {
        return outPostcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostcodeDTO that = (PostcodeDTO) o;

        if (inPostcode != null ? !inPostcode.equals(that.inPostcode) : that.inPostcode != null) {
            return false;
        }
        return !(outPostcode != null ? !outPostcode.equals(that.outPostcode) : that.outPostcode != null);

    }

    @Override
    public int hashCode() {
        int result = inPostcode != null ? inPostcode.hashCode() : 0;
        result = 31 * result + (outPostcode != null ? outPostcode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PostcodeDTO{" + "inPostcode='" + inPostcode + '\'' + ", outPostcode='" + outPostcode + '\'' + '}';
    }
}
