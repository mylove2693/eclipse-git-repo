package com.cynovo.sirius.devices.device;

import android.content.Context;

import com.cynovo.sirius.util.NetworkUtil;

public class DeviceNetLink extends DeviceBase {

	static volatile boolean netflag = false;

	public DeviceNetLink(StateChangeListener mStateChangeListener,
			Context context) {
		super(mStateChangeListener, context);
	}

	public void InitState() {
		currentState = NetworkUtil.isConnected(context);
	}

	@Override
	public int getDeviceID() {
		return DeviceID.DEVICE_ID_NETLINK;
	}
}
