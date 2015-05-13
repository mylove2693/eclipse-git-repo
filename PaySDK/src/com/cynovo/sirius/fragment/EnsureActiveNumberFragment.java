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
import android.widget.Toast;

import com.cynovo.sirius.PaySDK.ActiveActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.cynovo.sirius.util.CynovoHttpClient;
import com.cynovo.sirius.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint({ "NewApi", "ValidFragment" })
public class EnsureActiveNumberFragment extends AbstractLoadingFragment
		implements OnClickListener, TextView.OnEditorActionListener {
	private EditText activeNumberEditText;
	private TextView regainActiveNumberText;
	private int sec;
	private Button getActiveNumberButton;
	private Timer verificationTimer;
	// private Button login_bak2;
	private Handler handler2;
	private int activeno;

	public EnsureActiveNumberFragment() {
	}

	public EnsureActiveNumberFragment(Handler handler2) {
		this.handler2 = handler2;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activeno = 0;
		try {
			Bundle bundle = getArguments();
			activeno = bundle.getInt("activeno");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.ensure_active_number, null);

		MyLog.e("1、activeno:" + activeno);

		Button submitActiveNumberButton = (Button) v
				.findViewById(R.id.submitActiveNumberButton);
		submitActiveNumberButton.setOnClickListener(this);
		regainActiveNumberText = (TextView) v
				.findViewById(R.id.regainActiveNumberText);
		getActiveNumberButton = (Button) v
				.findViewById(R.id.getActiveNumberButton);

		activeNumberEditText = (EditText) v
				.findViewById(R.id.activeNumberEditText);
		activeNumberEditText.requestFocus();
		activeNumberEditText.setOnEditorActionListener(this);
		// login_bak2 = (Button) v.findViewById(R.id.login_bak2);
		// login_bak2.setOnClickListener(this);

		if (activeno == 1) {
			resetTimer();
		} else {
			FragmentManager fragmentManager = getActivity()
					.getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentManager.popBackStack();
			fragmentTransaction.commitAllowingStateLoss();
		}
		activeno = 0;

		getActiveNumberButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				regainActiveNumber();
			}
		});

		Message msg = new Message();
		msg.what = ActiveActivity.CHANGE_TITLE;
		msg.obj = getResources().getString(R.string.active_number_hint);
		handler2.sendMessage(msg);

		return v;
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message arg0) {
			try {
				sec--;
				if (sec == 0) {
					verificationTimer.cancel();
					regainActiveNumberText.setVisibility(View.GONE);
					getActiveNumberButton.setVisibility(View.VISIBLE);
					handler.sendEmptyMessage(ActiveActivity.SHOW_BACK_BUTTON);
				} else {
					regainActiveNumberText.setText(getResources().getString(
							R.string.regain_active_number_hint, sec));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	});

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.e("activeno:" + activeno);
	};

	private void resetTimer() {
		regainActiveNumberText.setVisibility(View.VISIBLE);
		getActiveNumberButton.setVisibility(View.GONE);
		verificationTimer = new Timer();
		sec = 60;
		regainActiveNumberText.setText(getString(
				R.string.regain_active_number_hint, sec));
		verificationTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitActiveNumberButton: // 提交
			submitActiveNumber();
			break;
		// case R.id.login_bak2:
		// Common.HideKeyboardIfExist(EnsureActiveNumberFragment.this);
		//
		// android.support.v4.app.FragmentManager fragmentManager =
		// getActivity().getSupportFragmentManager();
		// android.support.v4.app.FragmentTransaction fragmentTransaction =
		// fragmentManager.beginTransaction();
		// UserNameFragment userNameFragment = new UserNameFragment(handler2);
		// fragmentTransaction.replace(R.id.fragmentLayout2, userNameFragment);
		// fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		// fragmentTransaction.commitAllowingStateLoss();
		// break;
		}
	}

	private void submitActiveNumber() {
		if (activeNumberEditText.getText() == null
				|| activeNumberEditText.getText().toString().trim().length() != 6) {
			// 输入框为空
			Toast.makeText(getActivity(), R.string.active_number_null,
					Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("activeNumber", activeNumberEditText.getText().toString()
				.trim());
		params.put("phone",
				MySharedPreferencesEdit.getInstancePublic(getActivity())
						.getOwnerPhoneNumber());

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
								int state = response.getInt("state");
								FragmentManager fragmentManager = getActivity()
										.getFragmentManager();
								FragmentTransaction fragmentTransaction = fragmentManager
										.beginTransaction();
								PasswordSupportFragment userNameFragment = new PasswordSupportFragment(
										handler2);
								Bundle bundle = new Bundle();
								bundle.putInt("state", state);
								userNameFragment.setArguments(bundle);
								fragmentTransaction.replace(
										R.id.fragmentLayout2, userNameFragment);
								fragmentTransaction
										.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								fragmentTransaction.addToBackStack(null);
								fragmentTransaction.commitAllowingStateLoss();
								handler2.sendEmptyMessage(ActiveActivity.SHOW_BACK_BUTTON);
								if (verificationTimer != null) {
									verificationTimer.cancel();
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
									"EnsureActiveNumberFragment:网络异常1", Toast.LENGTH_LONG)
									.show();
						}
						super.onSuccess(statusCode, response);

						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						Toast.makeText(getActivity(), "EnsureActiveNumberFragment:网络异常2",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
						e.printStackTrace();
					}
				});
	}

	private void regainActiveNumber() {
		RequestParams params = new RequestParams();
		params.put("phone",
				MySharedPreferencesEdit.getInstancePublic(getActivity())
						.getOwnerPhoneNumber());
		CynovoHttpClient.post(getActivity(),
				"api/active_device/password/active_number.php", params,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						pDialog.show();
						super.onStart();
					}

					@Override
					public void onSuccess(JSONObject response) {
						super.onSuccess(response);
						MyLog.e("response:" + response);
						try {
							int ret = response.getInt("ret");
							if (ret == 0) {
								activeNumberEditText.setText("");
								activeNumberEditText.requestFocus();

								resetTimer();
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
									"EnsureActiveNumberFragment:网络异常3", Toast.LENGTH_LONG)
									.show();
						}
						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						e.printStackTrace();
						Toast.makeText(getActivity(), "EnsureActiveNumberFragment:网络异常4",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
					}
				});
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE
				|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.activeNumberEditText:
				submitActiveNumber();
				break;
			}
		}
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (verificationTimer != null) {
			verificationTimer.cancel();
		}
	}
}
