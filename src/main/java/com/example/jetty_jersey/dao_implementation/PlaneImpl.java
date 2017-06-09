
package com.example.jetty_jersey.dao_implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.jetty_jersey.dao.Flight;
import com.example.jetty_jersey.dao.Plane;
import com.example.jetty_jersey.dao.Status;
import com.example.jetty_jersey.dao_interface.PlaneDao;
import com.example.jetty_jersey.db.DatabaseConnecter;
import com.example.jetty_jersey.db.Utility;
import com.example.jetty_jersey.util.PlaneInfo;

public class PlaneImpl implements PlaneDao
{
	//return a list of all planes in the table plane wich have some type
	public List<Plane> getPlanesbyType(String type)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("plane", "planetype", type);
		dbc.close();
		List<Plane> pl = new ArrayList<Plane>();
		for (Map<String, String> m : res)
		{
			Plane p = new Plane(Utility.convertIntString(m.get("_id")), m.get("planetype"));
			pl.add(p);
		}
		return pl;
	}

	//return a list of all planes in the table plane
	public List<Plane> getAllPlanes()
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		List<Map<String, String>> res = dbc.selectAllFromTableName("plane");
		dbc.close();
		List<Plane> pl = new ArrayList<Plane>();
		for (Map<String, String> m : res)
		{
			Plane p = new Plane(Utility.convertIntString(m.get("_id")), m.get("planetype"));
			pl.add(p);
		}
		return pl;
	}

	//return a unique plane by his id
	public Plane getPlanebyId(int id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("plane", "_id", Integer.toString(id));
		dbc.close();
		Map<String, String> ligne = res.get(0);
		Plane p = new Plane(Utility.convertIntString(ligne.get("_id")), ligne.get("planetype"));
		return p;
	}

	//insert a plane into the table plane
	public Status addPlane(Plane plane)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.insertToTableName("plane", plane.toMap());
		dbc.close();
		return s;
	}

	//delete a plane by his id field
	public Status deletePlane(int id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.deleteAllFromTableNameWhereFieldEqValue("plane", "_id", String.valueOf(id));
		dbc.close();
		return s;
	}

	
	public List<PlaneInfo> getAllPlaneInfos()
	{
		List<Plane> lp = getAllPlanes();
		List<PlaneInfo> lpi = new ArrayList<PlaneInfo>();
		for (Plane p : lp)
		{
			PlaneInfo pinfo = new PlaneInfo(p, getAllFlights(p.getPlaneId()));
			lpi.add(pinfo);
		}
		return lpi;
	}

	//return a list of all flight concerning a plane
	private List<Flight> getAllFlights(int planeId)
	{
		FlightImpl fImpl = new FlightImpl();
		return fImpl.getFlightsbyPlaneId(planeId);
	}

	//print test of the function getAllplanes
	public static void main(String[] args)
	{
		PlaneImpl pi = new PlaneImpl();
		// Plane planetest = new Plane(-1, "Kaba en test");
		List<Plane> planeList = pi.getAllPlanes();
		for (Plane p : planeList)
		{
			System.out.println(p.toString());
		}
	}

}
