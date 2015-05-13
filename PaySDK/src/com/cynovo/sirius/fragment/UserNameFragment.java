package com.cynovo.sirius.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cynovo.sirius.PaySDK.ActiveActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.util.CynovoHttpClient;
import com.cynovo.sirius.util.GetJniInfo;
import com.cynovo.sirius.util.MyLog;
import com.kivvi.jni.ISO8583Interface;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.unionpay.CloudPosPaymentClient.aidl.ICloudPay;

@SuppressLint({ "NewApi", "ValidFragment" })
public class UserNameFragment extends AbstractLoadingFragment implements
		OnClickListener, TextView.OnEditorActionListener {
	private EditText userNameEditText;
	private EditText userPhoneEditText;
	private Handler handler;

	// added by wanhaiping------------------------------------->begin
	private ServiceConnection conn = null;
	private ICloudPay Icloudpay;
	// private Handler handler = null;

	String strUserName;
	String strPassword;

	// added by wanhaiping-------------------------------------<end

	public UserNameFragment() {
	}

	public UserNameFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.user_name, container, false);

		userNameEditText = (EditText) v.findViewById(R.id.userNameEditText1);
		userPhoneEditText = (EditText) v.findViewById(R.id.userPhoneEditText1);
		userNameEditText.setOnEditorActionListener(this);
		userPhoneEditText.setOnEditorActionListener(this);
		Button submitButton = (Button) v.findViewById(R.id.submitButton1);
		submitButton.setOnClickListener(this);

		try {
			handler.sendEmptyMessage(ActiveActivity.HIDE_BACK_BUTTON);

			Message msg = new Message();
			msg.what = ActiveActivity.CHANGE_TITLE;
			msg.obj = getResources().getString(R.string.active_device);
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// added by
		// wanhaiping--------------------------------------------->begin
		conn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				Icloudpay = null;
				Log.e("debug", "DUNIONPAY onServiceDisconnected");
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// IPaySDK.Stub.asInterface，获取接口
				Icloudpay = ICloudPay.Stub.asInterface(service);

				Log.e("debug", "DUNIONPAY onServiceConnected");

				/*
				 * Message message = new Message(); message.what = 1;
				 * handler.sendMessage(message);
				 */
			}
		};
		boolean ret = getActivity().bindService(
				new Intent(ICloudPay.class.getName()), conn,
				Context.BIND_AUTO_CREATE);
		if (ret) {
			Log.e("debug", "Connect DUNIONPAY Successfully");
		} else {
			Log.e("debug", "Connect DUNIONPAY Failed");
		}

		// added by
		// wanhaiping----------------------------------------------------------<end

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitButton1: // 提交

			submitMerchantInfo();
			break;
		}
	}

	private void submitMerchantInfo() {

		ActiveActivity.isDownloadKey = true;
		strUserName = userNameEditText.getText().toString();
		strPassword = userPhoneEditText.getText().toString();

		new Thread(new Runnable() {
			public void run() {
				try {
					int ret = 0;
					ret = SignIn();
					if (ret == 1) {
						Log.e("debug", "sendEmptyMessage ACTIVE_SUCCESS begin");
						ActiveActivity.handler
								.sendEmptyMessage(ActiveActivity.ACTIVE_SUCCESS);
						ActiveActivity.isDownloadKey = false;
						Log.e("debug", "sendEmptyMessage ACTIVE_SUCCESS end");
					} else {
						Log.e("debug", "sendEmptyMessage ACTIVE_ERROR begin");
						ActiveActivity.handler
								.sendEmptyMessage(ActiveActivity.ACTIVE_ERROR);
						Log.e("debug", "sendEmptyMessage ACTIVE_ERROR end");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

		/*
		 * if(userNameEditText.getText() != null &&
		 * userNameEditText.getText().toString().trim().length() > 0 &&
		 * userPhoneEditText.getText() != null &&
		 * userPhoneEditText.getText().toString().trim().length() > 0) {
		 * if(!Common
		 * .isMobilePhoneNO(userPhoneEditText.getText().toString().trim())) {
		 * Toast.makeText(getActivity(), R.string.error_phone_type,
		 * Toast.LENGTH_LONG).show(); return; }
		 * 
		 * RequestParams params = new RequestParams(); params.put("phone",
		 * userPhoneEditText.getText().toString().trim()); params.put("name",
		 * userNameEditText.getText().toString().trim()); params.put("cpuid",
		 * GetJniInfo.getCPUID());
		 * if(MySharedPreferencesEdit.getInstancePublic(getActivity
		 * ()).getMerchantAccountID() != null) { params.put("shouldSendSms",
		 * -1); } else { params.put("shouldSendSms", 1); }
		 * if(MySharedPreferencesEdit
		 * .getInstancePublic(getActivity()).getIsBandDeviceInfo()) { //
		 * 设备已经绑定，需检查cpuid和对应管理员之间的关系 params.put("shouldCkeckCpuid", 1); } else
		 * { params.put("shouldCkeckCpuid", -1); } MyLog.e("params:"+params);
		 * 
		 * CynovoHttpClient.post(getActivity(),
		 * "api/active_device/password/verify_phone_name.php", params, //
		 * CynovoHttpClient
		 * .post("api/active_device/password/verify_phone_name_state.php",
		 * params, new JsonHttpResponseHandler() {
		 * 
		 * @Override public void onStart() { pDialog.show(); super.onStart(); }
		 * 
		 * @Override public void onSuccess(JSONObject response) {
		 * 
		 * super.onSuccess(response); MyLog.e("response:"+response); try { int
		 * ret = response.getInt("ret"); if(ret == 1) { // 跳转到输入密码界面
		 * FragmentManager fragmentManager = getActivity().getFragmentManager();
		 * FragmentTransaction fragmentTransaction =
		 * fragmentManager.beginTransaction(); PasswordSupportFragment
		 * userNameFragment = new PasswordSupportFragment(handler); Bundle
		 * bundle = new Bundle(); bundle.putInt("state", 1);
		 * userNameFragment.setArguments(bundle);
		 * fragmentTransaction.replace(R.id.fragmentLayout2, userNameFragment);
		 * fragmentTransaction
		 * .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		 * fragmentTransaction.addToBackStack(null);
		 * fragmentTransaction.commitAllowingStateLoss();
		 * handler.sendEmptyMessage(ActiveActivity.SHOW_BACK_BUTTON);
		 * MySharedPreferencesEdit.getInstancePublic(getActivity()).
		 * setOwnerPhoneNumber(userPhoneEditText.getText().toString().trim()); }
		 * else if(ret == 0) { //
		 * if(MySharedPreferencesEdit.getInstancePublic(getActivity
		 * ()).getMerchantAccountID() != null) { // // 跳转到输入密码界面 //
		 * FragmentManager fragmentManager =
		 * getActivity().getSupportFragmentManager(); // FragmentTransaction
		 * fragmentTransaction = fragmentManager.beginTransaction(); //
		 * PasswordSupportFragment userNameFragment = new
		 * PasswordSupportFragment(handler); // Bundle bundle = new Bundle(); //
		 * bundle.putInt("state", 1); // userNameFragment.setArguments(bundle);
		 * // fragmentTransaction.replace(R.id.fragmentLayout2,
		 * userNameFragment); //
		 * fragmentTransaction.setTransition(FragmentTransaction
		 * .TRANSIT_FRAGMENT_OPEN); // fragmentTransaction.addToBackStack(null);
		 * // fragmentTransaction.commitAllowingStateLoss(); //
		 * handler.sendEmptyMessage(ActiveFragment.SHOW_BACK_BUTTON); //
		 * MySharedPreferencesEdit.getInstancePublic(getActivity()). //
		 * setOwnerPhoneNumber(userPhoneEditText.getText().toString().trim());
		 * // } else { FragmentManager fragmentManager =
		 * getActivity().getFragmentManager(); FragmentTransaction
		 * fragmentTransaction = fragmentManager.beginTransaction();
		 * EnsureActiveNumberFragment userNameFragment = new
		 * EnsureActiveNumberFragment(handler); Bundle bundle = new Bundle();
		 * bundle.putInt("activeno", 1); userNameFragment.setArguments(bundle);
		 * fragmentTransaction.replace(R.id.fragmentLayout2, userNameFragment);
		 * fragmentTransaction
		 * .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		 * fragmentTransaction.addToBackStack(null);
		 * fragmentTransaction.commitAllowingStateLoss(); //
		 * handler.sendEmptyMessage(ActiveFragment.SHOW_BACK_BUTTON); // }
		 * 
		 * MySharedPreferencesEdit.getInstancePublic(getActivity()).
		 * setOwnerPhoneNumber( userPhoneEditText.getText().toString().trim());
		 * } else { String msg = response.getString("msg"); new
		 * AlertDialog.Builder(getActivity()).setTitle(R.string.hint)
		 * .setMessage(msg) .setPositiveButton(R.string.sure, new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss(); } }) .show(); } } catch (Exception e) {
		 * e.printStackTrace(); Toast.makeText(getActivity(),
		 * R.string.network_error, Toast.LENGTH_LONG).show(); }
		 * pDialog.dismiss(); }
		 * 
		 * @Override public void onFailure(Throwable e, String content) {
		 * super.onFailure(e, content); e.printStackTrace();
		 * Toast.makeText(getActivity(), R.string.network_error,
		 * Toast.LENGTH_LONG).show(); pDialog.dismiss(); } }); } else {
		 * Toast.makeText(getActivity(), R.string.phone_and_name_null,
		 * Toast.LENGTH_LONG).show(); }
		 */
	}

	// added by
	// wanhaiping----------------------------------------------------->begin
	public void onDestroyView() {
		Log.e("debug", "UserNameFragment onDestroy begin");
		if (Icloudpay != null) {
			getActivity().unbindService(conn);
		}
		Icloudpay = null;
		super.onDestroyView();
		Log.e("debug", "UserNameFragment onDestroy end");
		// Log.d(TAG, "onDestroyView");
	}

	int SignIn() throws JSONException {

		//add by punan begin--------------------------->begin
		
		Log.e("debug", "login()..........");
		//send_buffer_len = ISO8583Interface.checkInReq(send_buffer, send_buffer.length);
		//ISO8583Interface.setTraceNo("000001");
		int ret = ISO8583Interface.checkIn("000001");
		Log.e("debug", "login(). ret" + ret);
		
		if(ret == 102){
			return 1;
		}else{
			return 0;
		}
		
		//add by punan end----------------------------->end
		
		/**
		Log.e("debug", "begin UNIONPAY SignIn");
		String responsecode = "";
		if (Icloudpay == null) {
			Log.e("debug", "Icloudpay is null");
			return 0;
		}

		try {
			String ret;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("appID", "50019");
			jsonObject.put("appName", "ClPAR10");

			// jsonObject.put("operatorNo", "01");
			// jsonObject.put("operatorPwd", "0000");

			jsonObject.put("operatorNo", strUserName);
			jsonObject.put("operatorPwd", strPassword);

			Log.e("debug", strUserName);
			Log.e("debug", strPassword);

			Log.e("debug", "signIn inpuit value:");
			Log.e("debug", jsonObject.toString());
			ret = Icloudpay.signIn(jsonObject.toString());

			Log.e("debug", "signIn return value:");
			Log.e("debug", ret);

			JSONObject jsonObjectRet = new JSONObject(ret);
			responsecode = jsonObjectRet.getString("rspCode");

			Log.e("debug", "responsecode");
			Log.e("debug", responsecode);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Log.e("debug", "begin compare");
		String strTemp = "00";

		Log.e("debug", responsecode);
		Log.e("debug", strTemp);
		if (responsecode.equals(strTemp)) {
			Log.e("debug", "SignIn return 1");
			return 1;
		} else {
			Log.e("debug", "SignIn return 0");
			return 0;
		}
		**/
	}

	// added by
	// wanhaiping--------------------------------------------------------<end

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE
				|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.userNameEditText1:
				userPhoneEditText.requestFocus();
				break;
			case R.id.userPhoneEditText1:
				submitMerchantInfo();
				break;
			}
		}
		return false;
	}
}
