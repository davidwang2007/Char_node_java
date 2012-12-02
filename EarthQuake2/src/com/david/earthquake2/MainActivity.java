package com.david.earthquake2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.earthquake2.adapter.EarthQuakeAdapter;
import com.david.earthquake2.provider.EarthQuakeProviderMetaData.EarthQuakeTableMetaData;
import com.david.earthquake2.receiver.BootcompleteReceiver;
import com.david.earthquake2.service.EarthQuakeService;
import com.david.earthquake2.task.EarthQuakeUpdator;
import com.david.earthquake2.vo.Quake;

public class MainActivity extends Activity implements OnSharedPreferenceChangeListener{

	private ListView list;
	private static final String TAG = "MainActivity";
	public static final int DIALOG_EXIT = 0x01;
	public static final int DIALOG_DETAIL = 0x02;
	
	private long currentItemId = 0;
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private AsyncTask<String,Integer,List<Quake>> task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (ListView)findViewById(R.id.earthquake_list_view);
		Cursor c = getContentResolver().query(EarthQuakeTableMetaData.CONTENT_URI,null,null,null,null);
		ListAdapter adapter = new EarthQuakeAdapter(this, R.layout.earthquake_list_item, c,
				new String[]{EarthQuakeTableMetaData.DATE,EarthQuakeTableMetaData.DETAILS,
							EarthQuakeTableMetaData.LATITUDE,EarthQuakeTableMetaData.LONGITUDE,EarthQuakeTableMetaData.MAGNITUDE}, 
				new int[]{R.id.view_date,R.id.view_details,R.id.view_latitude,R.id.view_longitude,R.id.view_magnitude});
		list.setAdapter(adapter);
		list.setOnItemClickListener(itemClickListener);
		task = new EarthQuakeUpdator(this);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		sp.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(EarthQuakeUpdator.NOTIFICATION_ID);
		
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			currentItemId = id;
			showDialog(DIALOG_DETAIL);
		}
	};
	
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.refresh_btn:
			//judge the task status
			AsyncTask.Status status = task.getStatus();
			if(status == AsyncTask.Status.RUNNING){//if running return else excute
				Toast.makeText(this, getString(R.string.updating_earthquake), Toast.LENGTH_SHORT).show();
			}else if(status == AsyncTask.Status.FINISHED){
				Toast.makeText(this, getString(R.string.updating_earthquake_finished), Toast.LENGTH_SHORT).show();
			}else{
				task.execute(getString(R.string.quake_feed));
			}
			return true;
		case R.id.setting_preferences:
			Intent i = new Intent();//(this,UserPreferences.class);
			i.setAction(UserPreferences.ACTION_USER_PREFERENCES);
			//i.addCategory(Intent.CATEGORY_DEFAULT);
			startActivity(i);
			return true;
			
		}
		
		return super.onOptionsItemSelected(item);
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case DIALOG_EXIT:
			builder.setTitle(R.string.exit);
			builder.setMessage(R.string.exit_confirm);
			builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MainActivity.this.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
			builder.setNegativeButton(R.string.cancel, null);
			return builder.create();
		case DIALOG_DETAIL:
			builder.setTitle(R.string.dialog_detail_title);
			View view = getLayoutInflater().inflate(R.layout.quake_details, null);
			builder.setView(view);
			return builder.create();
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		Cursor c = getContentResolver().query(ContentUris.withAppendedId(EarthQuakeTableMetaData.CONTENT_URI, currentItemId), null, null, null, null);
		if(null != c && c.getCount() > 0 && c.moveToFirst() && id == DIALOG_DETAIL){
			float longitude = c.getFloat(c.getColumnIndex(EarthQuakeTableMetaData.LONGITUDE));
			float latitude = c.getFloat(c.getColumnIndex(EarthQuakeTableMetaData.LATITUDE));
			String details = c.getString(c.getColumnIndex(EarthQuakeTableMetaData.DETAILS));
			long date = c.getLong(c.getColumnIndex(EarthQuakeTableMetaData.DATE));
			float magitude = c.getFloat(c.getColumnIndex(EarthQuakeTableMetaData.MAGNITUDE));
			String link = c.getString(c.getColumnIndex(EarthQuakeTableMetaData.LINK));
			
			String dateString = sdf.format(new Date(date));
			String quakeText = "Magnitude "+magitude+
					"\n" + details + "\n"
					+ link+"\n"
					+"longitude " + longitude+"\n"
					+"latitude " + latitude;
			AlertDialog quakeDialog = (AlertDialog)dialog;
			quakeDialog.setTitle(dateString);
			TextView tv = (TextView)quakeDialog.findViewById(R.id.quakeDetailsTextView);
			tv.setText(quakeText);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showDialog(DIALOG_EXIT);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.v(TAG,"onSharedPreferenceChanged key = " + key);
		if((key.equals("PREF_AUTO_UPDATE") || key.equals("PREF_UPDATE_FREQ")) && sharedPreferences.getBoolean("PREF_AUTO_UPDATE", false)){
			Intent intent = new Intent();
			intent.setAction(BootcompleteReceiver.EARTH_QUAKE_RECEIVER_ACTION);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			sendBroadcast(intent);
		}else if(key.equals("PREF_AUTO_UPDATE") && sharedPreferences.getBoolean("PREF_AUTO_UPDATE", false)){
			Log.v(TAG,"Cancel alarm");
			AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent();
			i.setAction(EarthQuakeService.REFRESH_EARTHQUAKE_SERVICE_ACTION);
			i.addCategory(Intent.CATEGORY_DEFAULT);
			PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
			am.cancel(pi);
		}
		
	}
}
