package com.cynovo.par10h6;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.kivvi.jni.HsmInterface;

public class RandomActivity extends Activity  implements OnClickListener {
	private static final String LOG_TAG = "[RandomTest]";
	private TextView tvRandom;
	private ImageView imgQRCodeRandom;
	private Button btnGenRandom;
	private boolean safeOpenFlag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_random);	
		
		tvRandom = (TextView) findViewById(R.id.tvRandom);
		imgQRCodeRandom = (ImageView) findViewById(R.id.QRCodeRandom);
		btnGenRandom = (Button) findViewById(R.id.btnGenRandom);
		
		btnGenRandom.setOnClickListener(this);

		int ret = HsmInterface.open();
		if (ret < 0) {
			Toast.makeText(RandomActivity.this, "safe open fail",
					Toast.LENGTH_SHORT).show();
			safeOpenFlag = false;
		} else {
			Toast.makeText(RandomActivity.this, "safe open succ",
					Toast.LENGTH_SHORT).show();
			safeOpenFlag = true;
		}
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (!safeOpenFlag)
			return;

		byte[] random = new byte[8];
		int ret = HsmInterface.getRandom(random, 8);
		if (ret < 0) {
			Toast.makeText(RandomActivity.this, "generate Random fail",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Bitmap image = null;
		try {
			String qrContent = bcd2Str(random, random.length);
			tvRandom.setText("Random Values：" + qrContent.toUpperCase());
			image = QRCode.createTwoQRCode(qrContent);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (image != null) {
			imgQRCodeRandom.setImageBitmap(image);
		}
	}	
	
	public static String bcd2Str(byte[] bcd, int len) {
		StringBuilder sb = new StringBuilder(len * 2);
		for (int i = 0; i < len; i++) {
			sb.append(String.format("%02x", bcd[i]));
		}
		return sb.toString();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		int ret = HsmInterface.close();
		if (ret < 0) {
			Log.i(LOG_TAG, "safe module close failed!");
		} else {
			Log.i(LOG_TAG, "safe module close succ!");
		}
	}	
}