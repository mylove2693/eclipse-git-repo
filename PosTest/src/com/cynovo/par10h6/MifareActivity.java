package com.cynovo.par10h6;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.kivvi.jni.ContactlessEvent;
import com.kivvi.jni.ContactlessInterface;
import com.cynovo.par10h6.Utility;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MifareActivity extends Activity implements OnClickListener {
	private static final String LOG_TAG = "[NfcMifareActivity]";

	private TextView tvText;
	private Button btnSearchBegin, btnSearchStop, btnVerify, btnWriteMifare,
			btnReadMifare, btnOpen, btnClose;
	private Button btnSetIQ;
	private EditText etWriteData;
	private TextView etReadData;
	private boolean bOpenFlag = false;
	private boolean SearchThreadFlag = false;
	private static final int sectorIndex = 1;
	private static final int blockIndex = 0;
	private TextView tvCardEvent;
	
	private static final int iSectorId =2;
	private static final int iBlockId = 2;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mifare);

		tvText = (TextView) findViewById(R.id.tvLog);
		btnOpen = (Button) findViewById(R.id.btnContactlessOpen);
		btnClose = (Button) findViewById(R.id.btnContactlessClose);
		btnSearchBegin = (Button) findViewById(R.id.btnSearchBegin);
		btnSearchStop = (Button) findViewById(R.id.btnSearchStop);
		btnVerify = (Button) findViewById(R.id.btnVerify);
		btnWriteMifare = (Button) findViewById(R.id.btnWriteMifare);
		btnReadMifare = (Button) findViewById(R.id.btnReadMifare);

		etWriteData = (EditText) findViewById(R.id.etWriteMifare);
		etWriteData.setText("12345678123456781234567812345678");
		etReadData = (TextView) findViewById(R.id.etReadMifare);


		btnOpen.setOnClickListener(this);
		btnClose.setOnClickListener(this);
		btnSearchBegin.setOnClickListener(this);
		btnSearchStop.setOnClickListener(this);
		btnVerify.setOnClickListener(this);
		btnWriteMifare.setOnClickListener(this);
		btnReadMifare.setOnClickListener(this);
		
		tvCardEvent = (TextView) findViewById(R.id.tvCardEventMifare);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnContactlessOpen:
			openContactlessCard();
			break;

		case R.id.btnContactlessClose:
			closeContactlessCard();
			break;

		case R.id.btnSearchBegin:
			searchTargetBegin();
			break;

		case R.id.btnSearchStop:
			searchTargetStop();
			break;

		case R.id.btnVerify:
			verifyCard();
			break;

		case R.id.btnWriteMifare:
			writeMifare();
			break;

		case R.id.btnReadMifare:
			readMifare();
			break;
			
		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.arg1 == 2) {
				return;
			}
			Log.i("APP", "get thread result ....\n");
			Bundle bundle = msg.getData();
			int nEventID = bundle.getInt("nEventID");
			int nEventDataLength = bundle.getInt("nEventDataLength");
			byte arryEventData[] = bundle.getByteArray("arryEventData");
			if (nEventID == 0 && nEventDataLength > 0) {
				String strDisplay = new String();
				for (int i = 0; i < nEventDataLength; i++)
					strDisplay += String.format("%02X ", arryEventData[i]);
				tvText.append("\n\t\tSearch Target Successful " + strDisplay);

				Toast.makeText(getApplicationContext(), "Search Target Successful", Toast.LENGTH_SHORT).show();
			} else if (nEventID == 2) {
				Toast.makeText(getApplicationContext(), "Search Target Failed", Toast.LENGTH_SHORT).show();
			}

		}
	};

	/**
	 * open contactless card
	 */
	private void openContactlessCard() {
		tvText.setText("");
		etReadData.setText("");
		
		int ret = ContactlessInterface.open();
		if (ret < 0) {
			Log.i(LOG_TAG, "contactless card open failed");
			Toast.makeText(getApplicationContext(), "Contactless Reader Open Failed",
					Toast.LENGTH_SHORT).show();
			bOpenFlag = false;
		} else {
			Toast.makeText(getApplicationContext(), "Contactless Reader Open Successful",
					Toast.LENGTH_SHORT).show();
			bOpenFlag = true;
			Log.i(LOG_TAG, "contactless card open succ");
		}
	}

	/**
	 * close contactless card
	 */
	private void closeContactlessCard() {
		tvText.setText("");
		etReadData.setText("");
		
		int ret = ContactlessInterface.close();
		if (ret < 0) {
			Toast.makeText(getApplicationContext(), "Contactless Reader Close Failed",
					Toast.LENGTH_SHORT).show();
			Log.i(LOG_TAG, "contactless card close failed");
		} else {
			Toast.makeText(getApplicationContext(), "Contactless Reader Close Successful",
					Toast.LENGTH_SHORT).show();
			Log.i(LOG_TAG, "contactless card close succ");
		}
	}

	/**
	 * search target begin
	 */
	private void searchTargetBegin() {
		if (!bOpenFlag)
			return;
		SearchThreadFlag = true;

		Thread thread = new Thread() {

			@Override
			public void run() {

				while (SearchThreadFlag) {
					ContactlessEvent event = new ContactlessEvent();
					int nReturn = -1;
					nReturn = ContactlessInterface.pollEvent(-1, event);

					if (nReturn >= 0) {
						SearchThreadFlag = false;
						NotifyEvent(event);
					}

				}
			}

		};
		thread.start();

		// searchItem();
		int ret = ContactlessInterface.searchTargetBegin(
				ContactlessInterface.CONTACTLESS_CARD_MODE_AUTO, 1, -1);
		if (ret >= 0) {
			Log.i(LOG_TAG, "searchTargetBegin() succ");
		} else {
			Log.i(LOG_TAG, "searchTargetBegin() fail");
		}
	}

	/**
	 * search target stop
	 */
	private void searchTargetStop() {
		if (!bOpenFlag)
			return;

		SearchThreadFlag = false;;
		ContactlessInterface.searchTargetEnd();
	}

	/**
	 * verify card
	 */
	private void verifyCard() {
		if (!bOpenFlag)
			return;

		boolean res = verifyPinMemory();
		if (!res) {
			Toast.makeText(getApplicationContext(), "Card Verify Failed", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(getApplicationContext(), "Card Verify Successful", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * write data into mifare card
	 */
	private void writeMifare() {
		if (!bOpenFlag)
			return;

		String strWriteMifare = etWriteData.getText().toString().trim();

		if (strWriteMifare == null || strWriteMifare.equals("")) {
			Toast.makeText(getApplicationContext(), "Please Input Data",
					Toast.LENGTH_SHORT).show();
			return;
		}

		byte[] bWriteMifare = Utility.Str2Bcd(strWriteMifare);

		if (!write(iSectorId, iBlockId, bWriteMifare)) {
			Toast.makeText(getApplicationContext(), "Write Data Failed",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Write Data Successful",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * read data from the mifare card
	 */
	private void readMifare() {
		if (!bOpenFlag)
			return;

		String strReadMifare = null;
		Log.i(LOG_TAG, "====iSectorId : " + iSectorId + "==iBlockId : "
				+ iBlockId);

		strReadMifare = read(iSectorId, iBlockId);
		if (strReadMifare == null) {
			Toast.makeText(getApplicationContext(), "Read Data Failed",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			Toast.makeText(getApplicationContext(), "Read Data Successful",
					Toast.LENGTH_SHORT).show();
			etReadData.setText(strReadMifare);
		}
	}

	/**
	 * 
	 * @param event
	 */
	private void NotifyEvent(ContactlessEvent event) {
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putInt("nEventID", event.nEventID);
		bundle.putInt("nEventDataLength", event.nEventDataLength);
		bundle.putByteArray("arryEventData", event.arryEventData);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}

	/**
	 * 
	 * @param str
	 */
	private void NotifyMessage(String str) {
		Message msg = new Message();
		msg.arg1 = 2;
		msg.obj = str;
		handler.sendMessage(msg);
	}

	/**
	 * 
	 * @param arryEventData
	 */
	public void testVerify(byte arryEventData[]) {
		boolean isMiFare = false;
		// 0x08 : Type A, 0x18 : Type B
		if (arryEventData[0] == 0x08 || arryEventData[0] == 0x18) {
			isMiFare = true;
		}
		if (isMiFare) {
			boolean isSuccess = verifyPinMemory();

			if (isSuccess) {
				tvText.append("\n\t\tVerify PIN Successful");
				String str = read(sectorIndex, blockIndex);
				tvText.append("\n\t\t " + str);
				byte[] bytes = new byte[] { 0x00, (byte) 0x01, 0x03, 0x03,
						(byte) 0x04 };
				tvText.append("\n\t 0x00, 0x01, 0x02, 0x03, 0x04");
				write(sectorIndex, blockIndex, bytes);
				String str2 = read(sectorIndex, blockIndex);
				tvText.append("\n\t\t" + str2);
			} else {
				tvText.append("\n\t\t");
			}

		}
	}

	/**
	 * verify pin memory
	 * @return
	 */
	private boolean verifyPinMemory() {
		boolean isSuccess = false;	
		String strKeyA = "FFFFFFFFFFFF";
		byte[] bKeyA = Utility.Str2Bcd(strKeyA);
				
		int result = ContactlessInterface.verifyPinMifare(iSectorId, 0x0A, bKeyA, bKeyA.length);//校验第二扇区
				
		Log.d(LOG_TAG, "virify [" + "][" + "]result =" + result + ", bytes.length = " + bKeyA.length);
		if (result >= 0) {
			isSuccess = true;
		}
			
		return isSuccess;
	}

	/**
	 * 
	 * @param sectorIndex
	 * @param blockIndex
	 * @return
	 */
	private String read(int sectorIndex, int blockIndex) {
		byte[] pDataBuffer = new byte[16];
		int result = ContactlessInterface.readMifare(sectorIndex, blockIndex,
				pDataBuffer, pDataBuffer.length);
		String temp = null;
		if (result >= 0) {
			for (byte b : pDataBuffer) {
				temp = temp + " " + b;
			}
			temp = getFormatString(pDataBuffer);
		}
		Log.i(LOG_TAG, "ReadMemory " + temp + ", result = " + result);
		return temp;
	}

	/**
	 * 
	 * @param sectorIndex
	 * @param blockIndex
	 * @param str
	 * @return
	 */
	private boolean write(int sectorIndex, int blockIndex, byte[] str) {
		boolean isSuccess = false;
		byte[] bytes = str;
		int result = ContactlessInterface.writeMifare(sectorIndex, blockIndex,
				bytes, bytes.length);
		Log.d(LOG_TAG, "write result = " + result);
		if (result >= 0) {
			isSuccess = true;
			Log.i(LOG_TAG, "write succ");
		} else {
			Log.i(LOG_TAG, "write fail");
		}

		return isSuccess;
	}
	
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private String getFormatString(byte[] bytes) {
		String value = "";
		for (byte b : bytes) {
			value += String.format("%02X ", b);
		}
		return value;
	}
	
	@Override
	public void onDestroy() {
		if(bOpenFlag)
		{
			if(SearchThreadFlag)
			{
				searchTargetStop();			
			}
			closeContactlessCard();			
		}
		
		Log.v(LOG_TAG,"\n\n onDestroy \n\n");	
		super.onDestroy();
	}	

}