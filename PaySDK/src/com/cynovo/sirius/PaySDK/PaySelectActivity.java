package com.cynovo.sirius.PaySDK;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.google.zxing.client.android.CaptureActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.unionpay.CloudPosPaymentClient.aidl.ICloudPay;

public class PaySelectActivity extends Activity implements OnClickListener {
	private Button btnCASH, btnCARD, btnONLINE;
	private TextView edtAmountText;
	private String json;
	public static PayMode paymode;
	public static boolean PAY_OVER;

	public static boolean PAY_ERROR;

	public static boolean PAY_SELECT;
	private CradMode cardmode;
	private ICloudPay Icloudpay;

	private ServiceConnection conn = null;
	private Handler handler = null;
	private String strAccountUnionLink;
	private String strAccountWeixin;

	private ProgressDialog p_dialog;
	private AlertDialog longinDialog;
	private View SignDialogView;
	private String strUserName;
	private String strPassword;

	public static boolean PAY_SUCCESS;
	public static String PAY_DESC;

	public enum CradMode {
		CARDCYNOVO, CARDUNIONPAY, CARDMISPOS
	}

	public enum PayMode {
		payCYNOVO, payUNIONPAY, payONLINE, payMISPOS, payCASH
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("debug", "begin PaySelectActivity onCreate");
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_payselect);

		paymode = PayMode.payCYNOVO;
		cardmode = CradMode.CARDUNIONPAY;

		PAY_ERROR = false;

		btnCASH = (Button) findViewById(R.id.btnCASH);
		btnCARD = (Button) findViewById(R.id.btnCARD);
		btnONLINE = (Button) findViewById(R.id.btnONLINE);
		// btnSign = (Button) findViewById(R.id.btnSign);

		edtAmountText = (TextView) findViewById(R.id.amountEditText);

		btnCASH.setOnClickListener(this);
		btnCARD.setOnClickListener(this);
		btnONLINE.setOnClickListener(this);
		// btnSign.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			json = bundle.getString("jsonData");
		}
		Icloudpay = null;

		// ////////////////////////////////////////////////////////////////////////////////

		p_dialog = null;
		longinDialog = null;
		SignDialogView = null;
		strUserName = "";
		strPassword = "";

		// ////////////////////////////////////////////////////////////////////////////////
		String strTempAccount = "";
		try {
			String temp = json.toString();
			JSONObject jsonObjectRet = new JSONObject(temp);
			strTempAccount = jsonObjectRet.getString("ReqTransAmount");
			Log.e("debug", strTempAccount);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("debug", "Failed get Account");
		}

		String amountShow = "金额:";
		amountShow += strTempAccount;
		amountShow += "元";
		edtAmountText.setText(amountShow);

		int location = strTempAccount.indexOf('.');
		if (location != -1) {
			String left = strTempAccount.substring(0, location);
			String right = strTempAccount.substring(location + 1,
					strTempAccount.length());
			strTempAccount = left + right;
		}
		strAccountWeixin = strTempAccount;

		int length = strTempAccount.length();
		String strZeor = "";
		if (length < 12) {
			int ZeroLen = 12 - length;
			for (int i = 0; i < ZeroLen; i++) {
				strZeor += "0";
			}
		}

		strZeor += strTempAccount;
		strAccountUnionLink = strZeor;
		Log.e("debug", "generate Account");
		Log.e("debug", strAccountUnionLink);

		// Toast.makeText(PaySelectActivity.this, strAccount,
		// Toast.LENGTH_LONG).show();

		// /////////////////////////////////////////////////////////////////////////////////////////////

		handler = new Handler() {
			public void handleMessage(Message msg) {
				int msgwhat = msg.what;
				if (msgwhat == 1) {

					try {

					}

					catch (Exception e) {
						e.printStackTrace();
					}
				}

				else if (msgwhat == 2) {
					Toast.makeText(PaySelectActivity.this, "签到成功",
							Toast.LENGTH_SHORT).show();
				}

				else if (msgwhat == 3) {
					Toast.makeText(PaySelectActivity.this, "签到失败",
							Toast.LENGTH_SHORT).show();
				}

				super.handleMessage(msg);
			}
		};

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
		boolean ret = bindService(new Intent(ICloudPay.class.getName()), conn,
				Context.BIND_AUTO_CREATE);
		if (ret) {
			Log.e("debug", "Connect DUNIONPAY Successfully");
		} else {
			Log.e("debug", "Connect DUNIONPAY Failed");
		}

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;

		switch (v.getId()) {
		case R.id.btnCARD:

			if (cardmode == CradMode.CARDCYNOVO) {
				Log.e("debug", "onClick select payCYNOVO");
				paymode = PayMode.payCYNOVO;
				intent = new Intent(PaySelectActivity.this,
						PayMainActivity.class);
				intent.putExtra("jsonData", json);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				PaySelectActivity.this.startActivity(intent);
				PaySelectActivity.this.finish();

			}

			else if (cardmode == CradMode.CARDMISPOS) {
				Log.e("debug", "onClick select payMISPOS");
				PaySelectActivity.this.finish();
				paymode = PayMode.payMISPOS;

			}

			else if (cardmode == CradMode.CARDUNIONPAY) {
				paymode = PayMode.payUNIONPAY;
				Log.e("debug", "onClick select payUNIONPAY");

				new Thread(new Runnable() {
					public void run() {

						try {
							StartPay();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();

			}

			PAY_SELECT = true;
			break;

		case R.id.btnCASH:
			paymode = PayMode.payCASH;
			Log.e("debug", "onClick select payCASH");
			PAY_OVER = true;
			// intent = new Intent();
			// intent.setClass(PaySelectActivity.this, EmvActivity.class);
			// startActivity(intent);
			PaySelectActivity.this.finish();

			// int nPid = android.os.Process.myPid();
			// android.os.Process.killProcess(nPid);
			// System.exit(0);
			PAY_SELECT = true;
			break;

		case R.id.btnONLINE:
			Log.e("debug", "select ONLINE Payment");
			paymode = PayMode.payONLINE;
			Log.e("debug", "onClick select payONLINE");
			intent = new Intent();
			intent.setClass(PaySelectActivity.this, CaptureActivity.class);
			startActivity(intent);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PaySelectActivity.this.finish();
			PAY_SELECT = true;
			break;

		// case R.id.btnSign:

		/*
		 * Log.e("debug", "onClick select signin");
		 * 
		 * new Thread(new Runnable() { public void run() {
		 * 
		 * try { SignIn(); } catch (JSONException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 * 
		 * } } ).start();
		 * ///////////////////////////////////////////////////////
		 * ////////////////////////////////// p_dialog = null; longinDialog =
		 * null; SignDialogView = null; strUserName = ""; strPassword = "";
		 * 
		 * // 动态加载布局生成View对象 LayoutInflater layoutInflater =
		 * LayoutInflater.from(PaySelectActivity.this); SignDialogView =
		 * layoutInflater.inflate(R.layout.dialog, null); Log.e("debug",
		 * "onClick select signin point1");
		 * 
		 * // 创建一个AlertDialog对话框 longinDialog = new AlertDialog.Builder(this)
		 * .setTitle("签到") .setView(SignDialogView) // 加载自定义的对话框式样
		 * .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { //
		 * TODO Auto-generated method // stub
		 * 
		 * // 获取布局中的控件 EditText mUserName = (EditText) SignDialogView
		 * .findViewById(R.id.AccountEditText); EditText mPassword = (EditText)
		 * SignDialogView .findViewById(R.id.PasswordEidtText);
		 * 
		 * strUserName = mUserName.getText().toString(); strPassword =
		 * mPassword.getText().toString();
		 * 
		 * p_dialog = ProgressDialog.show( PaySelectActivity.this, "请等待",
		 * "正在为您登录...", true); new Thread() { public void run() { int ret = 0;
		 * try { //sleep(3000); ret = SignIn(); Message message = new Message();
		 * if(ret == 1) { message.what = 2; } else { message.what = 3; }
		 * handler.sendMessage(message); } catch (Exception e) {
		 * e.printStackTrace(); } finally { p_dialog.dismiss();
		 * 
		 * } } }.start(); } })
		 * 
		 * .setNeutralButton( "取消", new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick( DialogInterface dialog, int which) {
		 * // TODO Auto-generated method // stub // Android_DialogActivity.this
		 * // .finish(); } }) .create(); Log.e("debug",
		 * "onClick select signin point2"); longinDialog.show(); Log.e("debug",
		 * "onClick select signin point3");
		 * 
		 * //////////////////////////////////////////////////////////////////////
		 * /////////////////// break;
		 */

		default:
			break;
		}

	}

	public void onDestroy() {
		Log.e("debug", "PaySelectActivity onDestroy begin");
		if (Icloudpay != null) {
			unbindService(conn);
		}
		Icloudpay = null;
		super.onDestroy();
		Log.e("debug", "PaySelectActivity onDestroy end");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			PAY_OVER = true;
			PAY_ERROR = true;
			PaySelectActivity.this.finish();
		}
		return false;
	}

	void StartPay() throws JSONException {

		Log.e("debug", "begin UNIONPAY StartPay");
		if (Icloudpay == null) {
			Log.e("debug", "Icloudpay is null");
			return;
		}

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mode", "01");
			jsonObject.put("AppID", "50019");
			jsonObject.put("AppName", "ClPAR10");
			jsonObject.put("TransIndexCode", ""
					+ MySharedPreferencesEdit.getInstancePublic(this)
							.getTraceNO());
			MySharedPreferencesEdit.getInstancePublic(this).setTraceNO(
					MySharedPreferencesEdit.getInstancePublic(this)
							.getTraceNO() + 1);

			// 金额
			// jsonObject.put("TransAmount", "000000000002");
			jsonObject.put("TransAmount", strAccountUnionLink);

			String ret;
			Log.e("debug", "payCash UNIONPAY  input value:");
			Log.e("debug", jsonObject.toString());
			ret = Icloudpay.payCash(jsonObject.toString());
			Log.e("debug", "payCash UNIONPAY return value:");
			Log.e("debug", ret);
			Log.e("debug", "end UNIONPAY payCash");

			JSONObject jsonObj = null;
			String strRespCode = null;
			String strRespDesc = null;
			boolean success = false;

			try {
				Log.e("debug", "get RespCode point1");
				if (ret != null) {
					jsonObj = new JSONObject(ret);
					Log.e("debug", "get RespCode point2");
				}
				if (jsonObj != null) {
					Log.e("debug", "get RespCode point3");
					strRespCode = jsonObj.getString("RespCode");
					strRespDesc = jsonObj.getString("RespDesc");
					if (strRespDesc == null) {
						strRespDesc = "";
					}
				}
				if (strRespCode != null) {
					Log.e("debug", "get RespCode point4");
					Log.e("debug", "strRespCode:");
					Log.e("debug", strRespCode);

					if (strRespCode.equals("00")) {
						Log.e("debug", "get RespCode point5");
						success = true;
						MainService.PAYC_SUCCESS = true;
					}
				}
			} catch (JSONException e) {

				Log.e("debug", "get RespCode point6");
				e.printStackTrace();
			}

			PaySelectActivity.this.finish();
			PAY_OVER = true;

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	int SignIn() throws JSONException {

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
			Log.e("debug", "end signIn");

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

	}

}
