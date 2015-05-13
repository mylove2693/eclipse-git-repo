package com.cynovo.sirius.devices.device;

import android.content.Context;

//import com.cynovo.sirius.jni.PbocInterface;
//import com.cynovo.sirius.jni.PbocRegistInterface;
import com.kivvi.jni.EmvL2Interface;

public class DeviceEmv extends DeviceBase {

	public DeviceEmv(StateChangeListener mStateChangeListener, Context context) {
		super(mStateChangeListener, context);
	}

	@Override
	public int getDeviceID() {
		return DeviceID.DEVICE_ID_EMV;
	}

	@Override
	public void InitState() {
		boolean state = false;
		// modified by wanhaiping---------------------------------------->begin
		/*
		 * int ret = PbocRegistInterface.open(); if(ret == 0) { ret =
		 * PbocInterface.OpenReader(); if(ret < 0) { state = false; } else { ret
		 * = PbocRegistInterface.checkEmv(); if(ret == 0) { state = true; }
		 * PbocInterface.CloseReader(); } } PbocRegistInterface.close();
		 */
		/*
		 * int ret = EmvL2Interface.openReader(); if(ret >=0) { state = true; }
		 * EmvL2Interface.closeReader();
		 */

		state = true;

		// modified by wanhaiping---------------------------------------<end
		this.currentState = state;

	}

}
