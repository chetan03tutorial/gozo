package com.lbg.ib.api.alligator.core;

public interface RequestDataHandler {

	public String doHandle(String data);

	public static class DefaultDataHandler implements RequestDataHandler {
		@Override
		public String doHandle(String data) {
			return data;
		}
	}
}
