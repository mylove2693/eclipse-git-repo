package com.cynovo.sirius.PaySDK;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cynovo.sirius.button.state.ButtonContext;
import com.cynovo.sirius.button.state.CheclInButtonState;
import com.cynovo.sirius.database.PayRecordManager;
import com.cynovo.sirius.flow.Input;
import com.cynovo.sirius.flow.Input.TradeType;
import com.cynovo.sirius.parameter.AllPayParameters;
import com.cynovo.sirius.parameter.EmvParameters;
import com.cynovo.sirius.parameter.MsrParameters;
import com.cynovo.sirius.parameter.PayResultParameters;
import com.cynovo.sirius.parameter.ResponseParameters;
import com.cynovo.sirius.parameter.SafeParameters;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.util.NumberFormater;
import com.cynovo.sirius.view.PayButton;
import com.kivvi.jni.PrinterInterface;

public class PayMainActivity extends Activity implements OnClickListener {
	private TextView shopNameText;
	public static boolean PAY_OVER;
	public static String PAY_RESULT;
	private Button backButton;
	private PayButton swap_card_btn;
	private JSONObject jsonData;
	public static Handler handler;
	// handler 需要接收的消息类型
	public final static int MSG_CHECK_IN_SUCCESS = 0; // 签到ok
	public final static int MSG_CHECK_IN_FAILED = 3; // 签到error
	public final static int MSG_SWIP_CARD_SUCCESS = 1; // 刷卡成功
	public final static int MSG_SWIP_CARD_FAILED = 4; // 刷卡成功
	public final static int MSG_PAY_END = 2; // 交易完成
	public final static int MSG_PAY_FAILED = 5; // 交易失败
	private int payState;
	private ButtonContext buttonContext;
	private String payMoney;

	public static String currentDate;
	public static String currentTime;
	public static String sTraceNo;
	public static String operator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("debug", "begin PayMainActivity onCreate");
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_pay_main);
		shopNameText = (TextView) findViewById(R.id.shopNameText);
		
		Log.e("debug", "begin PayMainActivity onCreate point1");
		
		
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_CHECK_IN_SUCCESS: // 签到中
					payState = MSG_CHECK_IN_SUCCESS;
					swap_card_btn.refreshCurrentState(buttonContext,
							PayMainActivity.this);
					Log.e("debug", "PayMainActivity handler MSG_CHECK_IN_SUCCESS");
					break;
				case MSG_SWIP_CARD_SUCCESS: // 刷卡ok
					payState = MSG_SWIP_CARD_SUCCESS;
					swap_card_btn.refreshCurrentState(buttonContext,
							PayMainActivity.this);
					Log.e("debug", "PayMainActivity handler MSG_SWIP_CARD_SUCCESS");
					break;
				case MSG_PAY_END: // 交易完成
					payState = MSG_PAY_END;
					swap_card_btn.refreshCurrentState(buttonContext,
							PayMainActivity.this);
					Log.e("debug", "PayMainActivity handler MSG_PAY_END");
					Log.e("debug", "PayMainActivity MSG_PAY_END point1");

					AllPayParameters.mPayResult
							.setPayResultParameters(PayMainActivity.this);
					JSONObject json = AllPayParameters.mPayResult
							.getPayResultJson();
					MyLog.e("json : " + json);
					Log.e("debug", "PayMainActivity MSG_PAY_END point2");
					if (json != null) {
						Log.e("debug", "PayMainActivity MSG_PAY_END point3");
						PayMainActivity.PAY_RESULT = json.toString();
						Log.e("debug", PayMainActivity.PAY_RESULT);
						//PayMainActivity.PAY_OVER = true;
						MainService.PAYC_END = true;
						MainService.PAYC_SUCCESS = true;
						Log.e("debug", "PayMainActivity MSG_PAY_END point4");
						PayRecordManager.recordPayResult(PayMainActivity.this,
								AllPayParameters.mPayResult);
						PayMainActivity.this.finish();
						// added printer
						// ------------------------------------------------->begin
						int ret = PrinterInterface.open();
						if (ret < 0) {
							Log.e("debug",
									"PayMainActivity PrinterInterface open failed");
						}

						PrinterInterface.set(0);
						Log.e("debug",
								"PayMainActivity PrinterInterface write begin");
						PrintLineStr("-----------------------------\n");
						PrintLineStr("名称    数量    单价     全额\n"
								+ "-----------------------------\n");
						PrintLineStr("百香果酸奶茶\n"
								+ "           1    9.00    9.00\n"
								+ "-----------------------------\n");
						PrintLineStr("合计：        9.00\n" + "现金：        9.00\n"
								+ "找零：        0.00\n"
								+ "-----------------------------\n");
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd   hh:mm:ss");
						String date = sDateFormat.format(new java.util.Date());
						PrintLineStr("收据号:000001  \n" + "收银员:001  \n" + date
								+ "\n" + "<><><><><><><><><><><><><><>\n"
								+ "地址 \n\n\n\n\n");

						Log.e("debug",
								"PayMainActivity PrinterInterface write end");
						PrinterInterface.close();
						// added
						// printer---------------------------------------------------<end
						Log.e("debug", "PayMainActivity MSG_PAY_END point5");
					}
					break;
				case MSG_CHECK_IN_FAILED: // 签到失败！
					showErrorDialog(R.string.check_in_error);
					break;
				case MSG_SWIP_CARD_FAILED: // 刷卡失败！
					showErrorDialog(R.string.swipefail_prompt);
					break;
				case MSG_PAY_FAILED:
					if (msg.obj == null) {
						showErrorDialog(R.string.pay_error);
					} else {
						showErrorDialog(Integer.valueOf((String) msg.obj));
					}
					break;
				}
				return false;
			}
		});

		// 初始化参数池
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String json = bundle.getString("jsonData");
			try {
				jsonData = new JSONObject(json);
				MyLog.e("jsonData:" + jsonData);
				Input input = new Input();
				ResponseParameters params = new ResponseParameters();
				// 消费/撤销/退货的金额
				payMoney = jsonData.getString("ReqTransAmount");
				input.setAmount(NumberFormater.twelveNumber(payMoney)); //
				try {
					operator = jsonData.getString("ReqTransOperator");
					input.setOperator(operator); //
				} catch (Exception e1) {
					// 操作员号不传时，默认就是1
					input.setOperator("1");
					e1.printStackTrace();
				}

				// 消费时需要传入消费日期
				currentDate = jsonData.getString("ReqTransDate");
				input.setCurrentDate(currentDate);
				// 消费的时间
				currentTime = jsonData.getString("ReqTransTime");
				input.setCurrentTime(currentTime);
				// 消费时的交易流水号
				input.setTraceNo(NumberFormater.sixNumber(jsonData
						.getString("ReqTraceNo")));

				switch (jsonData.getInt("ReqTransType")) {
				case 0:
					input.setType(TradeType.Sale);
					params.setOriginalBatchNo(jsonData.getString("BatchNo"));
					shopNameText.setText(R.string.info_input_card);
					break;
				case 1:
					Log.e("debug","撤销交易");
					input.setType(TradeType.Void);
					shopNameText.setText(R.string.revocation_order);
					// 撤销时需要传入日期
					input.setsTraceDate(jsonData.getString("ReqsTransDate"));
					// 撤销时的交易流水号
					sTraceNo = NumberFormater.sixNumber(jsonData
							.getString("ReqsTraceNo"));
					input.setsTraceNo(sTraceNo);
					params.setOriginalAuthorizationNo(jsonData
							.getString("AuthorizationNo"));
					params.setOriginalReferencNo(jsonData
							.getString("ReferenceNo"));
					params.setOriginalBatchNo(jsonData.
							getString("BatchNo"));
					Log.e("debug","sTraceNo = "+ sTraceNo +", OriginalAuthorizationNo"+jsonData
							.getString("AuthorizationNo")+", OriginalReferencNo"+jsonData
							.getString("ReferenceNo"));
					break;
				case 2:
					input.setType(TradeType.Refund);
					shopNameText.setText(R.string.revocation_order);
					// 退货时需要传入日期
					input.setsTraceDate(jsonData.getString("ReqsTransDate")); //
					// 退货时的交易流水号
					sTraceNo = NumberFormater.sixNumber(jsonData
							.getString("ReqsTraceNo"));
					input.setsTraceNo(sTraceNo); //
					params.setOriginalAuthorizationNo(jsonData
							.getString("AuthorizationNo"));
					params.setOriginalReferencNo(jsonData
							.getString("ReferenceNo"));
					break;
				default:
					shopNameText.setText(R.string.info_input_card);
					input.setType(TradeType.Sale);
					break;
				}
				input.setCardType(jsonData.getInt("cardType"));
				try {
					input.setSkinType(jsonData.getInt("skinType")); // 可以不传
				} catch (Exception e) {
					e.printStackTrace();
				}

				AllPayParameters.setInput(input);
				AllPayParameters.setEmv(new EmvParameters());
				AllPayParameters.setMsr(new MsrParameters());
				AllPayParameters.setRep(params);
				AllPayParameters.setSafe(new SafeParameters());
				AllPayParameters.setPayResult(new PayResultParameters());
			} catch (Exception e) {
				e.printStackTrace();
				// 如果出错，弹出对话框
				showErrorDialog(R.string.init_params_error);
				return;
			}
		} else {
			showErrorDialog(R.string.json_cannot_null);
			return;
		}
		Log.e("debug", "begin PayMainActivity onCreate point2");

		initViews();

		Log.e("debug", "begin PayMainActivity onCreate point3");
	}

	public void PrintLineStr(String str) {

		if (str == null || str.isEmpty())
			return;
		try {
			byte[] tmp = str.getBytes("gb2312");
			int ret = PrinterInterface.write(tmp, tmp.length);
			if (ret < 0) {
				Log.e("debug", "PayMainActivity PrinterInterface write failed");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void showErrorDialog(int resID) {
		new AlertDialog.Builder(PayMainActivity.this)
				.setTitle(R.string.hint)
				.setMessage(resID)
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								PayMainActivity.PAY_RESULT = "";
								Log.e("debug",
										"PayMainActivity set PAY_RESULT to NULL");
								//PayMainActivity.PAY_OVER = true;
								MainService.PAYC_END = true;
								PayMainActivity.this.finish();
							}
						}).show();
	}

	private void initViews() {
		Log.e("debug", "begin PayMainActivity initViews");
		backButton = (Button) findViewById(R.id.backButton);
		TextView total_money_text = (TextView) findViewById(R.id.total_money_text);
		total_money_text.setText(payMoney);
		backButton.setOnClickListener(this);
		swap_card_btn = (PayButton) findViewById(R.id.swap_card_btn);
		// swap_card_btn.setOnClickListener(this);
		Log.e("debug", "begin PayMainActivity initViews point1");
		buttonContext = new ButtonContext(new CheclInButtonState());
		Log.e("debug", "begin PayMainActivity initViews point2");
		swap_card_btn.setClickable(false);
		// activity 的context是否能释放掉?? wanhaiping

		swap_card_btn.refreshCurrentState(buttonContext, PayMainActivity.this);
		Log.e("debug", "begin PayMainActivity initViews point3");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backButton:
			if (payState == MSG_SWIP_CARD_SUCCESS) { // 刷卡ok，正在交易，此时不能返回
				Toast.makeText(PayMainActivity.this, R.string.is_paying,
						Toast.LENGTH_SHORT).show();
			} else {
				new AlertDialog.Builder(this)
						.setTitle(R.string.hint)
						.setMessage(R.string.drop_pay)
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										PayMainActivity.this.finish();
										//PAY_OVER = true;
										MainService.PAYC_END = true;
										PAY_RESULT = null;
										Log.e("debug",
												"PayMainActivity set PAY_RESULT to null");
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
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// 回收参数池
		Log.e("debug", "begin PayMainActivity onDestroy");
		AllPayParameters.setEmv(null);
		AllPayParameters.setMsr(null);
		AllPayParameters.setRep(null);
		AllPayParameters.setSafe(null);
		AllPayParameters.setInput(null);
		AllPayParameters.setPayResult(null);
		super.onDestroy();
		Log.e("debug", "end PayMainActivity onDestroy");
	}
}
