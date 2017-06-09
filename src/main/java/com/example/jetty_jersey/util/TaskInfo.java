package com.example.jetty_jersey.util;

import java.util.Date;

import com.example.jetty_jersey.dao.Flight;
import com.example.jetty_jersey.dao.MRO;
import com.example.jetty_jersey.dao.Plane;
import com.example.jetty_jersey.dao.Task;
import com.example.jetty_jersey.dao.TaskGeneric;

public class TaskInfo
{
	public TaskGeneric taskGeneric;
	public Task task;
	public Plane plane;
	public Flight flight;
	public MRO mro;

	public TaskInfo(Task task, TaskGeneric taskgeneric, Plane plane, Flight flight, MRO mro)
	{
		this.task = task;
		this.taskGeneric = taskgeneric;
		this.plane = plane;
		this.flight = flight;
		this.mro = mro;
	}

	@Override
	public String toString()
	{
		return "task=" + task + ";plane=" + plane + ";flight=" + flight + ";mro=" + mro + ";taskGeneric=" + taskGeneric;
	}

	// For unit test only
	public TaskInfo(Date dateInTheTask)
	{
		taskGeneric = new TaskGeneric();
		task = new Task(dateInTheTask);
		plane = new Plane(1, "Unit Test");
		flight = new Flight(1, 1);
		mro = new MRO();
	}
}
