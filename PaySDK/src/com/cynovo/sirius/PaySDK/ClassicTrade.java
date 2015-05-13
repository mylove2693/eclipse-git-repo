package com.cynovo.sirius.PaySDK;

import android.util.Log;

import com.cynovo.sirius.PaySDK.ClassicTradeFactory.PayMethods;
//import com.cynovo.sirius.jni.MsrInterface;
//import com.cynovo.sirius.jni.PbocInterface;
//import com.cynovo.sirius.jni.PbocRegistInterface;
//import com.cynovo.sirius.jni.PinPadInterface;
//import com.cynovo.sirius.jni.SafeInterface;
import com.cynovo.sirius.util.MyLog;
import com.kivvi.jni.EmvL2Interface;
//import com.cynovo.jni.MsrInterface;
//import com.cynovo.jni.PinPadInterface;
import com.kivvi.jni.MsrInterface;
import com.kivvi.jni.PinPadInterface;
import com.kivvi.jni.SafeInterface;

public abstract class ClassicTrade {
	// 初始化
	public int DeviceInit() {
		int retval = PinPadInterface.open();
		MyLog.e("PinPadInterface open retval:" + retval);
		if (retval < 0)
			return -2;

		//delete by punan-------------------------------------------->begin
		//retval = SafeInterface.open();
		//MyLog.e("SafeInterface open retval:" + retval);
		//if (retval < 0)
			//return -3;
		//delete by punan-------------------------------------------->end
		

		// delete by wanhaiping---------------------------------------->begin
		// retval = MsrInterface.loadMsr();
		// MyLog.e("MsrInterface loadMsr retval:" + retval);
		// if(retval < 0)
		// return -4;
		// delete by wanhaiping----------------------------------------<end

		retval = MsrInterface.open();
		MyLog.e("MsrInterface open retval:" + retval);
		if (retval < 0)
			return -4;

		// delete by wanhaiping---------------------------------------->begin
		// MsrInterface.setEvent(-1);
		// MsrInterface.startReadDataThread();
		// MyLog.e("MsrInterface startReadDataThread retval:" + retval);
		// if(retval < 0)
		// return -4;
		// delete by wanhaiping----------------------------------------<end

		// modified by wanhaiping------------------------------------->begin
		/*
		 * retval = PbocInterface.open(); if(retval < 0) return -5;
		 * 
		 * retval = PbocRegistInterface.open(); if(retval < 0) return -6;
		 */

		/*
		 * retval = EmvL2Interface.emvKernelInit(); if(retval < 0) {
		 * MyLog.e("EmvL2Interface emvKernelInit retval:" + retval); return -5;
		 * }
		 * 
		 * retval = EmvL2Interface.openReader(); if(retval < 0) {
		 * MyLog.e("EmvL2Interface openReader retval:" + retval); return -6; }
		 */

		// modified by wanhaiping-------------------------------------<end

		return 0;
	}

	// 消费
	public abstract int Sale();

	// 消费撤销
	public abstract int Void();

	// 消费冲正
	public abstract int SaleReversal();

	// 消费撤销冲正
	public abstract int VoidReversal();

	// 退货
	public abstract int refund();

	// 下载主密钥
	public abstract int updatemkey();

	// 下载工作密钥
	public abstract int updatewkey();

	// IC卡脚本处理结果通知
	public abstract int ICscriptNotification();

	// IC卡终端公钥下载到文件中
	public abstract int downloadICPKToFile();

	// IC卡终端参数下载到文件中
	public abstract int downloadICParamToFile();

	public abstract void SetCurrentMethod(PayMethods method);

	public abstract ClassicTrade GetCurrentTrade();

	public int EmvInit() {
		
		int ret = EmvL2Interface.emvKernelInit();
		if (ret < 0) {
			Log.i("debug", "load emv kernel failed!");
		}
		
		return ret;
	}

	public int DeviceClose() {
		int retval = PinPadInterface.close();
		if (retval < 0)
			return -2;

		retval = SafeInterface.close();
		if (retval < 0)
			return -3;

		retval = MsrInterface.close();
		if (retval < 0)
			return -4;

		// modified by wanhaiping---------------------------------------->begin
		// MsrInterface.unloadMsr();
		// delete by wanhaiping----------------------------------------<end

		// deleted by wanhaiping-------------------------------------->begin
		/*
		 * retval = PbocInterface.close(); if(retval < 0) return -5;
		 * 
		 * retval = PbocRegistInterface.close(); if(retval < 0) return -6;
		 */

		// EmvL2Interface.closeReader();

		// modified by wanhaiping--------------------------------------<end

		return 0;
	}
}
