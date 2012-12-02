/**
 * 
 */
package com.david.earthquake2;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author David
 *
 */
public class UserPreferences extends PreferenceActivity {

	public static final String ACTION_USER_PREFERENCES = "com.david.earthquake2.PREFERENCES_ACTION";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.userpreferences);
	}
}
