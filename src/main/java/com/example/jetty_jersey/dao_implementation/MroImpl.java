package com.example.jetty_jersey.dao_implementation;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jetty_jersey.dao.MRO;
import com.example.jetty_jersey.dao.Status;
import com.example.jetty_jersey.dao_interface.MroDao;
import com.example.jetty_jersey.db.DatabaseConnecter;
import com.example.jetty_jersey.db.Utility;

public class MroImpl implements MroDao
{

	private static Logger log = LogManager.getLogger(MccImpl.class.getName());
	private final Map<String, MRO> mroCache = new HashMap<String, MRO>();

	public MRO getMroById(int id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();

		MRO m = mroCache.get(id);
		if (m == null)
		{
			List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("mro", "_id", Integer.toString(id));
			if (res == null || res.size() <= 0)
				log.error("Unable to find MRO id : " + id + " in mro table!");
			else
			{
				m = new MRO(Utility.convertIntString(res.get(0).get("_id")), res.get(0).get("name"), res.get(0).get("email"));
			}
		}
		return m;
	}

	public List<MRO> getMrobyQualification(String qualification)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		List<MRO> mroList = new ArrayList<MRO>();

		List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("mro", "qualification", qualification);

		if (res == null || res.size() <= 0)
		{
			log.error("Unable to find MRO qualification : " + qualification + " in the database!");
			return null;
		} else
		{
			for (Map<String, String> m : res)
			{
				MRO mro = new MRO(Utility.convertIntString(m.get("_id")), m.get("name"), res.get(0).get("email"));
				mroList.add(mro);

			}
		}

		dbc.close();
		return mroList;
	}

	public List<MRO> getAllMros()
	{
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		List<Map<String, String>> results = dbConnect.selectAllFromTableName("mro");
		List<MRO> mroList = new ArrayList<MRO>();
		for (Map<String, String> m : results)
		{
			MRO mro = new MRO(Utility.convertIntString(m.get("_id")), m.get("name"), m.get("email"));
			mroList.add(mro);
		}
		dbConnect.close();
		return mroList;
	}

	public Status addMro(MRO mro)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.insertToTableName("mro", mro.toMap());
		dbc.close();
		return s;
	}

	public Status modifyMro(MRO mro)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.updateDataInTableNameWhereFieldEqValue("mro", "_id", String.valueOf(mro.getId()), mro.toMap());
		dbc.close();
		return s;
	}

	public Status deleteMro(int id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status deletedStatus = dbc.deleteAllFromTableNameWhereFieldEqValue("mro", "_id", String.valueOf(id));
		dbc.close();
		return deletedStatus;
	}

	public static void main(String[] args)
	{
		MroImpl test = new MroImpl();
		test.getAllMros();
		// MRO mro=new MRO(-1, "Kaba en test");
		// Status s = test.addMro(mro);
		test.getMrobyQualification("System Diagnostics design");
	}

}