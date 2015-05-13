package com.cynovo.sirius.finance;

import com.cynovo.sirius.PaySDK.ClassicTradeFactory.Channels;
import com.cynovo.sirius.finance.TradeActionFactory.CardTypes;

public class TradeDefine {

	public final static Channels channel = Channels.cardinfolink;
	public final static CardTypes cardtype = CardTypes.bankcard;
	// true: 连接讯联生产服务器，false: 连接讯联测试服务器
	public final static boolean PRODUCTING = false;

}
