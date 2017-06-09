package com.example.jetty_jersey.db;

import java.util.Comparator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapValueDescendingComparator implements Comparator<Map<String, String>>
{
	private final String fieldToSort;
	private static Logger log = LogManager.getLogger(MapValueDescendingComparator.class.getName());

	public MapValueDescendingComparator(String sFieldToSort)
	{
		super();
		fieldToSort = sFieldToSort;
	}

	public int compare(Map<String, String> m1, Map<String, String> m2)
	{
		String s1 = m1.get(fieldToSort);
		String s2 = m2.get(fieldToSort);
		if (s1 == null || s1.length() == 0 || s2 == null || s2.length() == 0)
		{
			log.error("Can not find " + fieldToSort + " in the map to compare for sorting");
			return 0;
		}

		if (fieldToSort.equals("_id"))
		{
			return Utility.convertIntString(s2) - Utility.convertIntString(s1);
		} else if (fieldToSort.toLowerCase().contains("time"))
		{
			return Utility.convertDateString(s2).compareTo(Utility.convertDateString(s1));
		} else
		{
			return s2.compareTo(s1);
		}
	}

}
