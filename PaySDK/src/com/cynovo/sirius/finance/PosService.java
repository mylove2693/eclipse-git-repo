package com.cynovo.sirius.finance;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.cynovo.sirius.servicejob.Device;
import com.cynovo.sirius.servicejob.DownloadICKey;
import com.cynovo.sirius.servicejob.DownloadICParams;
import com.cynovo.sirius.servicejob.DownloadMainKey;
import com.cynovo.sirius.servicejob.DownloadWorkKey;
import com.cynovo.sirius.servicejob.EmvKernelInit;
import com.cynovo.sirius.util.MyLog;

public class PosService extends IntentService {

	public static String ACTION_INIT = "com.cynovo.sirius.intent.action.init";
	public static String ACTION_MAINKEY = "com.cynovo.sirius.intent.action.mainkey";
	public static String ACTION_ICKEY = "com.cynovo.sirius.intent.action.ickey";
	public static String ACTION_ICPARAMS = "com.cynovo.sirius.intent.action.icparams";
	public static String ACTION_CHECKIN = "com.cynovo.sirius.intent.action.checkin";
	public static String ACTION_EMVINIT = "com.cynovo.sirius.intent.action.emvinit";

	public static String ACTION_TRADE_PASSWORD = "com.cynovo.sirius.intent.action.trade.password";
	public static String ACTION_TRADE_STOP = "com.cynovo.sirius.intent.action.trade.stop";
	public static String ACTION_TRADE_START = "com.cynovo.sirius.intent.action.trade.start";
	public static String ACTION_TRADE_EXITING = "com.cynovo.sirius.intent.action.trade.exiting";
	public static String ACTION_MSR_PAUSE = "com.cynovo.sirius.intent.action.msr.pause";
	public static String ACTION_MSR_HAVEEMV = "com.cynovo.sirius.intent.action.msr.checkhaveemv";

	// 磁条卡刷卡失败
	public static String ACTION_MSR_FAIL = "com.cynovo.sirius.intent.action.msr.fail";
	public static String ACTION_MSR_SUCCESS = "com.cynovo.sirius.intent.action.msr.success";
	public static String ACTION_EMV_FAIL = "com.cynovo.sirius.intent.action.emv.fail";
	public static String ACTION_EMV_FALLBACK = "com.cynovo.sirius.intent.action.emv.fallback";
	public static String ACTION_EMV_RETRY = "com.cynovo.sirius.intent.action.emv.retry";
	public static String ACTION_EMV_SUCCESS = "com.cynovo.sirius.intent.action.emv.success";

	public static String ACTION_ALL_RETRY = "com.cynovo.sirius.intent.action.all.retry";
	public static String ACTION_MSR_RETRY = "com.cynovo.sirius.intent.action.msr.retry";

	public static String ACTION_EMV_INPROCESS = "com.cynovo.sirius.intent.action.emv.inprocess";
	public static String ACTION_MSR_INPROCESS = "com.cynovo.sirius.intent.action.msr.inprocess";

	// 提示用户用MSR数据进行交易，如果没有刷卡重新刷卡
	public static String ACTION_MSR_PINPAD_START = "com.cynovo.sirius.intent.action.msr.pinpad.start";
	// 提示用户用IC数据进行交易
	public static String ACTION_EMV_PINPAD_START = "com.cynovo.sirius.intent.action.emv.pinpad.start";
	public static String ACTION_PINPAD_CANCEL = "com.cynovo.sirius.intent.action.pinpad.cancel";
	public static String ACTION_PINPAD_CONFIRM = "com.cynovo.sirius.intent.action.pinpad.confirm";
	public static String ACTION_PINPAD_NUMBER = "com.cynovo.sirius.intent.action.pinpad.number";
	public static String ACTION_PINPAD_BACKSPACE = "com.cynovo.sirius.intent.action.pinpad.backspace";

	public static String ACTION_POS_START = "com.cynovo.sirius.intent.action.pos.start";
	// 提示用户确认是否有IC卡
	public static String ACTION_POS_CHECKEMV = "com.cynovo.sirius.intent.action.pos.checkemv";
	public static String ACTION_POS_NO_RESPONSE = "com.cynovo.sirius.intent.action.pos.noresponse";
	public static String ACTION_POS_WRITE_ERROR = "com.cynovo.sirius.intent.action.pos.writeerror";
	public static String ACTION_POS_MAC_ERROR = "com.cynovo.sirius.intent.action.pos.macerror";
	public static String ACTION_POS_ERROR = "com.cynovo.sirius.intent.action.pos.error";
	public static String ACTION_NO_NETWORKERROR = "com.cynovo.sirius.intent.action.pos.networkerror";
	public static String ACTION_POS_UNKNOWN_ERROR = "com.cynovo.sirius.intent.action.pos.unknownerror";
	public static String ACTION_POS_EMV_DENY = "com.cynovo.sirius.intent.action.pos.emvdeny";
	public static String ACTION_POS_EMV_FAIL = "com.cynovo.sirius.intent.action.pos.emvfail";
	public static String ACTION_POS_SUCCESS = "com.cynovo.sirius.intent.action.pos.success";
	public static String ACTION_POS_REVERSE_FAIL = "com.cynovo.sirius.intent.action.pos.refundfail";
	public static String ACTION_BACK = "com.cynovo.sirius.intent.action.back";
	public static String ACTION_CLOSE = "com.cynovo.sirius.intent.action.close";

	public static final int POS_NOERROR = 0;
	public static final int POSERROR_NORESPONSE = -102;
	public static final int POSERROR_WRONGMAC = -3;
	public static final int POSERROR_NO39 = -5;
	public static final int POSERROR_WRONG39 = -4;
	public static final int POSERROR_NONETWORK101 = -101;
	public static final int POSERROR_NONETWORK10 = -10;
	public static final int POSERROR_EMVNEEDREVERSE = -150;
	public static final int POSERROR_EMVETRADERROR = -151;
	public static final int POSERROR_EMVFAIL = -152;
	private LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);

	public PosService() {
		super("PosService");
	}

	public void broadcastRetInt(String name, int ret) {
		Intent retIntent = new Intent(name);
		retIntent.putExtra("retint", ret);
		lbm.sendBroadcast(retIntent);
	}

	public void broadcastRetString(String name, String extra, String ret) {
		Intent retIntent = new Intent(name);
		retIntent.putExtra(extra, ret);
		lbm.sendBroadcast(retIntent);
	}

	public void broadcast(String name) {
		Intent retIntent = new Intent(name);
		lbm.sendBroadcast(retIntent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction() != null) {
			if (intent.getAction().equals(ACTION_INIT)) {
				// 所有的设备open
				MyLog.d("posService init...");
				int ret = Device.Open();
				broadcastRetInt(ACTION_INIT, ret);
			} else if (intent.getAction().equals(ACTION_MAINKEY)) {
				// 主密钥
				MyLog.d("posService mainkey...");
				int ret = DownloadMainKey.Start();
				broadcastRetInt(ACTION_MAINKEY, ret);
			} else if (intent.getAction().equals(ACTION_ICKEY)) {
				// ic卡密钥
				MyLog.d("posService ickey...");
				int ret = DownloadICKey.Start();
				broadcastRetInt(ACTION_ICKEY, ret);
			} else if (intent.getAction().equals(ACTION_ICPARAMS)) {
				// ic参数
				MyLog.d("posService icparams...");
				int ret = DownloadICParams.Start();
				broadcastRetInt(ACTION_ICPARAMS, ret);
			} else if (intent.getAction().equals(ACTION_CHECKIN)) {
				// 签到，即工作密钥
				MyLog.d("posService checkin...");
				int ret = DownloadWorkKey.Start();
				broadcastRetInt(ACTION_CHECKIN, ret);
			} else if (intent.getAction().equals(ACTION_EMVINIT)) {
				// emv kernel初始化
				MyLog.d("posService emvinit...");
				int ret = EmvKernelInit.Start();
				broadcastRetInt(ACTION_EMVINIT, ret);
			} else if (intent.getAction().equals(ACTION_TRADE_START)) {
				// 等待刷卡，等到后继续交易，直到交易完成
				MyLog.d("posService trade START...");
				TradeActionFactory factory = new TradeActionFactory(
						TradeDefine.cardtype, this);
				factory.getAction().Start();
			} else if (intent.getAction().equals(ACTION_BACK)) {
				// 点击返回页面
				MyLog.d("posService BACK...");
				TradeActionFactory factory = new TradeActionFactory(
						TradeDefine.cardtype, this);
				factory.getAction().Close();
				broadcastRetInt(ACTION_BACK, 0);
			} else if (intent.getAction().equals(ACTION_CLOSE)) {
				// 支付SDK服务终结
				MyLog.d("posService CLOSE...");
				int ret = Device.Close();
				broadcastRetInt(ACTION_CLOSE, ret);
			}
		}
	}
}
