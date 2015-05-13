package com.cynovo.sirius.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.cynovo.sirius.PaySDK.MainService;
import com.cynovo.sirius.PaySDK.PaySelectActivity;
import com.cynovo.sirius.PaySDK.WeinXinActivity;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.google.zxing.client.android.CaptureActivity;
import com.unionpay.CloudPosPaymentClient.aidl.ICloudPay;
//import com.unionpay.CloudPosPaymentClient.aidl.ICloudPay;


public class UninLinkPay {
	
	//private ICloudPay Icloudpay;
	//private ServiceConnection conn = null;
	//private Handler handler = null;
	//String strAccount;
				
			
	/*public boolean StartPay(String strAccountUnionLink, Context context) throws JSONException
	{
		boolean boolret ;
		Log.e("debug", "begin  UninLinkPay StartPay");
		strAccount = strAccountUnionLink;
		boolret = InitUninLinkPay(context);
		if(!boolret)
		{
			MainService.PAYC_END =  true;
			return false;
		}
				
		while(Icloudpay == null)
		{
			try 
			{
				Thread.yield();
			} catch (Exception e) 
			{
				Log.e("debug", "StartPay Thread yield Exception");
				e.printStackTrace();
			}
		}
				
		Log.e("debug", "begin UninLinkPay StartPay");
		boolean success = false;
		if(Icloudpay == null)
		{
			Log.e("debug", "Icloudpay is null");
		}
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mode", "01");
			jsonObject.put("AppID", "50019");
			jsonObject.put("AppName", "ClPAR10");
			jsonObject.put("TransIndexCode", ""
					+ MySharedPreferencesEdit.getInstancePublic(context)
							.getTraceNO());
			MySharedPreferencesEdit.getInstancePublic(context).setTraceNO(
					MySharedPreferencesEdit.getInstancePublic(context)
							.getTraceNO() + 1);
														
			//金额
			//jsonObject.put("TransAmount", "000000000002");
			jsonObject.put("TransAmount", strAccountUnionLink);
												
			String ret;			
			Log.e("debug", "UninLinkPay pay input value:");
			Log.e("debug", jsonObject.toString());
			ret = Icloudpay.payCash(jsonObject.toString());
			Log.e("debug", "UninLinkPay pay return value:");
			Log.e("debug", ret);
			Log.e("debug", "end UninLinkPay pay");
									
			JSONObject jsonObj = null;
			String strRespCode = null;
			String strRespDesc = null;
															
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
					if(strRespDesc == null)
					{
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
		} 
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
				
		UnInitUninLinkPay(context);
		MainService.PAYC_END =  true;
		return success;
	}*/
	
	
	
	public static boolean StartPay1(String strAccountUnionLink, Context context, ICloudPay Icloudpay) throws JSONException
	{
		boolean boolret ;
		Log.e("debug", "begin  UninLinkPay StartPay");
				
		boolean success = false;
		if(Icloudpay == null)
		{
			MainService.PAYC_END =  true;
			Log.e("debug", "Icloudpay is null");
			return false;
		}
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mode", "01");
			jsonObject.put("AppID", "50019");
			jsonObject.put("AppName", "ClPAR10");
			jsonObject.put("TransIndexCode", ""
					+ MySharedPreferencesEdit.getInstancePublic(context)
							.getTraceNO());
			MySharedPreferencesEdit.getInstancePublic(context).setTraceNO(
					MySharedPreferencesEdit.getInstancePublic(context)
							.getTraceNO() + 1);
														
			//金额
			//jsonObject.put("TransAmount", "000000000002");
			jsonObject.put("TransAmount", strAccountUnionLink);
												
			String ret;			
			Log.e("debug", "UninLinkPay pay input value:");
			Log.e("debug", jsonObject.toString());
			ret = Icloudpay.payCash(jsonObject.toString());
			Log.e("debug", "UninLinkPay pay return value:");
			Log.e("debug", ret);
			Log.e("debug", "end UninLinkPay pay");
									
			JSONObject jsonObj = null;
			String strRespCode = null;
			String strRespDesc = null;
															
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
					if(strRespDesc == null)
					{
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
		} 
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		MainService.PAYC_END =  true;
		return success;
	}
		
		
	/*

	private boolean InitUninLinkPay(Context context)
	{
		Log.e("debug", "begin  UninLinkPay InitUninLinkPay");
		//Icloudpay = null;
		handler = new Handler()
		{  
			public void handleMessage(Message msg)
			{   
				int msgwhat = msg.what;
				if( msgwhat == 1)
				{
					Log.e("debug", "WeinXinActivity receive finish message");
				}
				super.handleMessage(msg);  
			}           
		};   
		
		Log.e("debug", "begin  UninLinkPay InitUninLinkPay point1");
				
		
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
	    
	    return false;
	    /*
		boolean ret = context.bindService(new Intent(ICloudPay.class.getName()), conn, Context.BIND_AUTO_CREATE);
		if(ret)
		{
			Log.e("debug", "UninLinkPay Connect Successfully");
			return true;
		}
		else
		{
			Log.e("debug", "UninLinkPay Connect Failed");
			return false;
		}
	}*/
		
	/*private void UnInitUninLinkPay(Context context)
	{
		Log.e("debug", "UninLinkPay UnInitUninLinkPay begin");
		if (Icloudpay != null) 
		{
			context.unbindService(conn);
		}
		Icloudpay = null;
		Log.e("debug", "UninLinkPay UnInitUninLinkPay end");
	}*/
	
	
}
