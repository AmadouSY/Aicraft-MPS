package com.example.jetty_jersey.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jetty_jersey.util.TaskInfo;
import com.example.jetty_jersey.Email.EmailAlertService;
import com.example.jetty_jersey.dao.*;
import com.example.jetty_jersey.dao_implementation.TaskGenericImpl;
import com.example.jetty_jersey.dao_interface.TaskGenericDao;
import com.example.jetty_jersey.db.Utility;

@Path("/task")
public class TaskStub {
	private static Logger log = LogManager.getLogger(TaskStub.class.getName());
	private TaskGenericDao taskGeneric = new TaskGenericImpl();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public List<TaskInfo> allTasks() {
		if (LoginStub.connected) {
			// log.debug("Login connected : " + LoginStub.connected);
			List<TaskInfo> l = DAO.getTaskDao().getAllTasks();
			for (TaskInfo taskInfo : l) {
				l.toString();
			}
			return l;
		} else
			return new ArrayList<TaskInfo>();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/genericByPlane/{type}")
	public List<TaskGeneric> getGenericTasksByPlaneType(@PathParam("type") String type) {
		LoginStub.connected = true;
		if (LoginStub.connected) {
			log.debug("Login connected : " + LoginStub.connected);
			List<TaskGeneric> l = new ArrayList<TaskGeneric>();
			for (int i = 0; i < 10; i++) {
				l.add(new TaskGeneric(i, "description", "periodicity", "atacategory", true, 15, type));
			}
			//return l;
			return DAO.getTaskGenericDao().getGenericTasksByPlaneType(type);

		} else
			return new ArrayList<TaskGeneric>();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/plane/{id}")
	public List<TaskInfo> allTasksByPlaneId(@PathParam("id") int id) {
		if (LoginStub.connected) {
			return DAO.getTaskDao().getTasksByPlaneId(id);
		}
		return new ArrayList<TaskInfo>();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mro/{id}")
	public List<TaskInfo> allTasksByMroId(@PathParam("id") int mroId) {
		if (LoginStub.connected) {
			System.out.println("login");
			return DAO.getTaskDao().getTasksByMroId(mroId);// DAO.getTaskDao().getTasksByPlaneId(id);
		}
		return new ArrayList<TaskInfo>();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public TaskInfo taskById(@PathParam("id") int id) {
		if (LoginStub.connected) {
			return DAO.getTaskDao().getTasksById(id);
		}
		return null;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mro/{mroid}/{taskid}")
	public int attributeToMro(@PathParam("mroid") int mroId, @PathParam("taskid") int taskId) {
		if (LoginStub.connected) {
			System.out.println("MRO ID : " + mroId);
			System.out.println("TASK ID : " + taskId);
			DAO.getTaskDao().addMroToTask(mroId, taskId);
		}
		return 0;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create/{taskGenericId}/{planeId}/{date}")
	public int createTask(@PathParam("taskGenericId") int taskGeneric, @PathParam("planeId") int planeId,
			@PathParam("date") String date) {
		if (LoginStub.connected) {
			Task t = new Task(-1, taskGeneric, date, date, planeId, 0,-1,-1);
			System.out.println(t.toString());
			DAO.getTaskDao().addTask(t);
		}
		return 0;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/alert/{id}")

	public void sendAlert(@PathParam("id") int id)
	{
		log.info("SEND ALERT FOR TASK " + id);
		try
		{
			String s = Integer.toString(id);
			System.out.println("S IN ALERT "+ s);
			EmailAlertService.send_mail_to_MRO(s);
		} catch (Exception e)
		{
			log.error("Unable to send to address", e);
		} 
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/done/{id}")
	public void setTaskDone(@PathParam("id") int id){
		DAO.getTaskDao().notifyTaskDone(id);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/add")
	public void addTask(Task task) {
		System.out.println(task.toString());
		// DAO.getTaskDao().addTask(task);
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete/{id}")
	public void deleteTaskById(@PathParam("id") int id) {
		DAO.getTaskDao().deleteTask(id);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mpd/{mpd}")
	public void addMPD(@PathParam("mpd") String mpd)
	{
		
		String[] splitedFile;
		String[] splitedLine;
		try
		{	byte[] decoded = Base64.getDecoder().decode(mpd); 
			String tasks = new String(decoded);
			splitedFile = tasks.split("\n");
			for (int i = 0; i < splitedFile.length; i++)
			{
				splitedLine = splitedFile[i].split(",");
				if (splitedLine.length != 6)
				{
					log.error("Le fichier n'est pas dans le bon format!");
				} else
				{
					String description = splitedLine[0];
					String periodicity = splitedLine[1];
					String ataCategory = splitedLine[2];
					boolean needHangar = Utility.convertBoolString(splitedLine[3]);
					float duration = Integer.parseInt(splitedLine[4]);
					String typeAvion = splitedLine[5].replaceAll("\r", "");
					TaskGeneric tg = new TaskGeneric(-1,description,periodicity,ataCategory,needHangar,duration,typeAvion);
					System.out.println(i+" : "+tg.toString());
					System.out.println(DAO.getTaskGenericDao().addTaskGeneric(tg));
					
				}

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
