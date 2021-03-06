package WindowsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.sigar.DirStat;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.simple.JSONObject;

import Util.GlobalObjects;

public class FileSystemData implements Runnable {
	private static Sigar sigar = new Sigar();
	private final Logger LOGGER = LogManager.getLogger();
	private JSONObject lobjJsonFileSystemData = new JSONObject();
	private boolean IsJsonObjectSent = false;
	
	/**
	 * Constructor of the class Spawns a thread which fetches data from the user
	 * system.
	 */
	public FileSystemData() {
		try {
			new Thread(this).start();
		} catch (Exception ex) {
			LOGGER.error("Exception in FileSystemData : " + ex.getMessage());
		}
	}
	
	/**
	 * Fetches FileSystem related data and inserts into a JSON object Uses SIGAR's
	 * FileSystem class to fetch details.
	 */
	private void getFileSystemStatistics() {
		FileSystem[] lobjlstFileSystem = null;
		try {
			lobjlstFileSystem = sigar.getFileSystemList();
			
			int count = 0;
			int lintarrLength = lobjlstFileSystem.length;
			if (lobjlstFileSystem != null && lintarrLength > 0) {
				while (count < lintarrLength) {
					String lstrDirectoryName = lobjlstFileSystem[count].getDirName();
					
					getFileSystemInfo(lobjlstFileSystem[count]);
					getFileSystemUsage(lstrDirectoryName);
					
					count++;
					LOGGER.info(GlobalObjects.gstrSTAR);
				}
			}
		} catch (SigarException sigarEx) {
			LOGGER
			    .error("Exception in getSystemStatistics : " + sigarEx.getMessage());
		}
	}
	
	/**
	 * Fetches FileSystem's Directory related data and inserts into a JSON object
	 * Uses SIGAR's FileSystem class to fetch details.
	 * 
	 * @param pobjFileSystem
	 *          : Contains the file for which details are to be fetched.
	 */
	@SuppressWarnings("unchecked")
	private void getFileSystemInfo(FileSystem pobjFileSystem) {
		try {
			String lstrRetrievedValues = "";
			String lstrDirectoryName = pobjFileSystem.getDirName();
			
			DirStat lobjDirStat = sigar.getDirStat(lstrDirectoryName);
			LOGGER.info("Disk usage : " + lobjDirStat.getDiskUsage());
			
			lstrRetrievedValues = pobjFileSystem.getDevName();
			lobjJsonFileSystemData.put(
			    "FileSystem_" + lstrDirectoryName + "_DeviceName",
			    lstrRetrievedValues);
			LOGGER.info("Device Name : " + lstrRetrievedValues);
			
			lobjJsonFileSystemData.put(
			    "FileSystem_" + lstrDirectoryName + "_DirectoryName",
			    lstrRetrievedValues);
			LOGGER.info("Directory Name : " + lstrDirectoryName);
			
			lstrRetrievedValues = "" + pobjFileSystem.getFlags();
			lobjJsonFileSystemData.put("FileSystem_" + lstrDirectoryName + "_Flags",
			    lstrRetrievedValues);
			LOGGER.info("Flags : " + lstrRetrievedValues);
			
			lstrRetrievedValues = pobjFileSystem.getOptions();
			lobjJsonFileSystemData.put("FileSystem_" + lstrDirectoryName + "_Options",
			    lstrRetrievedValues);
			LOGGER.info("Options : " + lstrRetrievedValues);
			
			lstrRetrievedValues = pobjFileSystem.getSysTypeName();
			lobjJsonFileSystemData.put(
			    "FileSystem_" + lstrDirectoryName + "_SystemTypeName",
			    lstrRetrievedValues);
			LOGGER.info("System Type name : " + lstrRetrievedValues);
			
			lstrRetrievedValues = "" + pobjFileSystem.getType();
			lobjJsonFileSystemData.put("FileSystem_" + lstrDirectoryName + "_Type",
			    lstrRetrievedValues);
			LOGGER.info("Type : " + lstrRetrievedValues);
			
			lstrRetrievedValues = pobjFileSystem.getTypeName();
			lobjJsonFileSystemData.put(
			    "FileSystem_" + lstrDirectoryName + "_TypeName", lstrRetrievedValues);
			LOGGER.info("Type Name : " + lstrRetrievedValues);
		} catch (Exception ex) {
			LOGGER.error("Exception in getFileSystemInfo : " + ex.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Fetches FileSystem Usage related data and inserts into a JSON object Uses
	 * SIGAR's FileSysUsage class to fetch details.
	 * 
	 * @param lstrDirectoryName
	 */
	@SuppressWarnings("unchecked")
	private void getFileSystemUsage(String lstrDirectoryName) {
		LOGGER.info("Inside getFileSystemUsage ");
		
		FileSystemUsage lobjFileSysUsage = new FileSystemUsage();
		try {
			lobjFileSysUsage.gather(sigar, lstrDirectoryName);
			
			if (lobjFileSysUsage != null) {
				String lstrRetrievedValues = "";
				
				lstrRetrievedValues = "" + lobjFileSysUsage.getAvail() / 2048.0;
				lobjJsonFileSystemData.put(
				    "FileSystem_" + lstrDirectoryName + "_AvailableSpace",
				    lstrRetrievedValues);
				LOGGER.info("Available : " + lstrRetrievedValues + " GB");
				
				lstrRetrievedValues = "" + lobjFileSysUsage.getDiskQueue();
				lobjJsonFileSystemData.put(
				    "FileSystem_" + lstrDirectoryName + "_DiskQueue",
				    lstrRetrievedValues);
				LOGGER.info("Disk Queue : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjFileSysUsage.getFiles();
				lobjJsonFileSystemData.put(
				    "FileSystem_" + lstrDirectoryName + "_NumberOfFiles",
				    lstrRetrievedValues);
				LOGGER.info("Files : " + lstrRetrievedValues);
				
				lstrRetrievedValues = "" + lobjFileSysUsage.getFreeFiles();
				lobjJsonFileSystemData.put(
				    "FileSystem_" + lstrDirectoryName + "_FreeFiles",
				    lstrRetrievedValues);
				LOGGER.info("Free Files : " + lobjFileSysUsage.getFreeFiles());
				
				lstrRetrievedValues = "" + lobjFileSysUsage.getFree() / 2048.0;
				lobjJsonFileSystemData.put(
				    "FileSystem_" + lstrDirectoryName + "_FreeSpace",
				    lstrRetrievedValues);
				LOGGER.info("Free : " + lstrRetrievedValues + " GB");
				
				lstrRetrievedValues = "" + lobjFileSysUsage.getTotal() / 2048.0;
				lobjJsonFileSystemData.put(
				    "FileSystem_" + lstrDirectoryName + "_TotalSpace",
				    lstrRetrievedValues);
				LOGGER.info("Total : " + lstrRetrievedValues + " GB");
				
				lstrRetrievedValues = "" + lobjFileSysUsage.getUsed() / 2048.0;
				lobjJsonFileSystemData.put(
				    "FileSystem_" + lstrDirectoryName + "_UsedSpace",
				    lstrRetrievedValues);
				LOGGER.info("Used : " + lstrRetrievedValues + " GB");
				
				lstrRetrievedValues = "" + lobjFileSysUsage.getUsePercent() * 100;
				lobjJsonFileSystemData.put(
				    "FileSystem_" + lstrDirectoryName + "_UsedSpacePercent",
				    lstrRetrievedValues);
				LOGGER.info("Use Percentage : " + lstrRetrievedValues + " %");
			}
		} catch (SigarException sigarEx) {
			LOGGER.error("Exception in getFileSystemUsage : " + sigarEx.getMessage());
		}
		LOGGER.info(GlobalObjects.gstrSTAR);
	}
	
	/**
	 * Called when thread starts Calls the functions to fetch FileSystem related
	 * data And adds the JSON object into a global array
	 */
	public void run() {
		try {
			getFileSystemStatistics();
			
			synchronized (GlobalObjects.larrlstJson) {
				while (!IsJsonObjectSent) {
					if (lobjJsonFileSystemData != null) {
						IsJsonObjectSent = true;
						GlobalObjects.larrlstJson.add(lobjJsonFileSystemData);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in run : " + ex.getMessage());
		}
	}
}
