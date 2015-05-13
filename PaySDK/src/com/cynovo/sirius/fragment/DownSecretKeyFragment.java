package com.cynovo.sirius.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cynovo.sirius.PaySDK.ActiveActivity;
import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.data.MySharedPreferencesEdit;
import com.cynovo.sirius.jni.HostlinkInterface;
//import com.cynovo.sirius.jni.PbocInterface;
//import com.cynovo.sirius.jni.PinPadInterface;
import com.cynovo.sirius.jni.SafeInterface;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.util.GetJniInfo;
import com.cynovo.sirius.util.NetworkUtil;

//import com.cynovo.jni.PinPadInterface;
import com.kivvi.jni.PinPadInterface;
import com.kivvi.jni.EmvL2Interface;

@SuppressLint({ "NewApi", "ValidFragment" })
public class DownSecretKeyFragment extends AbstractLoadingFragment {
	private Handler handler;
	private Handler myHandler;
	private ProgressDialog pDialog;
	/**
	 * 下载主密钥
	 */
	private final int DOWN_SECRETKEY = 0;
	/**
	 * 下载IC卡终端公钥
	 */
	private final int DOWN_IC_SECRETKEY = 1;
	/**
	 * 下载IC卡终端参数
	 */
	private final int DOWN_IC_PARAMS = 2;
	private final int DOWN_SUCCESS = 3;
	private final int DOWN_FAILURE = 4; // 下载失败，此时需要联系讯联重新下载
	private final int NETWORK_ERROR = 5;
	private int downloadTimes; // 下载上述3种证书的次数，每种证书下载前清零，且每种证书都只能最多尝试3次下载
	private boolean state = false;
	private TextView downloadSecretSuccessText;
	private Button downloadSecretKyeButton;

	public DownSecretKeyFragment() {
	}

	public DownSecretKeyFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.down_secret_key, null);

		myHandler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_SECRETKEY:
					downloadTimes = 0;
					pDialog.setMessage(getString(R.string.is_down_secret_key_hint));
					pDialog.show();

					break;
				case DOWN_IC_SECRETKEY:
					downloadTimes = 0;
					pDialog.setMessage(getString(R.string.is_down_ic_secret_key_hint));
					startDownloadICSecretKey();
					break;
				case DOWN_IC_PARAMS:
					downloadTimes = 0;
					pDialog.setMessage(getString(R.string.is_down_ic_params_hint));
					startDownloadICParams();
					break;
				case DOWN_SUCCESS:
					pDialog.dismiss();
					// 下载成功
					MySharedPreferencesEdit.getInstancePublic(getActivity())
							.setIsDownloadSecretKey(true);
					downloadSecretSuccessText.setVisibility(View.VISIBLE);
					downloadSecretKyeButton.setVisibility(View.GONE);

					// deleted by
					// wanhaiping--------------------------------------------->begin
					// HostlinkInterface.close();
					// deleted by
					// wanhaiping---------------------------------------------<end

					PinPadInterface.close();

					// FragmentManager fragmentManager =
					// getActivity().getFragmentManager();
					// FragmentTransaction fragmentTransaction =
					// fragmentManager.beginTransaction();
					// UserNameFragment userNameFragment = new
					// UserNameFragment(handler);
					// fragmentTransaction.replace(R.id.fragmentLayout2,
					// userNameFragment);
					// fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					// fragmentTransaction.commitAllowingStateLoss();
					ActiveActivity.handler
							.sendEmptyMessage(ActiveActivity.ACTIVE_SUCCESS);
					ActiveActivity.isDownloadKey = false;
					break;
				case DOWN_FAILURE:
					pDialog.dismiss();
					// deleted by
					// wanhaiping--------------------------------------------->begin
					// HostlinkInterface.close();
					// deleted by
					// wanhaiping---------------------------------------------<end
					PinPadInterface.close();
					ActiveActivity.isDownloadKey = false;
					new AlertDialog.Builder(getActivity())
							.setTitle(R.string.hint)
							.setMessage(R.string.down_secret_key_error)
							.setPositiveButton(R.string.sure,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
					break;
				case NETWORK_ERROR:
					pDialog.dismiss();
					// deleted by
					// wanhaiping--------------------------------------------->begin
					// HostlinkInterface.close();
					// deleted by
					// wanhaiping---------------------------------------------<end
					PinPadInterface.close();
					ActiveActivity.isDownloadKey = false;
					new AlertDialog.Builder(getActivity())
							.setTitle(R.string.hint)
							.setMessage(R.string.network_error)
							.setPositiveButton(R.string.sure,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
					break;
				}
				return false;
			}
		});

		pDialog = new ProgressDialog(getActivity());
		pDialog.setTitle(R.string.hint);
		pDialog.setCancelable(false);

		downloadSecretSuccessText = (TextView) v
				.findViewById(R.id.downloadSecretSuccessText);
		downloadSecretSuccessText.setVisibility(View.GONE);

		downloadSecretKyeButton = (Button) v
				.findViewById(R.id.downloadSecretKyeButton);
		downloadSecretKyeButton.setVisibility(View.VISIBLE);
		downloadSecretKyeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// deleted by
				// wanhaiping--------------------------------------------->begin
				// HostlinkInterface.open();
				// deleted by
				// wanhaiping---------------------------------------------<end
				int pinPadRet = PinPadInterface.open();
				if (pinPadRet >= 0) {
					// modified by
					// wanhaiping-------------------------------->begin
					// pinPadRet = PinPadInterface.checkPinpad();
					// if(pinPadRet >= 0) {
					// state = true;
					// }

					state = true;
					// modified by
					// wanhaiping--------------------------------<end

				}
				if (!NetworkUtil.isConnected(getActivity())) {
					Toast.makeText(getActivity(), "DownSecretKeyFragment:网络异常1",
							Toast.LENGTH_LONG).show();
				} else if (!state) {
					Toast.makeText(getActivity(),
							R.string.pin_pad_connect_error, Toast.LENGTH_LONG)
							.show();
				} else {
					new AlertDialog.Builder(getActivity())
							.setTitle(R.string.hint)
							.setMessage(R.string.prepare_down_secret_key_hint)
							.setPositiveButton(R.string.sure,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ActiveActivity.isDownloadKey = true;
											startDownloadSecretKey();
											dialog.dismiss();
										}
									})
							.setNegativeButton(R.string.cancel,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				}
			}
		});
		RelativeLayout downSecretLayout = (RelativeLayout) v
				.findViewById(R.id.downSecretLayout);
		downSecretLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 捕获此View的监听事件，勿删
			}
		});

		Message msg = new Message();
		msg.what = ActiveActivity.CHANGE_TITLE;
		msg.obj = getString(R.string.download_secret_key);
		handler.sendMessage(msg);

		handler.sendEmptyMessage(ActiveActivity.HIDE_BACK_BUTTON);

		return v;
	}

	private void startDownloadSecretKey() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				myHandler.sendEmptyMessage(DOWN_SECRETKEY);
				Common.hostlinkInit();
				// modified by
				// wanhaiping--------------------------------------------->begin
				/*
				 * HostlinkInterface.setMerchantCode(GetJniInfo.getMID().getBytes
				 * ());
				 * HostlinkInterface.setTerminalCode(GetJniInfo.getTID().getBytes
				 * ());
				 * 
				 * int res; res = HostlinkInterface.updatemkey();
				 */
				int res = 0;
				// modified by
				// wanhaiping---------------------------------------------<end
				if (res == -101 || res == -102) {
					myHandler.sendEmptyMessage(NETWORK_ERROR);
					return;
				}
				if (res < 0) {
					myHandler.sendEmptyMessage(DOWN_FAILURE);
					return;
				}

				if (res == 0) {
					myHandler.sendEmptyMessage(DOWN_IC_SECRETKEY);
				} else {
					if (downloadTimes >= 2) {
						myHandler.sendEmptyMessage(DOWN_FAILURE);
					} else {
						downloadTimes++;
						startDownloadSecretKey();
					}
				}
			}
		}).start();
	}

	private void startDownloadICSecretKey() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int res = 0;

				// modified by wanhaiping---------------------------->begin
				// res = PbocInterface.open();
				res = EmvL2Interface.openReader();
				// modified by wanhaiping----------------------------<end

				if (res < 0) {
					myHandler.sendEmptyMessage(DOWN_FAILURE);
					return;
				}

				// deleted by wanmhaiping----------------------->begin
				// res = HostlinkInterface.downloadICPKToFile();
				// deleted by wanmhaiping-----------------------<end

				if (res == -101 || res == -102) {
					myHandler.sendEmptyMessage(NETWORK_ERROR);
					return;
				}
				if (res < 0) {
					myHandler.sendEmptyMessage(DOWN_FAILURE);
					return;
				}
				if (res == 0) {
					myHandler.sendEmptyMessage(DOWN_IC_PARAMS);
				} else {
					if (downloadTimes >= 2) {
						myHandler.sendEmptyMessage(DOWN_FAILURE);
					} else {
						downloadTimes++;
						startDownloadICSecretKey();
					}
				}
			}
		}).start();
	}

	private void startDownloadICParams() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int res = 0;
				// deleted by wanhaiping--------------------------->begin
				// res = HostlinkInterface.downloadICParamToFile();
				// deleted by wanhaiping---------------------------<end
				if (res == -101 || res == -102) {
					myHandler.sendEmptyMessage(NETWORK_ERROR);
					return;
				}
				if (res < 0) {
					myHandler.sendEmptyMessage(DOWN_FAILURE);
					return;
				}
				if (SafeInterface.open() <= 0) {
					myHandler.sendEmptyMessage(DOWN_FAILURE);
					return;
				}
				Log.i("SafeInterface", "open");

				// modified by wanhaiping---------------------------->begin
				// res = PbocInterface.EmvKernelInit();
				// res = EmvL2Interface.emvKernelInit();
				// modified by wanhaiping----------------------------<end

				if (res < 0) {
					SafeInterface.close();
					myHandler.sendEmptyMessage(DOWN_FAILURE);
					return;
				}

				SafeInterface.close();
				if (res == 0) {
					myHandler.sendEmptyMessage(DOWN_SUCCESS);
				} else {
					if (downloadTimes >= 2) {
						myHandler.sendEmptyMessage(DOWN_FAILURE);
					} else {
						downloadTimes++;
						startDownloadICParams();
					}
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		pDialog.dismiss();
	}
}
