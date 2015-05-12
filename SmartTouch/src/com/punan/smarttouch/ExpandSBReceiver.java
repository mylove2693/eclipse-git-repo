package com.punan.smarttouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ExpandSBReceiver extends BroadcastReceiver {
	public static final String CMDSTARTEXPAND = "com.cynovo.punan.expandstatusbar";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(CMDSTARTEXPAND)){
			Intent intent_service = new Intent();
			intent_service.setClass(context, ExpandSBService.class);
			context.startService(intent_service);
		}
		
	}

}
