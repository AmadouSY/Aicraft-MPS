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

import com.example.jetty_jersey.dao.DAO;
import com.example.jetty_jersey.dao.Plane;
import com.example.jetty_jersey.dao_interface.PlaneDao;
import com.example.jetty_jersey.util.PlaneInfo;

@Path("/plane")
public class PlaneStub
{
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public List<Plane> allPlanes()
	{
		return DAO.getPlaneDao().getAllPlanes();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/allPlaneInfos")
	public List<PlaneInfo> allPlaneInfos()
	{
		//return PlaneInfo.alea(); 
		return DAO.getPlaneDao().getAllPlaneInfos();
	}

	

	

}
