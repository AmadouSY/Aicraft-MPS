package com.example.jetty_jersey.ws;

import java.nio.charset.Charset;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.*;

import com.example.jetty_jersey.dao.DAO;
import com.example.jetty_jersey.dao.MRO;
import com.example.jetty_jersey.util.Couple;

import java.util.*;

@Path("/login")
public class LoginStub
{
	static boolean connected = true;
	private static Logger log = LogManager.getLogger(LoginStub.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUser")
	public List<String> getUser(@Context HttpServletRequest hsr)
	{
		List<String> l = new ArrayList<String>();
		final String authorization = hsr.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Basic"))
		{
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
			log.debug(credentials);
			String[] values = credentials.split(":");
			l.add(values[0]);
			String role = "mcc";// values[1].split(",")[1];
			l.add(role);
		}
		return l;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user}/{pass}")
	public String[] postMethod(@PathParam("user") String name, @PathParam("pass") String pass)
	{
		List<Couple> l = new ArrayList<Couple>();
		/*
		 * for (MCC mcc : DAO.getMccDao().getAllMccs()) {
		 * l.add(new Couple(mcc.getEmail(), mcc.getPass(), "mcc"));
		 * }
		 */for (MRO mro : DAO.getMroDao().getAllMros())
		{
			l.add(new Couple(mro.getId(), mro.getName(), "pass", "mro"));
		}

		l.add(new Couple("mcc", "pass", "mcc"));
		l.add(new Couple(1, "mro", "pass", "mro"));
		Couple c = new Couple(name, pass, "mcc");
		// Couple c = new Couple("mcc", "mro");
		log.debug("USER : " + c.user + "; PASS : " + c.pass);
		String role = Couple.inTab(l, c);
		if (!role.equals("incorrect"))
		{
			connected = true;
		} else
		{
			connected = false;
		}
		return new String[] { role };
	}

	@GET
	@Path("/logout")
	public void logout()
	{
		connected = false;
		log.info("User logout");
	}

	/*
	 * @GET
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("/connectMcc") public void connection(){ try { DefaultHttpClient
	 * Client = new DefaultHttpClient();
	 * Client.getCredentialsProvider().setCredentials( AuthScope.ANY, new
	 * UsernamePasswordCredentials("mcc", "mccpass") );
	 * 
	 * HttpGet httpGet = new
	 * HttpGet("https://httpbin.org/basic-auth/mcc/mccpass"); HttpResponse
	 * response = Client.execute(httpGet);
	 * 
	 * System.out.println("response = " + response);
	 * 
	 * BufferedReader breader = new BufferedReader(new
	 * InputStreamReader(response.getEntity().getContent())); StringBuilder
	 * responseString = new StringBuilder(); String line = ""; while ((line =
	 * breader.readLine()) != null) { responseString.append(line); }
	 * breader.close(); String responseStr = responseString.toString();
	 * System.out.println("responseStr = " + responseStr);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */

}
