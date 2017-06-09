package com.example.jetty_jersey.dao;

import com.example.jetty_jersey.db.CustomHashMap;

/*
 PUT plane/id
 {
 "planetype" : "exmaple of plane type"
 }
*/
public class Plane
{
	private int planeId;
	private String planeType;

	// For json serialization
	public Plane()
	{
	}

	public Plane(int planeId, String planeType)
	{
		this.planeId = planeId;
		this.planeType = planeType;
	}

	public int getPlaneId()
	{
		return planeId;
	}

	public void setPlaneId(int id)
	{
		planeId = id;
	}

	public String getPlaneType()
	{
		return planeType;
	}

	public void setPlaneType(String planeType)
	{
		this.planeType = planeType;
	}
	
	//using to insert a plane as a line into the table Flight
	public CustomHashMap<String, String> toMap()
	{
		CustomHashMap<String, String> l = new CustomHashMap<String, String>();
		l.put("_id", String.valueOf(planeId));
		l.put("planeType;", planeType);
		return l;
	}

	@Override
	public String toString()
	{
		return "Plane(" + planeId + "," + planeType + ")";
	}

}
