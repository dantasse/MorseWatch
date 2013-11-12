package com.dantasse.hellosmartwatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Receives a broadcast and starts the app? */
public class HelloWatchExtensionReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, final Intent intent) {
		intent.setClass(context, HelloWatchExtensionService.class);
		context.startService(intent);
	}
}
