package com.cynovo.par10h6;

import com.kivvi.jni.MsrInterface;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MsrActivity extends Activity implements OnClickListener {
	private static final String LOG_TAG = "[MsrActivity]";

	private Button msrBtn = null;
	private TextView mTextTrack1 = null;
	String[] ERRCODE = { "NOERROR", "ERR:DATA INCOMPLETE", "ERR:DATA", "ERR:LRC", "ERR:PARITY",
			"UNKNOW ERR", "TRACK TOO SHORT", "TRACK TOO LONG",  };

	waitevent mevent = null; 
	Handler  Handler = null;
	private  SoundPool soundPool = null;
	private  SoundPool soundPoolding = null;  
	private  boolean mStartFlag = false;
	private  AnimationDrawable mAnimDra = null;	
	
	private int start_MSR(){
		int ret = MsrInterface.open();
		if(ret <0){
			mTextTrack1.setText("Open msr fail");
			return -1;
		}
		mStartFlag = true;
		mTextTrack1.setText("");
		mevent = new waitevent();
		mevent.start();
		mAnimDra.start();
		return 0;
	}
	private int stop_MSR(){
		mStartFlag = false;
		try {
	        Thread.sleep(200);
		} catch (InterruptedException e) {
	        e.printStackTrace();
		}	
		mevent = null;
		mAnimDra.stop();
		MsrInterface.close();
		return 0;
	}
	
	class waitevent extends Thread{
		@Override
		public void run() {
			int event = 0;
			while (mStartFlag) {
				event = MsrInterface.poll();
				if(event == 0){
					Message msg = new Message();	
					msg.what = event;
					Handler.sendMessage(msg);
					break;
				}
			}
		}	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(LOG_TAG,"\n\n onCreate \n\n");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msr);	
		msrBtn = (Button) findViewById(R.id.msr_back);
		msrBtn.setOnClickListener(this);
		msrBtn.setBackgroundResource(R.drawable.swip_card_btn_cover);
		mAnimDra = (AnimationDrawable) msrBtn.getBackground();	
		
		mTextTrack1 = (TextView) findViewById(R.id.msr_show_1_trcak);		
        soundPoolding = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(MsrActivity.this, R.raw.swipe_us, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
				soundPool.play(1, 1, 1, 0, 0, 1);
			}
		}); 		
		
		Handler = new Handler(){	    
	    	public void handleMessage(Message msg){
	    		switch(msg.what){
	    		case 0x00:
	    			deal_with_data();
	    			break;
	    		}
	    		super.handleMessage(msg);
	    	}
	    };

	}

	void deal_with_data(){
		String str = null;	
		String track0 = readTrack(0);
		String track1 = readTrack(1);
		String track2 = readTrack(2);
		
		mTextTrack1.setText("DATA" + "\nTrack0:" + track0  + "\nTrack1:" + track1  + "\nTrack2:" + track2); 			
		
		playmusic();
		stop_MSR();

	}

	void playmusic(){
		soundPoolding.load(MsrActivity.this,R.raw.ding, 1);
		soundPoolding.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
				soundPoolding.play(1, 1, 1, 0, 0, 2);
			}
		});			
	}

		private String readTrack(int trackNo) {
			int length = MsrInterface.getTrackDataLength(trackNo);
			Log.i("track" + String.valueOf(trackNo + 1) + " length",
					String.valueOf(length));
			byte track[] = new byte[255];

			if (MsrInterface.getTrackData(trackNo, track, 255) >= 0) {
				if (length >= 0) {
					String str = new String(track, 0, length);
					Log.i("read track " + String.valueOf(trackNo + 1), str);
					return str;
				}
				return "";
			} else {
				Log.i("read track " + String.valueOf(trackNo + 1), "error");
				return "";
			}
		}	
		
	@Override
	public void onClick(View v) {
        switch (v.getId()) {
        	case R.id.msr_back:
        			Log.v("MSR ACTIVITY BUTTON EVENT","BUTTON");
        			if(!mStartFlag){
        				start_MSR();
            			Log.v("MSR ACTIVITY BUTTON EVENT"," start ");        				
        			}
        			else{
            			Log.v("MSR ACTIVITY BUTTON EVENT"," end ");        				
        				stop_MSR();
        			}
            break;
        default:
            break;
        }			
	}
		
	@Override
	public void onDestroy() {
		if(mStartFlag)
			stop_MSR();
		
		MsrInterface.close();		
		Log.v(LOG_TAG,"\n\n onDestroy \n\n");	
		super.onDestroy();
	}
}