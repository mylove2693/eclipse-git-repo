package com.cynovo.sirius.channel.Cardinfolink;

import com.cynovo.sirius.PaySDK.ClassicTrade;
import com.cynovo.sirius.PaySDK.ClassicTradeFactory.PayMethods;
import com.cynovo.sirius.finance.TradeDefine;
//import com.cynovo.sirius.jni.HostlinkInterface;
//import com.cynovo.sirius.jni.HostlinkInterface.ICDataClass;
//import com.cynovo.sirius.jni.SafeInterface;
//import com.cynovo.sirius.parameter.AllPayParameters;
import com.cynovo.sirius.parameter.AllPayParameters;
import com.kivvi.jni.ISO8583Interface;
import com.kivvi.jni.ISO8583Interface.ICDataClass;
import com.kivvi.jni.SafeInterface;

public class AdapterCardinfolink extends ClassicTrade {

	private CardinfolinkFactory mFactory;
	private PayMethods defaultMethod = PayMethods.msr;

	public AdapterCardinfolink() {
		super();
	}

	public ClassicTrade GetCurrentTrade() {
		mFactory = new CardinfolinkFactory(defaultMethod);
		return mFactory.getTrade();
	}

	@Override
	public int Sale() {
		return GetCurrentTrade().Sale();
	}

	@Override
	public int Void() {
		return GetCurrentTrade().Void();
	}

	@Override
	public int SaleReversal() {
		return GetCurrentTrade().SaleReversal();
	}

	@Override
	public int VoidReversal() {
		return GetCurrentTrade().VoidReversal();
	}

	@Override
	public void SetCurrentMethod(PayMethods method) {
		defaultMethod = method;
	}

	@Override
	public int refund() {
		return GetCurrentTrade().refund();
	}

	@Override
	public int updatemkey() {
		// modified by wanhaiping----------------->begin
		// return HostlinkInterface.updatemkey();
		return 0;
		// modified by wanhaiping-----------------<end
	}

	@Override
	public int updatewkey() {
		String traceNo = AllPayParameters.mInput.getTraceNo();
		String operatorNo = AllPayParameters.mInput.getOperator();
		//modified by punan begin
		//ISO8583Interface.setTraceNo(traceNo);
		//ISO8583Interface.setOperator(operatorNo);
		//return ISO8583Interface.updatewkey();
		return 0;
		//modified by punan end
	}

	@Override
	public int ICscriptNotification() {
		// 注意此处流水号自增1
		String price = AllPayParameters.mInput.getAmount();
		int flow = Integer.parseInt(AllPayParameters.mInput.getTraceNo());
		String traceNo = String.valueOf(flow + 1);
		String originalReferencNo = AllPayParameters.mRep
				.getOriginalReferencNo();
		String originalAuthorizationNo = AllPayParameters.mRep
				.getOriginalAuthorizationNo();
		String originalTraceNo = AllPayParameters.mInput.getTraceNo();
		String originalDate = AllPayParameters.mInput.getCurrentDate();
		ICDataClass icData = AllPayParameters.mEmv.getIcData();

		// modified by
		// wanhaiping------------------------------------------------------->begin
		// return HostlinkInterface.ICscriptNotification(price, traceNo,
		// originalReferencNo,
		// originalAuthorizationNo, originalTraceNo, originalDate, icData);
		return 0;
		// modified by
		// wanhaiping--------------------------------------------------------<end

	}

	@Override
	public int downloadICPKToFile() {
		// modified by
		// wanhaiping------------------------------------------------------->begin
		// return HostlinkInterface.downloadICPKToFile();
		return 0;
		// modified by
		// wanhaiping--------------------------------------------------------<end
	}

	@Override
	public int downloadICParamToFile() {
		// modified by
		// wanhaiping------------------------------------------------------->begin
		// return HostlinkInterface.downloadICParamToFile();
		return 0;
		// modified by
		// wanhaiping--------------------------------------------------------<end

	}

	@Override
	public int EmvInit() {
		return super.EmvInit();
	}

	@Override
	public int DeviceInit() {

		int retval = super.DeviceInit();
		if (retval < 0)
			return retval;

		// modified by wanhaiping------------------------>begin
		// retval = HostlinkInterface.open();
		retval = 0;
		// modified by wanhaiping------------------------<end

		if (retval < 0)
			return -1;

		if (TradeDefine.PRODUCTING) {
			// deleted by wanhaiping------------------------>begin
			/*
			 * HostlinkInterface.setIP("211.147.64.198");
			 * HostlinkInterface.setPort((short)5801);
			 * HostlinkInterface.setNetworkTimeout(15, 0);
			 * HostlinkInterface.setTPDU("6001010000");
			 * HostlinkInterface.setCurrencyCode("156");
			 * HostlinkInterface.setEnableLog(false);
			 */
			// deleted by wanhaiping------------------------<end
		} else {
			// deleted by wanhaiping------------------------>begin
			/*
			 * HostlinkInterface.setIP("140.207.50.238");
			 * HostlinkInterface.setPort((short) 5711);
			 * HostlinkInterface.setTPDU("6000080000");
			 * HostlinkInterface.setNetworkTimeout(60, 0);
			 * HostlinkInterface.setCurrencyCode("826");
			 * HostlinkInterface.setEnableLog(true);
			 */
			// deleted by wanhaiping------------------------<end
		}

		byte[] mid = new byte[15];
		byte[] tid = new byte[8];
		//SafeInterface.safe_readID(mid, tid); //modify by punan

		// deleted by wanhaiping------------------------>begin
		// HostlinkInterface.setMerchantCode(mid);
		// HostlinkInterface.setTerminalCode(tid);
		// deleted by wanhaiping------------------------<end

		return 0;
	}

	@Override
	public int DeviceClose() {

		int retval = super.DeviceClose();
		if (retval < 0)
			return retval;

		// modified by wanhaiping------------------------>begin
		// retval = HostlinkInterface.close();
		retval = 0;
		// modified by wanhaiping------------------------<end

		if (retval < 0)
			return -1;

		return 0;
	}

}
