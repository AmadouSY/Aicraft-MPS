package com.example.jetty_jersey.dao_interface;

import java.util.List;

import com.example.jetty_jersey.dao.*;

public interface FlightDao
{
	/**
	 * @return the flight corresponding to the given Id
	 **/
	Flight getFlightbyId(String commercialId);

	/**
	 * @return the list of flights that will be to the given airport
	 **/
	List<Flight> getFlightsbyAirport(String airport);

	/**
	 * @return the list of flights departing from given airport
	 **/
	List<Flight> getFlightsbyDepartureAirport(String airport);

	/**
	 * @return the list of flights that will land to the given airport
	 **/
	List<Flight> getFlightsbyArrivalAirport(String airport);

	/**
	 * @return the flight by the planeid
	 **/
	List<Flight> getFlightsbyPlaneId(int planeId);

	Status addFlight(Flight f);

}