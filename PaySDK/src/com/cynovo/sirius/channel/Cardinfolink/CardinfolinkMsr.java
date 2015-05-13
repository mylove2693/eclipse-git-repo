package com.cynovo.sirius.channel.Cardinfolink;

import android.util.Log;

import com.cynovo.sirius.parameter.AllPayParameters;
import com.cynovo.sirius.util.Utility;
import com.kivvi.jni.ISO8583Interface;
import com.kivvi.jni.ISO8583Interface.ICDataClass;

public class CardinfolinkMsr extends AdapterCardinfolink {

	//磁条卡消费（MSR Sale）
	public int Sale() {
		String price = AllPayParameters.mInput.getAmount(); 
		String traceNo = AllPayParameters.mInput.getTraceNo();
		String batchNo = AllPayParameters.mRep.getOriginalBatchNo();
		String track2 = AllPayParameters.mMsr.getTrack2(); 
		String track3 = AllPayParameters.mMsr.getTrack3();
		byte [] pinData = AllPayParameters.mSafe.getPinData(); 
		ICDataClass icdata = AllPayParameters.mMsr.getIcData(); 
		
		if(Utility.debug)
			Log.e("CardinfolinkMsr", "traceNo= "+traceNo+", \nbatchNo="+batchNo+", \nprice = "+price+
					", \ntraceNo = "+traceNo+", \ntrack2 = "+track2+", \ntrack3="+track3+
					", \npinData = "+pinData+	", \nicdata = "+icdata);
		int ret = ISO8583Interface.sale(traceNo, batchNo, price, track2, track3, pinData, icdata);
		
		if(Utility.debug)
		Log.e("CardinfolinkMsr", "ISO8583Interface Sale ret= "+ret);
		//return -102;
		return ret;

	}

	//磁条卡撤销（MSR Void）
	public int Void() {
		String price = AllPayParameters.mInput.getAmount();
		String traceNo = AllPayParameters.mInput.getTraceNo();
		String track2 =	AllPayParameters.mMsr.getTrack2();
		String track3 =	AllPayParameters.mMsr.getTrack3();
		String originalReferencNo =	AllPayParameters.mRep.getOriginalReferencNo();
		String originalAuthorizationNo = AllPayParameters.mRep.getOriginalAuthorizationNo();
		String originalBatchNo = AllPayParameters.mRep.getOriginalBatchNo();
		byte [] pinData = AllPayParameters.mSafe.getPinData();
		String originalTraceNo = AllPayParameters.mInput.getsTraceNo();
		ICDataClass icdata = AllPayParameters.mMsr.getIcData();
		if(Utility.debug)
			Log.e("debug", "price = "+price+", \ntraceNo = "+traceNo+", \ntrack2 = "+track2+", \ntrack3="+track3+
					", \noriginalReferencNo = "+originalReferencNo+", \noriginalAuthorizationNo = "+originalAuthorizationNo+", \npinData = "+pinData+
					", \noriginalTraceNo= "+originalTraceNo+", \noriginalBatchNo= "+originalBatchNo+", \nicdata = "+icdata);
		
		int ret = ISO8583Interface.saleVoid(traceNo, price, track2, track3, originalReferencNo, originalAuthorizationNo, pinData,	originalTraceNo, originalBatchNo,icdata);
		//return -102;
		return ret;
		
	}

	public int SaleReversal() {
		
		String price = AllPayParameters.mInput.getAmount();
		String traceNo = AllPayParameters.mInput.getTraceNo();
		String originalAuthorizationNo = AllPayParameters.mRep.getOriginalAuthorizationNo();
		String reversalNo = AllPayParameters.mRep.getReversalNo();
		ICDataClass icdata = AllPayParameters.mMsr.getIcData();
		
		Log.d("debug", "price= "+price+", \ntraceNo= "+traceNo+
				", \noriginalAuthorizationNo= "+originalAuthorizationNo+
				", \nreversalNo= "+reversalNo+", \nicdata= "+icdata);
		
		ISO8583Interface.saleReversal(traceNo, price, originalAuthorizationNo, reversalNo, icdata);
		
		byte[] temp = ISO8583Interface.getAckNo();
		if (temp == null){
			Log.e("debug", "CardinfolinkMsr SaleReversal getAckNo return null");
			return -1;
		}
		String ret = new String(ISO8583Interface.getAckNo()); 
		
		if(ret != null && (ret.equals("00") ||ret.equals("25") ||ret.equals("12"))){
			Log.e("debug", "冲正成功");
			return 0;
		}else{
			return -1;
		}

	}

	public int VoidReversal() {

		String price = AllPayParameters.mInput.getAmount(); 
		String originalTraceNo = AllPayParameters.mInput.getsTraceNo();
		String originalAuthorizationNo = AllPayParameters.mRep.getOriginalAuthorizationNo(); 
		String originalBatchNo = AllPayParameters.mRep.getOriginalBatchNo();
		String traceNo = AllPayParameters.mInput.getTraceNo(); 
		String reversalNo = AllPayParameters.mRep.getReversalNo(); 
		ICDataClass	icdata = AllPayParameters.mMsr.getIcData();
		
		ISO8583Interface.saleVoidReversal(originalTraceNo, price, originalAuthorizationNo, originalBatchNo, reversalNo, icdata);
		
		String ret = new String(ISO8583Interface.getAckNo());
		if(ret != null && (ret.equals("00") ||ret.equals("25") ||ret.equals("12")))
			return 0;
		else
			return -1;
		
		//return 0;
		
	}

	public int refund() {

		String price = AllPayParameters.mInput.getAmount(); 
		String traceNo = AllPayParameters.mInput.getTraceNo(); 
		String track2 = AllPayParameters.mMsr.getTrack2(); 
		String track3 = AllPayParameters.mMsr.getTrack3(); 
		String originalReferencNo =	AllPayParameters.mRep.getOriginalReferencNo(); 
		String originalAuthorizationNo = AllPayParameters.mRep.getOriginalAuthorizationNo(); 
		byte [] pinData = AllPayParameters.mSafe.getPinData(); 
		String originalTraceNo = AllPayParameters.mInput.getsTraceNo(); 
		String originalDate = AllPayParameters.mInput.getsTraceDate(); 
		ICDataClass icData = AllPayParameters.mMsr.getIcData();
		return	ISO8583Interface.refund(traceNo, price, track2, track3, originalReferencNo, originalAuthorizationNo, pinData, originalTraceNo, originalDate, icData);

		//return 0;
		// modified by
		// wanhaiping--------------------------------------------<end
	}

}
