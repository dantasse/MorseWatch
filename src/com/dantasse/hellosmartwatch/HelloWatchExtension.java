package com.dantasse.hellosmartwatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sonyericsson.extras.liveware.extension.util.R;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;

public class HelloWatchExtension extends ControlExtension{

	int width;
	int height;
	
	RelativeLayout layout;
	Canvas canvas;
	Bitmap bitmap;
	TextView textView;
	
	public HelloWatchExtension(Context context, String hostAppPackageName) {
		super(context, hostAppPackageName);
		
		width = getSupportedControlWidth(context);
		height = getSupportedControlHeight(context);
		
		layout = new RelativeLayout(context);
		textView = new TextView(context);
		textView.setText("Hello watch!");
		textView.setTextSize(9);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.WHITE);
		textView.layout(0, 0, width, height);
		layout.addView(textView);
	}
	
	@Override
	public void onResume() {
		updateVisual();
	}
	
	private void updateVisual() {

		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		layout.draw(canvas);

		showBitmap(bitmap);
	}

	public static int getSupportedControlWidth(Context context) {
		return context.getResources().getDimensionPixelSize(
				R.dimen.smart_watch_control_width);
	}

	public static int getSupportedControlHeight(Context context) {
		return context.getResources().getDimensionPixelSize(
				R.dimen.smart_watch_control_height);
	}
}
