package com.dantasse.morsewatch;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlViewGroup;

public class MorseExtension extends ControlExtension {

    int width;
    int height;
    Handler handler = new Handler();
    
    ControlViewGroup mLayout;
    TextView typedText; // where the letters go that you type with morse code

    public MorseExtension(Context context, String hostAppPackageName) {
        super(context, hostAppPackageName);

        width = getSupportedControlWidth(context);
        height = getSupportedControlHeight(context);
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.sample_control, null);
        mLayout = (ControlViewGroup) parseLayout(layout);
//        typedText = (TextView) layout.findViewById(R.id.typed_text);
//        typedText.setText("asdfasdf");
        
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
        
        Bundle b = new Bundle();
        b.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.typed_text);
        b.putString(Control.Intents.EXTRA_TEXT, "hello");

        showLayout(R.layout.sample_control, new Bundle[]{b});
    }

    @Override
    public void onPause() {
        Log.d(MorseExtensionService.LOG_TAG, "Stopping control");
    }

    @Override
    public void onDestroy() {
    }


    String touches = "";
    @Override
    public void onTouch(ControlTouchEvent event) {
        super.onTouch(event);
        if (event.getAction() == Control.Intents.TOUCH_ACTION_RELEASE) {
            if (event.getX() < width / 2) {
                Log.d(MorseExtensionService.LOG_TAG, "dot, x: " + event.getX());
                touches += '.';
            } else {
                Log.d(MorseExtensionService.LOG_TAG, "dash, x: " + event.getX());
                touches += '-';
            }
        }
        handler.removeCallbacks(finishLetter);
        handler.postDelayed(finishLetter, 1000);
    }
    
    private Runnable finishLetter = new Runnable() {
        @Override
        public void run() {
            char letter = matchLetter(touches);
            Log.d(MorseExtensionService.LOG_TAG, "Letter finished. Touches: " + touches +
                    ", letter: " + letter);
            touches = "";
        }
    };
    
    private char matchLetter(String touches) {
        if (touches.equals(".-")) return 'a';
        else if (touches.equals("-...")) return 'b';
        else if (touches.equals("-.-.")) return 'c';
        else if (touches.equals("-..")) return 'd';
        else if (touches.equals(".")) return 'e';
        else if (touches.equals("..-.")) return 'f';
        else if (touches.equals("--.")) return 'g';
        else if (touches.equals("....")) return 'h';
        else if (touches.equals("..")) return 'i';
        else if (touches.equals(".---")) return 'j';
        else if (touches.equals("-.-")) return 'k';
        else if (touches.equals(".-..")) return 'l';
        else if (touches.equals("--")) return 'm';
        else if (touches.equals("-.")) return 'n';
        else if (touches.equals("---")) return 'o';
        else if (touches.equals(".--.")) return 'p';
        else if (touches.equals("--.-")) return 'q';
        else if (touches.equals(".-.")) return 'r';
        else if (touches.equals("...")) return 's';
        else if (touches.equals("-")) return 't';
        else if (touches.equals("..-")) return 'u';
        else if (touches.equals("...-")) return 'v';
        else if (touches.equals(".--")) return 'w';
        else if (touches.equals("-..-")) return 'x';
        else if (touches.equals("-.--")) return 'y';
        else if (touches.equals("--..")) return 'z';

        return '?';
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
