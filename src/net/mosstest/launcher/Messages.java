package net.mosstest.launcher;


import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "net.mosstest.launcher.messages"; //$NON-NLS-1$

	private static ResourceBundle resBundle = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static void changeLanguage(String identifier) {
		resBundle = ResourceBundle.getBundle(BUNDLE_NAME + "." + identifier);
	}

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return resBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
