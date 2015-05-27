package com.punan.smarttouch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class SmartTouchAppWidget extends AppWidgetProvider {
	public static final String TAG = "SmartTouchAppWidget";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		final int N = appWidgetIds.length;
		
		//Perform this loop procedure for each App Widget that belongs to this provider
		for(int i=0; i<N; i++){
			int appWidgetId = appWidgetIds[i];
			
			//Create an Intent to launch Example Activity
			//Intent intent = new Intent(context, ExampleActivity.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			
			Intent intent = new Intent();
			intent.setAction(ExpandSBReceiver.CMDSTARTEXPAND);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
			
			//Get the layout for the App Widget and attach an on-click listener to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_main);
			views.setOnClickPendingIntent(R.id.imageview, pendingIntent);
			
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		
		
	}

}
