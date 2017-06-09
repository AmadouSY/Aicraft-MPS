package com.example.jetty_jersey.ws;

import java.util.Base64;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jetty_jersey.dao.*;
import com.example.jetty_jersey.dao_interface.FlightDao;
import com.example.jetty_jersey.dao_implementation.FlightImpl;
import com.example.jetty_jersey.dao_implementation.PlaneImpl;

@Path("/Flight")
public class FlightStub
{
	private static Logger log = LogManager.getLogger(FlightStub.class.getName());
	FlightDao flight = new FlightImpl();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/view/{id}")
	public Flight flightById(@PathParam("id") int id)
	{
		return flight.getFlightbyId(id + "");
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addFlights/{flights}")
	public void addFlights(@PathParam("flights") String flights)
	{
		String[] splitedFile;
		String[] splitedLine;
		byte[] decoded = Base64.getDecoder().decode(flights);
		String flightsDecoded = new String(decoded);
		splitedFile = flightsDecoded.split("\n");
		for (int i = 0; i < splitedFile.length; i++)
		{
			splitedLine = splitedFile[i].split(",");
			if (splitedLine.length != 6)
				log.error("Le fichier n'est pas dans le bon format!");

			String commercialId = splitedLine[0];
			String departureAirport = splitedLine[1];
			String arrivalAirport = splitedLine[2];
			String departureTime = splitedLine[3];
			String arrivalTime = splitedLine[4];
			int planeID = Integer.parseInt(splitedLine[5]);

			Flight f = new Flight(-1, commercialId, departureAirport, arrivalAirport, departureTime, arrivalTime, planeID);
			DAO.getFlightDao().addFlight(f);
		}

		FlightImpl f = new FlightImpl();
		List<Flight> l = f.getFlightsbyDepartureAirport("roissy");

		for (Flight fl : l)
		{
			log.info("Flight " + fl.getId() + " with plane " + fl.getPlaneId() + " added");
		}
	}

}
