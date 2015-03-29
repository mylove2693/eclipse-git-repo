package com.punan.aidlservice;

import com.punan.aidl.example;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MainService extends Service{
	
	private static final String TAG = "AIDL_Service";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "service on create");
		super.onCreate();
	}


	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "service on start id = "+startId);
		super.onStart(intent, startId);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "service on startcommand id = " + startId);
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "service on destory");
		super.onDestroy();
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "service on bind");
		return mBinder;
	}
	
	
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "service on unbind");
		return super.onUnbind(intent);
	}


	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "service on rebind");
		super.onRebind(intent);
	}



	private final example.Stub mBinder = new example.Stub() {
		
		@Override
		public int add(int a, int b) throws RemoteException {
			// TODO Auto-generated method stub
			Log.i(TAG, "add a = " + a + ", b = " + b);
			return a+b;
		}
	};

}
