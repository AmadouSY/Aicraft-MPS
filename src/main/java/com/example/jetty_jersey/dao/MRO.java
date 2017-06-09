package com.example.jetty_jersey.dao;

import com.example.jetty_jersey.db.CustomHashMap;

/**
 * 
 * PUT mro/id
 * {
 * 
 * "qualification" : "example of qualification"
 * "name" : "example of name"
 * }
 * 
 */
public class MRO
{
	private String qualification = "N/A";
	private String name = "Not assigned";
	private String email = "";
	private int id;

	// For json serialization
	public MRO()
	{
	}

	public MRO(int mroId, String name, String mail)
	{
		this.name = name;
		this.id = mroId;
		this.email = mail;
	}

	public MRO(String name)
	{
		this.name = name;
	}

	public String getQualification()
	{
		return qualification;
	}

	public void setQualification(String qualification)
	{
		this.qualification = qualification;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String e)
	{
		this.email = e;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public CustomHashMap<String, String> toMap()
	{
		CustomHashMap<String, String> chm = new CustomHashMap<String, String>();
		chm.put("_id", String.valueOf(id));
		chm.put("name", name);
		chm.put("qualification", qualification);
		chm.put("email", email);
		return chm;
	}

	@Override
	public String toString()
	{
		return "MRO(" + id + "," + name + "," + qualification + "," + email + ")";
	}

}
