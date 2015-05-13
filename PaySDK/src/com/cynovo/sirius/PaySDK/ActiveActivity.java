package com.cynovo.sirius.PaySDK;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.cynovo.sirius.fragment.UserNameFragment;
import com.cynovo.sirius.util.GetJniInfo;
import com.kivvi.jni.ISO8583Interface;

@SuppressLint("NewApi")
public class ActiveActivity extends FragmentActivity implements OnClickListener {
	public static boolean ACTIVE_OVER;
	public static String ACTIVE_RESULT;
	public static Handler handler;
	public static final int ACTIVE_SUCCESS = 0;
	public static final int ACTIVE_ERROR = 1;
	public static final int SHOW_BACK_BUTTON = 10;
	public static final int HIDE_BACK_BUTTON = 11;
	public static final int CHANGE_TITLE = 12;
	public static final int CHANGE_HINT_TEXT = 13;
	public static final String USER_SELECT = "USER_SELECT";
	public static final String USER_LOGIN = "USER_LOGIN";
	public static final String FORGET_PSW = "FORGET_PSW";
	public static final String NEW_PSW = "NEW_PSW";
	private Button backbtn;
	private TextView titleText;
	public static boolean isDownloadKey = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.active);
		isDownloadKey = false;

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case SHOW_BACK_BUTTON:
					// 显示返回按钮
					backbtn.setVisibility(View.VISIBLE);
					break;
				case HIDE_BACK_BUTTON:
					// 隐藏返回按钮
					backbtn.setVisibility(View.INVISIBLE);
					break;
				case CHANGE_TITLE:
					String title = (String) msg.obj;
					titleText.setText(title);
					break;
				case ACTIVE_SUCCESS:
					Log.e("debug", "Recv message ACTIVE_SUCCESS begin");
					JSONObject json = new JSONObject();
					try {
						Log.e("debug", "Recv message ACTIVE_SUCCESS point1");

						// modified by
						// wanhaiping--------------------------------------->begin
						/*
						 * json.put("mid", MySharedPreferencesEdit.
						 * getInstancePublic
						 * (ActiveActivity.this).getMerchantNo());
						 * json.put("tid", MySharedPreferencesEdit.
						 * getInstancePublic
						 * (ActiveActivity.this).getTerminalNo());
						 * json.put("cpuid", GetJniInfo.getCPUID());
						 * json.put("storeid", MySharedPreferencesEdit.
						 * getInstancePublic(ActiveActivity.this).getStoreID());
						 */
						// modified by
						// wanhaiping---------------------------------------<end

						String BatchNo = new String(ISO8583Interface.getBatchNo());
						
						json.put("mid", "001");
						json.put("tid", "001");
						json.put("cpuid", "001");
						json.put("storeid", "001");
						json.put("BatchNo", BatchNo);

						Log.e("debug", "Recv message ACTIVE_SUCCESS point2");
						ACTIVE_RESULT = json.toString();
						Log.e("debug", "ACTIVE_SUCCESS ACTIVE_RESULT= "+ACTIVE_RESULT);
					} catch (JSONException e) {
						Log.e("debug", "Recv message ACTIVE_SUCCESS point4");
						e.printStackTrace();
						ACTIVE_RESULT = "";
					}
					Log.e("debug", "Recv message ACTIVE_SUCCESS point5");
					ACTIVE_OVER = true;
					ActiveActivity.this.finish();
					Log.e("debug", "Recv message ACTIVE_SUCCESS point6");
					break;
				// added by
				// wanhaiping----------------------------------------------------->begin
				case ACTIVE_ERROR:
					Log.e("debug", "Recv message ACTIVE_ERROR begin");
					Toast.makeText(ActiveActivity.this, "登入失败",
							Toast.LENGTH_SHORT).show();
					Log.e("debug", "Recv message ACTIVE_ERROR end");
					break;

				// added by
				// wanhaiping-----------------------------------------------------<end

				}
				return false;
			}
		});

		backbtn = (Button) findViewById(R.id.login_bak1);
		backbtn.setOnClickListener(this);
		titleText = (TextView) findViewById(R.id.titleText);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		UserNameFragment userNameFragment = new UserNameFragment(handler);
		fragmentTransaction.replace(R.id.fragmentLayout2, userNameFragment);
		fragmentTransaction.addToBackStack("UserNameFragment");
		fragmentTransaction.commitAllowingStateLoss();

		TextView shopNameText = (TextView) findViewById(R.id.shopNameText);
		shopNameText.setText(R.string.active_device);
		Button backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_bak1:
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.popBackStack();
			break;
		case R.id.backButton:
			if (isDownloadKey) { // 正在下载主密钥，此时不能返回
				Toast.makeText(ActiveActivity.this,
						R.string.is_downloading_key, Toast.LENGTH_SHORT).show();
			} else {
				new AlertDialog.Builder(this)
						.setTitle(R.string.hint)
						.setMessage(R.string.drop_active)
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										ActiveActivity.this.finish();
										ACTIVE_RESULT = null;
										ACTIVE_OVER = true;
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();
			}
			break;
		}
	}

	/**
	 * 屏蔽返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ACTIVE_OVER = true;
			ACTIVE_RESULT = null;
			ActiveActivity.this.finish();
		}
		return false;
	}
}
