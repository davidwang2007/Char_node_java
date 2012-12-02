/**
 * 
 */
package com.david.earthquake2.provider;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.david.earthquake2.provider.EarthQuakeProviderMetaData.EarthQuakeTableMetaData;

/**
 * @author David
 *
 */
public class EarthQuakeProvider extends ContentProvider {

	private static final String TAG = "EarthQuakeProvider";
	private SQLiteOpenHelper mOpenHelper;
	
	private static final Map<String,String> sEarthQuakeProjections;
	static{
		sEarthQuakeProjections = new HashMap<String,String>();
		sEarthQuakeProjections.put(EarthQuakeTableMetaData._ID, EarthQuakeTableMetaData._ID);
		sEarthQuakeProjections.put(EarthQuakeTableMetaData.DATE, EarthQuakeTableMetaData.DATE);
		sEarthQuakeProjections.put(EarthQuakeTableMetaData.LINK, EarthQuakeTableMetaData.LINK);
		sEarthQuakeProjections.put(EarthQuakeTableMetaData.DETAILS, EarthQuakeTableMetaData.DETAILS);
		sEarthQuakeProjections.put(EarthQuakeTableMetaData.LONGITUDE, EarthQuakeTableMetaData.LONGITUDE);
		sEarthQuakeProjections.put(EarthQuakeTableMetaData.LATITUDE, EarthQuakeTableMetaData.LATITUDE);
		sEarthQuakeProjections.put(EarthQuakeTableMetaData.MAGNITUDE, EarthQuakeTableMetaData.MAGNITUDE);
	}
	
	//set up uris
	private static final UriMatcher sUriMatcher;
	private static final int INCOMING_EARTHQUAKE_COLLECTION_URI_INDICATOR = 1;
	private static final int INCOMING_EARTHQUAKE_ITEM_URI_INDICATOR = 2;
	static{
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(EarthQuakeProviderMetaData.AUTHORITY,"earthquakes",INCOMING_EARTHQUAKE_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(EarthQuakeProviderMetaData.AUTHORITY,"earthquakes/#", INCOMING_EARTHQUAKE_ITEM_URI_INDICATOR);
	}
	
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		Log.v(TAG,"onCreate...");
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder sq = new SQLiteQueryBuilder();
		sq.setTables(EarthQuakeProviderMetaData.TABLE_NAME);
		sq.setProjectionMap(sEarthQuakeProjections);
		switch(sUriMatcher.match(uri)){
		case INCOMING_EARTHQUAKE_COLLECTION_URI_INDICATOR:
			break;
		case INCOMING_EARTHQUAKE_ITEM_URI_INDICATOR:
			sq.appendWhere(EarthQuakeTableMetaData._ID+"="+uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknow URI " + uri);
		}
		if(TextUtils.isEmpty(sortOrder)){
			sortOrder = EarthQuakeTableMetaData.DEFAULT_ORDER;
		}
		Cursor c = sq.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
		
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)){
		case INCOMING_EARTHQUAKE_COLLECTION_URI_INDICATOR:
			return EarthQuakeTableMetaData.CONTENT_TYPE;
		case INCOMING_EARTHQUAKE_ITEM_URI_INDICATOR:
			return EarthQuakeTableMetaData.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		if(sUriMatcher.match(uri) != INCOMING_EARTHQUAKE_COLLECTION_URI_INDICATOR)
			throw new IllegalArgumentException("Unknown URI " + uri);
		values = null == values ? new ContentValues(): values;
		long id = mOpenHelper.getWritableDatabase().insertOrThrow(EarthQuakeProviderMetaData.TABLE_NAME, EarthQuakeTableMetaData.DETAILS, values);
		if(id > 0){
			Uri rowUri = ContentUris.withAppendedId(EarthQuakeTableMetaData.CONTENT_URI, id);
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		switch(sUriMatcher.match(uri)){
		case INCOMING_EARTHQUAKE_COLLECTION_URI_INDICATOR:
			count = db.delete(EarthQuakeProviderMetaData.TABLE_NAME, selection,selectionArgs);
			break;
		case INCOMING_EARTHQUAKE_ITEM_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			count = db.delete(EarthQuakeProviderMetaData.TABLE_NAME, 
					EarthQuakeTableMetaData._ID+"="+rowId+
					(TextUtils.isEmpty(selection) ? "" : (" AND (" + selection+')')), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknow URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		switch(sUriMatcher.match(uri)){
		case INCOMING_EARTHQUAKE_COLLECTION_URI_INDICATOR:
			count = db.update(EarthQuakeProviderMetaData.TABLE_NAME,values, selection,selectionArgs);
			break;
		case INCOMING_EARTHQUAKE_ITEM_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			count = db.update(EarthQuakeProviderMetaData.TABLE_NAME,values, 
					EarthQuakeTableMetaData._ID+"="+rowId+
					(TextUtils.isEmpty(selection) ? "" : (" AND (" + selection+')')), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknow URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper{
		public DatabaseHelper(Context context){
			super(context,EarthQuakeProviderMetaData.DB_NAME,null,EarthQuakeProviderMetaData.DB_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v(TAG,"DatabaseHelper innert onCreate...");
			StringBuilder sb = new StringBuilder("create table ")
				.append(EarthQuakeProviderMetaData.TABLE_NAME)
				.append("(")
					.append(EarthQuakeTableMetaData._ID).append(" integer primary key autoincrement").append(",")
					.append(EarthQuakeTableMetaData.DATE).append(" integer").append(",")
					.append(EarthQuakeTableMetaData.DETAILS).append(" TEXT").append(",")
					.append(EarthQuakeTableMetaData.LINK).append(" TEXT").append(",")
					.append(EarthQuakeTableMetaData.LATITUDE).append(" float").append(",")
					.append(EarthQuakeTableMetaData.LONGITUDE).append(" float").append(",")
					.append(EarthQuakeTableMetaData.MAGNITUDE).append(" float")
				.append(")");
			db.execSQL(sb.toString());
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.d(TAG,"inner onupgrade called");
			Log.w(TAG, "Upgrading database from version " +oldVersion + " to " + newVersion +
					", which will destroy all old data");
			db.execSQL("drop table is exists " + EarthQuakeProviderMetaData.TABLE_NAME);
			onCreate(db);
		}
	}
	
}
