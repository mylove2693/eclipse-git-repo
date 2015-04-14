package com.cynovo.paysetting;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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

	private CardView card_yinlian;
	private CardView card_zhifubao;
	private CardView card_weixin;
	private CardView card_yibao;
	
	private CheckBox cb_yinlian;
	private CheckBox cb_zhifubao;
	private CheckBox cb_weixin;
	private CheckBox cb_yibao;

	private PaySettingLogin LoginFragment;
	private DataUtil data;
	
	private boolean yinlianEnable;
	private boolean zhifubaoEnable;
	private boolean weixinEnable;
	private boolean yibaoEnable;
	
	private Button btn_test_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_setting);
		
		data = new DataUtil(PaySettingActivity.this);
		
		yinlianEnable = data.getBoolean(DataUtil.KEY_YINLIAN, false);
		zhifubaoEnable = data.getBoolean(DataUtil.KEY_ZHIFUBAO, false);
		weixinEnable = data.getBoolean(DataUtil.KEY_WEIXIN, false);
		yibaoEnable = data.getBoolean(DataUtil.KEY_YIBAO, false);

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
			data.putBoolean(DataUtil.KEY_YINLIAN, yinlianEnable);

			break;
			
		case R.id.card_zhifubao:
			zhifubaoEnable = !zhifubaoEnable;
			cb_zhifubao.setChecked(zhifubaoEnable);
			data.putBoolean(DataUtil.KEY_ZHIFUBAO, zhifubaoEnable);

			break;
			
		case R.id.card_weixin:
			weixinEnable = !weixinEnable;
			cb_weixin.setChecked(weixinEnable);
			data.putBoolean(DataUtil.KEY_WEIXIN, weixinEnable);
			
			break;
			
		case R.id.card_yibao:
			yibaoEnable = !yibaoEnable;
			cb_yibao.setChecked(yibaoEnable);
			data.putBoolean(DataUtil.KEY_YIBAO, yibaoEnable);
			
			break;
		}
	}

	@Override
	public void Login() {
		// TODO Auto-generated method stub
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.animator.accordion_right_in, R.animator.accordion_left_out);
		fragmentTransaction.hide(LoginFragment).commit();
		
		//getFragmentManager().beginTransaction().hide(LoginFragment).commit();
	}

}
