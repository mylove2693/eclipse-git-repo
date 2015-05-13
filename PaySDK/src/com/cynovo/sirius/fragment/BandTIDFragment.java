package com.cynovo.sirius.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cynovo.sirius.PaySDK.ActiveActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.adapter.BandTIDAdapter;
import com.cynovo.sirius.bean.TIDBean;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.cynovo.sirius.util.CynovoHttpClient;
import com.cynovo.sirius.util.GetJniInfo;
import com.cynovo.sirius.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint({ "NewApi", "ValidFragment" })
public class BandTIDFragment extends AbstractLoadingFragment implements
		OnItemClickListener {
	private String mid;
	private ArrayList<TIDBean> list = new ArrayList<TIDBean>();
	private ListView bandMidListView;
	private Handler handler;

	public BandTIDFragment() {
	}

	public BandTIDFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mid = bundle.getString("mid");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.band_tid, null);

		bandMidListView = (ListView) v.findViewById(R.id.bandTidListView);
		bandMidListView.setOnItemClickListener(this);

		getTIDFromNet();

		Message msg = new Message();
		msg.what = ActiveActivity.CHANGE_TITLE;
		msg.obj = getString(R.string.band_tid);
		handler.sendMessage(msg);

		return v;
	}

	private void getTIDFromNet() {
		RequestParams params = new RequestParams();
		params.put("phone",
				MySharedPreferencesEdit.getInstancePublic(getActivity())
						.getOwnerPhoneNumber());
		params.put("mid", mid);

		CynovoHttpClient.post(getActivity(),
				"api/active_device/band_device/get_tid.php", params,
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
								try {
									JSONArray jsonArray = response
											.getJSONArray("data");
									if (jsonArray != null
											&& jsonArray.length() > 0) {
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObject = jsonArray
													.getJSONObject(i);
											TIDBean tidBean = new TIDBean();
											tidBean.settID(jsonObject
													.getString("TID"));
											tidBean.setsID((jsonObject
													.getString("SID") == null || jsonObject
													.getString("SID").equals(
															"null")) ? ""
													: jsonObject
															.getString("SID"));
											tidBean.setStoreID(jsonObject
													.getString("STOREID"));
											list.add(tidBean);
										}
										BandTIDAdapter bandTIDAdapter = new BandTIDAdapter(
												getActivity(), list);
										bandMidListView
												.setAdapter(bandTIDAdapter);
									} else { // data is null
										new AlertDialog.Builder(getActivity())
												.setTitle(R.string.hint)
												.setMessage(
														R.string.tid_is_null)
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
											"BandTIDFragment:网络异常1",
											Toast.LENGTH_LONG).show();
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
									"BandTIDFragment:网络异常2", Toast.LENGTH_LONG)
									.show();
						}
						super.onSuccess(statusCode, response);

						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						Toast.makeText(getActivity(), "BandTIDFragment:网络异常3",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
						e.printStackTrace();
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		MyLog.e("list.get(arg2).getsID():" + list.get(arg2).getsID());
		if (list.get(arg2).getsID() == null
				|| list.get(arg2).getsID().length() == 0) { // band device
			new AlertDialog.Builder(getActivity())
					.setTitle(R.string.hint)
					.setMessage(
							getString(R.string.band_tid_hint, list.get(arg2)
									.gettID()))
					.setPositiveButton(R.string.sure,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									bandDevice(list.get(arg2).gettID());
									dialog.dismiss();
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
		} else { // change band device
			new AlertDialog.Builder(getActivity())
					.setTitle(R.string.hint)
					.setMessage(
							getString(R.string.change_band_tid_hint,
									list.get(arg2).gettID()))
					.setPositiveButton(R.string.sure,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									changeBandDevice(list.get(arg2).gettID(),
											list.get(arg2).getsID());
									dialog.dismiss();
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
	}

	private void bandDevice(final String tid) {
		RequestParams params = new RequestParams();
		params.put("mid", mid);
		params.put("tid", tid);
		params.put("cpuid", GetJniInfo.getCPUID());
		MyLog.e("params:" + params);

		CynovoHttpClient.post(getActivity(),
				"api/active_device/band_device/band_with_mid.php", params,
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
								Toast.makeText(getActivity(),
										R.string.band_device_ok,
										Toast.LENGTH_LONG).show();
								MySharedPreferencesEdit.getInstancePublic(
										getActivity())
										.setIsBandDeviceInfo(true);
								MySharedPreferencesEdit.getInstancePublic(
										getActivity()).setTerminalNo(tid);
								jumpToNextFragment();
								saveMidAndTidToSafeModel(tid);
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
									"BandTIDFragment:网络异常4", Toast.LENGTH_LONG)
									.show();
						}
						super.onSuccess(statusCode, response);

						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						Toast.makeText(getActivity(), "BandTIDFragment:网络异常5",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
						e.printStackTrace();
					}
				});
	}

	/**
	 * @param tid
	 *            终端id
	 * @param originalSID
	 *            原来绑定的serial number
	 */
	private void changeBandDevice(final String tid, String originalSID) {
		RequestParams params = new RequestParams();
		params.put("mid", mid);
		params.put("tid", tid);
		params.put("osid", originalSID);
		params.put("cpuid", GetJniInfo.getCPUID());

		CynovoHttpClient.post(getActivity(),
				"api/active_device/band_device/change_with_mid.php", params,
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
								Toast.makeText(getActivity(),
										R.string.change_band_ok,
										Toast.LENGTH_LONG).show();
								MySharedPreferencesEdit.getInstancePublic(
										getActivity())
										.setIsBandDeviceInfo(true);
								MySharedPreferencesEdit.getInstancePublic(
										getActivity()).setTerminalNo(tid);
								jumpToNextFragment();
								saveMidAndTidToSafeModel(tid);
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
									"BandTIDFragment:网络异常6", Toast.LENGTH_LONG)
									.show();
						}
						super.onSuccess(statusCode, response);

						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						Toast.makeText(getActivity(), "BandTIDFragment:网络异常7",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
						e.printStackTrace();
					}
				});
	}

	private void saveMidAndTidToSafeModel(String tid) {
		GetJniInfo.StoreMidAndTid(mid, tid);
	}

	private void jumpToNextFragment() {
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		DownSecretKeyFragment userNameFragment = new DownSecretKeyFragment(
				handler);
		fragmentTransaction.replace(R.id.fragmentLayout2, userNameFragment);
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();

		handler.sendEmptyMessage(ActiveActivity.SHOW_BACK_BUTTON);
	}
}
