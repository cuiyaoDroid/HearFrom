package com.tingshuo.service;

public interface IConnectionStatusCallback {
	public void connectionStatusChanged(int connectedState, String reason);
}
