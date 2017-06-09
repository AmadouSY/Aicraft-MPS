package com.example.jetty_jersey.dao_interface;

import java.util.List;

import com.example.jetty_jersey.dao.*;

public interface MroDao
{
	/**
	 * @return the MCC with the given id
	 **/
	MRO getMroById(int id);

	/**
	 * @return the list of mro members with the given qualification
	 **/
	public List<MRO> getMrobyQualification(String qualification);

	/**
	 * @return the list of all
	 **/
	List<MRO> getAllMros();

	/**
	 * @return add a MRO
	 **/
	Status addMro(MRO mro);

	/**
	 * @return modify a MRO
	 **/
	Status modifyMro(MRO mro);

	/**
	 * @return delete a MRO
	 **/
	Status deleteMro(int id);
}