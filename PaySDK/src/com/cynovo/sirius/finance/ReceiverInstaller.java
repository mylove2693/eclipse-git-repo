package com.cynovo.sirius.finance;

import com.cynovo.sirius.PaySDK.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

public class ReceiverInstaller {

	static public final int NORMAL_STATE = 0;
	static public final int WAITSWIP_STATE = 1;
	static public final int WARNING_STATE = 2;
	static public final int SUCCESS_STATE = 3;
	static public final int CHECKIN_STATE = 4;

	IntentFilter filter;
	BroadcastReceiver receiver;

	public ReceiverInstaller(Context context, Handler handler) {
		super();
		initFilter();
		initReceiver(handler);
		installReceiver(context);
	}

	private void installReceiver(Context context) {
		if (context != null)
			context.registerReceiver(receiver, filter);
	}

	private void initFilter() {
		filter = new IntentFilter();
		filter.addAction(PosService.ACTION_MSR_FAIL);
		filter.addAction(PosService.ACTION_MSR_SUCCESS);
		filter.addAction(PosService.ACTION_MSR_FAIL);
		filter.addAction(PosService.ACTION_EMV_FALLBACK);
		filter.addAction(PosService.ACTION_EMV_RETRY);
		filter.addAction(PosService.ACTION_EMV_SUCCESS);
		filter.addAction(PosService.ACTION_POS_CHECKEMV);

		filter.addAction(PosService.ACTION_EMV_INPROCESS);
		filter.addAction(PosService.ACTION_MSR_INPROCESS);
		filter.addAction(PosService.ACTION_ALL_RETRY);
		filter.addAction(PosService.ACTION_EMV_FAIL);

		filter.addAction(PosService.ACTION_MSR_PINPAD_START);
		filter.addAction(PosService.ACTION_PINPAD_CANCEL);
		filter.addAction(PosService.ACTION_PINPAD_CONFIRM);
		filter.addAction(PosService.ACTION_POS_START);
		filter.addAction(PosService.ACTION_POS_NO_RESPONSE);
		filter.addAction(PosService.ACTION_POS_MAC_ERROR);
		filter.addAction(PosService.ACTION_NO_NETWORKERROR);
		filter.addAction(PosService.ACTION_POS_ERROR);
		filter.addAction(PosService.ACTION_POS_UNKNOWN_ERROR);
		filter.addAction(PosService.ACTION_POS_SUCCESS);
		filter.addAction(PosService.ACTION_POS_WRITE_ERROR);
		filter.addAction(PosService.ACTION_POS_EMV_DENY);
		filter.addAction(PosService.ACTION_POS_EMV_FAIL);
	}

	private void initReceiver(final Handler handler) {

		final Message message = handler.obtainMessage();
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(
						PosService.ACTION_MSR_PINPAD_START)) {
					message.obj = context.getString(R.string.password_hint);
					message.what = SUCCESS_STATE;
					message.arg1 = 0;
				} else if (intent.getAction().equals(
						PosService.ACTION_PINPAD_CANCEL)) {
					message.obj = context.getString(R.string.swipe_card);
					message.what = NORMAL_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_PINPAD_CONFIRM)) {
					message.obj = context.getString(R.string.is_loading);
					message.what = SUCCESS_STATE;
					message.arg1 = 0;
				} else if (intent.getAction()
						.equals(PosService.ACTION_MSR_FAIL)) {
					message.obj = context.getString(R.string.swipefail_prompt);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_NO_RESPONSE)) {
					message.obj = context.getString(R.string.trade_fail);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_UNKNOWN_ERROR)) {
					message.obj = context.getString(R.string.unknown_error);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_MAC_ERROR)) {
					message.obj = context.getString(R.string.trade_mac_error);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_WRITE_ERROR)) {
					message.obj = context
							.getString(R.string.trade_network_error);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				} else if (intent.getAction()
						.equals(PosService.ACTION_EMV_FAIL)) {
					message.obj = context.getString(R.string.icfail_trymsr);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_EMV_FALLBACK)) {
					message.obj = context.getString(R.string.roll_back_msr);
					message.what = SUCCESS_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_EMV_RETRY)) {
					// 重刷EMV
					message.obj = context.getString(R.string.emv_retry);
					message.what = SUCCESS_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_EMV_SUCCESS)) {

				} else if (intent.getAction().equals(
						PosService.ACTION_POS_EMV_FAIL)) {
					message.obj = context.getString(R.string.emv_fail);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_EMV_DENY)) {
					message.obj = context.getString(R.string.ic_deny);
					message.what = SUCCESS_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_EMV_INPROCESS)) {
					message.obj = context.getString(R.string.donot_move_ic);
					message.what = SUCCESS_STATE;
					message.arg1 = 0;
				} else if (intent.getAction().equals(
						PosService.ACTION_MSR_INPROCESS)) {

				} else if (intent.getAction().equals(
						PosService.ACTION_MSR_RETRY)) {
					message.obj = context.getString(R.string.msr_retry);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				}
				// else if(intent.getAction().equals(
				// PosService.ACTION_POS_CHECKEMV))
				// {
				// mDialog = new AnyWhereDialog(context, 540, 280, 0, 0,
				// R.layout.check_emv, R.style.Theme_dialog1,
				// Gravity.LEFT | Gravity.TOP, false);
				// Button confirmBtn = (Button) mDialog
				// .findViewById(R.id.have_emv);
				// Button cancelBtn = (Button) mDialog
				// .findViewById(R.id.nothave_emv);
				// confirmBtn.setOnClickListener(MSRBaseButton.this);
				// cancelBtn.setOnClickListener(MSRBaseButton.this);
				// mDialog.show();
				// }
				else if (intent.getAction().equals(PosService.ACTION_ALL_RETRY)) {
					message.obj = context.getString(R.string.emv_move_retry);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_ERROR)) {
					String ackNo = intent.getExtras().getString("ACK_NO");
					if (ackNo.equals("55")) {
						message.obj = context
								.getString(R.string.pass_word_retry);
						message.what = SUCCESS_STATE;
						message.arg1 = 0;
					} else {
						// 显示交易失败原因
						String reason = PosReturnInfoManager
								.getPosReturn(ackNo).getReason();
						if (reason != null && !reason.isEmpty()) {
							message.obj = reason;
						} else {
							message.obj = ackNo;
						}
						message.what = WARNING_STATE;
						message.arg1 = 1;
					}

				} else if (intent.getAction().equals(
						PosService.ACTION_NO_NETWORKERROR))// 交易时无网络连接
				{
					message.obj = context.getString(R.string.no_network_error);
					message.what = WARNING_STATE;
					message.arg1 = 0;
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_SUCCESS)) {
					message.obj = context.getString(R.string.trade_success);
					message.what = SUCCESS_STATE;
					message.arg1 = 0;
				} else if (intent.getAction().equals(
						PosService.ACTION_POS_REVERSE_FAIL)) {
					message.obj = context.getString(R.string.reverse_fail);
					message.what = WARNING_STATE;
					message.arg1 = 1;
				}
				handler.sendMessage(message);
			}
		};
	}

}
