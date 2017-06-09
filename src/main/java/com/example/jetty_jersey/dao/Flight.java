package com.example.jetty_jersey.dao;

import java.util.Date;
import com.example.jetty_jersey.db.CustomHashMap;
import com.example.jetty_jersey.db.Utility;

/*
PUT flight/id
 {
 "comercialid" : "exmaple of comercial id"
 "departureairport" : "example of departure airport"
 "arrivalairport" : "example of arrival airport"
 "departuretime" : "example of departure time"
 "arrivaltime" : "example of arrival time"
 }
*/
public class Flight
{

	private final int id;
	private String commercialId;
	private String departureAirport;
	private String arrivalAirport;
	private String departureTime;
	private String arrivalTime;
	private int planeId;

	// For json serialization
	public Flight()
	{
		this.id = -1;
	}

	public Flight(int id, String commercialId, String departureAirport, String arrivalAirport, String departureTime, String arrivalTime, int planeId)
	{
		this.id = id;
		this.commercialId = commercialId;
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.planeId = planeId;
	}

	// For unit test only
	public Flight(int id, int planeId)
	{
		this.id = id;
		this.commercialId = "SIMDATA" + id;
		this.departureAirport = "Cristiano Ronaldo-FNC";
		this.arrivalAirport = "Narsarsuaq-UAK";
		this.departureTime = Utility.convertDateToString(new Date());
		this.arrivalTime = Utility.convertDateToString(new Date());
		this.planeId = planeId;
	}

	public int getId()
	{
		return id;
	}

	public int getPlaneId()
	{
		return planeId;
	}

	public void setPlaneId(int planeId)
	{
		this.planeId = planeId;
	}

	public String getCommercialId()
	{
		return commercialId;
	}

	public void setCommercialId(String commercialId)
	{
		this.commercialId = commercialId;
	}

	public String getDepartureAirport()
	{
		return departureAirport;
	}

	public void setDepartureAirport(String departureAirport)
	{
		this.departureAirport = departureAirport;
	}

	public String getArrivalAirport()
	{
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport)
	{
		this.arrivalAirport = arrivalAirport;
	}

	public String getDepartureTime()
	{
		return departureTime;
	}

	public void setDepartureTime(String departureTime)
	{
		this.departureTime = departureTime;
	}

	public String getArrivalTime()
	{
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime)
	{
		this.arrivalTime = arrivalTime;
	}

	public Date getDepartureTimeAsDate()
	{
		return Utility.convertDateString(departureTime);
	}

	public Date getArrivalTimeAsDate()
	{
		return Utility.convertDateString(arrivalTime);
	}

	@Override
	public String toString()
	{
		return "Flight(" + id + "," + commercialId + "," + departureAirport + "," + arrivalAirport + "," + departureTime + "," + arrivalTime + "," + planeId + ")";
	}
	
	//using to insert a flight as a line into the table Flight
	public CustomHashMap<String, String> toMap()
	{
		CustomHashMap<String, String> chm = new CustomHashMap<String, String>();
		chm.put("_id", String.valueOf(id));
		chm.put("commercialId", commercialId);
		chm.put("departureAirport", departureAirport);
		chm.put("arrivalAirport", arrivalAirport);
		chm.put("departureTime", departureTime);
		chm.put("arrivalTime", arrivalTime);
		chm.put("planeId", String.valueOf(planeId));
		return chm;
	}

}
