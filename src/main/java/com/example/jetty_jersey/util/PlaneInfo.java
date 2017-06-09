package com.example.jetty_jersey.util;

import java.util.ArrayList;
import java.util.List;

import com.example.jetty_jersey.dao.Flight;
import com.example.jetty_jersey.dao.Plane;

public class PlaneInfo
{
	private final Plane plane;
	private final List<Flight> flights;

	public PlaneInfo(Plane plane, List<Flight> flights)
	{
		this.plane = plane;
		this.flights = flights;
	}

	public Plane getPlane()
	{
		return plane;
	}

	public List<Flight> getFlighs()
	{
		return flights;
	}
	public static List<PlaneInfo> alea(){
		List<PlaneInfo> pa = new ArrayList<PlaneInfo>();
		List<Flight> tab;
		Plane pl ;
		for(int i = 0 ; i<10; i++){
			tab = new ArrayList<Flight>(); 
			tab.add(new Flight(i+10, "commercialId", "departureAirport", "arrivalAirport", "2017/12/24 09:15", "2017/12/24 09:15", i));
			pl = new Plane(i+10, "planeType"+(i+10));
			pa.add(new PlaneInfo(pl, tab));
		}
		return pa;
	}
	public static void main(String[] args) {
		for (PlaneInfo p: PlaneInfo.alea()){
			System.out.println(p.plane.toString());
			System.out.println(p.flights.get(0).toString());
		}
	}

}
