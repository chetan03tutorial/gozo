package org.gozo.funkysanta.components;

public interface ResponseMapper<U,V> {

	public V buildResponse(U serviceResponse);
	

}
