package com.cynovo.sirius.bankcard;

import com.cynovo.sirius.util.MyLog;

public class BankTradeStart {

	public final static int msrDetect = 1;
	public final static int emvDetect = 2;

	public static int Start(BankMsrData msrdata, BankEmvData emvdata) {
		int ret = MsrStart(msrdata);
		if (ret < 0)
			return ret;

		ret = EmvStart(emvdata);
		if (ret < 0)
			return ret;

		MyLog.e("debug", "BankTradeStart  PollStart point1");
		ret = PollStart(msrdata, emvdata);
		MyLog.e("debug", "BankTradeStart  PollStart point2");

		return ret;
	}

	private static int MsrStart(BankMsrData msrdata) {
		if (msrdata == null)
			return -1;

		msrdata.clearData();
		return 0;
	}

	private static int EmvStart(BankEmvData emvdata) {
		if (emvdata == null)
			return -1;

		emvdata.clearData();
		BankEmvData.open();
		return 0;
	}

	private static int PollStart(BankMsrData msrdata, BankEmvData emvdata) {
		while (true) {
			if (msrdata.QueryMsrSwipe())
				return msrDetect;
			else if (emvdata.QueryEmvPresence())
				return emvDetect;
		}
	}
}
