package com.example.jetty_jersey.dao;

import java.util.Date;

import com.example.jetty_jersey.db.CustomHashMap;
import com.example.jetty_jersey.db.Utility;

public class Task
{
	private final int id;
	private final int idTaskGeneric;
	private String startTime;
	private String endTime;
	private int planeId;
	private int taskStatus; // 1=Not dispatch to MRO, 0=Dispatched, 2=On going, 3=Done
	private int mroId;
	private int mccId;

	// For json serialization
	public Task()
	{
		this.id = -1;
		this.idTaskGeneric = id;
	}

	// For unit test only
	public Task(Date d)
	{
		this.id = (int) (Math.random() * 100);
		this.idTaskGeneric = id;
		this.startTime = Utility.convertDateToString(d);
		this.endTime = startTime;
		this.planeId = id;
		this.taskStatus = 1;
		this.mroId = -1;
		this.mccId = -1;
	}

	public Task(int id, int idTaskGeneric, String startTime, String endTime, int planeId, int taskStatus, int mroId, int mccId)
	{
		this.id = id;
		this.idTaskGeneric = idTaskGeneric;
		this.startTime = startTime;
		this.endTime = endTime;
		this.planeId = planeId;
		this.taskStatus = taskStatus;
		this.mroId = mroId;
		this.mccId = mccId;
	}

	public CustomHashMap<String, String> toMap()
	{
		CustomHashMap<String, String> chm = new CustomHashMap<String, String>();
		chm.put("_id", String.valueOf(id));
		chm.put("idTaskGeneric", String.valueOf(idTaskGeneric));
		chm.put("startTime", startTime);
		chm.put("endTime", endTime);
		chm.put("planeId", String.valueOf(planeId));
		chm.put("taskStatus", String.valueOf(taskStatus));
		chm.put("mroId", String.valueOf(mroId));
		chm.put("mccId", String.valueOf(mroId));
		return chm;
	}

	public int getId()
	{
		return id;
	}

	public int getIdTaskGeneric()
	{
		return idTaskGeneric;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public Date getStartTimeAsDate()
	{
		return Utility.convertDateString(startTime);
	}

	public Date getEndTimeAsDate()
	{
		return Utility.convertDateString(endTime);
	}

	/*
	 * public void setEndTime(Date endTime)
	 * {
	 * this.endTime = Utility.convertDateToString(endTime);
	 * }
	 */

	public int getPlaneId()
	{
		return planeId;
	}

	public void setPlaneId(int planeId)
	{
		this.planeId = planeId;
	}

	public int getTaskStatus()
	{
		return taskStatus;
	}

	/**
	 * @param integer
	 *            taskStatus
	 *            1 = Not dispatch to MRO
	 *            0 = Dispatched
	 *            2 = On going
	 *            3 = Done
	 */
	public void setTaskStatus(int taskStatus)
	{
		this.taskStatus = taskStatus;
	}

	public int getMroId()
	{
		return mroId;
	}

	public void setMroId(int mroId)
	{
		this.mroId = mroId;
	}

	public int getMccId()
	{
		return mccId;
	}

	public void setMccId(int mccId)
	{
		this.mccId = mccId;
	}

	@Override
	public String toString()
	{
		return "id=" + id + ";idTaskGeneric=" + idTaskGeneric + ";start=" + startTime + ";end=" + endTime + ";planeId=" + planeId + ";taskStatus=" + taskStatus + ";mroId=" + mroId + ";mccId=" + mccId;
	}

}
