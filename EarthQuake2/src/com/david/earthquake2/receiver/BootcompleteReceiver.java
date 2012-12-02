/**
 * 
 */
package com.david.earthquake2.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.david.earthquake2.R;
import com.david.earthquake2.service.EarthQuakeService;

/**
 * @author David
 *
 */
public class BootcompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootcompleteReceiver";
	public static final String EARTH_QUAKE_RECEIVER_ACTION = "com.david.earthquake2.receiver.RECEIVER_ACTION";

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onReceive intent = " + intent.getAction());
		if(intent.getAction() == Intent.ACTION_BOOT_COMPLETED){
			Log.v(TAG, "Cellphone boot completed....");
			Toast.makeText(context, R.string.boot_completed, Toast.LENGTH_SHORT).show();
		}
		else if(intent.getAction() == EARTH_QUAKE_RECEIVER_ACTION){
			Log.v(TAG,context.getPackageName());
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
			int minutes = Integer.valueOf(sp.getString("PREF_UPDATE_FREQ","2"));
			setAlarm(context, minutes);
		}
		
	}

	private void setAlarm(Context context,int minutes){
		//set alarm 
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent();
		i.setAction(EarthQuakeService.REFRESH_EARTHQUAKE_SERVICE_ACTION);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		Log.v(TAG,"alarm manager repeat "+minutes+" minutes");
		am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), minutes*60*1000, pi);
	}

}
