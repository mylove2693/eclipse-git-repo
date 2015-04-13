package com.cynovo.paysetting;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class PaySettingLogin extends Fragment {
	
	private TextView tv_login;
	private EditText et_passwd;
	
	private onLogin mLogin;
	
	public interface onLogin{
		void Login();
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mLogin = (onLogin) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView =  inflater.inflate(R.layout.fragment_login, container, false);
		
		tv_login = (TextView)rootView.findViewById(R.id.tv_login);
		et_passwd = (EditText)rootView.findViewById(R.id.et_password);
		
		et_passwd.addTextChangedListener(mTextWatcher);
		
		return rootView;
	}

	TextWatcher mTextWatcher = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if(PaySettingActivity.PASSWORD.equals(s.toString())){
				mLogin.Login();
				et_passwd.setText("");
			}
		}
		
	};
	
}
