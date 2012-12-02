/**
 * 
 */
package com.david.earthquake2.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.david.earthquake2.R;
import com.david.earthquake2.task.EarthQuakeUpdator;
import com.david.earthquake2.vo.Quake;

/**
 * @author David
 *
 */
public class EarthQuakeService extends Service {

	public static final String REFRESH_EARTHQUAKE_SERVICE_ACTION = "com.david.earthquake2.service.REFRESH_ACTION";
	private static final String TAG = "EarthQuakeService";
	private AsyncTask<String,Integer,List<Quake>> task;
	
	@Override
	public void onCreate() {
		Log.v(TAG,"onCreate calling...");
		super.onCreate();
		task = new EarthQuakeUpdator(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(TAG,"onStartCommand calling...");
		AsyncTask.Status status = task.getStatus();
		//如果是正在运行，则不刷新，否则执行刷新操作
		if(status == AsyncTask.Status.FINISHED){
			Log.v(TAG,"task finished. so create new one, and execute it...");
			task = new EarthQuakeUpdator(this);
			task.execute(this.getString(R.string.quake_feed));
		}else if(status == AsyncTask.Status.PENDING){
			Log.v(TAG,"task is pending, execute it now..");
			task.execute(this.getString(R.string.quake_feed));
		}else{
			Log.v(TAG,"task is running, so return");
		}
		return START_STICKY;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
