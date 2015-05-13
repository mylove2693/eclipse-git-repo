package com.punan.smarttouch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class SmartTouchAppWidget extends AppWidgetProvider {
	public static final String TAG = "SmartTouchAppWidget";
	public static SmartTouchAppWidget sInstance;

	private RemoteViews remoteView;

	static synchronized SmartTouchAppWidget getInstance() {
		if (sInstance == null) {
			sInstance = new SmartTouchAppWidget();
		}
		return sInstance;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Log.v(TAG, "onReceive");
		if (remoteView == null) {
			remoteView = new RemoteViews(context.getPackageName(),
					R.layout.appwidget_main);
		}
		
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onUpdate");
		if (remoteView == null) {
			remoteView = new RemoteViews(context.getPackageName(),
					R.layout.appwidget_main);
		}

		Intent intentClick = new Intent(ExpandSBReceiver.CMDSTARTEXPAND);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intentClick, 0);
		remoteView.setOnClickPendingIntent(R.id.imageview, pendingIntent);
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
