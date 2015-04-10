package com.cynovo.paysetting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class PaySettingActivity extends Activity implements
		OnCheckedChangeListener {
	
	public static final String TAG = "PaySetting";
	public static final String PREFERENCENAME = "payconfig";
	public static final String PASSWORD = "123456";

	public static final String KEY_YINLIAN = "yinlian";
	public static final String KEY_ZHIFUBAO = "zhifubao";
	public static final String KEY_WEIXIN = "weixin";
	public static final String KEY_YIBAO = "yibao";
	
	private CheckBox cb_yinlian;
	private CheckBox cb_zhifubao;
	private CheckBox cb_weixin;
	private CheckBox cb_yibao;
	
	private SharedPreferences PaySettingPreference;
	private Editor editor;

	private PaySettingLogin LoginFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_setting);

		PaySettingPreference = PaySettingActivity.this.getSharedPreferences(
				PREFERENCENAME, Context.MODE_PRIVATE);
		editor = PaySettingPreference.edit();
		
		editor.putString(KEY_YINLIAN, "closed");
		editor.putString(KEY_ZHIFUBAO, "closed");
		editor.putString(KEY_WEIXIN, "closed");
		editor.putString(KEY_YIBAO, "closed");

		editor.commit();

		cb_yinlian = (CheckBox) findViewById(R.id.cb_yinlian);
		cb_zhifubao = (CheckBox) findViewById(R.id.cb_zhifubao);
		cb_weixin = (CheckBox) findViewById(R.id.cb_weixin);
		cb_yibao = (CheckBox) findViewById(R.id.cb_yibao);

		cb_yinlian.setOnCheckedChangeListener(this);
		cb_zhifubao.setOnCheckedChangeListener(this);
		cb_weixin.setOnCheckedChangeListener(this);
		cb_yibao.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		int id = buttonView.getId();
		switch (id) {
		case R.id.cb_yinlian:
			try {
				Toast.makeText(PaySettingActivity.this, "银联：" + isChecked,
						Toast.LENGTH_SHORT).show();
				String str = AESCrypto.encrypt(PASSWORD, String.valueOf(isChecked));
				editor.putString(KEY_YINLIAN, str);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.cb_zhifubao:
			try {
				Toast.makeText(PaySettingActivity.this, "支付宝：" + isChecked,
						Toast.LENGTH_SHORT).show();
				String str = AESCrypto.encrypt(PASSWORD, String.valueOf(isChecked));
				editor.putString(KEY_ZHIFUBAO, str);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.cb_weixin:
			try {
				Toast.makeText(PaySettingActivity.this, "微信：" + isChecked,
						Toast.LENGTH_SHORT).show();
				String str = AESCrypto.encrypt(PASSWORD, String.valueOf(isChecked));
				editor.putString(KEY_WEIXIN, str);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.cb_yibao:
			try {
				Toast.makeText(PaySettingActivity.this, "易宝：" + isChecked,
						Toast.LENGTH_SHORT).show();
				String str = AESCrypto.encrypt(PASSWORD, String.valueOf(isChecked));
				editor.putString(KEY_YIBAO, str);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
}
