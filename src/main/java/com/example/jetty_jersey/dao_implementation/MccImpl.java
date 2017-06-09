package com.example.jetty_jersey.dao_implementation;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jetty_jersey.dao.MCC;
import com.example.jetty_jersey.dao.Status;
import com.example.jetty_jersey.dao_interface.MccDao;
import com.example.jetty_jersey.db.DatabaseConnecter;
import com.example.jetty_jersey.db.DatabaseSettings;
import com.example.jetty_jersey.util.Couple;

public class MccImpl implements MccDao
{

	private static Logger log = LogManager.getLogger(MccImpl.class.getName());
	private static Map<String, MCC> mccCache = new HashMap<String, MCC>();

	public String getEmailById(String id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		MCC m = mccCache.get(id);
		if (m == null)
		{
			List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("mcc", "_id", id);
			if (res == null || res.size() <= 0)
				log.error("Unable to find MCC id : " + id + " in the database!");
			else
			{
				m = new MCC(res.get(0).get("_id"), res.get(0).get("email"), res.get(0).get("pass"));
			}
		}
		return m.getEmail();
	}

	public List<MCC> getAllMccs()
	{
		return getMccsInRange(0, DatabaseSettings.MAX_RESULTS_PER_QUERY);
	}

	public MCC getMccById(String id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		MCC m = mccCache.get(id);
		if (m == null)
		{
			List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("mcc", "_id", id);
			if (res == null || res.size() <= 0)
				log.error("Unable to find MCC id : " + id + " in the database!");
			else
			{
				m = new MCC(res.get(0).get("_id"), res.get(0).get("email"), res.get(0).get("pass"));
				m.setEmail(res.get(0).get("email"));
				mccCache.put(id, m);
			}
		}
		return m;
	}

	public Status addMcc(MCC mc)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.insertToTableName("mcc", mc.toMap());
		dbc.close();
		return s;
	}

	public Status modifyMcc(MCC mc)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status s = dbc.updateDataInTableNameWhereFieldEqValue("mcc", "_id", mc.getMccId(), mc.toMap());
		dbc.close();
		return s;
	}

	public Status deleteMcc(int id)
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		Status deletedStatus = dbc.deleteAllFromTableNameWhereFieldEqValue("mcc", "_id", String.valueOf(id));
		dbc.close();
		return deletedStatus;
	}

	public List<MCC> getMccsInRange(int iStart, int iEnd)
	{
		DatabaseConnecter dbConnect = new DatabaseConnecter();
		List<Map<String, String>> results = dbConnect.selectInRangeFromTableName("mcc", iStart, iEnd);
		List<MCC> mccList = new ArrayList<MCC>();
		for (Map<String, String> m : results)
		{
			MCC mcc = new MCC(m.get("_id"), m.get("email"), m.get("pass"));
			Couple cp = new Couple(mcc.getEmail(), mcc.getPass(), "mcc");
			mccList.add(mcc);
		}
		dbConnect.close();
		return mccList;
	}

	// For unit test
	public static void main(String[] args)
	{
		MccImpl test = new MccImpl();
		test.getAllMccs();
		Status s = test.deleteMcc(1);
		System.out.println("Delete Mcc _id=1:" + s.toString());
	}

}
