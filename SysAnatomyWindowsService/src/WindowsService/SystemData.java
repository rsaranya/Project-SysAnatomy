
package WindowsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.sigar.OperatingSystem;
import org.json.simple.JSONObject;

import Util.GlobalObjects;

/**
 *
 * @author Saranya
 */
public class SystemData implements Runnable {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private JSONObject lobjJsonSystemData = new JSONObject();;
	private boolean IsJsonObjectSent = false;
	
	/**
	 * Constructor of the class Spawns a thread which fetches data from the user
	 * system.
	 */
	public SystemData() {
		try {
			new Thread(this).start();
		} catch (Exception ex) {
			LOGGER.error("Exception in SystemData : " + ex.getMessage());
		}
	}
	
	/**
	 * Fetches System related data and inserts into a JSON object Uses SIGAR's
	 * OperatingSystem class to fetch details.
	 */
	@SuppressWarnings("unchecked")
	public void getSystemDetails() {
		OperatingSystem lobjOperatingSystem = OperatingSystem.getInstance();
		String lstrRetrievedValues = "";
		
		try {
			if (lobjOperatingSystem != null) {
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getArch();
				lobjJsonSystemData.put("SystemData_Architecture", lstrRetrievedValues);
				LOGGER.info("System Architecture : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getCpuEndian();
				lobjJsonSystemData.put("SystemData_MachineEndian", lstrRetrievedValues);
				LOGGER.info("Machine Endian : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getDataModel();
				lobjJsonSystemData.put("SystemData_DataModel", lstrRetrievedValues);
				LOGGER.info("Data Model : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getDescription();
				lobjJsonSystemData.put("SystemData_Description", lstrRetrievedValues);
				LOGGER.info("Description : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getName();
				lobjJsonSystemData.put("SystemData_MachineName", lstrRetrievedValues);
				LOGGER.info("Machine Name : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getVendor();
				lobjJsonSystemData.put("SystemData_MachineVendor", lstrRetrievedValues);
				LOGGER.info("Machine Vendor : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getVendorCodeName();
				lobjJsonSystemData.put("SystemData_VendorCodeName",
				    lstrRetrievedValues);
				LOGGER.info("Machine Vendor Code Name : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getVendorName();
				lobjJsonSystemData.put("SystemData_VendorName", lstrRetrievedValues);
				LOGGER.info("Machine Vendor Name : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getVendorVersion();
				lobjJsonSystemData.put("SystemData_VendorVersion", lstrRetrievedValues);
				LOGGER.info("Machine Vendor Version : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjOperatingSystem.getVersion();
				lobjJsonSystemData.put("SystemData_MachineVersion",
				    lstrRetrievedValues);
				LOGGER.info("Machine Version : " + lstrRetrievedValues);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in getSystemDetails : " + ex.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Called when thread starts Calls the functions to fetch System related data
	 * And adds the JSON object into a global array
	 */
	public void run() {
		try {
			getSystemDetails();
			synchronized (GlobalObjects.larrlstJson) {
				while (!IsJsonObjectSent) {
					if (lobjJsonSystemData != null) {
						GlobalObjects.larrlstJson.add(lobjJsonSystemData);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in run : " + ex.getMessage());
		}
	}
}
