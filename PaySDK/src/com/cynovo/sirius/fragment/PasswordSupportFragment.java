package com.cynovo.sirius.fragment;

import java.util.Locale;

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
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.util.CynovoHttpClient;
import com.cynovo.sirius.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint({ "NewApi", "ValidFragment" })
public class PasswordSupportFragment extends AbstractLoadingFragment implements
		OnClickListener, OnEditorActionListener {
	private EditText passwordEditText;
	private EditText ensurePasswordEditText;
	private MySharedPreferencesEdit mySharedPreferencesEdit;
	private Handler handler;
	private int state; // 1表示已激活，只需验证，其他表示未激活

	public PasswordSupportFragment() {
	}

	public PasswordSupportFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Bundle bundle = getArguments();
			state = bundle.getInt("state");
		} catch (Exception e) {
			e.printStackTrace();
		}
		mySharedPreferencesEdit = MySharedPreferencesEdit
				.getInstancePublic(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = null;
		if (state == 1) {
			v = inflater.inflate(R.layout.password_verify, null);
			Button forgetPasswordButton11 = (Button) v
					.findViewById(R.id.forgetPasswordButton11);
			forgetPasswordButton11
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							FragmentManager fragmentManager = getActivity()
									.getFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager
									.beginTransaction();
							ActiveForgetPasswordFragment userNameFragment = new ActiveForgetPasswordFragment(
									handler);
							fragmentTransaction.replace(R.id.fragmentLayout2,
									userNameFragment);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commitAllowingStateLoss();

							handler.sendEmptyMessage(ActiveActivity.SHOW_BACK_BUTTON);
						}
					});
		} else {
			v = inflater.inflate(R.layout.password, null);
		}
		Button submitPasswordButton = (Button) v
				.findViewById(R.id.submitPasswordButton);
		submitPasswordButton.setOnClickListener(this);

		passwordEditText = (EditText) v.findViewById(R.id.passwordEditText);
		passwordEditText.requestFocus();
		ensurePasswordEditText = (EditText) v
				.findViewById(R.id.ensurePasswordEditText);
		passwordEditText.setOnEditorActionListener(this);
		ensurePasswordEditText.setOnEditorActionListener(this);

		Message msg = new Message();
		msg.what = ActiveActivity.CHANGE_TITLE;
		msg.obj = getResources().getString(R.string.input_password);
		handler.sendMessage(msg);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitPasswordButton: { // 提交
			submitPassword();
			break;
		}
		}
	}

	private void submitPassword() {
		if (passwordEditText.getText() == null
				|| passwordEditText.getText().toString().trim().length() == 0) {
			// 输入框为空
			Toast.makeText(getActivity(), R.string.passworderror_prompt,
					Toast.LENGTH_LONG).show();
			return;
		}
		if (state != 1) { // 账号还没有激活
			if (ensurePasswordEditText.getText() == null
					|| ensurePasswordEditText.getText().toString().trim()
							.length() == 0) {

			}
		}
		if (state != 1
				&& !passwordEditText
						.getText()
						.toString()
						.trim()
						.equals(ensurePasswordEditText.getText().toString()
								.trim())) {
			// 两次输入的密码不同
			Toast.makeText(getActivity(), R.string.psw_different,
					Toast.LENGTH_LONG).show();
			return;
		}
		if (state != 1
				&& (passwordEditText.getText().length() < 6 || passwordEditText
						.getText().length() > 16)) {
			// 密码长度限制
			Toast.makeText(getActivity(), R.string.psw_lenth_hint,
					Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("phone", mySharedPreferencesEdit.getOwnerPhoneNumber());
		try {
			params.put(
					"psw",
					Common.SHA1.toSHA1(
							passwordEditText.getText().toString().trim())
							.toUpperCase(Locale.getDefault()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.put("state", (state == 1) ? 1 : -1);

		CynovoHttpClient.post(getActivity(),
				"api/active_device/password/get_password.php", params,
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
								Common.HideKeyboardIfExist(getActivity(),
										PasswordSupportFragment.this);
								if (mySharedPreferencesEdit
										.getMerchantAccountID() == null
										|| mySharedPreferencesEdit
												.getMerchantAccountID()
												.length() == 0) {
									String accountID = response
											.getString("accountID");
									mySharedPreferencesEdit
											.setMerchantAccountID(accountID);
								}
								if (mySharedPreferencesEdit
										.getIsBandDeviceInfo()) {
									// 此设备已被绑定过
									// if(checkAccount()) {
									// }
									checkSecret();
								} else { // start band
									FragmentManager fragmentManager = getActivity()
											.getFragmentManager();
									FragmentTransaction fragmentTransaction = fragmentManager
											.beginTransaction();
									BandMIDFragment userNameFragment = new BandMIDFragment(
											handler);
									fragmentTransaction.replace(
											R.id.fragmentLayout2,
											userNameFragment);
									fragmentTransaction
											.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
									fragmentTransaction.addToBackStack(null);
									fragmentTransaction
											.commitAllowingStateLoss();

									handler.sendEmptyMessage(ActiveActivity.SHOW_BACK_BUTTON);
								}
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
									"PasswordSupportFragment: 网络异常1", Toast.LENGTH_LONG)
									.show();
						}
						super.onSuccess(statusCode, response);

						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						Toast.makeText(getActivity(), "PasswordSupportFragment: 网络异常2",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
						e.printStackTrace();
					}
				});
	}

	// private boolean checkAccount() {
	// return false;
	// }

	private void checkSecret() {
		// if(mySharedPreferencesEdit.getIsDownloadSecretKey()) {
		// // 已经下载过主密钥
		// new AlertDialog.Builder(getActivity())
		// .setTitle(R.string.hint)
		// .setMessage(R.string.already_download_secret)
		// .setPositiveButton(R.string.sure, new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// new AlertDialog.Builder(getActivity())
		// .setTitle(R.string.hint)
		// .setMessage(R.string.contact_xunlian)
		// .setPositiveButton(R.string.sure, new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// }).show();
		// }
		// })
		// .setNegativeButton(R.string.cancel, new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// getActivity().finish();
		// }
		// })
		// .show();
		// } else {
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		DownSecretKeyFragment userNameFragment = new DownSecretKeyFragment(
				handler);
		fragmentTransaction.replace(R.id.fragmentLayout2, userNameFragment);
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragmentTransaction.commitAllowingStateLoss();
		// }
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE
				|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.passwordEditText:
				if (state == 1) {
					submitPassword();
				} else {
					ensurePasswordEditText.requestFocus();
				}
				break;
			case R.id.ensurePasswordEditText:
				submitPassword();
				break;
			}
		}
		return false;
	}

}
