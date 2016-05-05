
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Saranya
 *
 */
@Path("/")
public class ReceiveRestService {
	private static final Logger LOGGER = LogManager.getLogger();

	@SuppressWarnings("unused")
	private static final ConnectServer connServer = new ConnectServer();

	@POST
	@Path("/WebService")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SysAnatomyREST(InputStream incomingData) {
		LOGGER.info("Inside SysAnatomyREST ");
		StringBuilder serviceBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				serviceBuilder.append(line);
			}
		} catch (Exception e) {
			LOGGER.error("Error Parsing: - ");
		}
		System.out.println("Data Received: " + serviceBuilder.toString());

		synchronized (ConnectServer.larrlstJson) {
			if (ConnectServer.larrlstJson != null)
				ConnectServer.larrlstJson.add(serviceBuilder.toString());
		}
		// return HTTP response 200 in case of success
		return Response.status(200).entity(serviceBuilder.toString()).build();
	}

	/**
	 * 
	 * @param incomingData
	 * @return
	 */
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "SysAnatomyRESTService Successfully started..";

		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
}
