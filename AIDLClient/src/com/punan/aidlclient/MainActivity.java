package com.punan.aidlclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.punan.aidl.example;

public class MainActivity extends Activity {
	
	public static final String TAG = "AIDL_Client";
	
	private Button btnBind;
	private Button btnUnBind;
	private Button btnStart;
	private Button btnStop;
	private Button btnAdd;
	
	private EditText eNum1;
	private EditText eNum2;
	private EditText eNum3;

	example mService;
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.i(TAG, "AIDL Client connect service");
			mService = example.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.i(TAG, "AIDL Client disconnect service");
			mService = null;
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "AIDL Client onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnBind = (Button)findViewById(R.id.btn_bind);
		btnUnBind = (Button)findViewById(R.id.btn_unbind);
		btnStart = (Button)findViewById(R.id.btn_start);
		btnStop = (Button)findViewById(R.id.btn_stop);
		btnAdd = (Button)findViewById(R.id.btn_add);
		
		eNum1 = (EditText)findViewById(R.id.factorOne);
		eNum2 = (EditText)findViewById(R.id.factorTwo);
		eNum3 = (EditText)findViewById(R.id.factorSum);
		
		Bundle args = new Bundle();
		final Intent intent = new Intent();
		intent.setAction("com.punan.aidlservice.MainService");
		
		btnBind.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "btnBind Clicked");
				bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
			}});
		
		btnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "btnStart Clicked");
				startService(intent);
			}});
		
		btnUnBind.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "btnUnBind Clicked");
				unbindService(mConnection);
			}});
		
		btnStop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "btnStop Clicked");
				stopService(intent);
			}});
		
		btnAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "btnAdd Clicked");
				
				int num1 = Integer.parseInt(eNum1.getText().toString());
				int num2 = Integer.parseInt(eNum2.getText().toString());
				Log.i(TAG, "num1 = " + num1 + ", num2 = " + num2);
				int sum = 0;
				
				try{
					sum = mService.add(num1, num2);
				} catch(RemoteException e){
					e.printStackTrace();
				}
				Log.i(TAG, "result = " + sum);
				
				eNum3.setText(sum + "");
			}});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
