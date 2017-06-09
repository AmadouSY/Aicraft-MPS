package com.example.jetty_jersey.dao_interface;

import java.util.List;

import com.example.jetty_jersey.dao.*;
import com.example.jetty_jersey.util.PlaneInfo;

public interface PlaneDao
{
	/**
	 * @return the list of planes the given type
	 **/
	List<Plane> getPlanesbyType(String type);

	/**
	 * @return the list of all planes
	 **/
	List<Plane> getAllPlanes();

	/**
	 * @return the list of all planes
	 **/
	List<PlaneInfo> getAllPlaneInfos();

	/**
	 * @return the plane corresponding to the given id
	 **/
	Plane getPlanebyId(int id);

	/**
	 * @return Status after added a Plane
	 **/
	Status addPlane(Plane p);
}