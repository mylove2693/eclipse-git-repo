package com.cynovo.sirius.pay.process;

import android.content.Context;

import com.cynovo.sirius.PaySDK.ClassicTradeFactory.Channels;
import com.cynovo.sirius.finance.TradeDefine;
import com.cynovo.sirius.view.PayButton;

public class PayProcessFactory {
	public static void processPayProcess(Context context, PayButton payButton) {
		if (TradeDefine.channel == Channels.demo) {// demo
			PayProcessCardinfolinkDemo payProcessCardinfolinkDemo = new PayProcessCardinfolinkDemo();
			payProcessCardinfolinkDemo.payprocess(context, payButton);
		} else if (TradeDefine.channel == Channels.cardinfolink) { // 讯联
			PayProcessCardinfolink payProcessCardinfolink = new PayProcessCardinfolink();
			payProcessCardinfolink.payprocess(context, payButton);
		} else if (TradeDefine.channel == Channels.allinpay) { // 通联
			PayProcessAllinpay payProcessAllinpay = new PayProcessAllinpay();
			payProcessAllinpay.payprocess(context, payButton);
		} else if (TradeDefine.channel == Channels.unionpay) { // 银联
			PayProcessUnionpay payProcessUnionpay = new PayProcessUnionpay();
			payProcessUnionpay.payprocess(context, payButton);
		}
	}
}
