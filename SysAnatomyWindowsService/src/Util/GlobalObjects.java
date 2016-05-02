package Util;

import java.util.ArrayList;

import org.json.simple.JSONObject;

/**
 * This class contains global objects which will be used by the classes in
 * WindowsService package to send it to the Web Service.
 * 
 * @author Saranya
 */
public class GlobalObjects {
	// Contains the functions to send data to the Web Server.
	public static final ConnectServer gobjConnectToServer = new ConnectServer();
	
	// Queue which is used by all the classes to push JSON objects with data.
	public static ArrayList<JSONObject> larrlstJson = new ArrayList<JSONObject>(
	    10);
	
	// Used as a constant to separate blocks of similar data.
	public static final String gstrSTAR = "**************************************";
}
