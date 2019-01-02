package com.lbg.ib.api.alligator.util;

import com.lbg.ib.api.alligator.core.RequestDataHandler;

public class DataHandlerUtil {

	public static String dataHandler(Class<?> dataHandlerClass, String data)
	{
		RequestDataHandler dataHandler= null;
		String requestData = null;
		if(RequestDataHandler.class.isAssignableFrom(dataHandlerClass))
		{
			try {
				dataHandler = (RequestDataHandler)dataHandlerClass.newInstance();
				requestData = dataHandler.doHandle(data);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return requestData;
	}
	
}
