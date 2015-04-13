package com.cynovo.paysetting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.cynovo.paysetting.PaySettingLogin.onLogin;

public class PaySettingActivity extends Activity implements
		OnClickListener, onLogin{
	
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
	
	private boolean yinlianEnable;
	private boolean zhifubaoEnable;
	private boolean weixinEnable;
	private boolean yibaoEnable;
	
	private Button btn_test_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_setting);

		PaySettingPreference = PaySettingActivity.this.getSharedPreferences(
				PREFERENCENAME, Context.MODE_PRIVATE);
		editor = PaySettingPreference.edit();
		
		yinlianEnable = PaySettingPreference.getBoolean(KEY_YINLIAN, false);
		zhifubaoEnable = PaySettingPreference.getBoolean(KEY_ZHIFUBAO, false);
		weixinEnable = PaySettingPreference.getBoolean(KEY_WEIXIN, false);
		yibaoEnable = PaySettingPreference.getBoolean(KEY_YIBAO, false);

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
		
		cb_yinlian.setChecked(yinlianEnable);
		cb_zhifubao.setChecked(zhifubaoEnable);
		cb_weixin.setChecked(weixinEnable);
		cb_yibao.setChecked(yibaoEnable);
		
		btn_test_data = (Button)findViewById(R.id.btn_test_data);
		btn_test_data.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PaySettingActivity.this, GetDataTest.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				PaySettingActivity.this.startActivity(intent);
			}});
		
		LoginFragment = new PaySettingLogin();
		getFragmentManager().beginTransaction().add(R.id.Frame_container, LoginFragment).commit();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//getFragmentManager().beginTransaction().show(LoginFragment).commit();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.card_yinlian :
			yinlianEnable = !yinlianEnable;
			cb_yinlian.setChecked(yinlianEnable);
			editor.putBoolean(KEY_YINLIAN, yinlianEnable);
			editor.commit();

			break;
			
		case R.id.card_zhifubao:
			zhifubaoEnable = !zhifubaoEnable;
			cb_zhifubao.setChecked(zhifubaoEnable);
			editor.putBoolean(KEY_ZHIFUBAO, zhifubaoEnable);
			editor.commit();

			break;
			
		case R.id.card_weixin:
			weixinEnable = !weixinEnable;
			cb_weixin.setChecked(weixinEnable);
			editor.putBoolean(KEY_WEIXIN, weixinEnable);
			editor.commit();
			
			break;
			
		case R.id.card_yibao:
			yibaoEnable = !yibaoEnable;
			cb_yibao.setChecked(yibaoEnable);
			editor.putBoolean(KEY_YIBAO, yibaoEnable);
			editor.commit();
			
			break;
		}
	}

	@Override
	public void Login() {
		// TODO Auto-generated method stub
		getFragmentManager().beginTransaction().hide(LoginFragment).commit();
	}

}
