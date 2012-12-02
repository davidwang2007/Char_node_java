package com.david.earthquake2.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;

public class Quake {

	private Date date;
	private String details;
	private Location location;
	private double magnitude;
	private String link;
	public Quake(Date date, String details, Location location, double magnitude,
			String link) {
		super();
		this.date = date;
		this.details = details;
		this.location = location;
		this.magnitude = magnitude;
		this.link = link;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public double getMagnitude() {
		return magnitude;
	}
	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
		String dateString = sdf.format(date);
		
		return dateString + ": " + magnitude + " " + details;
	}
}
