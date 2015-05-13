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
import com.cynovo.sirius.adapter.BandMIDAdapter;
import com.cynovo.sirius.bean.MIDBean;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.cynovo.sirius.util.CynovoHttpClient;
import com.cynovo.sirius.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint({ "NewApi", "ValidFragment" })
public class BandMIDFragment extends AbstractLoadingFragment implements
		OnItemClickListener {
	private ListView bandMidListView;
	private ArrayList<MIDBean> list = new ArrayList<MIDBean>();
	private Handler handler;

	public BandMIDFragment() {
	}

	public BandMIDFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.band_mid, null);
		bandMidListView = (ListView) v.findViewById(R.id.bandMidListView);
		bandMidListView.setOnItemClickListener(this);

		getMidListFromNet();

		Message msg = new Message();
		msg.what = ActiveActivity.CHANGE_TITLE;
		msg.obj = getString(R.string.band_mid);
		handler.sendMessage(msg);

		return v;
	}

	private void getMidListFromNet() {
		RequestParams params = new RequestParams();
		params.put("phone",
				MySharedPreferencesEdit.getInstancePublic(getActivity())
						.getOwnerPhoneNumber());

		CynovoHttpClient.post(getActivity(),
				"api/active_device/band_device/get_mid.php", params,
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
									if (list != null && list.size() > 0) {
										list.clear();
									}
									JSONArray jsonArray = response
											.getJSONArray("data");
									if (jsonArray != null
											&& jsonArray.length() > 0) {
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObject = jsonArray
													.getJSONObject(i);
											MIDBean midBean = new MIDBean();
											midBean.setmID(jsonObject
													.getString("MERCHANTNO"));
											midBean.setmName(jsonObject
													.getString("STORENAME"));
											midBean.setStoreID(jsonObject
													.getString("STOREID"));
											list.add(midBean);
										}
										BandMIDAdapter bandMIDAdapter = new BandMIDAdapter(
												getActivity(), list);
										bandMidListView
												.setAdapter(bandMIDAdapter);
									} else { // data is null
										new AlertDialog.Builder(getActivity())
												.setTitle(R.string.hint)
												.setMessage(
														R.string.mid_is_null)
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
											"BandMIDFragment:网络异常1",
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
									"BandMIDFragment:网络异常2", Toast.LENGTH_LONG)
									.show();
						}
						super.onSuccess(statusCode, response);

						pDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable e, String content) {
						super.onFailure(e, content);
						Toast.makeText(getActivity(), "BandMIDFragment:网络异常3",
								Toast.LENGTH_LONG).show();
						pDialog.dismiss();
						e.printStackTrace();
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		BandTIDFragment userNameFragment = new BandTIDFragment(handler);
		Bundle bundle = new Bundle();
		bundle.putString("mid", list.get(arg2).getmID());
		MySharedPreferencesEdit.getInstancePublic(getActivity()).setStoreID(
				list.get(arg2).getStoreID());
		// try {
		// StoreManager.updateStoreId(Integer.parseInt(list.get(arg2).getStoreID()));
		// } catch (Exception e) {
		// StoreManager.updateStoreId(0);
		// e.printStackTrace();
		// }
		MySharedPreferencesEdit.getInstancePublic(getActivity()).setMerchantNo(
				list.get(arg2).getmID());
		userNameFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.fragmentLayout2, userNameFragment);
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();

		handler.sendEmptyMessage(ActiveActivity.SHOW_BACK_BUTTON);
	}
}
