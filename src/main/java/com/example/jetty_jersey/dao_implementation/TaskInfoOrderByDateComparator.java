package com.example.jetty_jersey.dao_implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.example.jetty_jersey.dao.Task;
import com.example.jetty_jersey.db.Utility;
import com.example.jetty_jersey.util.TaskInfo;

public class TaskInfoOrderByDateComparator implements Comparator<TaskInfo>
{
	private final Date now = new Date();

	public int compare(TaskInfo ti1, TaskInfo ti2)
	{
		Task t1 = ti1.task;
		Task t2 = ti2.task;
		if (t1 != null && t2 != null)
		{
			Date dTask1Start = t1.getStartTimeAsDate();
			Date dTask2Start = t2.getStartTimeAsDate();
			return (int) (dTask1Start.getTime() - dTask2Start.getTime());
		}
		return 0;
	}

	public static void main(String[] args)
	{
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		TaskInfo tinfo1 = new TaskInfo(Utility.convertDateString("2017/12/25 12:00"));
		list.add(tinfo1);
		TaskInfo tinfo2 = new TaskInfo(new Date());
		list.add(tinfo2);
		TaskInfo tinfo3 = new TaskInfo(Utility.convertDateString("2017/05/01 13:30"));
		list.add(tinfo3);
		TaskInfo tinfo4 = new TaskInfo(Utility.convertDateString("2017/06/01 06:50"));
		list.add(tinfo4);
		TaskInfo tinfo5 = new TaskInfo(Utility.convertDateString("2017/06/25 20:20"));
		list.add(tinfo5);
		Collections.sort(list, new TaskInfoOrderByDateComparator());
		for (TaskInfo t : list)
		{
			System.out.println(t.toString());
		}
	}

}
