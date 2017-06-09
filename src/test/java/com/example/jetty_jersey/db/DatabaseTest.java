package com.example.jetty_jersey.db;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.example.jetty_jersey.dao.Status;
import com.example.jetty_jersey.dao.Task;
import com.example.jetty_jersey.dao_implementation.TaskImpl;

public class DatabaseTest
{

	@Before
	public void initialize()
	{
		DatabaseSimulator.execute();
	}

	@Test
	public void testDatabaseFunction()
	{
		DatabaseConnecter dbc = new DatabaseConnecter();
		String id = dbc.generateNewId("mro");
		assertEquals(id, "10");

		String emailTxt = "test@gmail.com";
		Map<String, String> data = new CustomHashMap<String, String>();
		data.put("qualification", "JUnit test qualification");
		data.put("name", "JUnit");
		data.put("surname", "Test");
		data.put("email", emailTxt);
		data.put("_id", "-1");
		Status s = dbc.insertToTableName("mro", data);
		assertTrue(s.getExecutionStatus() == Status.Execution.SUCCESSFUL);
		List<Map<String, String>> res = dbc.selectAllFromTableWhereFieldEqValue("mro", "email", emailTxt);
		assertTrue(res.size() > 0);

		data.put("qualification", "Modified junit qualification");
		data.put("_id", dbc.getMaxIdFromCache("mro").toString());

		s = dbc.updateDataInTableNameWhereFieldEqValue("mro", "_id", dbc.getMaxIdFromCache("mro").toString(), data);
		assertTrue(s.getExecutionStatus() == Status.Execution.SUCCESSFUL);

		s = dbc.deleteAllFromTableNameWhereFieldEqValue("mro", "_id", dbc.getMaxIdFromCache("mro").toString());
		assertTrue(s.getExecutionStatus() == Status.Execution.SUCCESSFUL);

		dbc.close();
	}

	@Test
	public void testTaskImpl()
	{
		TaskImpl test = new TaskImpl();
		Task t = new Task(-1, 1, "2017/05/02 19:15", "2017/05/02 23:15", 99, 3, 1, 1);

		Status s = test.addTask(t);
		assertTrue(s.getExecutionStatus() == Status.Execution.SUCCESSFUL);

		// List<TaskInfo> tInfoList = test.getTasksByPlaneId(99);
		// if (tInfoList.size() == 0)
		// fail("Unable to find newly added task into database.");
		//
		// Task newTask = tInfoList.get(0).task;
		// s = test.modifyTask(new Task(newTask.getId(), 3, newTask.getStartTime(), newTask.getEndTime(), newTask.getPlaneId(), newTask.getTaskStatus(), newTask.getMccId(), newTask.getMroId()));
		// assertTrue(s.getExecutionStatus() == Status.Execution.SUCCESSFUL);
		//
		// s = test.deleteTask(newTask.getId());
		// assertTrue(s.getExecutionStatus() == Status.Execution.SUCCESSFUL);

	}

}
