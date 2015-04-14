package com.cynovo.paysetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class DataUtil {

	public static final String TAG = "PaySetting";
	public static final String PREFERENCE_DATA = "payconfig";
	public static final String PREFERENCE_VERIFY = "payverify";
	public static final String PASSWORD = "12345678";
	
	public static final String KEY_YINLIAN = "yinlian";
	public static final String KEY_ZHIFUBAO = "zhifubao";
	public static final String KEY_WEIXIN = "weixin";
	public static final String KEY_YIBAO = "yibao";
	
	public static final String KEY_VERIFY = "verify";
	
	private SharedPreferences PaySettingPreference;
	private SharedPreferences PayVerifyPreference;
	private Editor editor_setting;
	private Editor editor_verify;

	private Context context;
	
	public DataUtil(Context context){

		this.context = context;
		
		PaySettingPreference = this.context.getSharedPreferences(
				PREFERENCE_DATA, Context.MODE_PRIVATE);
		editor_setting = PaySettingPreference.edit();
		
		PayVerifyPreference = this.context.getSharedPreferences(
				PREFERENCE_VERIFY, Context.MODE_PRIVATE);
		editor_verify = PayVerifyPreference.edit();
		
	}
	
	protected void putBoolean(String key, boolean value){
		try{
				
			byte[] encrypt = DESCrypt.encryptDES(PASSWORD,String.valueOf(value));
			String enStr = DESCrypt.parseByte2HexStr(encrypt);
			editor_setting.putString(key, enStr);
			editor_setting.commit();
		
			int hashcode = PaySettingPreference.hashCode();
			byte[] enverify = DESCrypt.encryptDES(PASSWORD,String.valueOf(hashcode));
			String enverStr = DESCrypt.parseByte2HexStr(enverify);
			editor_verify.putString(KEY_VERIFY, enverStr);
			editor_verify.commit();
			
		}catch (Exception e){
				
			e.printStackTrace();
		}
	}
	
	protected boolean getBoolean(String key, boolean value){
		
		if(!VerifyPreference()){
			Toast.makeText(context, "错误：数据被修改", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		Toast.makeText(context, "Verify Hash Code Pass", Toast.LENGTH_SHORT).show();;
		
		try{
		
			String enStr = PaySettingPreference.getString(key, getEncryptBoolean(false));
			byte[] encrypt = DESCrypt.parseHexStr2Byte(enStr);
			byte[] decrypt = DESCrypt.decryptDES(PASSWORD,new String(encrypt));
			boolean Enable = Boolean.valueOf(new String(decrypt));
			return Enable;
		
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	protected boolean VerifyPreference(){
		try{
			String enHashCode = PayVerifyPreference.getString(KEY_VERIFY, "false");
			if("false".equals(enHashCode)){
				Toast.makeText(context, "First time run", Toast.LENGTH_SHORT).show();
				return true;
			}
			
			byte[] enverify = DESCrypt.parseHexStr2Byte(enHashCode);
			byte[] deverify = DESCrypt.decryptDES(PASSWORD,new String(enverify));
		
			int hashcode = Integer.valueOf(new String(deverify));
		
			return PaySettingPreference.hashCode() == hashcode;
			
		} catch (Exception e){
			return false;
		}
	}
	
	protected String getEncryptBoolean(boolean value){
		
		try {
			byte[] encrypt = DESCrypt.encryptDES(PASSWORD, String.valueOf(value));
			String enStr = DESCrypt.parseByte2HexStr(encrypt);
			return enStr;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "5A3274796566666A4F6E773D0A";
	}
	
}
