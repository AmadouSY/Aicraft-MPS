package com.example.jetty_jersey.ws;

import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/alert")
public class AlertResource
{
	private static Logger log = LogManager.getLogger(AlertResource.class.getName());

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/Flight")
	public void addAlert()
	{
		log.info("Add an alert for missing plane and crew");
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("Flight/{id}")
	public void deleteAlert(@PathParam("id") long id)
	{
		log.info("delete an alert for missing plane and crew");
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/aircraft")
	public void addAlertOFP()
	{
		log.info("Add an alert for missing aircraft");
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("aircraft/{id}")
	public void deleteAlertAircraft(@PathParam("id") long id)
	{
		log.info("delete an alert for missing aircraft=" + id);
	}

}
