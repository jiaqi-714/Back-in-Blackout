package unsw.blackout;
import java.time.LocalTime;


public class ConnectInfo{
   
   private LocalTime startTime;
   private LocalTime endTime;
   private int minutesActive;
   private String satelliteConnectedId;
   private String deviceId;
   private boolean isConnected;
	private int currentDay = 0;
	private int timeToConnect;


   /** Create a new ConnectInfo which use to store the connection information, store as a list in satellite
    * @param deviceId 
    * @param satelliteConnectedId
    * @param startTime
    * @param isConnected
	 * @param timeToConnect
    */
   public ConnectInfo(String deviceId, String satelliteConnectedId, LocalTime startTime, boolean isConnected, int timeToConnect) {
		this.deviceId = deviceId;
      this.satelliteConnectedId = satelliteConnectedId;
      this.startTime = startTime;
      this.isConnected = true;
		this.timeToConnect = timeToConnect;
   }
 	
	 /** 
	  * @return int
	  */
	 public int getTimeToConnect() {
		return this.timeToConnect;
	}

	
	/** 
	 * @param timeToConnect
	 */
	public void setTimeToConnect(int timeToConnect) {
		this.timeToConnect = timeToConnect;
	}

	  
	
	/** 
	 * @return int
	 */
	public int getCurrentDay() {
		return this.currentDay;
	}

	
	/** 
	 * @param currentDay
	 */
	public void setCurrentDay(int currentDay) {
		this.currentDay = currentDay;
	}
   
	/** 
	 * @return boolean
	 */
	public boolean getIsConnected() {
		return this.isConnected;
   }

   
	/** 
	 * @param isConnected
	 */
	public void setIsConnected(boolean isConnected) {
      this.isConnected = isConnected;
   }
	
	/** 
	 * @return LocalTime
	 */
	public LocalTime getStartTime() {
		return this.startTime;
	}

	
	/** 
	 * @param startTime
	 */
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	
	/** 
	 * @return LocalTime
	 */
	public LocalTime getEndTime() {
		return this.endTime;
	}

	
	/** 
	 * @param endTime
	 */
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	
	/** 
	 * @return int
	 */
	public int getMinutesActive() {
		return this.minutesActive;
	}

	
	/** 
	 * @param minutesActive
	 */
	public void setMinutesActive(int minutesActive) {
		this.minutesActive = minutesActive;
	}

	
	/** 
	 * @return String
	 */
	public String getSatelliteConnectedId() {
		return this.satelliteConnectedId;
	}

	
	/** 
	 * @param satelliteConnectedId
	 */
	public void setSatelliteConnectedId(String satelliteConnectedId) {
		this.satelliteConnectedId = satelliteConnectedId;
	}

	
	/** 
	 * @return String
	 */
	public String getDeviceId() {
		return this.deviceId;
	}

	
	/** 
	 * @param id
	 */
	public void setDeviceId(String id) {
		this.deviceId = id;
	}


   
}
