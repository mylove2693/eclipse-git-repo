package com.huaqin.punan.testandroid;

import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView mytv;
	TelephonyManager mTelephonyManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mytv = (TextView)findViewById(R.id.mytv);
		  try{
			     ApplicationInfo ai = getPackageManager().getApplicationInfo("com.sifong.Anyhealth", 0);
			     ZipFile zf = new ZipFile(ai.sourceDir);
			     ZipEntry ze = zf.getEntry("classes.dex");
			     long time = ze.getTime();
			     String s = SimpleDateFormat.getInstance().format(new java.util.Date(time));
			     zf.close();
			     Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
					mytv.setText(s);
			  }catch(Exception e){
				  Toast.makeText(this, "Exception:"+e, Toast.LENGTH_SHORT).show();
			  }

		NoLooperThread mT = new NoLooperThread();
		//mT.start();
		
		mTelephonyManager = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);

		if (mTelephonyManager.getCallState() == TelephonyManager.CALL_STATE_RINGING
				|| mTelephonyManager.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {

		}

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

	class NoLooperThread extends Thread {

		@Override
		public void run() {

			{
				Handler handler = new Handler();
				handler.sendEmptyMessage(1);
			}
		}
	}

}
