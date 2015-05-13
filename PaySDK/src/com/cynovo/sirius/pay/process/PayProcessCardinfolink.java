package com.cynovo.sirius.pay.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import com.cynovo.sirius.PaySDK.PayMainActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.finance.PosService;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.view.PayButton;

public class PayProcessCardinfolink implements PayProcessInterface {
	private BroadcastReceiver receiver;
	private LocalBroadcastManager lbm;
	private PayButton payButton;

	@Override
	public void payprocess(Context context, PayButton payButton) {
		this.payButton = payButton;
		lbm = LocalBroadcastManager.getInstance(context);

		payButton.setBackgroundResource(R.drawable.swip_both_card_btn_cover);
		AnimationDrawable animationDrawable = (AnimationDrawable) payButton
				.getBackground();
		animationDrawable.start();
		payButton.setPadding(300, 0, 0, 0);
		payButton.setCompoundDrawables(null, null, null, null);
		payButton.setText(R.string.input_password);

		registerReceiver(context);
	}

	private void registerReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(PosService.ACTION_PINPAD_CANCEL);
		filter.addAction(PosService.ACTION_NO_NETWORKERROR);
		filter.addAction(PosService.ACTION_POS_REVERSE_FAIL);
		filter.addAction(PosService.ACTION_POS_SUCCESS);
		filter.addAction(PosService.ACTION_POS_NO_RESPONSE);
		filter.addAction(PosService.ACTION_POS_WRITE_ERROR);
		filter.addAction(PosService.ACTION_POS_MAC_ERROR);
		filter.addAction(PosService.ACTION_POS_ERROR);
		filter.addAction(PosService.ACTION_POS_UNKNOWN_ERROR);
		filter.addAction(PosService.ACTION_POS_EMV_FAIL);
		filter.addAction(PosService.ACTION_POS_EMV_DENY);
		filter.addAction(PosService.ACTION_PINPAD_CONFIRM); // 等待输入密码

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(PosService.ACTION_PINPAD_CANCEL)) {
					// 密码为空
					MyLog.e("密码为空");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				} else if (intent.getAction().equals(
						PosService.ACTION_NO_NETWORKERROR)) {
					// 网络错误
					MyLog.e("网络错误");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_REVERSE_FAIL)) {
					// TODO 冲正不成功，需要提示用户人工冲正
					MyLog.e("不需要冲正或冲正成功");
					lbm.unregisterReceiver(receiver);
					Message msg = new Message();
					msg.what = PayMainActivity.MSG_PAY_FAILED;
					msg.obj = R.string.reverse_fail + "";
					PayMainActivity.handler.sendMessage(msg);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_SUCCESS)) {
					// 交易成功，发送交易成功的消息，然后直接返回
					MyLog.e("交易成功");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_END);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_NO_RESPONSE)) {
					// POS中心无响应
					MyLog.e("POS中心无响应");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_WRITE_ERROR)) {
					// 网络故障
					MyLog.e("网络故障");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_MAC_ERROR)) {
					// MAC错误
					MyLog.e("MAC错误");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_ERROR)) {
					// 39域不为"00"
					MyLog.e("39域不为00");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_UNKNOWN_ERROR)) {
					// 没有39域
					MyLog.e("没有39域");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				} else if (intent.getAction().equals(
						PosService.ACTION_PINPAD_CONFIRM)) {
					// 密码输入成功
					payButton.setText(R.string.is_paying);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_EMV_FAIL)) {
					// emv出错
					MyLog.e("emv出错");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_EMV_DENY)) {
					// IC卡拒绝交易
					MyLog.e("IC卡拒绝交易");
					lbm.unregisterReceiver(receiver);
					PayMainActivity.handler
							.sendEmptyMessage(PayMainActivity.MSG_PAY_FAILED);
				}
			}
		};

		lbm.registerReceiver(receiver, filter);
	}
}
