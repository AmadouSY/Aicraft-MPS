package com.example.jetty_jersey.dao_implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.jetty_jersey.dao.Status;
import com.example.jetty_jersey.dao.TaskGeneric;
import com.example.jetty_jersey.dao_interface.TaskGenericDao;
import com.example.jetty_jersey.db.DatabaseConnecter;
import com.example.jetty_jersey.db.Utility;

public class TaskGenericImpl implements TaskGenericDao
{

	public Status addTaskGeneric(TaskGeneric tg)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.insertToTableName("taskgeneric", tg.toMap());
		dbc.close();
		return s;
	}

	public Status modifyTaskGeneric(TaskGeneric tg)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.updateDataInTableNameWhereFieldEqValue("taskgeneric", "_id", String.valueOf(tg.getId()), tg.toMap());
		dbc.close();
		return s;
	}

	public Status deleteTaskGeneric(int id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status deletedStatus = dbc.deleteAllFromTableNameWhereFieldEqValue("taskgeneric", "_id", String.valueOf(id));
		dbc.close();
		return deletedStatus;
	}

	public List<TaskGeneric> getGenericTasksByPlaneType(String planeType)
	{
		List<TaskGeneric> l = new ArrayList<TaskGeneric>();
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		List<Map<String, String>> res = dbConnect.selectAllFromTableWhereFieldEqValueSortAscendingByField("taskgeneric", "planeType", planeType, "_id");
		dbConnect.close();
		for (Map<String, String> m : res)
		{
			TaskGeneric tg = new TaskGeneric(Utility.convertIntString(m.get("_id")), m.get("description"), m.get("periodicity"), m.get("ataCategory"), Utility.convertBoolString(m.get("hangarNeed")),
					Utility.convertFloatString(m.get("duration")), m.get("planeType"));
			l.add(tg);
		}
		return l;
	}

	// For unit test
	public static void main(String[] args)
	{
		TaskGenericImpl test = new TaskGenericImpl();
		List<TaskGeneric> l2 = test.getGenericTasksByPlaneType("Airbus A380");
		for (TaskGeneric tg : l2)
		{
			System.out.println(tg);
		}
	}

}
