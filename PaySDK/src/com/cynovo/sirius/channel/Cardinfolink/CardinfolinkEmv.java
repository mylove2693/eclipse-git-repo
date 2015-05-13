package com.cynovo.sirius.channel.Cardinfolink;

import android.util.Log;

import com.cynovo.sirius.bankcard.BankEmvData;
import com.cynovo.sirius.finance.PosBase;
import com.cynovo.sirius.finance.PosService;
//import com.cynovo.sirius.jni.HostlinkInterface;
//import com.cynovo.sirius.jni.PbocInterface;
//import com.cynovo.sirius.jni.HostlinkInterface.ICDataClass;
import com.cynovo.sirius.parameter.AllPayParameters;
import com.kivvi.jni.EmvL2Interface;
import com.kivvi.jni.ISO8583Interface;
import com.kivvi.jni.ISO8583Interface.ICDataClass;

public class CardinfolinkEmv extends AdapterCardinfolink {

	public int Sale() {

		String price = AllPayParameters.mInput.getAmount();
		String traceNo = AllPayParameters.mInput.getTraceNo();
		String batchNo = AllPayParameters.mRep.getOriginalBatchNo();
		String track2 = AllPayParameters.mEmv.getTrack2();
		String track3 = AllPayParameters.mEmv.getTrack3();
		byte[] pinData = AllPayParameters.mSafe.getPinData();
		ICDataClass icdata = AllPayParameters.mEmv.getIcData();
		//ISO8583Interface.setTraceNo(traceNo);
		
		Log.e("debug", "traceNo= "+traceNo+" ,\nbatchNo= "+batchNo+", \nprice="+price+
				", \ntraceNo="+traceNo+", \ntrack2="+ track2+", \ntrack3="+track3+
				", \npinData="+pinData+", \nicdata="+icdata);
		
		int retval = ISO8583Interface.sale(traceNo, batchNo, price, track2, track3,
				pinData, icdata);//modify by punan
		Log.e("debug", "ISO8583Interface.sale retval = "+retval);
		
		// 如果有脚本，执行脚本
		int ret = BankEmvData.emvReceive();
		Log.e("debug", "emvReceive ret = "+ret);
		if (ret == -1 || ret == 1)
			scriptNotify();

		if(ISO8583Interface.getAckNo() == null)
			return PosService.POSERROR_WRONG39; //-4
		
		if (ISO8583Interface.getAckNo().equals(PosBase.POS_WRONG_PASS))//55
			return PosService.POSERROR_WRONG39; //-4

		// modified by wanhaiping----------------------------------------->begin
		/*
		 * ret = PbocInterface.IssuerScriptAuth(); if(ret < 0) {
		 * Log.i("EMVSALETAG","PbocInterface.IssuerScriptAuth: "+ret); } int
		 * tradeendRet = PbocInterface.TradeEnd();
		 * 
		 * Log.i("EMVSALETAG","PbocInterface.TradeEnd: "+tradeendRet);
		 * if((tradeendRet & 0x02) == 0x02) return
		 * PosService.POSERROR_EMVNEEDREVERSE;
		 */

		int emvret = EmvL2Interface.tradeEnd();
		if (emvret < 0)
			return PosService.POSERROR_EMVNEEDREVERSE; //-150

		// modified by wanhaiping-----------------------------------------<end
		// return retval;
		return -102;
	}

	public int Void() {

		String price = AllPayParameters.mInput.getAmount();
		String traceNo = AllPayParameters.mInput.getTraceNo();
		String track2 = AllPayParameters.mEmv.getTrack2();
		String track3 = AllPayParameters.mEmv.getTrack3();
		String originalReferencNo = AllPayParameters.mRep
				.getOriginalReferencNo();
		String originalAuthorizationNo = AllPayParameters.mRep
				.getOriginalAuthorizationNo();
		byte[] pinData = AllPayParameters.mSafe.getPinData();
		String originalTraceNo = AllPayParameters.mInput.getsTraceNo();
		String originalBatchNo = AllPayParameters.mRep.getOriginalBatchNo();
		ICDataClass icdata = AllPayParameters.mEmv.getIcData();
		icdata.f55 = null;
		icdata.f55Length = 0;
		//ISO8583Interface.setTraceNo(traceNo);
		
		Log.e("debug", "price= "+price+", traceNo= "+traceNo+", track2= "+track2+", track3= "+track3+
				", originalReferencNo= "+originalReferencNo+", pinData= "+pinData+", originalTraceNo= "+originalTraceNo+
				", originalBatchNo= "+originalBatchNo+", icdata="+icdata);
		
		int ret = ISO8583Interface.saleVoid(traceNo, price, track2, track3,
				originalReferencNo, originalAuthorizationNo, pinData,
				originalTraceNo, originalBatchNo, icdata);//modify by punan
		
		return ret;
		//return -102;
		
	}

	public int SaleReversal() {
		// 为了便于事后可复用，必须都是从参数池AllPayParameters中读取参数。
		String price = AllPayParameters.mInput.getAmount();
		String originalTraceNo = AllPayParameters.mInput.getTraceNo();
		String originalAuthorizationNo = AllPayParameters.mRep
				.getOriginalAuthorizationNo();
		String reversalNo = AllPayParameters.mRep.getReversalNo();
		ICDataClass icdata = AllPayParameters.mEmv.getIcData();
		//ISO8583Interface.setTraceNo(originalTraceNo);
		
		Log.e("debug", "Sale Reversal originalTraceNo= "+originalTraceNo+"\nprice= "+price+
				"\noriginalAuthorizationNo= "+originalAuthorizationNo+"\nreversalNo= "+reversalNo+
				"\nicdata= "+icdata);
		
		ISO8583Interface.saleReversal(originalTraceNo, price, originalAuthorizationNo, reversalNo, icdata);
		
		String ret = new String(ISO8583Interface.getAckNo());
		if (ret != null
				&& (ret.equals("00") || ret.equals("25") || ret.equals("12")))
			return 0;
		else
			return -1;
	}

	public int VoidReversal() {

		String price = AllPayParameters.mInput.getAmount();
		String originalAuthorizationNo = AllPayParameters.mRep
				.getOriginalAuthorizationNo();
		String originalTraceNo = AllPayParameters.mInput.getsTraceNo();
		String originalBatchNo = AllPayParameters.mRep.getOriginalBatchNo();
		String reversalNo = AllPayParameters.mRep.getReversalNo();
		ICDataClass icdata = AllPayParameters.mEmv.getIcData();
		icdata.f55 = null;
		icdata.f55Length = 0;
		//ISO8583Interface.setTraceNo(originalTraceNo);
		ISO8583Interface.saleVoidReversal(originalTraceNo, price, originalAuthorizationNo,
				originalBatchNo, reversalNo, icdata);
		String ret = new String(ISO8583Interface.getAckNo());
		if (ret != null
				&& (ret.equals("00") || ret.equals("25") || ret.equals("12")))
			return 0;
		else
			return -1;
	}

	public int refund() {

		String price = AllPayParameters.mInput.getAmount();
		String traceNo = AllPayParameters.mInput.getTraceNo();
		String track2 = AllPayParameters.mEmv.getTrack2();
		String track3 = AllPayParameters.mEmv.getTrack3();
		String originalReferencNo = AllPayParameters.mRep
				.getOriginalReferencNo();
		String originalAuthorizationNo = AllPayParameters.mRep
				.getOriginalAuthorizationNo();
		byte[] pinData = AllPayParameters.mSafe.getPinData();
		String originalTraceNo = AllPayParameters.mInput.getsTraceNo();
		String originalDate = AllPayParameters.mInput.getsTraceDate();
		ICDataClass icData = AllPayParameters.mEmv.getIcData();
		icData.f55 = null;
		icData.f55Length = 0;
		return ISO8583Interface.refund(traceNo, price, track2, track3,
				originalReferencNo, originalAuthorizationNo, pinData,
				originalTraceNo, originalDate, icData);//modify by punan
	}

	private void scriptNotify() {
		ICDataClass icDataClass = BankEmvData.get62IcF55();
		Log.i("debug","scriptNotify pData " + "=========================================");
		String str = "";
		for (int i = 0; i < icDataClass.f55Length; i++)
			str += String.format("%02x ", icDataClass.f55[i]);
		Log.i("debug","scriptNotify pData "+ str);
		Log.i("debug","scriptNotify pData " + "=========================================");
		AllPayParameters.mEmv.setIcData(icDataClass);
		ICscriptNotification();
	}
}
