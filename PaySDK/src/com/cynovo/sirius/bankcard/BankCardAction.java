package com.cynovo.sirius.bankcard;

import android.content.Context;
import android.util.Log;

import com.cynovo.sirius.PaySDK.ClassicTradeFactory.PayMethods;
import com.cynovo.sirius.finance.HardPinPad;
import com.cynovo.sirius.finance.PosBase;
import com.cynovo.sirius.finance.PosService;
import com.cynovo.sirius.finance.TradeAction;
import com.cynovo.sirius.flow.Input.TradeType;
//import com.cynovo.sirius.jni.HostlinkInterface;
//import com.cynovo.sirius.jni.HostlinkInterface.ICDataClass;
import com.cynovo.sirius.parameter.AllPayParameters;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.util.NetworkUtil;
import com.cynovo.sirius.util.NumberFormater;
import com.kivvi.jni.EmvL2Interface;
import com.kivvi.jni.ISO8583Interface;
import com.kivvi.jni.ISO8583Interface.ICDataClass;

public class BankCardAction extends TradeAction {

	private PosService mcontext;
	// added by wanhaiping------------------------------>begin
	private int emvPinpadret = -1;

	// added by wanhaiping------------------------------<end

	public BankCardAction(Context context) {
		super();
		mcontext = (PosService) context;
	}

	@Override
	public void Start() {
		BankMsrData msrdata = new BankMsrData();
		BankEmvData emvdata = new BankEmvData();
		
		MyLog.d("debug", "BankCardAction Start point1");
		int ret = BankTradeStart.Start(msrdata, emvdata);// 这里阻塞状态，直到有人刷卡
		MyLog.d("debug", "BankCardAction Start point2");
		
		
		// 以下是侦测到刷卡后的代码
		TradeType mode = AllPayParameters.mInput.getType();
		String amount = AllPayParameters.mInput.getAmount();
		MyLog.d("debug", "BankCardAction Start point3");

		if (ret == BankTradeStart.msrDetect) {
			MyLog.d("debug", "BankCardAction Start point4");
			BankEmvData.close();// 关闭emv，此轮交易走msr
			ret = checkCardDataInMsr(msrdata);
			MyLog.d("debug", "BankCardAction Start point5");
			if (ret == 0) { // 通过check的合法的刷卡数据会用于交易
				MyLog.d("debug", "BankCardAction Start point6");
				mcontext.broadcastRetInt(PosService.ACTION_TRADE_START, 0);//modified by punan
				MyLog.d("debug", "BankCardAction Start point7");
				msrTradeStart(msrdata, amount);
				MyLog.d("debug", "BankCardAction Start point8");
			}
			MyLog.d("debug", "BankCardAction Start point9");
			return;
		} else if (ret == BankTradeStart.emvDetect) {
			
			MyLog.d("debug", "BankCardAction Start point11");
			ret = checkCardDataInEmv(emvdata, amount, mode);
			
			if (ret == 0) { // 通过check的合法的刷卡数据会用于交易
				mcontext.broadcastRetInt(PosService.ACTION_TRADE_START, 0);
				MyLog.d("debug", "BankCardAction Start point12");
				emvTradeStart(emvdata, amount);
			}
			// 交易结束，关闭emv
			MyLog.d("debug", "BankCardAction Start point13");
			BankEmvData.close();
			return;
		}
	}

	@Override
	public void Close() {
		BankEmvData.close();
	}

	private void msrTradeStart(BankMsrData msrdata, String amount) {
		initMsrDataToParameter(msrdata);// 初始化mMsr交易参数，供交易时读取
		String price = NumberFormater.MoneyFromTwelveNumber(amount);
		String cardno = msrdata.getTrackData().getCardNumber();
		TradeType type = AllPayParameters.mInput.getType();
		MyLog.d("debug", "price="+price+", cardno="+cardno+", type="+type);
		TradeWithMsrData(price, cardno, type);
	}

	private void emvTradeStart(BankEmvData emvdata, String amount) {
		TradeType type = AllPayParameters.mInput.getType();
//		if (type == TradeType.Sale && emvdata.calculateF55() < 0) {// 先把55域计算并赋值，不需要的时候可以再填null
//			mcontext.broadcast(PosService.ACTION_POS_EMV_FAIL);
//			return;
//		}

		initEmvDataToParameter(emvdata);
		String price = NumberFormater.MoneyFromTwelveNumber(amount);
		String cardno = emvdata.getData().getPAN();
		TradeWithEmvData(price, cardno, type, emvdata);
	}

	private int checkCardDataInMsr(BankMsrData msrdata) {
		MyLog.d("debug", "checkCardDataInMsr  point1");
		if (msrdata.readTrackData().isValid()) {
			// 不包含EMV信息，交易直接进入磁条卡流程
			MyLog.d("debug", "checkCardDataInMsr  point2");
			mcontext.broadcast(PosService.ACTION_MSR_PINPAD_START); //modified by punan
			MyLog.d("debug", "checkCardDataInMsr  point3");
			return 0;
		} else {
			// 读磁条卡失败，继续读MSR或EMV
			// TODO:need check , may cause problem
			MyLog.d("debug", "checkCardDataInMsr  point4");
			mcontext.broadcast(PosService.ACTION_ALL_RETRY);
			MyLog.d("debug", "checkCardDataInMsr  point5");
			return -1;
		}
	}

	private void initMsrDataToParameter(BankMsrData msrdata) {
		String track2 = msrdata.getTrackData().getTrack2();
		String track3 = msrdata.getTrackData().getTrack3();
		track2 = track2 != null ? track2.replace('=', 'D') : null;
		track3 = track3 != null ? track3.replace('=', 'D') : null;
		AllPayParameters.mMsr.setTrack2(track2);
		AllPayParameters.mMsr.setTrack3(track3);
		AllPayParameters.mMsr.setIcData(null);
	}

	private void initEmvDataToParameter(BankEmvData emvdata) {
		String track2 = emvdata.getData().getTrack2();
		track2 = track2 != null ? track2.replace('=', 'D') : null;
		String track3 = null;
		/*
		 * 下一行会有一个错误 02-24 11:55:56.250: E/AndroidRuntime(1996): FATAL
		 * EXCEPTION: IntentService[PosService] 02-24 11:55:56.250:
		 * E/AndroidRuntime(1996): java.lang.NullPointerException 02-24
		 * 11:55:56.250: E/AndroidRuntime(1996): at
		 * com.cynovo.sirius.bankcard.BankCardAction
		 * .initEmvDataToParameter(BankCardAction.java:108) 02-24 11:55:56.250:
		 * E/AndroidRuntime(1996): at
		 * com.cynovo.sirius.bankcard.BankCardAction.emvTradeStart
		 * (BankCardAction.java:74) 02-24 11:55:56.250: E/AndroidRuntime(1996):
		 * at
		 * com.cynovo.sirius.bankcard.BankCardAction.Start(BankCardAction.java
		 * :48) 02-24 11:55:56.250: E/AndroidRuntime(1996): at
		 * com.cynovo.sirius.
		 * finance.PosService.onHandleIntent(PosService.java:140) 02-24
		 * 11:55:56.250: E/AndroidRuntime(1996): at
		 * android.app.IntentService$ServiceHandler
		 * .handleMessage(IntentService.java:65) 02-24 11:55:56.250:
		 * E/AndroidRuntime(1996): at
		 * android.os.Handler.dispatchMessage(Handler.java:99) 02-24
		 * 11:55:56.250: E/AndroidRuntime(1996): at
		 * android.os.Looper.loop(Looper.java:137) 02-24 11:55:56.250:
		 * E/AndroidRuntime(1996): at
		 * android.os.HandlerThread.run(HandlerThread.java:60)
		 */
		AllPayParameters.mEmv.setTrack2(track2);
		AllPayParameters.mEmv.setTrack3(track3);
		AllPayParameters.mEmv.setIcData(emvdata.getData().getIcdata());
	}

	private void TradeWithMsrData(String price, String CardNo, TradeType type) {
		MyLog.d("debug", "TradeWithMsrData Start point1");
		HardPinPad hardpinpad = new HardPinPad();
		int ret = hardpinpad.dealTrackData(price, CardNo);
		MyLog.d("debug", "TradeWithMsrData Start point2 ret="+ret);

		if (ret < 0 && ret != HardPinPad.NULL_PINDATA) {// 密码为空
			mcontext.broadcast(PosService.ACTION_PINPAD_CANCEL);
			MyLog.d("debug", "punan PASSWORD MAY BE NULL");
			MyLog.d("debug", "TradeWithMsrData Start point3");
			return;
		} else {
			MyLog.d("debug", "TradeWithMsrData Start point4");
			if (ret == HardPinPad.NULL_PINDATA)
				hardpinpad.clearPinData();// 当密码为空时，设置密码为null

			mcontext.broadcast(PosService.ACTION_PINPAD_CONFIRM);
			MyLog.d("debug", "TradeWithMsrData Start point5");

			
			//if (!NetworkUtil.isConnected(mcontext)) {
			//mcontext.broadcast(PosService.ACTION_NO_NETWORKERROR); return; }
			
			MyLog.d("debug", "TradeWithMsrData Start point6");
			AllPayParameters.mSafe.setPinData(hardpinpad.getPinData());// 将密文保存到mSafe交易参数中
			hardpinpad.clearPinData();// 保存完毕，清除交易用的密文

			MyLog.d("debug", "TradeWithMsrData Start point7");
			PosBase posbase = new PosBase();
			MyLog.d("debug", "TradeWithMsrData Start point8");
			posbase.init(PayMethods.msr);// 生成交易用的内部对象
			MyLog.d("debug", "TradeWithMsrData Start point9");
			posbase.preProcess();// 交易前预处理工作
			MyLog.d("debug", "TradeWithMsrData Start point10");

			ret = posbase.Process(type);// 执行交易 
			
			MyLog.d("debug", "TradeWithMsrData Start point11 ret ===>>> "+ret);
			
			String reversalNo =	posbase.getRetErrorMsg(ret);
			AllPayParameters.mRep.setReversalNo(reversalNo);// 设置好冲正参数，以备冲正要用
			
			if (posbase.postProcess(ret, type) != 0) {// 不需要冲正和冲正成功都返回0
				Log.e("debug", "冲正失败");
				mcontext.broadcast(PosService.ACTION_POS_REVERSE_FAIL);
				return;
				
			}

			if (ret == PosService.POS_NOERROR) { // 交易成功
				mcontext.broadcast(PosService.ACTION_POS_SUCCESS);
			} else if (ret == PosService.POSERROR_NORESPONSE) { // POS中心无响应
				mcontext.broadcast(PosService.ACTION_POS_NO_RESPONSE);
			} else if (ret == PosService.POSERROR_NONETWORK10) { // 网络故障
				mcontext.broadcast(PosService.ACTION_POS_WRITE_ERROR);
			} else if (ret == PosService.POSERROR_NONETWORK101) {
				mcontext.broadcast(PosService.ACTION_NO_NETWORKERROR);
			} else if (ret == PosService.POSERROR_WRONGMAC) { // MAC错误
				mcontext.broadcast(PosService.ACTION_POS_MAC_ERROR);
			} else if (ret == PosService.POSERROR_WRONG39) { // 39域不为"00"

				
				String ackNo = new String(ISO8583Interface.getAckNo());
				mcontext.broadcastRetString(PosService.ACTION_POS_ERROR,"ACK_NO", ackNo); 
//				if (ackNo.equals(PosBase.POS_WRONG_PASS))
//				{
//					// 密码错误 
//					TradeWithMsrData(price, CardNo, type); 
//				}
				
			} else if (ret == PosService.POSERROR_NO39) { // 没有39域
				mcontext.broadcast(PosService.ACTION_POS_UNKNOWN_ERROR);
			} else {
				mcontext.broadcast(PosService.ACTION_POS_UNKNOWN_ERROR);
			}
		}
	}

	private int checkCardDataInEmv(BankEmvData emvdata, String price,
			TradeType mode) {
		// 检测到EMV插入，开始读EMV信息，这是一个耗时过程，如果中途拔出会出问题
		// 中途拔出和选择应用失败应该有不同的提示信息
		mcontext.broadcast(PosService.ACTION_EMV_INPROCESS);

		// modified by wanhaiping------------------------------------->begin
		// int ret = emvdata.GetEMVData(price, mode);

		int ret = emvdata.GetEMVData(price, mode, emvPinpadret);

		// modified by wanhaiping-------------------------------------<end

		if (ret == 0) {
			// 读卡成功，走IC卡交易流程
			MyLog.d("get emv data success...");
			mcontext.broadcast(PosService.ACTION_EMV_PINPAD_START);
			return 0;
		} else {
			mcontext.broadcast(PosService.ACTION_ALL_RETRY);
			return -1;
		}
	}

	private void TradeWithEmvData(String price, String CardNo, TradeType type, BankEmvData emvdata) {
		
		HardPinPad hardpinpad = new HardPinPad();
		int ret = hardpinpad.dealTrackData(price, CardNo);
		emvPinpadret = ret;
		
		ret = EmvL2Interface.tradeProcess(4);
		
		if (ret < 0) {
			MyLog.i("debug" , "EmvL2Interface" + "tradeProcess failed");
			return;
		}
		MyLog.e("debug" , "EmvL2Interface" + "tradeProcess success");
		
		ret = EmvL2Interface.onlineRequest();
		
		if (ret < 0) {
			MyLog.i("debug" , "EmvL2Interface" + "tradeProcess failed");
			return;
		}
		MyLog.e("debug" , "EmvL2Interface" + "tradeProcess success"); 
		
		
		emvdata.packF55Data();
		
		if (ret < 0 && ret != HardPinPad.NULL_PINDATA) {// 密码为空
			mcontext.broadcast(PosService.ACTION_PINPAD_CANCEL);
			return;
		} else {
			if (ret == HardPinPad.NULL_PINDATA) {
				hardpinpad.clearPinData();// 当密码为空时，设置密码为null
			}
			mcontext.broadcast(PosService.ACTION_PINPAD_CONFIRM);

			//if (!NetworkUtil.isConnected(mcontext)) {
			//	mcontext.broadcast(PosService.ACTION_NO_NETWORKERROR);
			//	return;
			//}
			
			AllPayParameters.mSafe.setPinData(hardpinpad.getPinData());// 将密文保存到mSafe交易参数中
			hardpinpad.clearPinData();// 保存完毕，清除交易用的密文

			PosBase posbase = new PosBase();
			posbase.init(PayMethods.emv);// 生成交易用的内部对象
			
			// 交易前预处理工作
			posbase.preProcess();
			
			// 执行交易
			ret = posbase.Process(type);
			
			
			if (type == TradeType.Sale) {
				ICDataClass icData = BankEmvData.getReverseIcF55();
				AllPayParameters.mEmv.setIcData(icData);
			}
			String reversalNo = posbase.getRetErrorMsg(ret);
			AllPayParameters.mRep.setReversalNo(reversalNo);// 设置好冲正参数，以备冲正要用
			
			if (posbase.postProcess(ret, type) != 0) {// 不需要冲正和冲正成功都返回0
				Log.e("debug", "冲正失败");
				mcontext.broadcast(PosService.ACTION_POS_REVERSE_FAIL);
				return;
			}
			
			if (ret == PosService.POS_NOERROR) { // 交易成功  0
				mcontext.broadcast(PosService.ACTION_POS_SUCCESS);
			} else if (ret == PosService.POSERROR_NORESPONSE) { // POS中心无响应 -102
				mcontext.broadcast(PosService.ACTION_POS_NO_RESPONSE);
			} else if (ret == PosService.POSERROR_NONETWORK10) { // 网络故障 -10
				mcontext.broadcast(PosService.ACTION_POS_WRITE_ERROR);
			} else if (ret == PosService.POSERROR_NONETWORK101) { //-101
				mcontext.broadcast(PosService.ACTION_NO_NETWORKERROR);
			} else if (ret == PosService.POSERROR_WRONGMAC) { // MAC错误 -3
				mcontext.broadcast(PosService.ACTION_POS_MAC_ERROR);
			} else if (ret == PosService.POSERROR_WRONG39) { // 39域不为"00" -4
				
				
				//String ackNo = new String(ISO8583Interface.getAckNo());
				mcontext.broadcastRetString(PosService.ACTION_POS_ERROR,"ACK_NO", "-5");
				
				/*  if (ackNo.equals(PosBase.POS_WRONG_PASS))
				 * {// 密码错误 TradeWithEmvData(price, CardNo, type); }
				 */
				
			} else if (ret == PosService.POSERROR_NO39) { // 没有39域 //-5
				mcontext.broadcast(PosService.ACTION_POS_UNKNOWN_ERROR);
			} else if (ret == PosService.POSERROR_EMVNEEDREVERSE) { //-150
				mcontext.broadcast(PosService.ACTION_POS_EMV_DENY);
			} else if (ret == PosService.POSERROR_EMVFAIL) {  //-152
				mcontext.broadcast(PosService.ACTION_POS_EMV_FAIL);
			} else {
				mcontext.broadcast(PosService.ACTION_POS_UNKNOWN_ERROR);
			}
		}
	}
}
