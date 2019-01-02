package com.lbg.ib.api.services.beans;

import java.util.List;

public class Addresses {

    public List<PostalAddress> getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(List<PostalAddress> postalAddress) {
		this.postalAddress = postalAddress;
	}

	private List<PostalAddress> postalAddress;
}
