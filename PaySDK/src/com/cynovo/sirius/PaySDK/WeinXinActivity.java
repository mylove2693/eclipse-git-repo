package com.cynovo.sirius.PaySDK;

//import com.cynovo.sirius.SaleActivity;
//import com.cynovo.sirius.constants.MyConstants;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.cynovo.sirius.util.InterfacePara;
import com.cynovo.sirius.util.WeiXinPay;
import com.google.zxing.client.android.CaptureActivity;

public class WeinXinActivity extends Activity {

	public static boolean PAYCHECK_OVER;
	// public static boolean PAYC_SUCCESS;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("debug", "WeinXinActivity onCreate begin");
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.e("debug", "WeinXinActivity onCreate begin point1");
		setContentView(R.layout.activity_weixinpay);

		Log.e("debug", "WeinXinActivity onCreate begin point1");

		handler = new Handler() {
			public void handleMessage(Message msg) {
				int msgwhat = msg.what;
				if (msgwhat == 1) {
					Log.e("debug", "WeinXinActivity receive finish message");
					CaptureActivity.PAY_OVER = true;
					WeinXinActivity.this.finish();
				}
				super.handleMessage(msg);
			}
		};

		handler.postDelayed(WeixinPaycheck, 100);
		Log.e("debug", "WeinXinActivity onCreate end");
	}

	Runnable WeixinPaycheck = new Runnable() {
		@Override
		public void run() {
			int count = 3;
			Log.e("debug", "WeixinPaycheck started");
			boolean success;
			success = false;
			String strtemp = CaptureActivity.strBarCode;
			int ret = WeiXinPay.weixinPay1(InterfacePara.strAccountWeixin, strtemp, WeinXinActivity.this);
			if (ret != 1) {
				for (int i = 0; i < count; i++) {

					ret = WeiXinPay.QueryOrder1(WeiXinPay.last_out_trade_no);

					if (ret == 1) {
						Log.e("debug", "QueryOrder success payed");
						success = true;
						MainService.PAYC_SUCCESS = true;
						break;
					} else {
						Log.e("debug", "QueryOrder pay not successful");
						try {
							Thread.sleep(10 * 1000);
						} catch (InterruptedException e) {
							Log.e("debug", "QueryOrder Failed sleep");
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else {
				success = true;
				MainService.PAYC_SUCCESS = true;
			}

			if (!success) {
				// WeiXinPay.ReverseOrder(WeiXinPay.last_out_trade_no,
				// WeinXinActivity.this);
				WeiXinPay.ReverseOrder1(WeiXinPay.last_out_trade_no);
				// return null;
			}

			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
			Log.e("debug", "WeixinPaycheck end");
			PAYCHECK_OVER = true;

			MainService.PAYC_END = true;
		}
	};
}
