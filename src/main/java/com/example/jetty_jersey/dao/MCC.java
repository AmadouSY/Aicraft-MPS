package com.example.jetty_jersey.dao;

import com.example.jetty_jersey.db.CustomHashMap;

/**
 * 
 * PUT mcc/id
 * {
 * "email" : "email@exmaple"
 * }
 */
public class MCC
{
	private final String mccId;
	private String email;
	private String pass;

	// For json serialization
	public MCC()
	{
		mccId = "-1";
	}

	public MCC(String mccId, String email,String pass)
	{
		this.mccId = mccId;
		this.email = email;
		this.pass=pass;
	}

	public CustomHashMap<String, String> toMap()
	{
		CustomHashMap<String, String> chm = new CustomHashMap<String, String>();
		chm.put("_id", mccId);
		chm.put("email", email);
		chm.put("pass", pass);
		return chm;
	}

	public String getMccId()
	{
		return mccId;
	}

	public String getEmail()
	{
		return email;
	}
	
	public String getPass()
	{
		return pass;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Override
	public String toString()
	{
		return "id=" + mccId + ";email=" + email;
	}

}
