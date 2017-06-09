package com.example.jetty_jersey.util;

import java.util.List;

public class Couple
{
	public int id = -1;
	public String user;
	public String pass;
	public String role;

	public Couple(String user, String pass, String role)
	{
		this.user = user;
		this.pass = pass;
		this.role = role;
	}

	public Couple(int id, String user, String pass, String role)
	{
		super();
		this.id = id;
		this.user = user;
		this.pass = pass;
		this.role = role;
	}

	public static String inTab(List<Couple> l, Couple c)
	{
		String res;
		for (Couple e : l)
		{
			res = (e.id != -1) ? e.id + "" : "";
			if (e.user.equals(c.user) && e.pass.equals(c.pass))
				return e.role + "-" + res;
		}
		return "incorrect";
	}
}
