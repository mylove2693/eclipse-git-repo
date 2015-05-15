package com.cynovo.par10h6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kivvi.jni.SnInterface;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends Activity implements OnClickListener {
	private Button btnMSR, btnEMV, btnNFC,btnNFCmifare,btnprinter,btnpinpad,btnled,btnrandom;
	private TextView textsn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textsn = (TextView)findViewById(R.id.sn);			
		
		btnMSR = (Button) findViewById(R.id.btnMSR);
		btnEMV = (Button) findViewById(R.id.btnEMV);
		btnNFC = (Button) findViewById(R.id.btnNFC);
		btnNFCmifare = (Button) findViewById(R.id.btnNFCmifare);		
		btnprinter = (Button) findViewById(R.id.btnprinter);	
		btnpinpad = (Button) findViewById(R.id.btnpinpad);
		btnled = (Button) findViewById(R.id.btnled);		
		btnrandom = (Button) findViewById(R.id.btnrandom);				
		
		btnMSR.setOnClickListener(this);
		btnEMV.setOnClickListener(this);
		btnNFC.setOnClickListener(this);
		btnNFCmifare.setOnClickListener(this);
		btnprinter.setOnClickListener(this);
		btnpinpad.setOnClickListener(this);
		btnled.setOnClickListener(this);	
		btnrandom.setOnClickListener(this);	
					
		copyAssets("aid_param.ini", "/mnt/sdcard/aid_param.ini");
		copyAssets("ca_param.ini", "/mnt/sdcard/ca_param.ini");
		copyAssets("qpboc_param.ini", "/mnt/sdcard/qpboc_param.ini");
		copyAssets("ter_param.ini", "/mnt/sdcard/ter_param.ini");
		
		getserialno();	
	}
	
	void getserialno(){
		byte serialno[] = new byte[32];
		int ret = SnInterface.getSN(serialno);
		
		if (ret >= 0) {
				String str = new String(serialno, 0, ret);
				textsn.setText("        SN:" + str);				
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btnMSR:	
			intent = new Intent();			
			intent.setClass(MainActivity.this, MsrActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btnEMV:
			intent = new Intent();
			intent.setClass(MainActivity.this, EmvActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btnNFC:
			intent = new Intent();
			intent.setClass(MainActivity.this, NfcActivity.class);
			startActivity(intent);
			break;	
		case R.id.btnNFCmifare:
			intent = new Intent();
			intent.setClass(MainActivity.this, MifareActivity.class);
			startActivity(intent);
			break;				
		case R.id.btnprinter:
			intent = new Intent();
			intent.setClass(MainActivity.this, PrinterActivity.class);
			startActivity(intent);
			break;	
		case R.id.btnpinpad:
			intent = new Intent();
			intent.setClass(MainActivity.this, PinpadActivity.class);
			startActivity(intent);
			break;		
		case R.id.btnled:
			intent = new Intent();
			intent.setClass(MainActivity.this, LedActivity.class);
			startActivity(intent);
			break;		
		case R.id.btnrandom:
			intent = new Intent();
			intent.setClass(MainActivity.this, RandomActivity.class);
			startActivity(intent);
			break;			
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param filename
	 * @param dest
	 */
	private void copyAssets(String filename, String dest) {
		// String filePath = fileDirPath + "/DCIM/" + filename;

		try {
			if (new File(dest).exists()) {
				return;
			}

			File outFile = new File(dest);
			File pdir = new File(outFile.getParent());
			if (!pdir.exists())
				pdir.mkdirs();
			InputStream is = getAssets().open(filename);
			OutputStream os = new FileOutputStream(outFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
			os.flush();
			os.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
