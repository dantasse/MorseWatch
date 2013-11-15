package com.dantasse.morsewatch;

import android.content.ContentValues;
import android.content.Context;

import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

public class MorseRegistrationInformation extends RegistrationInformation {
	final Context context;
	
	protected MorseRegistrationInformation(Context context) {
		if (context == null) {
            throw new IllegalArgumentException("context == null");
        }
        this.context = context;
	}
	
	@Override
	public ContentValues getExtensionRegistrationConfiguration() {
		String extensionIcon = ExtensionUtils.getUriString(context,
                R.drawable.ic_extension);
        String iconHostapp = ExtensionUtils.getUriString(context,
                R.drawable.ic_launcher);

        String configurationText = context.getString(R.string.configuration_text);
        String extensionName = context.getString(R.string.extension_name);

        ContentValues values = new ContentValues();

        values.put(Registration.ExtensionColumns.CONFIGURATION_TEXT, configurationText);
        values.put(Registration.ExtensionColumns.EXTENSION_ICON_URI, extensionIcon);
        values.put(Registration.ExtensionColumns.EXTENSION_KEY,
                MorseExtensionService.EXTENSION_KEY);
        values.put(Registration.ExtensionColumns.HOST_APP_ICON_URI, iconHostapp);
        values.put(Registration.ExtensionColumns.NAME, extensionName);
        values.put(Registration.ExtensionColumns.NOTIFICATION_API_VERSION,
                getRequiredNotificationApiVersion());
        values.put(Registration.ExtensionColumns.PACKAGE_NAME, context.getPackageName());

        return values;
	}
	
	@Override
	public int getRequiredNotificationApiVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRequiredWidgetApiVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRequiredControlApiVersion() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getRequiredSensorApiVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
    public boolean isDisplaySizeSupported(int width, int height) {
        return ((width == MorseExtension.getSupportedControlWidth(context) && height == MorseExtension
                .getSupportedControlHeight(context)));
    }
}
