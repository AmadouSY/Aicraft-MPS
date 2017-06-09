package com.example.jetty_jersey.ws;

import java.util.ArrayList;
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
import com.example.jetty_jersey.dao.MRO;

@Path("/mro")
public class MroStub
{
	private static Logger log = LogManager.getLogger(MroStub.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public List<MRO> allMro()
	{
		List<MRO> l = new ArrayList<MRO>();
		l = DAO.getMroDao().getAllMros();
		return l;

	}

	

}
