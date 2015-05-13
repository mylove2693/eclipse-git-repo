package com.cynovo.sirius.devices.device;

import android.content.Context;
import android.util.Log;

import com.cynovo.sirius.devices.printer.SPRTPrinter;

public class DevicePrinter extends DeviceBase {

	public DevicePrinter(StateChangeListener mStateChangeListener,
			Context context) {
		super(mStateChangeListener, context);
	}

	@Override
	public int getDeviceID() {
		return DeviceID.DEVICE_ID_PRINTER;
	}

	@Override
	public void InitState() {
		boolean state = false;
		SPRTPrinter sp = new SPRTPrinter();
		sp.openPrinter();
		int ret = sp.isPrintOK();
		sp.closePrinter();
		if (ret >= 0) {
			state = true;
		}
		Log.d("-------------------> SPRTPrinter ()", "       " + state);
		currentState = state;
	}
}
