package com.cynovo.paysetting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

public class PaySettingActivity extends Activity implements
		OnClickListener{
	
	public static final String TAG = "PaySetting";
	public static final String PREFERENCENAME = "payconfig";
	public static final String PASSWORD = "123456";

	public static final String KEY_YINLIAN = "yinlian";
	public static final String KEY_ZHIFUBAO = "zhifubao";
	public static final String KEY_WEIXIN = "weixin";
	public static final String KEY_YIBAO = "yibao";
	
	private CardView card_yinlian;
	private CardView card_zhifubao;
	private CardView card_weixin;
	private CardView card_yibao;
	
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

		card_yinlian = (CardView)findViewById(R.id.card_yinlian);
		card_zhifubao = (CardView)findViewById(R.id.card_zhifubao);
		card_weixin = (CardView)findViewById(R.id.card_weixin);
		card_yibao = (CardView)findViewById(R.id.card_yibao);
		
		card_yinlian.setOnClickListener(this);
		card_zhifubao.setOnClickListener(this);
		card_weixin.setOnClickListener(this);
		card_yibao.setOnClickListener(this);
		
		cb_yinlian = (CheckBox) findViewById(R.id.cb_yinlian);
		cb_zhifubao = (CheckBox) findViewById(R.id.cb_zhifubao);
		cb_weixin = (CheckBox) findViewById(R.id.cb_weixin);
		cb_yibao = (CheckBox) findViewById(R.id.cb_yibao);

		//cb_yinlian.setOnCheckedChangeListener(this);
		//cb_zhifubao.setOnCheckedChangeListener(this);
		//cb_weixin.setOnCheckedChangeListener(this);
		//cb_yibao.setOnCheckedChangeListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.card_yinlian :
			if(cb_yinlian.isChecked()){
				cb_yinlian.setChecked(false);
			}else{
				cb_yinlian.setChecked(true);
			}
			
			try {
				Toast.makeText(PaySettingActivity.this, "银联：" + cb_yinlian.isChecked(),
					Toast.LENGTH_SHORT).show();
				String str = AESCrypto.encrypt(PASSWORD, String.valueOf(cb_yinlian.isChecked()));
				editor.putString(KEY_YINLIAN, str);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case R.id.card_zhifubao:
			if(cb_zhifubao.isChecked()){
				cb_zhifubao.setChecked(false);
			}else{
				cb_zhifubao.setChecked(true);
			}
			
			try {
				Toast.makeText(PaySettingActivity.this, "支付宝：" + cb_zhifubao.isChecked(),
					Toast.LENGTH_SHORT).show();
				String str = AESCrypto.encrypt(PASSWORD, String.valueOf(cb_zhifubao.isChecked()));
				editor.putString(KEY_ZHIFUBAO, str);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case R.id.card_weixin:
			if(cb_weixin.isChecked()){
				cb_weixin.setChecked(false);
			}else{
				cb_weixin.setChecked(true);
			}
			try {
				Toast.makeText(PaySettingActivity.this, "微信：" + cb_weixin.isChecked(),
					Toast.LENGTH_SHORT).show();
				String str = AESCrypto.encrypt(PASSWORD, String.valueOf(cb_weixin.isChecked()));
				editor.putString(KEY_WEIXIN, str);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case R.id.card_yibao:
			if(cb_yibao.isChecked()){
				cb_yibao.setChecked(false);
			}else{
				cb_yibao.setChecked(true);
			}
			try {
				Toast.makeText(PaySettingActivity.this, "易宝：" + cb_yibao.isChecked(),
					Toast.LENGTH_SHORT).show();
				String str = AESCrypto.encrypt(PASSWORD, String.valueOf(cb_yibao.isChecked()));
				editor.putString(KEY_YIBAO, str);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

}
