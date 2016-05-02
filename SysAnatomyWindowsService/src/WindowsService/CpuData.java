
package WindowsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.simple.JSONObject;

import Util.GlobalObjects;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;

/**
 * Captures CPU related data from the client system
 *
 * @author Saranya
 */
public class CpuData implements Runnable {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static Sigar sigar = new Sigar();
	private JSONObject lobjJsonCpuData = new JSONObject();
	private boolean IsJsonObjectSent = false;
	
	/**
	 * Constructor of the class Spawns a thread which fetches data from the user
	 * system.
	 */
	public CpuData() {
		try {
			new Thread(this).start();
		} catch (Exception ex) {
			LOGGER.error("Exception in CpuData : " + ex.getMessage());
		}
	}
	
	/**
	 * Fetches CPU related data and inserts into a JSON object Uses SIGAR's Cpu
	 * class to fetch details.
	 */
	@SuppressWarnings("unchecked")
	private void getDataFromCpu() {
		LOGGER.info("Inside getDataFromCpu");
		Cpu[] lobjCpuList = null;
		
		try {
			String lstrRetrievedValues = "";
			lobjCpuList = sigar.getCpuList();
			double ldblUptime = sigar.getUptime().getUptime();
			lobjJsonCpuData.put("CpuData_SystemUpTime", ldblUptime);
			LOGGER.info("System Up time : " + ldblUptime);
			
			int count = 0;
			int lintArrLength = lobjCpuList.length;
			if (lobjCpuList != null && lintArrLength > 0) {
				while (count < lintArrLength) {
					lstrRetrievedValues = "" + lobjCpuList[count].getIdle();
					lobjJsonCpuData.put("CpuData_IdleTime", lstrRetrievedValues);
					LOGGER.info("Total system cpu idle time : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lobjCpuList[count].getIrq();
					lobjJsonCpuData.put("CpuData_TimeServicingInterupts",
					    lstrRetrievedValues);
					LOGGER.info("Total system cpu time servicing interrupts : "
					    + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lobjCpuList[count].getNice();
					lobjJsonCpuData.put("CpuData_NiceTime", lstrRetrievedValues);
					LOGGER.info(
					    "Total system cpu nice time : " + lobjCpuList[count].getNice());
					
					lstrRetrievedValues = "" + lobjCpuList[count].getSoftIrq();
					lobjJsonCpuData.put("CpuData_TimeServicingSoftInterupts",
					    lstrRetrievedValues);
					LOGGER.info("Total system cpu time servicing softirqs : "
					    + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lobjCpuList[count].getStolen();
					lobjJsonCpuData.put("CpuData_InvoluntaryWaitTime",
					    lstrRetrievedValues);
					LOGGER.info("Total system cpu involuntary wait time : "
					    + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lobjCpuList[count].getSys();
					lobjJsonCpuData.put("CpuData_CpuKernelTime", lstrRetrievedValues);
					LOGGER.info("Total system cpu kernel time : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lobjCpuList[count].getTotal();
					lobjJsonCpuData.put("CpuData_SystemCpuTime", lstrRetrievedValues);
					LOGGER.info("Total system cpu time : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lobjCpuList[count].getUser();
					lobjJsonCpuData.put("CpuData_CpuUserTime", lstrRetrievedValues);
					LOGGER.info("Total system cpu user time : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lobjCpuList[count].getWait();
					lobjJsonCpuData.put("CpuData_CpuIOWaitTime", lstrRetrievedValues);
					LOGGER.info("Total system cpu io wait time : " + lstrRetrievedValues);
					count++;
				}
			}
		} catch (SigarException sigarEx) {
			LOGGER.error("Exception in getDataFromCpu : " + sigarEx.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Fetches CPU related data and inserts into a JSON object Uses SIGAR's
	 * CpuInfo class to fetch details.
	 */
	@SuppressWarnings("unchecked")
	private void getDataFromCpuInfo() {
		LOGGER.info("Inside getDataFromCpuInfo");
		
		try {
			CpuInfo[] lcpuInfoInstance = sigar.getCpuInfoList();
			int count = 0;
			String lstrRetrievedValues = "";
			int lintArrLength = lcpuInfoInstance.length;
			if (lcpuInfoInstance != null && lintArrLength > 0) {
				while (count < lintArrLength) {
					lstrRetrievedValues = "" + lcpuInfoInstance[count].getCacheSize();
					lobjJsonCpuData.put("CpuData_CacheSize", lstrRetrievedValues);
					LOGGER.info("CPU cache size : " + lstrRetrievedValues);
					
					lstrRetrievedValues = ""
					    + lcpuInfoInstance[count].getCoresPerSocket();
					lobjJsonCpuData.put("CpuData_CoresPerSocket", lstrRetrievedValues);
					LOGGER.info(
					    "Number of CPU cores per CPU socket : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lcpuInfoInstance[count].getMhz();
					lobjJsonCpuData.put("CpuData_CpuSpeed", lstrRetrievedValues);
					LOGGER.info("CPU speed : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lcpuInfoInstance[count].getModel();
					lobjJsonCpuData.put("CpuData_CpuModel", lstrRetrievedValues);
					LOGGER.info("CPU model : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lcpuInfoInstance[count].getTotalCores();
					lobjJsonCpuData.put("CpuData_LogicalCores", lstrRetrievedValues);
					LOGGER.info("Total CPU cores (logical) : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lcpuInfoInstance[count].getTotalSockets();
					lobjJsonCpuData.put("CpuData_PhysicalSockets", lstrRetrievedValues);
					LOGGER.info("Total CPU sockets (physical) : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lcpuInfoInstance[count].getVendor();
					lobjJsonCpuData.put("CpuData_Vendor", lstrRetrievedValues);
					LOGGER.info("CPU vendor id : " + lstrRetrievedValues);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in getDataFromCpuInfo : " + ex.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Fetches CPU related data and inserts into a JSON object Uses SIGAR's
	 * CpuPerc class to fetch details.
	 */
	@SuppressWarnings("unchecked")
	private void getDataFromCpuPerc() {
		LOGGER.info("Inside getDataFromCpuPerc (Output in Percentage)");
		
		CpuPerc[] lArrCpuPerc = null;
		try {
			lArrCpuPerc = sigar.getCpuPercList();
			int count = 0;
			String lstrRetrievedValues = "";
			int lintArrLength = lArrCpuPerc.length;
			if (lArrCpuPerc != null && lintArrLength > 0) {
				while (count < lintArrLength) {
					lstrRetrievedValues = "" + lArrCpuPerc[count].getCombined() * 100;
					lobjJsonCpuData.put("CpuData_USER|SYS|NICE|WAIT_Percent",
					    lstrRetrievedValues);
					LOGGER.info("Sum of User+Sys+Nice+Wait : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lArrCpuPerc[count].getIdle() * 100;
					lobjJsonCpuData.put("CpuData_IdleTimePercent", lstrRetrievedValues);
					LOGGER.info("Idle : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lArrCpuPerc[count].getIrq() * 100;
					lobjJsonCpuData.put("CpuData_InterruptTimePercent",
					    lstrRetrievedValues);
					LOGGER.info("cpu time servicing interrupts : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lArrCpuPerc[count].getNice() * 100;
					lobjJsonCpuData.put("CpuData_NiceTimePercent", lstrRetrievedValues);
					LOGGER.info("cpu nice time : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lArrCpuPerc[count].getSys() * 100;
					lobjJsonCpuData.put("CpuData_SystemTimePercent", lstrRetrievedValues);
					LOGGER.info("system : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lArrCpuPerc[count].getUser() * 100;
					lobjJsonCpuData.put("CpuData_UserTimePercent", lstrRetrievedValues);
					LOGGER.info("user : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lArrCpuPerc[count].getWait() * 100;
					lobjJsonCpuData.put("CpuData_WaitTimePercent", lstrRetrievedValues);
					LOGGER.info("wait : " + lstrRetrievedValues);
					
					lstrRetrievedValues = "" + lArrCpuPerc[count].getStolen() * 100;
					lobjJsonCpuData.put("CpuData_StolenTimePercent", lstrRetrievedValues);
					LOGGER.info("stolen : " + lstrRetrievedValues);
					count++;
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in getDataFromCpuPerc : " + ex.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Called when thread starts Calls the functions to fetch CPU related data And
	 * adds the JSON object into a global array
	 */
	public void run() {
		try {
			getDataFromCpu();
			getDataFromCpuInfo();
			getDataFromCpuPerc();
			
			synchronized (GlobalObjects.larrlstJson) {
				while (!IsJsonObjectSent) {
					if (lobjJsonCpuData != null) {
						GlobalObjects.larrlstJson.add(lobjJsonCpuData);
						IsJsonObjectSent = true;
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in run : " + ex.getMessage());
		}
	}
}
