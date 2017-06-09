package com.example.jetty_jersey.db;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;

public class DatabaseQuery
{
	private final String method;
	private final String url;
	private final StringBuffer sbBody = new StringBuffer();
	private DatabaseExecuteType executeType = DatabaseExecuteType.UPDATE;
	private HashMap<String, String> filterConditions = null;

	public DatabaseQuery(String strMethod, String strURL)
	{
		method = strMethod;
		url = strURL;
	}

	public DatabaseQuery(String tableName, DatabaseExecuteType t)
	{
		method = "POST";
		executeType = t;
		switch (executeType)
		{
		case SELECT:
			url = DatabaseSettings.DB_NAME + "/" + tableName + "/_search";
			break;
		default:
			url = DatabaseSettings.DB_NAME + "/" + tableName;
			break;
		}
	}

	public String getMethod()
	{
		return method;
	}

	public String getURL()
	{
		return url;
	}

	public HttpEntity getEntity() throws Exception
	{
		return new NStringEntity("{\n" + buildQueryBody() + "\n}", ContentType.APPLICATION_JSON);
	}

	private String buildQueryBody() throws Exception
	{
		switch (executeType)
		{
		case SELECT:
			return buildQuerySelect();
		default:
			return sbBody.toString();
		}
	}

	private String buildQuerySelect() throws Exception
	{
		StringBuffer sb = new StringBuffer();
		if (filterConditions == null || filterConditions.isEmpty())
			sb.append("\"query\": {\n \"match_all\": {} \n}\n");
		else
			sb.append("\"query\" : {\n \"term\" : { " + buildCondition() + " \n}");
		return sb.toString();
	}

	private String buildCondition() throws Exception
	{
		if (filterConditions == null || filterConditions.isEmpty())
			throw new Exception("Query condition should not be null or empty");

		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> e : filterConditions.entrySet())
		{
			sb.append("\"" + e.getKey() + "\":\"" + e.getValue() + "\"\n");
		}
		return sb.toString();
	}

	/**
	 * @param strBody
	 *            Body of Elasticsearch query string e.g:
	 * 
	 *            PUT /DB/TABLE/ID
	 *            {
	 *            strBody...
	 *            }
	 * 
	 *            This method is used to read the .query file
	 */
	public void putQueryContent(String strBody)
	{
		sbBody.append(strBody + "\n");
	}

	/**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 *            This method is used to construct a query as SQL form : ... WHERE fieldName = fieldValue
	 */
	public void addConditionEq(String fieldName, String fieldValue)
	{
		if (filterConditions == null)
			filterConditions = new HashMap<String, String>();

		filterConditions.put(fieldName, fieldValue);
	}

	public HashMap<String, String> getConditions()
	{
		return filterConditions;
	}

	@Override
	public String toString()
	{
		try
		{
			return method + " " + url + "\n" + buildQueryBody();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.toString();
	}

}
