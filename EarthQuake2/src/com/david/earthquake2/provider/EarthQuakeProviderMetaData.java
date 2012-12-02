/**
 * 
 */
package com.david.earthquake2.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author David
 *
 */
public class EarthQuakeProviderMetaData {

	public static final String AUTHORITY = "com.david.earthquake2.provider.EarthQuakeProvider";
	public static final String DB_NAME = "earthquake.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "earthquakes";
	
	private EarthQuakeProviderMetaData(){}
	
	public static final class EarthQuakeTableMetaData implements BaseColumns{
		private EarthQuakeTableMetaData(){}
		public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/earthquakes");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.david.earthquake";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.david.earthquake";
		/*
		private Date date;
		private String details;
		private Location location;
		private double magnitude;
		private String link;
		 * */
		public static final String DEFAULT_ORDER = "_date desc";
		
		public static final String DATE = "_date";
		public static final String DETAILS = "details";
		public static final String LONGITUDE = "longitude";
		public static final String LATITUDE = "latitude";
		public static final String MAGNITUDE = "magnitude";
		public static final String LINK = "link";
	}
	
}
