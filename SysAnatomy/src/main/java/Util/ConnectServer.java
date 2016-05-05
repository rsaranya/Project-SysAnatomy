package Util;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public class ConnectServer implements Runnable  {
	private static int gintCompletedCount = 0;

	// Queue which is used by all the classes to push JSON objects with data.
	public static ArrayList<String> larrlstJson = new ArrayList<String>(10);
		
	public ConnectServer()
	{
	/*	synchronized (this) {
			while (GlobalObjects.gobjConnectToServer.getCompletedCount() != 3) {
				GlobalObjects.gobjConnectToServer.sendJsonToServer(lobjJsonMemData);
			}
		}*/
	}
	
	public int getCompletedCount()
	{
		return gintCompletedCount;
	}
	
	

	public void sendJsonToServer(JSONObject lobjJsonMemData) {
		// TODO Auto-generated method stub
		
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

}
