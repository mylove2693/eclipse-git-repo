package com.cynovo.sirius.checkin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cynovo.sirius.PaySDK.PayMainActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.finance.PosService;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.view.PayButton;

public class CheckInCardinfolink implements CheckInInterface {
	private BroadcastReceiver receiver;
	private LocalBroadcastManager lbm;

	@Override
	public void checkIn(Context context, PayButton payButton) {
		Log.e("debug", "begin CheckInCardinfolink  checkIn point1");
		payButton.setText(R.string.check_in_ing);
		payButton.setTextColor(0xFF818181);
		Log.e("debug", "begin CheckInCardinfolink  checkIn point2");

		Intent intent1 = new Intent(context, PosService.class);
		Log.e("debug", "begin CheckInCardinfolink  checkIn point3");
		intent1.setAction(PosService.ACTION_INIT);
		Log.e("debug", "begin CheckInCardinfolink  checkIn point4");
		context.startService(intent1);
		Log.e("debug", "begin CheckInCardinfolink  checkIn point5");

		lbm = LocalBroadcastManager.getInstance(context);
		Log.e("debug", "begin CheckInCardinfolink  checkIn point6");
		// 注册监听的广播
		registerReceiver(context);
		Log.e("debug", "begin CheckInCardinfolink  checkIn point7");
	}

	private void registerReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(PosService.ACTION_INIT);
		filter.addAction(PosService.ACTION_CHECKIN);
		filter.addAction(PosService.ACTION_EMVINIT);

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(PosService.ACTION_INIT)) { // 初始化完成
					int retInt = intent.getExtras().getInt("retint");
					MyLog.e("retInt:" + retInt);
					if (retInt == PosService.POS_NOERROR) {
						Intent intent2 = new Intent(context, PosService.class);
						intent2.setAction(PosService.ACTION_CHECKIN);
						context.startService(intent2);
					} else {
						PayMainActivity.handler
								.sendEmptyMessage(PayMainActivity.MSG_CHECK_IN_FAILED);
						lbm.unregisterReceiver(receiver);
					}
				} else if (intent.getAction().equals(PosService.ACTION_CHECKIN)) { // 签到
					int retInt = intent.getExtras().getInt("retint");
					MyLog.e("retInt:" + retInt);
					if (retInt == PosService.POS_NOERROR) {
						// 签到成功
						Intent intent3 = new Intent(context, PosService.class);
						intent3.setAction(PosService.ACTION_EMVINIT);
						context.startService(intent3);
					} else {
						PayMainActivity.handler
								.sendEmptyMessage(PayMainActivity.MSG_CHECK_IN_FAILED);
						lbm.unregisterReceiver(receiver);
					}
				} else if (intent.getAction().equals(PosService.ACTION_EMVINIT)) {
					int retInt = intent.getExtras().getInt("retint");
					MyLog.e("retInt:" + retInt);
					if (retInt == PosService.POS_NOERROR) {
						PayMainActivity.handler
								.sendEmptyMessage(PayMainActivity.MSG_CHECK_IN_SUCCESS);

						lbm.unregisterReceiver(receiver);
					} else {
						PayMainActivity.handler
								.sendEmptyMessage(PayMainActivity.MSG_CHECK_IN_FAILED);
						lbm.unregisterReceiver(receiver);
					}
				}
			}
		};

		lbm.registerReceiver(receiver, filter);
	}
}
