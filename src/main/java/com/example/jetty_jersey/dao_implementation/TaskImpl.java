package com.example.jetty_jersey.dao_implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jetty_jersey.util.TaskInfo;
import com.example.jetty_jersey.dao.Flight;
import com.example.jetty_jersey.dao.MRO;
import com.example.jetty_jersey.dao.Plane;
import com.example.jetty_jersey.dao.Status;
import com.example.jetty_jersey.dao.Task;
import com.example.jetty_jersey.dao.TaskGeneric;
import com.example.jetty_jersey.dao_interface.TaskDao;
import com.example.jetty_jersey.db.CustomHashMap;
import com.example.jetty_jersey.db.DatabaseConnecter;
import com.example.jetty_jersey.db.DatabaseSettings;
import com.example.jetty_jersey.db.Utility;

public class TaskImpl implements TaskDao
{
	private static Logger log = LogManager.getLogger(TaskImpl.class.getName());
	private final Map<String, Plane> planeCache = new HashMap<String, Plane>();
	private final Map<String, Flight> flightByPlaneIdCache = new HashMap<String, Flight>();
	private final Map<String, MRO> mroCache = new HashMap<String, MRO>();
	private final Map<String, TaskGeneric> taskGenericCache = new HashMap<String, TaskGeneric>();

	public TaskImpl()
	{
	}

	public List<TaskInfo> getAllTasks()
	{
		List<TaskInfo> l = getTasksInRange(0, DatabaseSettings.MAX_RESULTS_PER_QUERY);
		return l;
	}

	public TaskInfo getTasksById(int id)
	{
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		List<Map<String, String>> results = dbConnect.selectAllFromTableWhereFieldEqValue("task", "_id", Integer.toString(id));
		Map<String, String> m = results.get(0);
		Task t = new Task(Utility.convertIntString(m.get("_id")), Utility.convertIntString(m.get("idTaskGeneric")), m.get("startTime"), m.get("endTime"), Utility.convertIntString(m.get("planeId")),
				Utility.convertIntString(m.get("taskStatus")), Utility.convertIntString(m.get("mroId")), Utility.convertIntString(m.get("mccId")));
		MRO mro = getMROById(dbConnect, m.get("mroId"));
		Plane p = getPlaneById(dbConnect, m.get("planeId"));
		Flight f = getFlightByPlaneId(dbConnect, m.get("planeId"));
		TaskGeneric tg = getTaskGeneric(dbConnect, m.get("idTaskGeneric"));
		TaskInfo wrap = new TaskInfo(t, tg, p, f, mro);
		dbConnect.close();
		return wrap;
	}

	private TaskGeneric getTaskGeneric(DatabaseConnecter dbc, String id)
	{
		TaskGeneric tg = taskGenericCache.get(id);
		if (tg == null)
		{
			List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("taskgeneric", "_id", id);
			if (res == null || res.size() <= 0)
				log.error("Unable to find taskgeneric id : " + id + " in taskgeneric table!");
			else
			{
				Map<String, String> fst = res.get(0);
				tg = new TaskGeneric(Utility.convertIntString(fst.get("_id")), fst.get("description"), fst.get("periodicity"), fst.get("ataCategory"), Utility.convertBoolString(fst.get("hangarNeed")),
						Float.valueOf(fst.get("duration")), fst.get("planeType"));
				taskGenericCache.put(id, tg);
			}
		}
		return tg;
	}

	public List<TaskInfo> getTasksByPlaneId(int id)
	{
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		List<Map<String, String>> results = dbConnect.selectAllFromTableWhereFieldEqValue("task", "planeId", Integer.toString(id));

		List<TaskInfo> tl = new ArrayList<TaskInfo>();
		for (Map<String, String> m : results)
		{
			Task t = new Task(Utility.convertIntString(m.get("_id")), Utility.convertIntString(m.get("idTaskGeneric")), m.get("startTime"), m.get("endTime"),
					Utility.convertIntString(m.get("planeId")), Utility.convertIntString(m.get("taskStatus")), Utility.convertIntString(m.get("mroId")), Utility.convertIntString(m.get("mccId")));
			MRO mro = getMROById(dbConnect, m.get("mroId"));
			Plane p = getPlaneById(dbConnect, m.get("planeId"));
			Flight f = getFlightByPlaneId(dbConnect, m.get("planeId"));
			TaskGeneric tg = getTaskGeneric(dbConnect, m.get("idTaskGeneric"));
			TaskInfo wrap = new TaskInfo(t, tg, p, f, mro);
			tl.add(wrap);
		}
		dbConnect.close();
		Collections.sort(tl, new TaskInfoOrderByDateComparator());
		return tl;
	}

	public Status addTask(Task t)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.insertToTableName("task", t.toMap());
		dbc.close();
		return s;
	}

	public Status modifyTask(Task t)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.updateDataInTableNameWhereFieldEqValue("task", "_id", String.valueOf(t.getId()), t.toMap());
		dbc.close();
		return s;
	}

	public Status deleteTask(int id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status deletedStatus = dbc.deleteAllFromTableNameWhereFieldEqValue("task", "_id", String.valueOf(id));
		dbc.close();
		return deletedStatus;
	}

	public List<TaskInfo> getTasksInRange(int iStart, int iEnd)
	{
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		List<Map<String, String>> results = dbConnect.selectInRangeFromTableName("task", iStart, iEnd);
		List<TaskInfo> tl = new ArrayList<TaskInfo>();
		for (Map<String, String> m : results)
		{
			Task t = new Task(Utility.convertIntString(m.get("_id")), Utility.convertIntString(m.get("idTaskGeneric")), m.get("startTime"), m.get("endTime"),
					Utility.convertIntString(m.get("planeId")), Utility.convertIntString(m.get("taskStatus")), Utility.convertIntString(m.get("mroId")), Utility.convertIntString(m.get("mccId")));
			MRO mro = getMROById(dbConnect, m.get("mroId"));
			Plane p = getPlaneById(dbConnect, m.get("planeId"));
			Flight f = getFlightByPlaneId(dbConnect, m.get("planeId"));
			TaskGeneric tg = getTaskGeneric(dbConnect, m.get("idTaskGeneric"));
			TaskInfo wrap = new TaskInfo(t, tg, p, f, mro);
			tl.add(wrap);
		}
		dbConnect.close();
		return tl;
	}

	private MRO getMROById(DatabaseConnecter dbc, String id)
	{
		if (id == null || id.length() == 0 || id.equals("-1"))
			return new MRO(-1, "Not assigned", "");
		MRO m = mroCache.get(id);
		if (m == null)
		{
			List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("mro", "_id", id);
			if (res == null || res.size() <= 0)
				log.error("Unable to find MRO id : " + id + " in mro table!");
			else
			{
				m = new MRO(Utility.convertIntString(id), res.get(0).get("name"), res.get(0).get("email"));
				m.setQualification(res.get(0).get("qualification"));
				mroCache.put(id, m);
			}
		}
		return m;
	}

	private Plane getPlaneById(DatabaseConnecter dbc, String id)
	{
		Plane p = planeCache.get(id);
		if (p == null)
		{
			List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("plane", "_id", id);
			if (res == null || res.size() <= 0)
				log.error("Unable to find plane id : " + id + " in plane table!");
			else
			{
				p = new Plane(Utility.convertIntString(id), res.get(0).get("planetype"));
				planeCache.put(id, p);
			}
		}
		return p;
	}

	private Flight getFlightByPlaneId(DatabaseConnecter dbc, String pid)
	{
		Flight f = flightByPlaneIdCache.get(pid);
		if (f == null || f.getId() == -1) // In case of empty flight, we query again the database to see if there is new flight added
		{
			List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValueSortAscendingByField("flight", "planeId", pid, "departureTime");
			if (res == null || res.size() <= 0)
			{
				log.warn("Unable to find planeId id : " + pid + " in flight table! Creating empty flight");
				f = new Flight(-1, "NO FLIGHT FOR PLANE " + pid, "N/A", "N/A", Utility.convertDateToString(new Date()), Utility.convertDateToString(new Date()), Integer.parseInt(pid));
			} else
			{
				Map<String, String> fst = res.get(0);
				f = new Flight(Utility.convertIntString(fst.get("_id")), fst.get("commercialId"), fst.get("departureAirport"), fst.get("arrivalAirport"), fst.get("departureTime"),
						fst.get("arrivalTime"), Utility.convertIntString(fst.get("planeId")));
				flightByPlaneIdCache.put(pid, f);
			}
		}
		return f;
	}

	// For unit test
	public static void main(String[] args)
	{
		TaskImpl test = new TaskImpl();
		List<TaskInfo> l = test.getAllTasks();
		for (TaskInfo ti : l)
		{
			System.out.println(ti);
		}
	}

	public Status addMroToTask(int mroId, int taskId)
	{
		Map<String, String> data = new CustomHashMap<String, String>();
		data.put("mroId", Integer.toString(mroId));
		data.put("taskStatus", "1");
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		Status s = dbConnect.updateDataInTableNameWhereFieldEqValue("task", "_id", Integer.toString(taskId), data);
		dbConnect.close();
		return s;
	}

	public Status notifyTaskDone(int taskId)
	{
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		Map<String, String> data = new CustomHashMap<String, String>();
		data.put("taskStatus", "2");
		Status s = dbConnect.updateDataInTableNameWhereFieldEqValue("task", "_id", Integer.toString(taskId), data);
		dbConnect.close();
		return s;
	}

	public List<TaskInfo> getTasksByMroId(int mroId)
	{
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		List<Map<String, String>> results = dbConnect.selectAllFromTableWhereFieldEqValue("task", "mroId", Integer.toBinaryString(mroId));
		List<TaskInfo> tl = new ArrayList<TaskInfo>();
		for (Map<String, String> m : results)
		{
			Task t = new Task(Utility.convertIntString(m.get("_id")), Utility.convertIntString(m.get("idTaskGeneric")), m.get("startTime"), m.get("endTime"),
					Utility.convertIntString(m.get("planeId")), Utility.convertIntString(m.get("taskStatus")), Utility.convertIntString(m.get("mroId")), Utility.convertIntString(m.get("mccId")));
			MRO mro = getMROById(dbConnect, m.get("mroId"));
			Plane p = getPlaneById(dbConnect, m.get("planeId"));
			Flight f = getFlightByPlaneId(dbConnect, m.get("planeId"));
			TaskGeneric tg = getTaskGeneric(dbConnect, m.get("idTaskGeneric"));
			TaskInfo wrap = new TaskInfo(t, tg, p, f, mro);
			tl.add(wrap);
		}
		dbConnect.close();
		return tl;
	}

}
