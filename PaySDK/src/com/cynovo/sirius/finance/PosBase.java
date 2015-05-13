package com.cynovo.sirius.finance;

import android.util.Log;

import com.cynovo.sirius.PaySDK.ClassicTrade;
import com.cynovo.sirius.PaySDK.ClassicTradeFactory;
import com.cynovo.sirius.PaySDK.ClassicTradeFactory.PayMethods;
import com.cynovo.sirius.flow.Input.TradeType;

public class PosBase {
	// 冲正原因码
	public static final String POS_REVESAL_NO_RESPONCE = "98";
	public static final String POS_REVESAL_MACHINE_ERROR = "96";
	public static final String POS_REVESAL_MAC_ERROR = "A0";
	public static final String POS_REVESAL_EMVR = "06";
	public static final String POS_REVESAL_OTHER = "06";
	public static final String POS_WRONG_PASS = "55";
	public static final int REVENO = 2;

	public ClassicTrade mtrade = null;

	public void init(PayMethods cardmode) {
		ClassicTradeFactory factory = new ClassicTradeFactory(
				TradeDefine.channel);
		mtrade = factory.getTrade();
		mtrade.SetCurrentMethod(cardmode);
		mtrade = mtrade.GetCurrentTrade();
	}

	public String getRetErrorMsg(int errorcode) {
		switch (errorcode) {
		case 0:
			return null;
		case PosService.POSERROR_NORESPONSE: //-102
			return POS_REVESAL_NO_RESPONCE; //98
			
		case PosService.POSERROR_WRONGMAC: //-3
			return POS_REVESAL_MAC_ERROR;  //A0
			
		case PosService.POSERROR_EMVNEEDREVERSE: //-150
			return POS_REVESAL_EMVR; //06
			
		default:
			return POS_REVESAL_OTHER; //06
		}
	}

	public int Process(TradeType mode) {
		if (mtrade == null)
			return -1000;

		int ret = -1001;
		
		if (mode == TradeType.Sale){
			Log.i("debug", "trade process Sale begin");
			ret = mtrade.Sale();
			Log.e("debug", "trade process Sale ret ===>>>"+ret);
			
			
		}else if (mode == TradeType.Void){
			Log.i("debug", "trade process Void begin");
			ret = mtrade.Void();
			Log.e("debug", "trade process Void ret ===>>>"+ret);
			
			
		}else if (mode == TradeType.Refund){
			Log.i("debug", "trade process Refund begin");
			ret = mtrade.refund();
			Log.e("debug", "trade process Refund ret ===>>>"+ret);
			
		}

		return ret;
	}

	// 交易前的工作
	public void preProcess() {
		// 需要存下交易相关的信息，以备突然断电下次冲正……
	}

	// 交易后的工作
	public int postProcess(int retval, TradeType mode) {
		// 退货交易没有冲正
		if (mode == TradeType.Refund)
			return 0;

		// 判断是否需要冲正
		if (retval == PosService.POSERROR_NORESPONSE
				|| retval == PosService.POSERROR_WRONGMAC
				|| retval == PosService.POSERROR_EMVNEEDREVERSE) {
			
			Log.e("debug", "需要冲正");
			
			int ret = -1;
			for (int i = 0; i < REVENO; i++) {
				if (mode == TradeType.Sale)
					ret = mtrade.SaleReversal();
				
				else if (mode == TradeType.Void)
					//不支持消费撤销冲正
					//ret = mtrade.VoidReversal();

				if (ret == 0) {
					return ret;
				}
			}
			Log.e("debug", "冲正失败");
			return -1;
		} else {
			Log.e("debug", "不需要冲正");
			return 0;
		}
	}
}
