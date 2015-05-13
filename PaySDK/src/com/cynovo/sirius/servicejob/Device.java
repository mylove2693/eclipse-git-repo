package com.cynovo.sirius.servicejob;

import com.cynovo.sirius.PaySDK.ClassicTrade;
import com.cynovo.sirius.PaySDK.ClassicTradeFactory;
import com.cynovo.sirius.finance.TradeDefine;

public class Device {

	/**
	 * TODO 打开相关设备，请注明返回参数的意义
	 * 
	 * @return 1： 2： ...
	 */
	public static int Open() {
		ClassicTradeFactory factory = new ClassicTradeFactory(
				TradeDefine.channel);
		ClassicTrade trade = factory.getTrade();
		int retval = trade.DeviceInit();
		return retval;
	}

	/**
	 * TODO 关闭相关设备，请注明返回参数的意义
	 * 
	 * @return 1： 2： ...
	 */
	public static int Close() {
		ClassicTradeFactory factory = new ClassicTradeFactory(
				TradeDefine.channel);
		ClassicTrade trade = factory.getTrade();
		int retval = trade.DeviceClose();
		return retval;
	}

}
