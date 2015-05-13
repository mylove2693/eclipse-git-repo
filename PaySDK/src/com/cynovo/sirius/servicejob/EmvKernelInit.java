package com.cynovo.sirius.servicejob;

import com.cynovo.sirius.PaySDK.ClassicTrade;
import com.cynovo.sirius.PaySDK.ClassicTradeFactory;
import com.cynovo.sirius.finance.TradeDefine;

public class EmvKernelInit {

	public static int Start() {

		ClassicTradeFactory factory = new ClassicTradeFactory(
				TradeDefine.channel);
		ClassicTrade trade = factory.getTrade();
		int retval = trade.EmvInit();
		return retval;
	}
}
