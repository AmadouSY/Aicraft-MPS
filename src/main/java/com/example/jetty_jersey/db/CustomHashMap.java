package com.example.jetty_jersey.db;

import java.util.HashMap;

public class CustomHashMap<T1, T2> extends HashMap<String, String>
{
	private static final long serialVersionUID = 1L;

	@Override
	public String get(Object key)
	{
		String o = super.get(key);
		if (o == null)
			return "";

		return o;
	}

}
