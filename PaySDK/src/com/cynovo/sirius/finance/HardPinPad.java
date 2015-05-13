package com.cynovo.sirius.finance;

import android.util.Log;

//import com.cynovo.sirius.jni.PinPadInterface;
import com.cynovo.sirius.util.NumberFormater;

//import com.cynovo.jni.PinPadInterface;
import com.kivvi.jni.PinPadInterface;

public class HardPinPad {
	public static final int NULL_PINDATA = -100;
	private byte[] pinData = null;

	/**
	 * 等待输入密码
	 * 
	 * @param price
	 * @param bankCardNo
	 * @return
	 */
	public int dealTrackData(String price, String bankCardNo) {
		price = NumberFormater.currencyFormat(price);

		// 金额
		byte[] topText = new byte[price.length() + 3];
		topText[0] = (byte) 0x8B;
		topText[1] = (byte) 0x86;
		topText[2] = (byte) ':';
		System.arraycopy(price.getBytes(), 0, topText, 3, price.length());

		// 请输入密码
		byte[] bottomText = new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0x82,
				(byte) 0x83, (byte) 0x84 };

		// delete by wanhaiping----------------------------------->begin
		// PinPadInterface.scrclrLine(0);
		// PinPadInterface.scrclrLine(1);
		// delete by wanhaiping-----------------------------------<end
		
		// 打开密码键盘
		int retOpen = PinPadInterface.open();
		if (retOpen >= 0) {
			boolean pinpadFlag = true;
			Log.i("debug", "pinpadFlag:"+pinpadFlag);
		}

		PinPadInterface.showText(0, topText, topText.length, 1);
		PinPadInterface.showText(1, bottomText, bottomText.length, 1);

		PinPadInterface.selectKey(1, 0, 0, 0);
		pinData = new byte[8];

		// pinpadwanhaiping
		int ret = PinPadInterface.calculatePinBlock(bankCardNo.getBytes(),
				bankCardNo.length(), pinData, 120000, 1);

		Log.i("debug", "calculatePinBlock : " + String.valueOf(ret));
		return ret;
	}

	public byte[] getPinData() {
		return pinData;
	}

	public void clearPinData() {
		pinData = null;
	}

}
