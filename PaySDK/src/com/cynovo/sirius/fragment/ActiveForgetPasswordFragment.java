package com.cynovo.sirius.fragment;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.cynovo.sirius.PaySDK.ActiveActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.cynovo.sirius.util.CynovoHttpClient;
import com.cynovo.sirius.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint({ "NewApi", "ValidFragment" })
public class ActiveForgetPasswordFragment extends AbstractLoadingFragment
		implements OnClickListener, OnEditorActionListener {
	private TextView userPhoneEditText11;
	private EditText activeNumberEditText11;
	private TextView regainActiveNumberText11;
	private int sec;
	private Button getActiveNumberButton11;
	private Timer verificationTimer;
	private int curretStep;
	private String phone;
	private Handler handler2;

	public ActiveForgetPasswordFragment() {
	}

	public ActiveForgetPasswordFragment(Handler handler2) {
		this.handler2 = handler2;
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message arg0) {
			sec--;
			if (sec == 0) {
				verificationTimer.cancel();
				regainActiveNumberText11.setVisibility(View.GONE);
				getActiveNumberButton11.setVisibility(View.VISIBLE);
			} else {
				regainActiveNumberText11.setText(getString(
						R.string.regain_active_number_hint, sec));
			}
			return true;
		}
	});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		curretStep = 0;
		MySharedPreferencesEdit mySharedPreferencesEdit = MySharedPreferencesEdit
				.getInstancePublic(getActivity());
		phone = mySharedPreferencesEdit.getOwnerPhoneNumber();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(
				R.layout.active_verify_phone_active_number, null);

		if (handler2 != null) {
			Message msg = new Message();
			msg.obj = getString(R.string.forget_password);
			msg.what = 2; // LoginPageFragment.CHANGE_HINT_TEXT;
			handler2.sendMessage(msg);
		}

		Button sureButton1 = (Button) v.findViewById(R.id.sureButton1);
		sureButton1.setOnClickListener(this);

		userPhoneEditText11 = (TextView) v
				.findViewById(R.id.userPhoneEditText11);
		userPhoneEditText11.setText(phone);
		activeNumberEditText11 = (EditText) v
				.findViewById(R.id.activeNumberEditText11);
		activeNumberEditText11.setOnEditorActionListener(this);

		regainActiveNumberText11 = (TextView) v
				.findViewById(R.id.regainActiveNumberText11);
		getActiveNumberButton11 = (Button) v
				.findViewById(R.id.getActiveNumberButton11);
		getActiveNumberButton11.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sureButton1:
			submitActiveNumber();
			break;
		case R.id.getActiveNumberButton11:
			getActiveNumber();
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE
				|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.activeNumberEditText11:
				submitActiveNumber();
				break;
			}
		}
		return false;
	}

	private void getActiveNumber() {
		RequestParams params = new RequestParams();
		params.put("phone", phone);

		CynovoHttpClient.post(getActivity(),
				"api/active_device/password/active_number.php", params,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						pDialog.show();
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						MyLog.e("response:" + response);
						try {
							int ret = response.getInt("ret");
							if (ret == 0) { // success!
								// TODO
								resetTimer();
								curretStep = 1;
								activeNumberEditText11.requestFocus();
							} else {
								String msg = response.getString("msg");
								new AlertDialog.Builder(getActivity())
										.setTitle(R.string.hint)
										.setMessage(msg)
										.setPositiveButton(
												R.string.sure,
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();
													}
												}).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(getActivity(),
									"ActiveForgetPasswordFragment:网络异常1", Toast.LENGTH_LONG)
									.show();
						}
						super.onSuccess(statusCode, response);

						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						Toast.makeText(getActivity(), "ActiveForgetPasswordFragment:网络异常2",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
						e.printStackTrace();
					}
				});
	}

	private void resetTimer() {
		regainActiveNumberText11.setVisibility(View.VISIBLE);
		getActiveNumberButton11.setVisibility(View.GONE);
		verificationTimer = new Timer();
		sec = 60;
		regainActiveNumberText11.setText(getString(
				R.string.regain_active_number_hint, sec));
		verificationTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}

	private void submitActiveNumber() {
		if (curretStep == 0) {
			Toast.makeText(getActivity(), R.string.get_active_number_first,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (activeNumberEditText11.getText() == null
				|| activeNumberEditText11.getText().toString().trim().length() != 6) {
			// 输入框为空
			Toast.makeText(getActivity(), R.string.active_number_null,
					Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("activeNumber", activeNumberEditText11.getText().toString()
				.trim());
		params.put("phone", phone);

		CynovoHttpClient.post(getActivity(),
				"api/active_device/password/verify_active_number.php", params,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						pDialog.show();
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						MyLog.e("response:" + response);
						try {
							int ret = response.getInt("ret");
							if (ret == 0) {
								FragmentManager fragmentManager = getFragmentManager();
								FragmentTransaction fragmentTransaction = fragmentManager
										.beginTransaction();
								ActivePasswordFragment userNameFragment = new ActivePasswordFragment(
										handler2);
								Bundle bundle = new Bundle();
								bundle.putInt(PasswordFragment.FROM_TYPE,
										PasswordFragment.IS_FROM_FORGET);
								bundle.putString("phone", phone);
								userNameFragment.setArguments(bundle);
								fragmentTransaction.replace(
										R.id.fragmentLayout2, userNameFragment);
								fragmentTransaction
										.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								fragmentTransaction
										.addToBackStack(ActiveActivity.FORGET_PSW);
								fragmentTransaction.commitAllowingStateLoss();

								handler2.sendEmptyMessage(ActiveActivity.SHOW_BACK_BUTTON);
							} else {
								String msg = response.getString("msg");
								new AlertDialog.Builder(getActivity())
										.setTitle(R.string.hint)
										.setMessage(msg)
										.setPositiveButton(
												R.string.sure,
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();
													}
												}).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(getActivity(),
									"ActiveForgetPasswordFragment:网络异常3", Toast.LENGTH_LONG)
									.show();
						}
						super.onSuccess(statusCode, response);

						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						Toast.makeText(getActivity(), "ActiveForgetPasswordFragment:网络异4",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
						e.printStackTrace();
					}
				});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			if (verificationTimer != null) {
				verificationTimer.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
