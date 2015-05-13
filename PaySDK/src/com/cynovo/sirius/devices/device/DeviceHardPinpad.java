package com.cynovo.sirius.devices.device;

import android.content.Context;
import android.util.Log;

//import com.cynovo.sirius.jni.PinPadInterface;
//import com.cynovo.jni.PinPadInterface;
import com.kivvi.jni.PinPadInterface;

public class DeviceHardPinpad extends DeviceBase {

	public DeviceHardPinpad(StateChangeListener mStateChangeListener,
			Context context) {
		super(mStateChangeListener, context);
	}

	@Override
	public int getDeviceID() {
		return DeviceID.DEVICE_ID_HARDPINPAD;
	}

	public void InitState() {
		boolean state = false;
		// int pinPadRet = PinPadInterface.open();
		// if(pinPadRet >= 0)
		// {
		// modified by wanhaiping-------------------------------------->begin
		// int pinPadRet = PinPadInterface.checkPinpad();
		// if(pinPadRet >= 0)
		// {
		// state = true;
		// }

		state = true;
		// modified by wanhaiping--------------------------------------->end

		// }
		// PinPadInterface.close();
		Log.d("-------------------> PinPadInterface.open()", "       " + state);
		currentState = state;
	}
}
