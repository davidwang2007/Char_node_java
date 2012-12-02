package com.david.earthquake2.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.david.earthquake2.R;
import com.david.earthquake2.provider.EarthQuakeProviderMetaData.EarthQuakeTableMetaData;

public class EarthQuakeAdapter extends SimpleCursorAdapter {

	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public EarthQuakeAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void bindView(View view, Context context, Cursor c) {
		// TODO Auto-generated method stub
		float longitude = c.getFloat(c.getColumnIndex(EarthQuakeTableMetaData.LONGITUDE));
		float latitude = c.getFloat(c.getColumnIndex(EarthQuakeTableMetaData.LATITUDE));
		String details = c.getString(c.getColumnIndex(EarthQuakeTableMetaData.DETAILS));
		long date = c.getLong(c.getColumnIndex(EarthQuakeTableMetaData.DATE));
		float magitude = c.getFloat(c.getColumnIndex(EarthQuakeTableMetaData.MAGNITUDE));
		
		TextView tv = (TextView)view.findViewById(R.id.view_date);
		tv.setText(sdf.format(new Date(date)));
		tv = (TextView)view.findViewById(R.id.view_details);
		tv.setText(details);
		tv = (TextView)view.findViewById(R.id.view_latitude);
		tv.setText("latitude " + latitude);
		tv = (TextView)view.findViewById(R.id.view_longitude);
		tv.setText("longitude " + longitude);
		tv = (TextView)view.findViewById(R.id.view_magnitude);
		tv.setText("magnitude " + magitude);
		
	}
}
