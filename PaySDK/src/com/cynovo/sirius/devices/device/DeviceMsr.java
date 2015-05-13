package com.cynovo.sirius.devices.device;

import com.cynovo.sirius.jni.SystemInfoInterface;

import android.content.Context;
import android.util.Log;

public class DeviceMsr extends DeviceBase {

	public DeviceMsr(StateChangeListener mStateChangeListener, Context context) {
		super(mStateChangeListener, context);
	}

	@Override
	public int getDeviceID() {
		return DeviceID.DEVICE_ID_MSR;
	}

	@Override
	public void InitState() {
		this.currentState = getMsrState();
	}

	private boolean getMsrState() {
		byte[] tmp = new byte[64];
		int len = 0;
		int count = 0;

		while (SystemInfoInterface.getinfo(
				new String("ro.cynovo.msr").getBytes(), tmp) == 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (++count > 2)
				break;
		}
		byte[] msrstate = new byte[len];
		System.arraycopy(tmp, 0, msrstate, 0, len);
		Log.d("MSRSTATE", "ro.cynovo.msr   " + new String(msrstate));
		String ret = new String(msrstate);

		if (ret.startsWith("unknown")) {
			Log.d("MSRSTATE", "ro.cynovo.msr   state:" + false);
			return false;
		} else {
			Log.d("MSRSTATE", "ro.cynovo.msr   state:" + true);
			return true;
		}
	}
}
