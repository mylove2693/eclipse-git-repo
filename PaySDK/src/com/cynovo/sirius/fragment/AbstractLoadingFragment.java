package com.cynovo.sirius.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.cynovo.sirius.PaySDK.R;

public class AbstractLoadingFragment extends Fragment {
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage(getActivity().getResources().getString(
				R.string.is_loading));
		pDialog.setCancelable(false);
	}
}
