package com.cynovo.sirius.PaySDK;

import com.cynovo.sirius.channel.Allinpay.AdapterAllinpay;
import com.cynovo.sirius.channel.Cardinfolink.AdapterCardinfolink;
import com.cynovo.sirius.channel.Demo.AdapterDemo;
import com.cynovo.sirius.channel.Unionpay.AdapterUnionpay;

public class ClassicTradeFactory {

	protected ClassicTrade mtrade;

	public enum Channels {
		demo, cardinfolink, allinpay, unionpay
	}

	public enum PayMethods {
		msr, emv, nfc;
	}

	public ClassicTradeFactory(Channels channel) {
		if (Channels.demo == channel) {
			mtrade = new AdapterDemo();
		} else if (Channels.cardinfolink == channel) {
			mtrade = new AdapterCardinfolink();
		} else if (Channels.allinpay == channel) {
			mtrade = new AdapterAllinpay();
		} else if (Channels.unionpay == channel) {
			mtrade = new AdapterUnionpay();
		} else
			mtrade = new AdapterCardinfolink();
	}

	public ClassicTradeFactory() {

	}

	public ClassicTrade getTrade() {
		return mtrade;
	}

}
