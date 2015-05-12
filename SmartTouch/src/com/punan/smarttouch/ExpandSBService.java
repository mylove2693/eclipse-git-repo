package com.punan.smarttouch;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ExpandSBService extends Service {
	public static String TAG = "SmartTouch ExpandSBService";
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		ExpandStatusBar();
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void ExpandStatusBar() {
		try {
			Object service = getSystemService("statusbar");
			Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
			Method expand = statusbarManager.getMethod("expandNotificationsPanel");
			expand.invoke(service);

		} catch (Exception ex) {
			Log.e(TAG, "Exception on Expand Status Bar "+ex.getMessage());
		}
	}
	
}
