package com.cynovo.sirius.PaySDK;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.cynovo.sirius.aidl.IPaySDK.Stub;
import com.cynovo.sirius.devices.device.DeviceEmv;
import com.cynovo.sirius.devices.device.DeviceHardPinpad;
import com.cynovo.sirius.devices.device.DeviceMsr;
import com.cynovo.sirius.devices.device.DeviceNetLink;
import com.cynovo.sirius.devices.device.DevicePrinter;
import com.cynovo.sirius.parameter.AllPayParameters;
//import com.cynovo.sirius.jni.PbocInterface;
//import com.cynovo.sirius.jni.PinPadInterface;
import com.cynovo.sirius.util.GetJniInfo;
import com.cynovo.sirius.util.MyLog;
import com.kivvi.jni.ISO8583Interface;
import com.kivvi.jni.MsrInterface;
//import com.cynovo.jni.PinPadInterface;
import com.kivvi.jni.PinPadInterface;
import com.kivvi.jni.EmvL2Interface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.unionpay.CloudPosPaymentClient.aidl.ICloudPay;
import com.cynovo.sirius.PaySDK.PaySelectActivity.PayMode;
import com.google.zxing.client.android.CaptureActivity;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cynovo.sirius.util.MD5;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.cynovo.sirius.util.WeiXinPay;
import com.cynovo.sirius.util.InterfacePara;
import com.cynovo.sirius.util.InterfacePara.CardMode;
import com.cynovo.sirius.util.InterfacePara.PaymentMode;
import com.cynovo.sirius.util.UninLinkPay;

public class MainService extends Service {
	private PaySDKBinder binder;
	private Context ctx;
	public static final String PAY_PERMISSION = "com.cynovo.Payment.permission.DemoPay";
	public static final String DEVICE_INFO_PERMISSION = "com.cynovo.Payment.permission.PosInfo";
	public static final String DEVICE_ACTIVE_PERMISSION = "com.cynovo.Payment.permission.Register";
	public static final String DEVICE_HWSTATE_PERMISSION = "com.cynovo.Payment.permission.HWState";
	public static boolean PAYC_SUCCESS;
	public static boolean PAYC_END;
	
	private ICloudPay Icloudpay;
	private ServiceConnection conn = null;
	
	//add by punan---------------------------->begin
	
	//private String traceNo = "000001";
	private String operatorNo = "999";
	private String terminalNo = "60101010";
	private String merchantNo = "874110145112613";
	//private String merchantNo = "123456789012345";
	//private String orgCode = "001115";
	
	//add by punan---------------------------->end

	public class PaySDKBinder extends Stub {
		private boolean IsGranted(String permission) {
			int uid = Binder.getCallingUid();
			int pid = Binder.getCallingPid();
			if (ctx.checkPermission(permission, pid, uid) == PackageManager.PERMISSION_GRANTED) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String payCash(String jsonData) throws RemoteException {
			Log.e("debug", "begin payCash");
			boolean grant = IsGranted(PAY_PERMISSION);
			MyLog.e("grant:" + grant);
			PAYC_SUCCESS = false;
			PAYC_END = false;
			if (grant && jsonData != null && jsonData.length() > 0) {
				Log.e("debug", "begin payCash point1");
					
				
			
				// modified by
				// wanhaiping------------------------------------------------>begin
				// Intent intent = new Intent(MainService.this,
				// PayMainActivity.class);
				/*
				 * Intent intent = new Intent(MainService.this,
				 * PaySelectActivity.class);
				 * 
				 * // modified by //
				 * wanhaiping----------------------------------
				 * --------------<end intent.putExtra("jsonData", jsonData);
				 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * MainService.this.startActivity(intent); Log.e("debug",
				 * "begin payCash point2");
				 * 
				 * // modified by //
				 * wanhaiping----------------------------------
				 * --------------------->begin
				 * 
				 * PaySelectActivity.PAY_SELECT = false;
				 * PayMainActivity.PAY_OVER = false;
				 * 
				 * while (!PaySelectActivity.PAY_SELECT) {
				 * 
				 * if (PayMainActivity.PAY_OVER) { return null; } try {
				 * Thread.yield(); } catch (Exception e) { Log.e("debug",
				 * "begin payCash point2.1"); e.printStackTrace(); } }
				 * Log.e("debug", "begin payCash point2.2");
				 * 
				 * if (PaySelectActivity.paymode == PayMode.payCYNOVO) {
				 * Log.e("debug", "begin payCash point3");
				 * PayMainActivity.PAY_OVER = false; while
				 * (!PayMainActivity.PAY_OVER) { try { Thread.yield(); } catch
				 * (Exception e) { Log.e("debug", "begin payCash point3.5");
				 * e.printStackTrace(); } } Log.e("debug",
				 * "begin payCash point4"); Log.e("debug",
				 * PayMainActivity.PAY_RESULT); return
				 * PayMainActivity.PAY_RESULT; }
				 * 
				 * else if (PaySelectActivity.paymode == PayMode.payCASH) {
				 * Log.e("debug", "begin payCash point5");
				 * 
				 * }
				 * 
				 * else if (PaySelectActivity.paymode == PayMode.payMISPOS) {
				 * Log.e("debug", "begin payCash point5.7"); }
				 * 
				 * else if (PaySelectActivity.paymode == PayMode.payONLINE) {
				 * Log.e("debug", "begin payCash weixinpay begin");
				 * CaptureActivity.PAY_OVER = false; while
				 * (!CaptureActivity.PAY_OVER) { try { Thread.yield(); } catch
				 * (Exception e) { e.printStackTrace(); } } Log.e("debug",
				 * "begin payCash weixinpay end");
				 * 
				 * } else if (PaySelectActivity.paymode == PayMode.payUNIONPAY)
				 * { Log.e("debug", "begin payCash point8");
				 * 
				 * PaySelectActivity.PAY_OVER = false;
				 * 
				 * while (!PaySelectActivity.PAY_OVER) { try { Thread.yield(); }
				 * catch (Exception e) { Log.e("debug",
				 * "begin payCash point2.1"); e.printStackTrace(); } }
				 * Log.e("debug", "begin payCash point8.5"); }
				 */
				// added by wanhaiping 4.13------------------------>begin

				InterfacePara paypara;
				paypara = new InterfacePara();
				boolean success = paypara.ParseInput(jsonData);
				if (!success) {
					Log.e("debug", "check json unsucess! return");
					return null;
				}
				
				if (paypara.paymode == PaymentMode.CardPay)
				{
					//根据配置情况，选用一种支付方式
					/*
					Log.e("debug", "begin payCash CardPay");
					try 
					{
						UninLinkPay.StartPay1(paypara.strAccountUnionLink, MainService.this, Icloudpay);
					} 
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
																
					Intent intent = new Intent(MainService.this, PayMainActivity.class);
					intent.putExtra("jsonData", jsonData);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					MainService.this.startActivity(intent);
							
						
				}

				else if (paypara.paymode == PaymentMode.WeiXin) {
					Log.e("debug", "begin payCash WeiXin");
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(MainService.this, CaptureActivity.class);
					startActivity(intent);
			
				} else {
					return null;
				}

				while (!PAYC_END) {
					try {
						Thread.yield();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// added by wanhaiping 4.13------------------------<end

				Log.e("debug", "begin payCash point9");
				JSONObject json = new JSONObject();
				try {

					json.put("Acquirer", AllPayParameters.mPayResult.getAcquirer());
					json.put("AuthorizationNo", AllPayParameters.mPayResult.getAuthorizationNo());
					json.put("BatchNo", AllPayParameters.mPayResult.getBatchNo());
					json.put("CardNo", AllPayParameters.mPayResult.getCardNo());
					json.put("ClearDate", AllPayParameters.mPayResult.getClearDate());
					json.put("Issuer", AllPayParameters.mPayResult.getIssuer());
					json.put("MerchantNo", AllPayParameters.mPayResult.getMerchantNo());
					json.put("ReferenceNo", AllPayParameters.mPayResult.getReferenceNo());
					json.put("ReqTransAmount", AllPayParameters.mPayResult.getReqTransAmount());
					json.put("ReqTransDate", AllPayParameters.mPayResult.getReqTransDate());
					json.put("ReqTransOperator", AllPayParameters.mPayResult.getReqTransOperator());
					json.put("ReqTransTime", AllPayParameters.mPayResult.getReqTransTime());
					json.put("ReqTransType", AllPayParameters.mPayResult.getReqTransType());
					json.put("RespCode", AllPayParameters.mPayResult.getRespCode());
					json.put("RespDesc", AllPayParameters.mPayResult.getRespDesc());
					json.put("RespTransDate", AllPayParameters.mPayResult.getRespTransDate());
					json.put("RespTransTime", AllPayParameters.mPayResult.getRespTransTime());
					json.put("TerminalNo", AllPayParameters.mPayResult.getTerminalNo());
					json.put("TraceNo", AllPayParameters.mPayResult.getTraceNo());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("debug", "begin payCash point10");
					e.printStackTrace();
				}
				Log.e("debug", "end payCash");
				if (PAYC_SUCCESS) {
					return json.toString();
				} else {
					return null;
				}

			} else {
				// 参数错误，直接返回null
				Log.e("debug", "begin payCash point11");
				return null;
			}
		}

		@Override
		public String registerPOS(String jsonData) throws RemoteException {
			Log.e("debug", "begin registerPOS");
			boolean grant = IsGranted(DEVICE_INFO_PERMISSION);
			MyLog.e("grant:" + grant);
			if (grant && jsonData != null && jsonData.length() > 0) {
				Log.e("debug", "begin registerPOS point1");
				try {
					Intent intent = new Intent(MainService.this,
							ActiveActivity.class);
					intent.putExtra("jsonData", jsonData);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Log.e("debug", "begin registerPOS point2");
					MainService.this.startActivity(intent);
					ActiveActivity.ACTIVE_OVER = false;
					Log.e("debug", "begin registerPOS point3");
					while (!ActiveActivity.ACTIVE_OVER) {
						try {
							Thread.yield();
						} catch (Exception e) {
							e.printStackTrace();

						}
					}
					Log.e("debug", "begin registerPOS point4");
					return ActiveActivity.ACTIVE_RESULT;
				} catch (Exception e) {
					Log.e("debug", "begin registerPOS point5");
					e.printStackTrace();
					return null;
				}
			} else {
				// 参数错误，直接返回null
				Log.e("debug", "begin registerPOS point6");
				return null;
			}
		}

		@Override
		public String getPOSInfo(String jsonData) throws RemoteException {
			boolean grant = IsGranted(DEVICE_INFO_PERMISSION);
			if (grant && jsonData != null && jsonData.length() > 0) {
				try {
					String mid = GetJniInfo.getMID();
					String tid = GetJniInfo.getTID();
					String cpuid = GetJniInfo.getCPUID();
					JSONObject json = new JSONObject();
					json.put("MerchantID", mid);
					json.put("TerminaID", tid);
					json.put("CPUID", cpuid);
					return json.toString();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				// 参数错误，直接返回null
				return null;
			}
		}

		@Override
		public String getHardWareState() throws RemoteException {
			MyLog.e("getHardWareState");
			boolean grant = IsGranted(DEVICE_HWSTATE_PERMISSION);
			MyLog.e("grant:" + grant);
			if (grant) {
				PinPadInterface.open();

				// modified by
				// wanhaiping------------------------------------------->begin
				// PbocInterface.open();
				// EmvL2Interface.openReader();

				// modified by
				// wanhaiping--------------------------------------------<end

				JSONObject json = new JSONObject();
				DeviceEmv emv = new DeviceEmv(null, MainService.this);
				emv.checkState();
				boolean enable1 = emv.isEnabled();

				DeviceMsr msr = new DeviceMsr(null, MainService.this);
				msr.checkState();
				boolean enable2 = msr.isEnabled();

				DeviceHardPinpad pinpad = new DeviceHardPinpad(null,
						MainService.this);
				pinpad.checkState();
				boolean enable3 = pinpad.isEnabled();

				DevicePrinter printer = new DevicePrinter(null,
						MainService.this);
				printer.checkState();
				boolean enable4 = printer.isEnabled();

				DeviceNetLink network = new DeviceNetLink(null,
						MainService.this);
				network.checkState();
				boolean enable5 = network.isEnabled();

				try {
					json.put("emv", enable1);
					json.put("msr", enable2);
					json.put("pinpad", enable3);
					json.put("printer", enable4);
					json.put("network", enable5);
				} catch (Exception e) {
					e.printStackTrace();
					try {
						json.put("emv", false);
						json.put("msr", false);
						json.put("pinpad", false);
						json.put("printer", false);
						json.put("network", false);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

				return json.toString();
			} else {
				return null;
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//add by punan--------------------------------------->begin
		
		// 打开ISO8583库
		ISO8583Interface.open();

		// 打开密码键盘
		int ret = PinPadInterface.open();
		if (ret >= 0) {
			boolean pinpadFlag = true;
			Log.i("debug", "pinpadFlag:"+pinpadFlag);
		}

		// 打开MSR
		ret = MsrInterface.open();
		if (ret >= 0) {
			boolean msrFlag = true;
			Log.i("debug", "msrFlag:"+msrFlag);
		}
		
		ret = EmvL2Interface.emvKernelInit();
		if (ret < 0) {
			Log.i("debug", "load kernel failed");
		}

		ISO8583Interface.setEnableLog(true);
		ISO8583Interface.setCurrencyCode("156");
		//ISO8583Interface.setIP("116.31.92.201");
		//ISO8583Interface.setPort((short) 15214);
		ISO8583Interface.setIP("119.161.147.124");
		ISO8583Interface.setPort((short) 28000);
		ISO8583Interface.setTPDU("6000040000");
		
		ISO8583Interface.setOperator("999");
		ISO8583Interface.setNetworkTimeout(75, 0);
		byte[] bTerminalCode = terminalNo.getBytes();
		ISO8583Interface.setTerminalCode(bTerminalCode);
		byte[] bMerchantCode = merchantNo.getBytes();
		ISO8583Interface.setMerchantCode(bMerchantCode);
		//ISO8583Interface.setOrgCode(orgCode);
		
		//add by punan---------------------------------------->end
		
		//added by wanhaiping---------------------------------------------------------------->begin
		conn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				Icloudpay = null;
				Log.e("debug", "UninLinkPay onServiceDisconnected");
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// IPaySDK.Stub.asInterface，获取接口
				Icloudpay = ICloudPay.Stub.asInterface(service);
				Log.e("debug", "UninLinkPay onServiceConnected");
		
			}
	    };
	  				    
	    
	    Log.e("debug", "InitUninLinkPay bindService begin");
	    
	 
		boolean retbind = bindService(new Intent(ICloudPay.class.getName()), conn, Context.BIND_AUTO_CREATE);
		if(retbind)
		{
			Log.e("debug", "UninLinkPay Connect Successfully");
		
		}
		else
		{
			Log.e("debug", "UninLinkPay Connect Failed");
			
		}
		
		//added by wanhaiping----------------------------------------------------------------<end
		
		ctx = super.getApplicationContext();
		// 实例化Binder对象
		binder = new PaySDKBinder();
		MyLog.e("MainService onCreate");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onDestroy() {
		MyLog.e("MainService onDestroy");
		
		if (Icloudpay != null) 
		{
			unbindService(conn);
		}
		Icloudpay = null;

		super.onDestroy();

	}
}
