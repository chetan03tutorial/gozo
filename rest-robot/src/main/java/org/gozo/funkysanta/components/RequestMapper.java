package org.gozo.funkysanta.components;

public interface RequestMapper<U,V> {

	public V buildRequest(U clientRequest);
	

}
