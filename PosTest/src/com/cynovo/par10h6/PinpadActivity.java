package com.cynovo.par10h6;

import com.kivvi.jni.PinPadInterface;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class PinpadActivity extends Activity implements OnClickListener {
	private static final String LOG_TAG = "[PINPAD-TEST]";
	private Button btnShowText;
	// private Button button_encrypt_text;
	private Button btnCalPinblock;
	private Button btnCalMAC;
	private Button btnUpdatePinblock;
	private Button btnUpdateMAC;
	private Button btnUpdateMasterKey;

	private EditText etTextDataLine1;
	private EditText etTextDataLine2;
	private EditText etMacInput;
	private EditText etMacOutput;
	private EditText etMasterKeyInput;
	private EditText etPinblockInput;
	private EditText etMacKeyInput;
	private EditText etCardNo;
	private EditText etMacType;
	private EditText etCalPinblockOutput;

	private TextView tvUserKeyID;
	private Spinner spMasterKeyID, spUserKeyID;

	private static Integer[] arrayMasterKeyId = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
			27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
			44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
			61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77,
			78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94,
			95, 96, 97, 98, 99 };

	private static Integer[] arrayUserKeyId = { 0, 1 };

	private ArrayAdapter<Integer> adapterMasterKeyID = null;
	private ArrayAdapter<Integer> adapterUserKeyID = null;

	private int iMasterKeyID = 0;
	private int iUserKeyID = 0;

	private boolean openFlag = false;
	private boolean show_text_flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinpad);

		// TextView
		tvUserKeyID = (TextView) findViewById(R.id.tvUserKeyID);

		// Spinner
		spMasterKeyID = (Spinner) findViewById(R.id.spMasterKeyID);
		spUserKeyID = (Spinner) findViewById(R.id.spUserKeyID);

		adapterMasterKeyID = new ArrayAdapter<Integer>(PinpadActivity.this,
				android.R.layout.simple_spinner_item, arrayMasterKeyId);
		adapterMasterKeyID
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapterUserKeyID = new ArrayAdapter<Integer>(PinpadActivity.this,
				android.R.layout.simple_spinner_item, arrayUserKeyId);
		adapterUserKeyID
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spMasterKeyID.setAdapter(adapterMasterKeyID);
		spMasterKeyID.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				iMasterKeyID = arrayMasterKeyId[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spUserKeyID.setAdapter(adapterUserKeyID);
		spUserKeyID.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				iUserKeyID = arrayUserKeyId[arg2];

				if (iUserKeyID == 0) {
					tvUserKeyID.setText("pin key");
				} else if (iUserKeyID == 1) {
					tvUserKeyID.setText("encrypt key");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// button
		btnShowText = (Button) findViewById(R.id.button_show_text);
		btnCalPinblock = (Button) findViewById(R.id.button_pinblock);
		btnCalMAC = (Button) findViewById(R.id.button_mac);
		btnUpdatePinblock = (Button) findViewById(R.id.button_update_pinblock);
		btnUpdateMAC = (Button) findViewById(R.id.button_update_mac);
		btnUpdateMasterKey = (Button) findViewById(R.id.button_master);

		// edit text
		etTextDataLine1 = (EditText) findViewById(R.id.editText_text_data1);
		etTextDataLine1.setText("abcdefg");
		etTextDataLine2 = (EditText) findViewById(R.id.editText_text_data2);
		etTextDataLine2.setText("12345678");
		etMasterKeyInput = (EditText) findViewById(R.id.editText_master_key);
		//etMasterKeyInput.setText("2F40FE287F770C08A8318333CA53DD6B");
		etMasterKeyInput.setText("6883E9F22673DC0234D9A7CE076DE0C4");
		etPinblockInput = (EditText) findViewById(R.id.editText_pinblock);
		//etPinblockInput.setText("12345678123456781234567812345678");
		etPinblockInput.setText("549ba2bac75245b9e462a31916cbc715");
		etMacKeyInput = (EditText) findViewById(R.id.editText_mac_key);
		//etMacKeyInput.setText("87654321876543218765432187654321");
		etMacKeyInput.setText("68A47A3BFBD5D375");
		etMacType = (EditText) findViewById(R.id.editText_mac_type);
		etMacType.setText("Unionpay ECB");
		etMacInput = (EditText) findViewById(R.id.editText_mac_data);
		//etMacInput.setText("12345678123456781234567812345678");
		//etMacInput.setText("4C99C5442091E190");
		etMacInput.setText("0000000000000000");
		etCardNo = (EditText) findViewById(R.id.editText_card_no);
		etCardNo.setText("6222612345640417847");
		etCalPinblockOutput = (EditText)findViewById(R.id.editText_pinblock_out);
		etMacOutput = (EditText) findViewById(R.id.editText_mac_out);

		// set on click listener
		btnShowText.setOnClickListener(new ButtonListener());
		btnCalPinblock.setOnClickListener(new ButtonListener());
		btnCalMAC.setOnClickListener(new ButtonListener());
		btnUpdateMasterKey.setOnClickListener(new ButtonListener());
		btnUpdatePinblock.setOnClickListener(new ButtonListener());
		btnUpdateMAC.setOnClickListener(new ButtonListener());

		// when the activity is onCreate, open the pinpad module
		int ret = PinPadInterface.open();
		if (ret >= 0) {
			openFlag = true;
			Toast.makeText(PinpadActivity.this, "open pinpad succ",
					Toast.LENGTH_SHORT).show();
		} else {
			openFlag = false;
			Toast.makeText(PinpadActivity.this, "open pinpad fail",
					Toast.LENGTH_SHORT).show();
		}

	}

	private class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			// show text
			case R.id.button_show_text:
				showText();
				break;

			// pinblock
			case R.id.button_pinblock:
				calculatePinblock();
				break;

			// mac
			case R.id.button_mac:
				calculateMac();
				break;

			// update pinblock key
			case R.id.button_update_pinblock:
				updatePinblockKey();
				break;

			// update mac key
			case R.id.button_update_mac:
				updateMacKey();
				break;

			// update master key
			case R.id.button_master:
				updateMasterKey();
				break;

			default:
				break;

			}
		}

	}

	/**
	 * calculate pin block
	 */
	protected void showText() {
		if (!openFlag)
			return;

		if (show_text_flag) {
			// clear the pinpad screen
			PinPadInterface.showText(0, null, 0, 0);
			PinPadInterface.showText(1, null, 0, 0);

			// get text from the edittext
			String text1 = etTextDataLine1.getText().toString();
			byte[] text_to_show1 = text1.getBytes();
			String text2 = etTextDataLine2.getText().toString();
			byte[] text_to_show2 = text2.getBytes();

			// show text in line one and line two
			byte[] arrayTextLine_1 = text_to_show1;
			PinPadInterface.showText(0, arrayTextLine_1,
					arrayTextLine_1.length, 0);
			byte[] arrayTextLine_2 = text_to_show2;
			PinPadInterface.showText(1, arrayTextLine_2,
					arrayTextLine_2.length, 0);
			show_text_flag = false;
		} else {
			// clean the pinpad screen
			PinPadInterface.showText(0, null, 0, 0);
			PinPadInterface.showText(1, null, 0, 0);
			show_text_flag = true;
		}
	}

	/**
	 * calculate pin block
	 */
	protected void calculatePinblock() {
		etCalPinblockOutput.setText("");
		if (!openFlag)
			return;

		byte[] out = new byte[255];

		String card_no = etCardNo.getText().toString().trim();
		if (card_no == null || card_no.equals("")) {
			Toast.makeText(PinpadActivity.this, "CardNo can not be empty",
					Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] byte_card_no = card_no.getBytes();

		// clear pinpad screen
		PinPadInterface.showText(0, null, 0, 0);
		PinPadInterface.showText(1, null, 0, 0);

		// set pinlen
		PinPadInterface.setPinLength(0x6, 0x1);

		// prompt "please input secret"
		byte[] arrayTextLine_1 = new byte[] { (byte) 0x80, (byte) 0x81,
				(byte) 0x82, (byte) 0x83, (byte) 0x84 };
		PinPadInterface.showText(0, arrayTextLine_1, arrayTextLine_1.length, 0);

		// set key
		PinPadInterface.selectKey(0x2, iMasterKeyID, iUserKeyID, 0x1);

		int ret = PinPadInterface.calculatePinBlock(byte_card_no,
				byte_card_no.length, out, 10000, 1); // 100ms timeout
		if (ret >= 0) {
			String temp = "";
			for (int i = 0; i < 8; i++) { // pinblock always 8 bytes
				temp += String.format("%02x ", out[i]);
			}
			temp = temp.trim();
			etCalPinblockOutput.setText(temp);

			Toast.makeText(PinpadActivity.this,"cal_pinblock_suc", Toast.LENGTH_SHORT).show();
		} else {
			String temp = "";
			temp = temp.trim();
			etCalPinblockOutput.setText(temp);
			Toast.makeText(PinpadActivity.this,"cal_pinblock_fail", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * calculate mac
	 */
	protected void calculateMac() {
		etMacOutput.setText("");
		if (!openFlag)
			return;

		byte[] out = new byte[255];
		// int mac_flag = 1; // 1 ECB 0 x99
		int macType = 0x10;

		// use mac key
		PinPadInterface.selectKey(0x2, iMasterKeyID, iUserKeyID, 0x1);

		String str = etMacInput.getText().toString();
		if (str == null || str.equals("")) {
			Toast.makeText(PinpadActivity.this, "mac can not be empty",Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] mac = Utility.Str2Bcd(str);
		for (int i = 0; i < mac.length; i++) {
			Log.i("mac_data", "0x" + String.format("%02x ", mac[i]));
		}

		int ret = PinPadInterface.calculateMac(mac, mac.length, macType, out);
		if (ret >= 0) {
			String temp = "";
			for (int i = 0; i < 8; i++) { // mac always 8 bytes
				temp += String.format("%02x ", out[i]);
			}
			temp = temp.trim();
			etMacOutput.setText(temp);
			Toast.makeText(PinpadActivity.this, "cal mac succ",Toast.LENGTH_SHORT).show();
		} else {
			String temp = "";
			temp = temp.trim();
			etMacOutput.setText(temp);
			Toast.makeText(PinpadActivity.this, "cal mac fail",Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * update pinblock
	 */
	protected void updatePinblockKey() {
		if (!openFlag)
			return;

		// set pinblock key
		PinPadInterface.selectKey(0x2, iMasterKeyID, iUserKeyID, 0x1);

		String str = etPinblockInput.getText().toString();
		if (str == null || str.equals("")) {
			Toast.makeText(PinpadActivity.this,"pinblock can not be empty", Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] user_key = Utility.Str2Bcd(str);
		for (int i = 0; i < user_key.length; i++) {
			Log.i(LOG_TAG, "0x" + String.format("%02x ", user_key[i]));
		}

		int ret = PinPadInterface.updateUserKey(iMasterKeyID, iUserKeyID,
				user_key, user_key.length);
		if (ret >= 0)
			Toast.makeText(PinpadActivity.this,"update pinblock succ", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(PinpadActivity.this,"update pinblock fail", Toast.LENGTH_SHORT).show();

	}

	/**
	 * update mac
	 */
	protected void updateMacKey() {
		if (!openFlag)
			return;

		// set mac key
		PinPadInterface.selectKey(0x2, iMasterKeyID, iUserKeyID, 0x1);

		String str = etMacKeyInput.getText().toString();
		if (str == null || str.equals("")) {
			Toast.makeText(PinpadActivity.this,"mac can not be empty", Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] user_key = Utility.Str2Bcd(str);
		for (int i = 0; i < user_key.length; i++) {
			Log.i(LOG_TAG, "0x" + String.format("%02x ", user_key[i]));
		}

		int ret = PinPadInterface.updateUserKey(iMasterKeyID, iUserKeyID,
				user_key, user_key.length);
		if (ret >= 0)
			Toast.makeText(PinpadActivity.this,"update mac succ", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(PinpadActivity.this,"update mac fail", Toast.LENGTH_SHORT).show();

	}

	/**
	 * update master key
	 */
	protected void updateMasterKey() {
		if (!openFlag)
			return;

		byte[] old_key = new byte[] { 0x2F, 0x40, (byte) 0xFE, 0x28, 0x7F,
				0x77, 0x0C, 0x08, (byte) 0xA8, 0x31, (byte) 0x83, 0x33,
				(byte) 0xCA, 0x53, (byte) 0xDD, 0x6B };

		// set master key
		PinPadInterface.selectKey(0x2, iMasterKeyID, iUserKeyID, 0x1);

		String str = etMasterKeyInput.getText().toString();
		if (str == null || str.equals("")) {
			Toast.makeText(PinpadActivity.this, "master key can not be empty",Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] master_key = Utility.Str2Bcd(str);

		for (int i = 0; i < master_key.length; i++) {
			Log.i(LOG_TAG, "0x" + String.format("%02x ", old_key[i]));
		}

		int ret = PinPadInterface.updateMasterKey(iMasterKeyID, old_key,
				old_key.length, master_key, master_key.length);

		if (ret >= 0)
			Toast.makeText(PinpadActivity.this, "update master key succ",Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(PinpadActivity.this, "update master key fail",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// show text
		case R.id.button_show_text:
			showText();
			break;

		// pinblock
		case R.id.button_pinblock:
			calculatePinblock();
			break;

		// mac
		case R.id.button_mac:
			calculateMac();
			break;

		// update pinblock key
		case R.id.button_update_pinblock:
			updatePinblockKey();
			break;

		// update mac key
		case R.id.button_update_mac:
			updateMacKey();
			break;

		// update master key
		case R.id.button_master:
			updateMasterKey();
			break;

		default:
			break;
		}
	}

}
