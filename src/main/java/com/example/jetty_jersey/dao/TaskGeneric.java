package com.example.jetty_jersey.dao;

import com.example.jetty_jersey.db.CustomHashMap;

public class TaskGeneric
{
	private final int id;
	private final String description;
	private final String periodicity;
	private final String ataCategory;
	private final boolean hangarNeed;
	private final float duration; // in hour
	private final String planeType;

	// For json serialization
	public TaskGeneric()
	{
		int rand = (int) (Math.random() * 1000);
		this.id = rand;
		this.description = "Demain, dès l’aube, à l’heure où blanchit la campagne. Je partirai. Vois-tu, je sais que tu m’attends. J’irai par la forêt, j’irai par la montagne. Je ne puis demeurer loin de toi plus longtemps.";
		this.periodicity = "Daily";
		this.ataCategory = "Demain, dès l’aube (Victor Hugo)";
		this.hangarNeed = (Math.random() < 0.5) ? true : false;
		this.duration = (long) (Math.random() * 12);
		this.planeType = "Fake Data Type" + rand;
	}

	public TaskGeneric(int id, String description, String periodicity, String ataCategory, boolean hangarNeed, float duration, String planeType)
	{
		this.id = id;
		this.description = description;
		this.periodicity = periodicity;
		this.ataCategory = ataCategory;
		this.hangarNeed = hangarNeed;
		this.duration = duration;
		this.planeType = planeType;
	}

	public int getId()
	{
		return id;
	}

	public float getduration()
	{
		return duration;
	}

	public String getPlaneType()
	{
		return planeType;
	}

	public boolean isHangarNeed()
	{
		return hangarNeed;
	}

	public String getDescription()
	{
		return description;
	}

	public String getPeriodicity()
	{
		return periodicity;
	}

	public String getAtaCategory()
	{
		return ataCategory;
	}

	public CustomHashMap<String, String> toMap()
	{
		CustomHashMap<String, String> chm = new CustomHashMap<String, String>();
		chm.put("_id", String.valueOf(id));
		chm.put("description", description);
		chm.put("periodicity", periodicity);
		chm.put("ataCategory", ataCategory);
		chm.put("planeType", planeType);
		chm.put("duration", Float.toString(duration));
		chm.put("hangarNeed", Boolean.toString(hangarNeed));
		return chm;
	}

	@Override
	public String toString()
	{
		return "id=" + id + ";description=" + description + ";periodicity=" + periodicity + ";ataCategory=" + ataCategory + ";hangarNeed=" + hangarNeed + ";duration=" + duration + ";planeType="
				+ planeType;
	}

}
