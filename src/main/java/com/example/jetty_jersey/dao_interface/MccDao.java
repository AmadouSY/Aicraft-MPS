package com.example.jetty_jersey.dao_interface;

import java.util.List;

import com.example.jetty_jersey.dao.MCC;
import com.example.jetty_jersey.dao.Status;

public interface MccDao
{

	/**
	 * @return the MCC with the given id
	 **/
	MCC getMccById(String id);

	String getEmailById(String id);
	List<MCC> getAllMccs();
	List<MCC> getMccsInRange(int nStart, int nEnd);

	/**
	 * @return add a MCC
	 **/
	Status addMcc(MCC t);

	/**
	 * @return modify a MCC
	 **/
	Status modifyMcc(MCC t);

	/**
	 * @return delete a MCC
	 **/
	Status deleteMcc(int MccId);
}
