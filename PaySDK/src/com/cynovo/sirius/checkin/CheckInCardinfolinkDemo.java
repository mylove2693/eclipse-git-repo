package com.cynovo.sirius.checkin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.cynovo.sirius.PaySDK.PayMainActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.finance.PosService;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.view.PayButton;

public class CheckInCardinfolinkDemo implements CheckInInterface {
	private BroadcastReceiver receiver;
	private LocalBroadcastManager lbm;

	@Override
	public void checkIn(Context context, PayButton payButton) {
		payButton.setText(R.string.check_in_ing);
		payButton.setTextColor(0xFF818181);

		Intent intent1 = new Intent(context, PosService.class);
		intent1.setAction(PosService.ACTION_INIT);
		context.startService(intent1);

		lbm = LocalBroadcastManager.getInstance(context);
		// LocalBroadcastManager

		// 注册监听的广播
		registerReceiver(context);
	}

	private void registerReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(PosService.ACTION_INIT);
		filter.addAction(PosService.ACTION_EMVINIT);

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(PosService.ACTION_INIT)) { // 初始化完成
					int retInt = intent.getExtras().getInt("retint");
					MyLog.e("retInt:" + retInt);
					if (retInt == PosService.POS_NOERROR) {
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
