package com.example.jetty_jersey.dao_interface;

import java.util.List;

import com.example.jetty_jersey.util.TaskInfo;
import com.example.jetty_jersey.dao.*;

public interface TaskDao
{
	/**
	 * @return the list of all
	 **/
	List<TaskInfo> getAllTasks();

	/**
	 * @return the list of all
	 **/
	List<TaskInfo> getTasksInRange(int nStart, int nEnd);

	/**
	 * @return the task with the given id
	 **/
	TaskInfo getTasksById(int id);

	/**
	 * @return the task with the given plane id
	 **/
	List<TaskInfo> getTasksByPlaneId(int id);

	/**
	 * @return status to check if execute successful or not
	 **/
	Status addTask(Task t);

	/**
	 * @return status to check if execute successful or not
	 **/
	Status modifyTask(Task t);

	/**
	 * @return status to check if execute successful or not
	 **/
	Status deleteTask(int taskId);

	/**
	 * Used to assign MRO to a task
	 * 
	 * @param mroId
	 * @param taskId
	 * @return
	 */
	Status addMroToTask(int mroId, int taskId);

	/**
	 * @return the task with the given mro id
	 **/
	List<TaskInfo> getTasksByMroId(int mroId);

	/**
	 * @return status to check if execute successful or not
	 **/
	Status notifyTaskDone(int taskId);

}