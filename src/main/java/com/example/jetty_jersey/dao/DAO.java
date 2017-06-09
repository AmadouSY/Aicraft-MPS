package com.example.jetty_jersey.dao;

import com.example.jetty_jersey.dao_implementation.FlightImpl;
import com.example.jetty_jersey.dao_implementation.MccImpl;
import com.example.jetty_jersey.dao_implementation.MroImpl;
import com.example.jetty_jersey.dao_implementation.PlaneImpl;
import com.example.jetty_jersey.dao_implementation.TaskGenericImpl;
import com.example.jetty_jersey.dao_implementation.TaskImpl;
import com.example.jetty_jersey.dao_interface.FlightDao;
import com.example.jetty_jersey.dao_interface.MccDao;
import com.example.jetty_jersey.dao_interface.MroDao;
import com.example.jetty_jersey.dao_interface.PlaneDao;
import com.example.jetty_jersey.dao_interface.TaskDao;
import com.example.jetty_jersey.dao_interface.TaskGenericDao;

public class DAO
{
	private static TaskDao t;
	private static FlightDao f;
	private static PlaneDao p;
	private static TaskGenericDao tg;
	private static MccDao mccdao;
	private static MroDao mrodao;

	public static MccDao getMccDao(){
		if (mccdao == null){
			mccdao = new MccImpl();
		}
		return mccdao;
	}
	
	public static MroDao getMroDao(){
		if(mrodao == null){
			mrodao =new MroImpl();
		}
		return mrodao;
	}
	public static FlightDao getFlightDao()
	{
		if (f == null)
		{
			f = new FlightImpl();
		}
		return f;
	}

	public static TaskDao getTaskDao()
	{
		if (t == null)
		{
			t = new TaskImpl();
		}
		return t;
	}

	public static TaskGenericDao getTaskGenericDao()
	{
		if (tg == null)
		{
			tg = new TaskGenericImpl();
		}
		return tg;
	}

	public static PlaneDao getPlaneDao()
	{
		if (p == null)
		{
			p = new PlaneImpl();
		}
		return p;
	}

}
