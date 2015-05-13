package com.cynovo.sirius.checkin;

import android.content.Context;
import android.util.Log;

import com.cynovo.sirius.PaySDK.ClassicTradeFactory.Channels;
import com.cynovo.sirius.finance.TradeDefine;
import com.cynovo.sirius.view.PayButton;

public class CheckInFactory {

	public static void processCheckIn(Context context, PayButton payButton) {
		if (TradeDefine.channel == Channels.demo) { // 使用讯联的demo展示交易
			Log.e("debug", "begin processCheckIn point1");
			CheckInCardinfolinkDemo checkInCardinfolinkDemo = new CheckInCardinfolinkDemo();
			checkInCardinfolinkDemo.checkIn(context, payButton);
		} else if (TradeDefine.channel == Channels.cardinfolink) { // 讯联
			Log.e("debug", "begin processCheckIn point2");
			CheckInCardinfolink checkInCardinfolink = new CheckInCardinfolink();
			checkInCardinfolink.checkIn(context, payButton);
		} else if (TradeDefine.channel == Channels.allinpay) { // 通联
			Log.e("debug", "begin processCheckIn point3");
			CheckInAllinpay checkInAllinpay = new CheckInAllinpay();
			checkInAllinpay.checkIn(context, payButton);
		} else if (TradeDefine.channel == Channels.unionpay) { // 银联
			Log.e("debug", "begin processCheckIn point4");
			CheckInUnionpay checkInUnionpay = new CheckInUnionpay();
			checkInUnionpay.checkIn(context, payButton);
		}
	}
}
