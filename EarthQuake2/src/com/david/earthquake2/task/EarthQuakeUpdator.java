/**
 * 
 */
package com.david.earthquake2.task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.david.earthquake2.MainActivity;
import com.david.earthquake2.R;
import com.david.earthquake2.provider.EarthQuakeProviderMetaData.EarthQuakeTableMetaData;
import com.david.earthquake2.vo.Quake;
/**
 * @author David
 *
 */
public class EarthQuakeUpdator extends AsyncTask<String, Integer, List<Quake>> {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
	private Context context;
	private static final String TAG = "EarthQuakeUpdator";
	private ProgressDialog dialog = null;
	private NotificationManager nm;
	public static final int NOTIFICATION_ID = 0x11;
	
	private EarthQuakeUpdator(){}
	
	public EarthQuakeUpdator(Context mContext){
		context = mContext;
		nm = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	@Override
	protected void onPostExecute(List<Quake> result) {
		//update to database
		for(Quake quake : result){
			String link = quake.getLink();
			String details = quake.getDetails();
			long date = quake.getDate().getTime();
			double longitude = quake.getLocation().getLongitude();
			double latitude = quake.getLocation().getLatitude();
			double magnitude = quake.getMagnitude();
			
			//判断是否已经存在了
			Cursor cursor = context.getContentResolver().query(EarthQuakeTableMetaData.CONTENT_URI, null, 
					EarthQuakeTableMetaData.DETAILS+"=? AND " +
					EarthQuakeTableMetaData.DATE+"=?",new String[]{details,String.valueOf(date)} , null);
			if(null == cursor || cursor.getCount() == 0){
				ContentValues values = new ContentValues();
				values.put(EarthQuakeTableMetaData.DATE, date);
				values.put(EarthQuakeTableMetaData.DETAILS, details);
				values.put(EarthQuakeTableMetaData.LATITUDE, latitude);
				values.put(EarthQuakeTableMetaData.LINK, link);
				values.put(EarthQuakeTableMetaData.LONGITUDE, longitude);
				values.put(EarthQuakeTableMetaData.MAGNITUDE, magnitude);
				context.getContentResolver().insert(EarthQuakeTableMetaData.CONTENT_URI, values);
				//判断是否需要通知
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
				int limitMagnitude = Integer.valueOf(sp.getString("PREF_MIN_MAG", "3"));
				if(magnitude > limitMagnitude){
					notificateEarthQuake(details, magnitude, date);
				}
			}
		}
		if(null != dialog)
			dialog.dismiss();
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		String msg = String.format(context.getResources().getText(R.string.add_earthquake_format).toString(), values[0]);
		if(values[0] == 0 && context instanceof Activity){
			dialog = new ProgressDialog(context);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setTitle(R.string.dialog_status);
			dialog.setMessage(context.getResources().getText(R.string.updating_earthquake));
			dialog.setCancelable(false);
			dialog.show();
		}
		if(null != dialog)
			dialog.setMessage(msg);
	}
	
	
	@Override
	protected List<Quake> doInBackground(String... params) {
		publishProgress(0);
		String feed = params[0];
		List<Quake> earthquakes = new ArrayList<Quake>();
		int count = 0;
		URL url;
		try{
			url = new URL(feed);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK){
				InputStream in = conn.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				//解析地震数据
				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();
				//获得一个地震项的列表
				NodeList nl = docEle.getElementsByTagName("entry");
				
				if(nl != null && nl.getLength() > 0){
					
					for(int i = 0; i < nl.getLength(); i++){
						publishProgress(++count);
						Element entry = (Element)nl.item(i);
						Element title = (Element)entry.getElementsByTagName("title").item(0);
						Element g = (Element)entry.getElementsByTagName("georss:point").item(0);
						Element when = (Element)entry.getElementsByTagName("updated").item(0);
						Element link = (Element)entry.getElementsByTagName("link").item(0);
						
						String details = title.getFirstChild().getNodeValue();
						String hostname = "";//"http://earthquake.usgs.gov";
						String linkString = hostname + link.getAttribute("href");
						
						String point = g.getFirstChild().getNodeValue();
						String dt = when.getFirstChild().getNodeValue();
						Date qdate = new GregorianCalendar(0, 0, 0).getTime();
						try{
							qdate = sdf.parse(dt);
						}catch(ParseException ex){
							Log.e(TAG, ex.getMessage());
							ex.printStackTrace();
						}
						String[] location = point.split(" ");
						Location l = new Location("dummyGPS");
						l.setLatitude(Double.parseDouble(location[0]));
						l.setLongitude(Double.parseDouble(location[1]));
						
						String magnitudeString = details.split(" ")[1];
						int end = magnitudeString.length() -1;
						double magnitude = Double.parseDouble(magnitudeString.substring(0, end));
						
						details = details.split(",")[1].trim();
						Quake quake = new Quake(qdate,details,l,magnitude,linkString);
						earthquakes.add(quake);
					}
				}
			}
		}catch(Exception ex){
			Log.e(TAG, ex.getMessage());
			ex.printStackTrace();
		}
		return earthquakes;
	}

	private void notificateEarthQuake(String details,double magnitude,long when){
		Notification n = new Notification();
		n.flags |= Notification.FLAG_NO_CLEAR;
		n.icon = android.R.drawable.ic_dialog_alert;
		n.when = when;
		
		double vibrateLength = 100*Math.exp(0.53*magnitude);
		long[] vibrate = new long[]{100,100,(long)vibrateLength};
		n.vibrate = vibrate;
		
		Intent i = new Intent();
		i.setClassName(MainActivity.class.getPackage().getName(), MainActivity.class.getName());
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
		String title = details + " earthquake occured!";
		String text = "magnitude " + magnitude + "\nTime " + sdf.format(new Date(when));
		n.setLatestEventInfo(context, title, text, pi);
		nm.notify(NOTIFICATION_ID, n);
		Toast.makeText(context, title+"\n"+text, Toast.LENGTH_SHORT).show();
	}
	
}
