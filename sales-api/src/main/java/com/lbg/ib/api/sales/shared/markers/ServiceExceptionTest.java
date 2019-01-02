package com.lbg.ib.api.sales.shared.markers;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.gozo.lang.MethodDetails;
import com.lbg.ib.api.sales.shared.exception.ServiceError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;

public class ServiceExceptionTest {

	public static void main(String[] args) throws Throwable {
		ServiceExceptionTest test = new ServiceExceptionTest();
		try {
			test.handlePcaException(new RuntimeException());
		} catch (ServiceError ex) {
			System.out.println("1");
			throw ex;
		} catch (InvalidFormatException ex) {
			System.out.println("2");
			throw ex;
		} catch (ServiceException ex) {
			System.out.println("3");
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"));
		} catch (RuntimeException ex) {
			System.out.println("4");
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"));
		}
	}
	
	
	
	public Object handlePcaException(Exception e) throws Throwable {
		
		try {
			throw e;
		} catch (ServiceError ex) {
			System.out.println("1");
			throw ex;
		} catch (InvalidFormatException ex) {
			System.out.println("2");
			throw ex;
		} catch (ServiceException ex) {
			System.out.println("3");
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"));
		} catch (RuntimeException ex) {
			System.out.println("4");
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"));
		}
	}
}
