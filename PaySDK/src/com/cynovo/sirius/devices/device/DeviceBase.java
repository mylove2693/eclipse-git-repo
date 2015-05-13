package com.cynovo.sirius.devices.device;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class DeviceBase {

	private StateChangeListener mStateChangeListener = null;
	protected boolean currentState = false;
	protected Context context = null;

	public abstract int getDeviceID();

	public abstract void InitState();

	public static final String ACTION_DEVICE_STATUS = "com.cynovo.sirius.intent.action.devices.status";
	public static final String ACTION_DEVICE_CHECKEND = "com.cynovo.sirius.intent.action.devices.checkend";
	public static final String DEVICE = "device";
	public static final String STATUS = "status";

	public DeviceBase(StateChangeListener mStateChangeListener, Context context) {
		this.context = context;
		setOnStateChangedListener(mStateChangeListener);
	}

	public void setOnStateChangedListener(
			StateChangeListener mStateChangeListener) {
		this.mStateChangeListener = mStateChangeListener;
	}

	public void checkState() {
		InitState();
		sendBroadCast();
	}

	protected void sendBroadCast() {
		Intent intent = new Intent(ACTION_DEVICE_STATUS);
		intent.putExtra(DEVICE, getDeviceID());
		intent.putExtra(STATUS, isEnabled());
		context.sendBroadcast(intent);
	}

	public void stateChange(boolean flag) {
		Log.d("=======>...", "Device stateChange:" + this.getDeviceID() + "   "
				+ flag);
		if (getDeviceID() != DeviceID.DEVICE_ID_INVALID
				&& mStateChangeListener != null) {
			// 设备状态改变
			if (flag != currentState) {
				currentState = flag;
				mStateChangeListener
						.onStateChanged(getDeviceID(), currentState);
			}
		}
	}

	public boolean isEnabled() {
		return currentState;
	}
}
