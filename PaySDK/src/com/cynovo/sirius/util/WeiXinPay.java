package com.cynovo.sirius.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.cynovo.sirius.util.MD5;
import com.cynovo.sirius.PaySDK.MainService;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
//import  org.apache.commons.httpclient. * ;
// import  org.apache.commons.httpclient.methods.GetMethod;
// import  org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

//import org.apache.http;  

public class WeiXinPay {
	static int OnLineEnd;
	static int retResult;
	static public String last_out_trade_no;
	private static AsyncHttpClient client = new AsyncHttpClient();

	// static Context context;

	public WeiXinPay() {

	}

	public static int QueryOrder1(String out_trade_no) {
		Log.e("debug", "QueryOrder begin");
		int retResultQuery = 0;
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");

		String strSign;
		// String strmch_id = mch_id;
		// String strmnonce_str = nonce_str;
		String strmch_id = "7551000001";
		String strmnonce_str = "adf880d5c8986bd0deb6423c92c9d948";
		String strout_trade_no = out_trade_no;
		String strservice = "trade.single.query";

		root.addElement("mch_id").setText(strmch_id);
		root.addElement("nonce_str").setText(strmnonce_str);
		root.addElement("out_trade_no").setText(strout_trade_no);
		root.addElement("service").setText(strservice);

		strSign = "mch_id=";
		strSign += strmch_id;
		strSign += "&";

		strSign += "nonce_str=";
		strSign += strmnonce_str;
		strSign += "&";

		strSign += "out_trade_no=";
		strSign += strout_trade_no;
		strSign += "&";

		strSign += "service=";
		strSign += strservice;
		strSign += "&";

		strSign += "key=";
		strSign += "9d101c97133837e13dde2d32a5054abb";

		Log.e("debug", "String to Sign");
		Log.e("debug", strSign);

		String tempmd5 = MD5.md5s(strSign);
		String strmd5 = tempmd5.toUpperCase();
		root.addElement("sign").setText(strmd5);

		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);

		try {
			output.write(doc);
			writer.close();
			output.close();
			System.out.println(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		String strXML = writer.toString();
		Log.e("debug", "payCash payONLINE  QueryOrder Value");
		Log.e("debug", strXML);
		HttpEntity entity;

		try {
			entity = new StringEntity(strXML, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

		HttpPost post = new HttpPost("https://pay.swiftpass.cn/pay/gateway");
		post.addHeader("Content-Type", "text/xml");
		post.setEntity(entity);

		HttpResponse response;
		try {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			HttpClient httpclient = new DefaultHttpClient();
			response = httpclient.execute(post);
			int responseCode = response.getStatusLine().getStatusCode();
			Log.e("debug", "responseCode:");
			Log.e("debug", "" + responseCode);

			HttpEntity resEntity = response.getEntity();
			InputStreamReader reader = new InputStreamReader(
			// resEntity.getContent(), "ISO-8859-1");
					resEntity.getContent(), "utf-8");
			String strResponse = "";

			// char[] buff = new char[1024];
			// int length = 0;

			/*
			 * while ((length = reader.read(buff)) != -1) { strResponse = new
			 * String(buff, 0, length); }
			 */

			BufferedReader in = new BufferedReader(reader);
			in.toString();

			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			strResponse = buffer.toString();

			Log.e("debug", "strResponse:");
			Log.e("debug", strResponse);

			httpclient.getConnectionManager().shutdown();

			doc = DocumentHelper.parseText(strResponse); // 将字符串转为XML
			root = doc.getRootElement();
			String strtradestate = root.elementText("trade_state");

			if (strtradestate != null) {

				Log.e("debug", "trade_state:");
				Log.e("debug", strtradestate);
				String strtemp = "SUCCESS";
				if (strtradestate.equals(strtemp)) {
					retResultQuery = 1;
				}
			}

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		catch (DocumentException e) {
			e.printStackTrace();
		}
		Log.e("debug", "QueryOrder end");
		return retResultQuery;
	}

	public static int QueryOrder(String out_trade_no, Context context) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");

		String strSign;
		// String strmch_id = mch_id;
		// String strmnonce_str = nonce_str;
		String strmch_id = "7551000001";
		String strmnonce_str = "adf880d5c8986bd0deb6423c92c9d948";
		String strout_trade_no = out_trade_no;
		String strservice = "trade.single.query";

		root.addElement("mch_id").setText(strmch_id);
		root.addElement("nonce_str").setText(strmnonce_str);
		root.addElement("out_trade_no").setText(strout_trade_no);
		root.addElement("service").setText(strservice);

		strSign = "mch_id=";
		strSign += strmch_id;
		strSign += "&";

		strSign += "nonce_str=";
		strSign += strmnonce_str;
		strSign += "&";

		strSign += "out_trade_no=";
		strSign += strout_trade_no;
		strSign += "&";

		strSign += "service=";
		strSign += strservice;
		strSign += "&";

		strSign += "key=";
		strSign += "9d101c97133837e13dde2d32a5054abb";

		Log.e("debug", "String to Sign");
		Log.e("debug", strSign);

		String tempmd5 = MD5.md5s(strSign);
		String strmd5 = tempmd5.toUpperCase();
		root.addElement("sign").setText(strmd5);

		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);

		try {
			output.write(doc);
			writer.close();
			output.close();
			System.out.println(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		String strXML = writer.toString();
		Log.e("debug", "payCash payONLINE  QueryOrder Value");
		Log.e("debug", strXML);
		HttpEntity entity;

		try {
			entity = new StringEntity(strXML, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

		OnLineEnd = 0;
		retResult = 0;

		// AsyncHttpClient httpclient = new AsyncHttpClient();
		client.post(context, "https://pay.swiftpass.cn/pay/gateway", entity,
				"text/xml;charset=ISO-8859-1", new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						System.out.println(response);
						Log.e("debug", "QueryOrder post onSuccess");
						Log.e("debug", response);

						try {
							Document doc = DocumentHelper.parseText(response); // 将字符串转为XML
							Element root = doc.getRootElement();
							String strtradestate = root
									.elementText("trade_state");

							String strtemp = "SUCCESS";
							if (strtradestate.equals(strtemp)) {
								retResult = 1;
							}

						}

						catch (DocumentException e) {
							e.printStackTrace();
						}
						OnLineEnd = 1;
					}

					@Override
					public void onStart() {
						super.onStart();
						// System.out.println("onStart");
						Log.e("debug", "post onStart");

					}

					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						OnLineEnd = 1;
						Log.e("debug", "QueryOrder post onFailure");
						Log.e("debug", content);

					}
				});

		while (OnLineEnd == 0) {
			try {
				Thread.yield();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retResult;
	}

	public static int ReverseOrder(String out_trade_no, Context context) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");

		String strSign;
		// String strmch_id = mch_id;
		// String strmnonce_str = nonce_str;
		String strmch_id = "7551000001";
		String strmnonce_str = "adf880d5c8986bd0deb6423c92c9d948";
		String strout_trade_no = out_trade_no;
		String strservice = "pay.weixin.micropay.reverse";

		root.addElement("mch_id").setText(strmch_id);
		root.addElement("nonce_str").setText(strmnonce_str);
		root.addElement("out_trade_no").setText(strout_trade_no);
		root.addElement("service").setText(strservice);

		strSign = "mch_id=";
		strSign += strmch_id;
		strSign += "&";

		strSign += "nonce_str=";
		strSign += strmnonce_str;
		strSign += "&";

		strSign += "out_trade_no=";
		strSign += strout_trade_no;
		strSign += "&";

		strSign += "service=";
		strSign += strservice;
		strSign += "&";

		strSign += "key=";
		strSign += "9d101c97133837e13dde2d32a5054abb";

		Log.e("debug", "String to Sign");
		Log.e("debug", strSign);

		String tempmd5 = MD5.md5s(strSign);
		String strmd5 = tempmd5.toUpperCase();
		root.addElement("sign").setText(strmd5);

		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);

		try {
			output.write(doc);
			writer.close();
			output.close();
			System.out.println(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		String strXML = writer.toString();
		Log.e("debug", "payCash payONLINE  QueryOrder Value");
		Log.e("debug", strXML);
		HttpEntity entity;

		try {
			entity = new StringEntity(strXML, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

		OnLineEnd = 0;
		retResult = 0;

		// AsyncHttpClient httpclient = new AsyncHttpClient();
		client.post(context, "https://pay.swiftpass.cn/pay/gateway", entity,
				"text/xml;charset=ISO-8859-1", new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						System.out.println(response);
						Log.e("debug", "ReverseOrder post onSuccess");
						Log.e("debug", response);

						try {
							Document doc = DocumentHelper.parseText(response); // 将字符串转为XML
							Element root = doc.getRootElement();
							String strresultcode = root
									.elementText("result_code");

							String strtemp = "0";
							if (strresultcode.equals(strtemp)) {
								retResult = 1;
							}

						}

						catch (DocumentException e) {
							e.printStackTrace();
						}
						OnLineEnd = 1;
					}

					@Override
					public void onStart() {
						super.onStart();
						// System.out.println("onStart");
						Log.e("debug", "ReverseOrder post onStart");

					}

					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						OnLineEnd = 1;
						Log.e("debug", "ReverseOrder post onFailure");
						Log.e("debug", content);

					}
				});

		while (OnLineEnd == 0) {
			try {
				Thread.yield();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retResult;
	}

	public static int ReverseOrder1(String out_trade_no) {
		Log.e("debug", "ReverseOrder begin");
		int retResultReverse = 0;
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");

		String strSign;
		// String strmch_id = mch_id;
		// String strmnonce_str = nonce_str;
		String strmch_id = "7551000001";
		String strmnonce_str = "adf880d5c8986bd0deb6423c92c9d948";
		String strout_trade_no = out_trade_no;
		String strservice = "pay.weixin.micropay.reverse";

		root.addElement("mch_id").setText(strmch_id);
		root.addElement("nonce_str").setText(strmnonce_str);
		root.addElement("out_trade_no").setText(strout_trade_no);
		root.addElement("service").setText(strservice);

		strSign = "mch_id=";
		strSign += strmch_id;
		strSign += "&";

		strSign += "nonce_str=";
		strSign += strmnonce_str;
		strSign += "&";

		strSign += "out_trade_no=";
		strSign += strout_trade_no;
		strSign += "&";

		strSign += "service=";
		strSign += strservice;
		strSign += "&";

		strSign += "key=";
		strSign += "9d101c97133837e13dde2d32a5054abb";

		Log.e("debug", "String to Sign");
		Log.e("debug", strSign);

		String tempmd5 = MD5.md5s(strSign);
		String strmd5 = tempmd5.toUpperCase();
		root.addElement("sign").setText(strmd5);

		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);

		try {
			output.write(doc);
			writer.close();
			output.close();
			System.out.println(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		String strXML = writer.toString();
		Log.e("debug", "payCash payONLINE Reverse Value");
		Log.e("debug", strXML);
		HttpEntity entity;

		try {
			entity = new StringEntity(strXML, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		// ////////////////////////////////////////////////////////////////

		HttpPost post = new HttpPost("https://pay.swiftpass.cn/pay/gateway");
		post.addHeader("Content-Type", "text/xml");
		post.setEntity(entity);

		HttpResponse response;
		try {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			HttpClient httpclient = new DefaultHttpClient();
			response = httpclient.execute(post);
			int responseCode = response.getStatusLine().getStatusCode();
			Log.e("debug", "responseCode:");
			Log.e("debug", "" + responseCode);

			HttpEntity resEntity = response.getEntity();
			InputStreamReader reader = new InputStreamReader(
			// resEntity.getContent(), "ISO-8859-1");
					resEntity.getContent(), "utf-8");
			String strResponse = "";

			BufferedReader in = new BufferedReader(reader);
			in.toString();

			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			strResponse = buffer.toString();

			Log.e("debug", "strResponse:");
			Log.e("debug", strResponse);

			httpclient.getConnectionManager().shutdown();

			doc = DocumentHelper.parseText(strResponse); // 将字符串转为XML
			root = doc.getRootElement();
			String strresultcode = root.elementText("result_code");

			if (strresultcode != null) {

				Log.e("debug", "result_code:");
				Log.e("debug", strresultcode);
				String strtemp = "0";
				if (strresultcode.equals(strtemp)) {
					retResultReverse = 1;
				}
			}

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		catch (DocumentException e) {
			e.printStackTrace();
		}
		Log.e("debug", "ReverseOrder end");
		return retResultReverse;

	}

	public static int weixinPay(String count, String auth_code, Context context) {
		String strTempTraceNo;
		Log.e("debug", "payCash weixinpay point1");
		int tracenumber = MySharedPreferencesEdit.getInstancePublic(context)
				.getTraceNO();
		Log.e("debug", "payCash weixinpay point2");
		strTempTraceNo = tracenumber + "";
		Log.e("debug", strTempTraceNo);
		MySharedPreferencesEdit.getInstancePublic(context).setTraceNO(
				tracenumber + 1);

		String strTraceNO = "CYNOVO";
		strTraceNO += strTempTraceNo;
		Log.e("debug", strTraceNO);

		last_out_trade_no = strTraceNO;

		// AsyncHttpClient httpclient = new AsyncHttpClient();

		try {

			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("xml");

			String strSign;
			String strauth_code = auth_code;
			String strbody = "cynovopaytest";
			String strdevice_info = "1000";
			String strmch_id = "7551000001";
			String strmnonce_str = "adf880d5c8986bd0deb6423c92c9d948";
			// String strout_trade_no = "5706046766";
			String strout_trade_no = strTraceNO;
			String strservice = "pay.weixin.micropay";
			String strmch_create_ip = "127.0.0.1";
			String strtotal_fee = "1";

			root.addElement("auth_code").setText(strauth_code);
			root.addElement("body").setText(strbody);
			root.addElement("device_info").setText(strdevice_info);
			root.addElement("mch_id").setText(strmch_id);
			root.addElement("nonce_str").setText(strmnonce_str);
			root.addElement("out_trade_no").setText(strout_trade_no);
			root.addElement("service").setText(strservice);
			root.addElement("mch_create_ip").setText(strmch_create_ip);
			root.addElement("total_fee").setText(strtotal_fee);

			strSign = "auth_code=";
			strSign += auth_code;
			strSign += "&";

			strSign += "body=";
			strSign += strbody;
			strSign += "&";

			strSign += "device_info=";
			strSign += strdevice_info;
			strSign += "&";

			strSign += "mch_create_ip=";
			strSign += strmch_create_ip;
			strSign += "&";

			strSign += "mch_id=";
			strSign += strmch_id;
			strSign += "&";

			strSign += "nonce_str=";
			strSign += strmnonce_str;
			strSign += "&";

			strSign += "out_trade_no=";
			strSign += strout_trade_no;
			strSign += "&";

			strSign += "service=";
			strSign += strservice;
			strSign += "&";

			strSign += "total_fee=";
			strSign += strtotal_fee;
			strSign += "&";

			strSign += "key=";
			strSign += "9d101c97133837e13dde2d32a5054abb";

			Log.e("debug", "String to Sign");
			Log.e("debug", strSign);

			String tempmd5 = MD5.md5s(strSign);
			String strmd5 = tempmd5.toUpperCase();
			root.addElement("sign").setText(strmd5);

			OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
			StringWriter writer = new StringWriter();
			XMLWriter output = new XMLWriter(writer, format);
			try {
				output.write(doc);
				writer.close();
				output.close();
				System.out.println(writer.toString());
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}

			String strXML = writer.toString();
			Log.e("debug", "payCash payONLINE Value");
			Log.e("debug", strXML);

			HttpEntity entity = new StringEntity(strXML, "utf-8");
			OnLineEnd = 0;
			retResult = 0;

			client.post(context, "https://pay.swiftpass.cn/pay/gateway",
					entity, "text/xml;charset=ISO-8859-1",
					new AsyncHttpResponseHandler() {
						public void onSuccess(String response) {
							System.out.println(response);
							Log.e("debug", "weixinPay post onSuccess");
							Log.e("debug", response);

							try {

								Document doc = DocumentHelper
										.parseText(response); // 将字符串转为XML
								Element root = doc.getRootElement();

								String strpayresult = root
										.elementText("pay_result");
								String strresultcode = root
										.elementText("result_code");

								Log.e("debug", "strpayresult:");
								Log.e("debug", strpayresult);
								Log.e("debug", "strresultcode:");
								Log.e("debug", strresultcode);

								String strtemp = "0";
								if (strpayresult.equals(strtemp)
										&& strresultcode.equals(strtemp)) {
									Log.e("debug", "Pay successful");
									retResult = 1;
								}
							}

							catch (DocumentException e) {
								Log.e("debug", "response parseText Exception");
								e.printStackTrace();
							}
							OnLineEnd = 1;
						}

						@Override
						public void onStart() {
							super.onStart();
							// System.out.println("onStart");
							Log.e("debug", "weixinPay post onStart");
						}

						public void onFailure(Throwable e, String content) {
							super.onFailure(e, content);
							OnLineEnd = 1;
							retResult = -1;
							Log.e("debug", "weixinPay post onFailure");
							Log.e("debug", content);
						}
					});

			while (OnLineEnd == 0) {
				try {
					Thread.yield();
				} catch (Exception e) {
					Log.e("debug", "weixinPay point3.5");
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		Log.e("debug", "weixinPay post end return");
		Log.e("debug", "" + retResult);
		return retResult;
	}

	public static int weixinPay1(String count, String auth_code, Context context) {
		int retResultpay = 0;
		String strTempTraceNo;
		Log.e("debug", "weixinPay begin count:");
		Log.e("debug", count);
		int tracenumber = MySharedPreferencesEdit.getInstancePublic(context)
				.getTraceNO();
		Log.e("debug", "payCash weixinpay point2");
		strTempTraceNo = tracenumber + "";
		Log.e("debug", strTempTraceNo);
		MySharedPreferencesEdit.getInstancePublic(context).setTraceNO(
				tracenumber + 1);

		String strTraceNO = "CYNOVO";
		strTraceNO += strTempTraceNo;
		Log.e("debug", strTraceNO);

		last_out_trade_no = strTraceNO;

		// AsyncHttpClient httpclient = new AsyncHttpClient();

		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");

		String strSign;
		String strauth_code = auth_code;
		String strbody = "cynovopaytest";
		String strdevice_info = "1000";
		String strmch_id = "7551000001";
		String strmnonce_str = "adf880d5c8986bd0deb6423c92c9d948";
		// String strout_trade_no = "5706046766";
		String strout_trade_no = strTraceNO;
		String strservice = "pay.weixin.micropay";
		String strmch_create_ip = "127.0.0.1";
		String strtotal_fee = "1";

		root.addElement("auth_code").setText(strauth_code);
		root.addElement("body").setText(strbody);
		root.addElement("device_info").setText(strdevice_info);
		root.addElement("mch_id").setText(strmch_id);
		root.addElement("nonce_str").setText(strmnonce_str);
		root.addElement("out_trade_no").setText(strout_trade_no);
		root.addElement("service").setText(strservice);
		root.addElement("mch_create_ip").setText(strmch_create_ip);
		root.addElement("total_fee").setText(strtotal_fee);

		strSign = "auth_code=";
		strSign += auth_code;
		strSign += "&";

		strSign += "body=";
		strSign += strbody;
		strSign += "&";

		strSign += "device_info=";
		strSign += strdevice_info;
		strSign += "&";

		strSign += "mch_create_ip=";
		strSign += strmch_create_ip;
		strSign += "&";

		strSign += "mch_id=";
		strSign += strmch_id;
		strSign += "&";

		strSign += "nonce_str=";
		strSign += strmnonce_str;
		strSign += "&";

		strSign += "out_trade_no=";
		strSign += strout_trade_no;
		strSign += "&";

		strSign += "service=";
		strSign += strservice;
		strSign += "&";

		strSign += "total_fee=";
		strSign += strtotal_fee;
		strSign += "&";

		strSign += "key=";
		strSign += "9d101c97133837e13dde2d32a5054abb";

		Log.e("debug", "String to Sign");
		Log.e("debug", strSign);

		String tempmd5 = MD5.md5s(strSign);
		String strmd5 = tempmd5.toUpperCase();
		root.addElement("sign").setText(strmd5);

		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);
		try {
			output.write(doc);
			writer.close();
			output.close();
			System.out.println(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		String strXML = writer.toString();
		Log.e("debug", "payCash payONLINE Value");
		Log.e("debug", strXML);
		try {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			HttpClient httpclient = new DefaultHttpClient();
			HttpEntity entity = new StringEntity(strXML, "utf-8");
			// ///////////////////////////////////////////////////////////////////////////////////
			HttpPost post = new HttpPost("https://pay.swiftpass.cn/pay/gateway");
			post.addHeader("Content-Type", "text/xml");
			post.setEntity(entity);

			HttpResponse response;

			response = httpclient.execute(post);
			int responseCode = response.getStatusLine().getStatusCode();
			Log.e("debug", "responseCode:");
			Log.e("debug", "" + responseCode);

			HttpEntity resEntity = response.getEntity();
			InputStreamReader reader = new InputStreamReader(
			// resEntity.getContent(), "ISO-8859-1");
					resEntity.getContent(), "utf-8");

			String strResponse = "";

			// char[] buff = new char[1024];
			// int length = 0;

			/*
			 * while ((length = reader.read(buff)) != -1) { strResponse = new
			 * String(buff, 0, length); }
			 */

			BufferedReader in = new BufferedReader(reader);
			in.toString();

			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			strResponse = buffer.toString();

			Log.e("debug", "strResponse:");
			Log.e("debug", strResponse);

			httpclient.getConnectionManager().shutdown();

			doc = DocumentHelper.parseText(strResponse); // 将字符串转为XML
			root = doc.getRootElement();

			String strpayresult = root.elementText("pay_result");
			String strresultcode = root.elementText("result_code");

			if (strpayresult != null && strresultcode != null) {

				Log.e("debug", "strpayresult:");
				Log.e("debug", strpayresult);
				Log.e("debug", "strresultcode:");
				Log.e("debug", strresultcode);

				String strtemp = "0";
				if (strpayresult.equals(strtemp)
						&& strresultcode.equals(strtemp)) {
					Log.e("debug", "Pay successful");
					retResultpay = 1;
				}
			} else {
				Log.e("debug", "Paying............");
			}

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		catch (DocumentException e) {
			e.printStackTrace();
		}
		Log.e("debug", "weixinPay end");
		return retResultpay;
	}

}
