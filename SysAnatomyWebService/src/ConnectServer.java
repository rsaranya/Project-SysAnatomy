
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Connects to the server sends JSON objects
 * 
 * @author Saranya
 *
 */
public class ConnectServer implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	
	// Queue which is used by all the classes to push JSON objects with data.
	public static ArrayList<String> larrlstJson = new ArrayList<String>(10);
	
	/**
	 * 
	 */
	public ConnectServer() {
		try {
		new Thread(this).start();
		} catch (Exception ex) {
			LOGGER.error("Exception in ConnectServer : " + ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param pstrJsonToSend
	 */
	public void sendJsonToServer(String pstrJsonToSend) {
		try {
			// Pass JSON Data to REST Service
			try {
				LOGGER.info("Inside sendJsonToServer");
				
				// URL of the Web Service
				URL lurlSysAnatomy = new URL(
				    "http://localhost:8080/SysAnatomyWebApplication/api/WebApp");
				
				// Properties for the Json object
				URLConnection lurlconSysAnatomy = lurlSysAnatomy.openConnection();
				lurlconSysAnatomy.setDoOutput(true);
				lurlconSysAnatomy.setRequestProperty("Content-Type",
				    "application/json");
				lurlconSysAnatomy.setConnectTimeout(5000);
				lurlconSysAnatomy.setReadTimeout(5000);
				
				OutputStreamWriter loutStreamWriter = new OutputStreamWriter(
				    lurlconSysAnatomy.getOutputStream());
				loutStreamWriter.write(pstrJsonToSend);
				loutStreamWriter.close();
				
				BufferedReader in = new BufferedReader(
				    new InputStreamReader(lurlconSysAnatomy.getInputStream()));
				
				while (in.readLine() != null) {
				}
				System.out
				    .println("SysAnatomy Send REST Service Invoked Successfully..");
				in.close();
			} catch (Exception e) {
				System.out
				    .println("\nError while calling SysAnatomy Send REST Service");
				System.out.println(e);
			}
			
		} catch (Exception ex) {
			LOGGER.error("Exception in sendJsonToServer : " + ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	public void run() {
		synchronized (larrlstJson) {
			while (true) {
				if (!larrlstJson.isEmpty()) {
					sendJsonToServer(larrlstJson.remove(0));
				}
			}
		}
	}
}