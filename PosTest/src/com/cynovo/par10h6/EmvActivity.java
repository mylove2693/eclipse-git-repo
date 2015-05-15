package com.cynovo.par10h6;

import com.kivvi.jni.EmvL2Interface;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmvActivity extends Activity {
	private final static String LOG_TAG = "EmvActivity";
	private TextView tvNo;
	private Button mretry;
	waitevent mwait = null;
	
	private boolean flag = false;
	private String strCardNo = "";
	byte[] cardNo = new byte[20];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emv);	
		tvNo = (TextView) findViewById(R.id.tvSmartCardNo);
		mretry = (Button) findViewById(R.id.retest);
		mretry.setOnClickListener(new Button.OnClickListener()
        { 
				@Override
			    public void onClick(View arg0) {
		    		EmvL2Interface.emvKernelInit();						
		    		EmvL2Interface.openReader();		    		
		    		flag = true;
		    		mwait = new waitevent();
		    		mwait.start();	
		    		tvNo.setText("");
				}
        });
		
		EmvL2Interface.emvKernelInit();					
		EmvL2Interface.openReader();
		
		flag = true;
		mwait = new waitevent();
		mwait.start();	
	}
		
		
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				break;
			case 4:
				trade();		
			case 96:
				tvNo.setText("fail");	
				EmvL2Interface.closeReader();
				break;
			case 120:
				tvNo.setText(strCardNo);
				EmvL2Interface.closeReader();
				break;
			}
		}
	};
	
	class waitevent extends Thread{
		@Override
		public void run() {
			try { 
					while (flag) {
						switch (EmvL2Interface.getCardEvent()) {
						case 0:
						{
							Message msg = mHandler.obtainMessage();
							msg.what = 4;
							msg.sendToTarget();
							flag = false;
							EmvL2Interface.setCardEvent(-1);
							break; 
						}
					}
				} 
			}catch (Exception e) {
			}
		}
	}	
	
	/**
	 * @return
	 */
	private int trade() {
				
		Message msg = mHandler.obtainMessage();
		
		int ret = EmvL2Interface.tradePrepare();
		if (ret < 0) {
			msg = mHandler.obtainMessage();
			msg.what = 96;
			msg.sendToTarget();
			return -3;
		}
		
		int cardNoLen = EmvL2Interface.getTagValue((short)0x5A, cardNo);
		strCardNo = Utility.Bcd2Ascii2(cardNo, cardNoLen);
		Log.i(LOG_TAG, strCardNo);
		if (strCardNo.substring(strCardNo.length() - 1, strCardNo.length()).equals("F")) {
			strCardNo = strCardNo.substring(0, strCardNo.length() - 1);
			Log.i(LOG_TAG, "No1:" + strCardNo);
		} else {
			Log.i(LOG_TAG, "No2:" + strCardNo);
		}
		
		msg = mHandler.obtainMessage();
		msg.what = 120;
		msg.sendToTarget();
		
		return 0;
	}
}
