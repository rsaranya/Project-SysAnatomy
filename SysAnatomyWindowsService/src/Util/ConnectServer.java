package Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
/**
 * Connects to the server sends JSON objects
 * 
 * @author Saranya
 *
 */
public class ConnectServer implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	
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
	 * @param pobjJsonToSend
	 */
	public void sendJsonToServer(JSONObject pobjJsonToSend) {
		try {
			// Pass JSON Data to REST Service
			try {
				LOGGER.info("Inside sendJsonToServer");
				
				// URL of the Web Service
				URL lurlSysAnatomy = new URL(
				    "http://localhost:8080/SysAnatomyWebService/api/WebService");
				// Properties for the Json object
				URLConnection lurlconSysAnatomy = lurlSysAnatomy.openConnection();
				lurlconSysAnatomy.setDoOutput(true);
				lurlconSysAnatomy.setRequestProperty("Content-Type",
				    "application/json");
				lurlconSysAnatomy.setConnectTimeout(5000);
				lurlconSysAnatomy.setReadTimeout(5000);
				
				OutputStreamWriter loutStreamWriter = new OutputStreamWriter(
				    lurlconSysAnatomy.getOutputStream());
				loutStreamWriter.write(pobjJsonToSend.toString());
				loutStreamWriter.close();
				
				BufferedReader in = new BufferedReader(
				    new InputStreamReader(lurlconSysAnatomy.getInputStream()));
				
				while (in.readLine() != null) {
				}
				System.out.println("SysAnatomy REST Service Invoked Successfully..");
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling SysAnatomy REST Service");
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
		try {
			synchronized (GlobalObjects.larrlstJson) {
				while (true) {
					if (!GlobalObjects.larrlstJson.isEmpty()) {
						sendJsonToServer(GlobalObjects.larrlstJson.remove(0));
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in run : " + ex.getMessage());
		}
	}
}