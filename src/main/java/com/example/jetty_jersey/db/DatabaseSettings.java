package com.example.jetty_jersey.db;

import org.elasticsearch.common.unit.TimeValue;

public class DatabaseSettings
{
	public static final String DB_NAME = "gla";
	public static final String DB_HOST = "localhost";
	public static final int DB_PORT_DEFAULT = 9200;
	public static final int DB_PORT_SECOND = 9201;
	public static final int DB_PORT_TRANSPORT = 9300;
	public static final int MAX_RESULTS_PER_QUERY = 50;
	public static final int MAX_BOLK_RESULTS = 100;
	public static final TimeValue MAX_DATA_KEEP_TIME = new TimeValue(60000);
}
