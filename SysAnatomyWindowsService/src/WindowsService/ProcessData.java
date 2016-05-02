package WindowsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.sigar.ProcCredName;
import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.simple.JSONObject;

import Util.GlobalObjects;

public class ProcessData implements Runnable {
	private static Sigar sigar = new Sigar();
	private final Logger LOGGER = LogManager.getLogger();
	private JSONObject lobjJsonProcessData = new JSONObject();
	private boolean IsJsonObjectSent = false;
	
	/**
	 * Constructor of the class Spawns a thread which fetches data from the user
	 * system.
	 */
	public ProcessData() {
		try {
			new Thread(this).start();
		} catch (Exception ex) {
			LOGGER.error("Exception in ProcessData : " + ex.getMessage());
		}
	}
	
	/**
	 * Called when thread starts. Calls the function to fetch Process related data
	 * And adds the JSON object into a global array
	 */
	@Override
	public void run() {
		try {
			getProcessData();
			
			synchronized (GlobalObjects.larrlstJson) {
				while (!IsJsonObjectSent) {
					if (lobjJsonProcessData != null) {
						GlobalObjects.larrlstJson.add(lobjJsonProcessData);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in run : " + ex.getMessage());
		}
	}
	
	/**
	 * Calls functions to fetch process related data. Fetched the list of Process
	 * Id's from the Sigar class instance.
	 */
	private void getProcessData() {
		try {
			long[] llngProcList = sigar.getProcList();
			
			long pid = 0;
			for (int count = 0; count < llngProcList.length; count++) {
				pid = llngProcList[count];
				
				getDataFromProcMem(pid);
				getDataFromProcState(pid);
				getDataFromProcCred(pid);
				getDataFromProcExe(pid);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in getProcessData : " + ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getDataFromProcExe(long pid) {
		ProcExe exe = new ProcExe();
		String lstrRetrievedValues = "";
		try {
			exe = new ProcExe();
			exe.gather(sigar, pid);
			
			lstrRetrievedValues = "" + exe.getName();
			lobjJsonProcessData.put("ProcessData_ExeName", lstrRetrievedValues);
			LOGGER.info("Exe Name  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + exe.getCwd();
			lobjJsonProcessData.put("ProcessData_ExeCwd", lstrRetrievedValues);
			LOGGER.info("Exe Cwd  : " + lstrRetrievedValues);
		} catch (SigarException sigarEx) {
			LOGGER.error("Exception in getDataFromProcExe : " + sigarEx.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Fetches Process related data and inserts into a JSON object Uses SIGAR's
	 * ProcCredName class to fetch details.
	 * 
	 * @param pid
	 *          : Contains the Process Id for which details are to be fetched.
	 */
	@SuppressWarnings("unchecked")
	private void getDataFromProcCred(long pid) {
		ProcCredName cred = new ProcCredName();
		String lstrRetrievedValues = "";
		try {
			cred.gather(sigar, pid);
			
			lstrRetrievedValues = "" + cred.getGroup();
			lobjJsonProcessData.put("ProcessData_CredGroup", lstrRetrievedValues);
			LOGGER.info("Cred Group  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + cred.getUser();
			lobjJsonProcessData.put("ProcessData_CredUser", lstrRetrievedValues);
			LOGGER.info("Cred User  : " + lstrRetrievedValues);
		} catch (SigarException sigarEx) {
			LOGGER
			    .error("Exception in getDataFromProcCred : " + sigarEx.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Fetches Process related data and inserts into a JSON object Uses SIGAR's
	 * ProcMem class to fetch details.
	 * 
	 * @param pid
	 *          : Contains the Process Id for which details are to be fetched.
	 */
	@SuppressWarnings("unchecked")
	private void getDataFromProcMem(long pid) {
		ProcMem memory = null;
		String lstrRetrievedValues = "";
		try {
			memory = new ProcMem();
			memory.gather(sigar, pid);
			
			lstrRetrievedValues = "" + memory.getSize();
			lobjJsonProcessData.put("ProcessData_MemSize", lstrRetrievedValues);
			LOGGER.info("Memory Size  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + memory.getMajorFaults();
			lobjJsonProcessData.put("ProcessData_MajorFaults", lstrRetrievedValues);
			LOGGER.info("Major Faults  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + memory.getMinorFaults();
			lobjJsonProcessData.put("ProcessData_MinorFaults", lstrRetrievedValues);
			LOGGER.info("Minor Faults  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + memory.getPageFaults();
			lobjJsonProcessData.put("ProcessData_PageFaults", lstrRetrievedValues);
			LOGGER.info("Page Faults  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + memory.getResident();
			lobjJsonProcessData.put("ProcessData_Resident", lstrRetrievedValues);
			LOGGER.info("Resident  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + memory.getShare();
			lobjJsonProcessData.put("ProcessData_Share", lstrRetrievedValues);
			LOGGER.info("Share  : " + lstrRetrievedValues);
		} catch (SigarException sigarEx) {
			LOGGER.error("Exception in getDataFromProcMem : " + sigarEx.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Fetches Process related data and inserts into a JSON object Uses SIGAR's
	 * ProcState class to fetch details.
	 * 
	 * @param pid
	 *          : Contains the Process Id for which details are to be fetched.
	 */
	@SuppressWarnings("unchecked")
	private void getDataFromProcState(long pid) {
		ProcState state = null;
		String lstrRetrievedValues = "";
		try {
			state = new ProcState();
			state.gather(sigar, pid);
			
			lstrRetrievedValues = "" + state.getName();
			lobjJsonProcessData.put("ProcessData_StateName", lstrRetrievedValues);
			LOGGER.info("State Name  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + state.getNice();
			lobjJsonProcessData.put("ProcessData_Nice", lstrRetrievedValues);
			LOGGER.info("Process Nice  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + state.getPpid();
			lobjJsonProcessData.put("ProcessData_Ppid", lstrRetrievedValues);
			LOGGER.info("Process Parent Id  : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + state.getPriority();
			lobjJsonProcessData.put("ProcessData_Priority", lstrRetrievedValues);
			LOGGER.info("Process Priority : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + state.getProcessor();
			lobjJsonProcessData.put("ProcessData_Prcessor", lstrRetrievedValues);
			LOGGER.info("Processor : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + state.getState();
			lobjJsonProcessData.put("ProcessData_State", lstrRetrievedValues);
			LOGGER.info("Process State : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + state.getThreads();
			lobjJsonProcessData.put("ProcessData_Threads", lstrRetrievedValues);
			LOGGER.info("Process Threads : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + state.getTty();
			lobjJsonProcessData.put("ProcessData_Tty", lstrRetrievedValues);
			LOGGER.info("Process Tty : " + lstrRetrievedValues);
		} catch (SigarException sigarEx) {
			LOGGER
			    .error("Exception in getDataFromProcState : " + sigarEx.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
}
