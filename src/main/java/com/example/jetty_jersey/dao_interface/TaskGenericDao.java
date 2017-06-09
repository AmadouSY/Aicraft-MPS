package com.example.jetty_jersey.dao_interface;

import java.util.List;

import com.example.jetty_jersey.dao.Status;
import com.example.jetty_jersey.dao.TaskGeneric;

public interface TaskGenericDao
{

	/**
	 * @return Add a generic task with the given plane type
	 **/
	Status addTaskGeneric(TaskGeneric tg);

	/**
	 * @return Modify a generic task with the given plane type
	 **/
	Status modifyTaskGeneric(TaskGeneric tg);

	/**
	 * @return Delete a generic task with the given plane type
	 **/
	Status deleteTaskGeneric(int id);

	/**
	 * @return the generic task with the given plane type
	 **/
	List<TaskGeneric> getGenericTasksByPlaneType(String planeType);
}
