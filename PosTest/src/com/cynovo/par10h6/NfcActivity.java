package com.cynovo.par10h6;

import com.kivvi.jni.NfcL2Interface;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NfcActivity extends Activity {
	private final static String LOG_TAG = "EmvActivity";
	private TextView tvNo;
	private Button mretry;	
	private boolean flag = false;
	private String strCardNo = "";
	byte[] cardNo = new byte[20];
	byte[] state = new byte[1];
	Thread thread1 = null;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);

		tvNo = (TextView) findViewById(R.id.tvContactlessCardNo);
		mretry = (Button) findViewById(R.id.retest);
		
		mretry.setOnClickListener(new Button.OnClickListener()
        { @Override
		    public void onClick(View arg0) {
	    		NfcL2Interface.loadKernel();
	    		flag = true;
	    		thread1 = new waitevent();	    		
	    		thread1.start();
	    		tvNo.setText("");
	    		tradeStart();	    		
        	}
	    });	

		NfcL2Interface.loadKernel();
		flag = true;
		thread1 = new waitevent();
		thread1.start();

		tradeStart();
	}

	class waitevent extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (flag) {
					switch (NfcL2Interface.getCardEvent()) {
						case 0:
						{
							Log.i(LOG_TAG, "found contactless card");
							Message msg = mHandler.obtainMessage();
							msg.what = 0;
							msg.sendToTarget();
							
							flag = false;
							NfcL2Interface.setCardEvent(-1);
							break;
						}
						case 1:
						{
							Message msg = mHandler.obtainMessage();
							msg.what = 1;
							msg.sendToTarget();
							flag = false;
							NfcL2Interface.setCardEvent(-1);
							break;
						}
						case 2:
						{
							Message msg = mHandler.obtainMessage();
							msg.what = 2;
							msg.sendToTarget();
							flag = false;
							NfcL2Interface.setCardEvent(-1);
							break;
						}

						case 3:
						{
							Message msg = mHandler.obtainMessage();
							msg.what = 3;
							msg.sendToTarget();
							flag = false;
							NfcL2Interface.setCardEvent(-1);
							break;
						}
						case 4: 
						{
							Message msg = mHandler.obtainMessage();
							msg.what = 24;
							msg.sendToTarget();
							flag = false;
							NfcL2Interface.setCardEvent(-1);

							break;
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}	
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
 				readCardNo();
 			} else if (msg.what == 4) {
				tvNo.setText(strCardNo);
			}else{
				tvNo.setText("读卡失败");
			}
		}
	};	

	private void tradeStart() {
		NfcL2Interface.rfReaderInit();
		NfcL2Interface.emvKernelInit(1);

		Log.i(LOG_TAG, "rfReaderActiveCard		start");
		int ret = NfcL2Interface.rfReaderActiveCard(60000);
		Log.i(LOG_TAG, "rfReaderActiveCard		end");
		if (ret < 0) {
			NfcL2Interface.rfReaderRelease();
		}
	}

	private int readCardNo() {
		int ret = NfcL2Interface.appSelect((char) 1);
		Log.i(LOG_TAG, "ret = " + ret);
		if (ret != 0) {
			Log.i(LOG_TAG, "app seletc failed");
			return -1;
		}

		ret = NfcL2Interface.qpbocAppInit(state);
		if (ret != 0) {
			Log.i(LOG_TAG, "qpboc app init failed");
			return -2;
		}

		ret = NfcL2Interface.readAppData((char) 1);

		int cardNoLen = NfcL2Interface.getTagValue((short) 0x57, cardNo);
		strCardNo = Utility.Bcd2Ascii2(cardNo, cardNoLen);
		strCardNo = strCardNo.substring(0, 19);
		Log.i(LOG_TAG, strCardNo);
		Message msg = mHandler.obtainMessage();
		msg.what = 4;
		msg.sendToTarget();

		NfcL2Interface.rfReaderRelease();

		return 0;
	}
}
