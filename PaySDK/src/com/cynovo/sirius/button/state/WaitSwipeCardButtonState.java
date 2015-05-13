package com.cynovo.sirius.button.state;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cynovo.sirius.PaySDK.PayMainActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.finance.PosService;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.view.PayButton;

/**
 * @author ycb 等待刷卡的Button 状态
 */
public class WaitSwipeCardButtonState extends ButtonState {
	private BroadcastReceiver receiver;
	private LocalBroadcastManager lbm;

	@Override
	public void changeButtonState(ButtonContext buttonContext, Context context,
			PayButton payButton) {
		Log.e("debug", "begin WaitSwipeCardButtonState changeButtonState");
		lbm = LocalBroadcastManager.getInstance(context);

		payButton.setBackgroundResource(R.drawable.swip_both_card_btn_cover);
		AnimationDrawable animationDrawable = (AnimationDrawable) payButton
				.getBackground();
		animationDrawable.start();
		payButton.setPadding(300, 0, 0, 0);
		payButton.setCompoundDrawables(null, null, null, null);
		payButton.setText(R.string.swipe_card_plz);
		payButton.setTextColor(0xFFCCCCCC);
		
		
		Log.e("debug", "begin WaitSwipeCardButtonState changeButtonState point1");
		
		Intent intent1 = new Intent(context, PosService.class);
		intent1.setAction(PosService.ACTION_TRADE_START);
		context.startService(intent1);
		
		registerReceiver(context);
		Log.e("debug", "begin WaitSwipeCardButtonState changeButtonState point2");
		
		buttonContext.setButtonState(new ProcessingButtonState());
		Log.e("debug", "begin WaitSwipeCardButtonState changeButtonState point3");
	}

	private void registerReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(PosService.ACTION_TRADE_START);

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(PosService.ACTION_TRADE_START)) {
					int retInt = intent.getExtras().getInt("retint");
					MyLog.e("retInt:" + retInt);
					if (retInt == PosService.POS_NOERROR) {
						PayMainActivity.handler
								.sendEmptyMessage(PayMainActivity.MSG_SWIP_CARD_SUCCESS);
					} else {
						PayMainActivity.handler
								.sendEmptyMessage(PayMainActivity.MSG_SWIP_CARD_FAILED);
					}
					lbm.unregisterReceiver(receiver);
				}
			}
		};

		lbm.registerReceiver(receiver, filter);
	}
}
