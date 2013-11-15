package com.dantasse.morsewatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Receives a broadcast and starts the app? */
public class MorseExtensionReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, final Intent intent) {
		intent.setClass(context, MorseExtensionService.class);
		context.startService(intent);
	}
}
