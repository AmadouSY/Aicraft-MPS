package com.example.jetty_jersey.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class DatabaseSimulator
{
	public static void main(String[] args)
	{
		execute();
	}

	protected static void execute()
	{
		String queryPath = "src/main/resources/initDB.query";
		File fQuery = new File(queryPath);
		System.out.println("Read query file from : " + fQuery.getAbsolutePath());

		FileInputStream fstream = null;
		BufferedReader br = null;
		RestClient restClient = RestClient
				.builder(new HttpHost(DatabaseSettings.DB_HOST, DatabaseSettings.DB_PORT_DEFAULT, "http"), new HttpHost(DatabaseSettings.DB_HOST, DatabaseSettings.DB_PORT_SECOND, "http")).build();
		try
		{
			fstream = new FileInputStream(queryPath);
			br = new BufferedReader(new InputStreamReader(fstream));
			ArrayList<DatabaseQuery> queries = new ArrayList<DatabaseQuery>();
			String strLine;
			int syntaxStatus = 0;
			int lineCount = 0;
			DatabaseQuery currentQuery = null;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{
				lineCount++;
				strLine = strLine.trim();
				if (strLine.startsWith("##") || strLine.length() == 0)
					continue;
				if (strLine.startsWith("PUT") || strLine.startsWith("GET") || strLine.startsWith("POST") || strLine.startsWith("DELETE"))
				{
					if (syntaxStatus != 0)
						throw new Exception("Incorrect syntax : Query is not closed with {...} at line " + lineCount);

					int pos1StSpace = strLine.indexOf(" ");
					String strMethod = strLine.substring(0, pos1StSpace);
					String strURL = strLine.substring(pos1StSpace + 1, strLine.length());
					currentQuery = new DatabaseQuery(strMethod.trim(), strURL.trim());
					syntaxStatus++;
				} else
				{
					if (strLine.length() > 0)
					{
						if (currentQuery != null && syntaxStatus > 0)
						{
							if (syntaxStatus == 1)
							{
								if (strLine.startsWith("{") && strLine.length() == 1)
								{
									syntaxStatus++;
								}
							} else if (syntaxStatus == 2 && strLine.length() > 0)
							{
								if (strLine.startsWith("}") && strLine.length() == 1)
								{
									syntaxStatus = 0;
									queries.add(currentQuery);
								} else
								{
									currentQuery.putQueryContent(strLine);
									// System.out.println("Line(" + lineCount + "):" + strLine);
								}
							}
						} else
						{
							throw new Exception("Incorrect syntax : Query should start with \"WebServiceMethod PATH/TO/DATABASE...\"");
						}
					}
				}
			}

			for (DatabaseQuery q : queries)
			{
				// System.out.println("Execute query:" + q.toString());
				Response indexResponse = restClient.performRequest(q.getMethod(), q.getURL(), Collections.<String, String> emptyMap(), q.getEntity());
				System.out.println(EntityUtils.toString(indexResponse.getEntity()));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			// Close the input stream
			try
			{
				if (br != null)
					br.close();

				if (restClient != null)
					restClient.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
