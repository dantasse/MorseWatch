package com.dantasse.morsewatch;

import com.sonyericsson.extras.liveware.extension.util.ExtensionService;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;


public class HelloWatchExtensionService extends ExtensionService {

	public static final String EXTENSION_KEY = "com.dantasse.hellosmartwatch.key";
	public static final String LOG_TAG = "HelloWatchExtension";
	
	public HelloWatchExtensionService() {
		super(EXTENSION_KEY);
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
    }
	
	@Override
	protected RegistrationInformation getRegistrationInformation() {
		// TODO Auto-generated method stub
		return new HelloWatchRegistrationInformation(this);
	}

	@Override
	protected boolean keepRunningWhenConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ControlExtension createControlExtension(String hostAppPackageName) {
		return new HelloWatchExtension(this, hostAppPackageName);
	}
}
