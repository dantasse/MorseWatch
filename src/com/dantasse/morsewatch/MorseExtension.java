package com.dantasse.morsewatch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlViewGroup;

public class MorseExtension extends ControlExtension {

    int width;
    int height;
    
    ControlViewGroup mLayout;

    public MorseExtension(Context context, String hostAppPackageName) {
        super(context, hostAppPackageName);

        width = getSupportedControlWidth(context);
        height = getSupportedControlHeight(context);
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.sample_control, null);
        mLayout = (ControlViewGroup) parseLayout(layout);

        // doesn't seem to work, not sure why:
//        if (mLayout != null) {
//            ControlView xmlText = mLayout.findViewById(R.id.dot_text);
//            xmlText.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick() {
//                    Log.d(MorseExtensionService.LOG_TAG, "clicked the text thing");
//                }
//            });
//        }
    }

    @Override
    public void onResume() {
        Log.d(MorseExtensionService.LOG_TAG, "Starting control");

        // Note: Setting the screen to be always on will drain the accessory
        // battery. It is done here solely for demonstration purposes
        setScreenState(Control.Intents.SCREEN_STATE_ON);
        showLayout(R.layout.sample_control, new Bundle[0]);

    }

    @Override
    public void onPause() {
        Log.d(MorseExtensionService.LOG_TAG, "Stopping control");
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onTouch(ControlTouchEvent event) {
        super.onTouch(event);
        if (event.getAction() == Control.Intents.TOUCH_ACTION_RELEASE) {
            if (event.getX() < width / 2) {
                Log.d(MorseExtensionService.LOG_TAG, "dot, x: " + event.getX());                
            } else {
                Log.d(MorseExtensionService.LOG_TAG, "dash, x: " + event.getX());
            }
        }
    }
    
    // doesn't work, not sure why:
    @Override
    public void onObjectClick(final ControlObjectClickEvent event) {
        Log.d(MorseExtensionService.LOG_TAG, "onObjectClick() " + event.getClickType());
        Log.d(MorseExtensionService.LOG_TAG, "" + event.getLayoutReference());
        if (event.getLayoutReference() != -1) {
            mLayout.onClick(event.getLayoutReference());
        }
    }

    public static int getSupportedControlWidth(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_width);
    }

    public static int getSupportedControlHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_height);
    }
}
