package com.example.jetty_jersey.dao;

/**
 * This class is used to provide the status of backend execution.
 * In case there is an error, the front end should be able to display the error by retrieving from getMessage() & getException()
 */
public class Status
{
	public enum Execution
	{
		SUCCESSFUL, FAILED,
	}

	private int count = 0;
	private String msg = "";
	private Execution exe = Execution.FAILED;

	// For json serialization
	public Status()
	{
	}

	public Status(Execution e)
	{
		exe = e;
	}

	public String getMessage()
	{
		return msg;
	}

	public void setResultyCount(int c)
	{
		count = c;
	}

	public int getResultCount()
	{
		return count;
	}

	public void setMessage(String str)
	{
		msg = str;
	}

	public Execution getExecutionStatus()
	{
		return exe;
	}

	@Override
	public String toString()
	{
		return "Status:" + exe + " Count:" + count + " Msg:" + msg;
	}

}
