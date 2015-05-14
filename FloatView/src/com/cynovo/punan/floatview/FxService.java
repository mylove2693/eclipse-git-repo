package com.cynovo.punan.floatview;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FxService extends Service {

	//定义浮动窗口
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	
	WindowManager mWindowManager;
	
	ImageView mFloatView;
	
	private static final String TAG = "FxService";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		createFloatView();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void createFloatView(){
		wmParams = new WindowManager.LayoutParams();
		mWindowManager = (WindowManager) getApplication().
				getSystemService(getApplication().WINDOW_SERVICE);
		
		Log.i(TAG, "mWindowManager--->" + mWindowManager);
		
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		
		wmParams.x = 0;
		wmParams.y = 0;
		
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		
		LayoutInflater inflater = LayoutInflater.from(getApplication());
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
		mWindowManager.addView(mFloatLayout, wmParams);
		
		mFloatView = (ImageView)mFloatLayout.findViewById(R.id.image);
		
		mFloatLayout.measure(View.MeasureSpec.
				makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 
				View.MeasureSpec.
				makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);  
	    Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2); 
		
	    mFloatView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.v(TAG, "onTouch");
				
				wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;
				wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2;
				
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				
				return false;
			}});
	    
		mFloatView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "onClick");
				ExpandStatusBar();
			}});
	}
	
	
	public void ExpandStatusBar() {
		try {
			Object service = getSystemService("statusbar");
			Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
			Method expand = statusbarManager.getMethod("expandNotificationsPanel");
			expand.invoke(service);

		} catch (Exception ex) {
			Log.e(TAG, "Exception on Expand Status Bar "+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mFloatLayout != null){
			mWindowManager.removeView(mFloatLayout);
		}
	}
}
