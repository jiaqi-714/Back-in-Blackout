package unsw.blackout;
import java.util.*;
import java.time.LocalTime;

public class Device {
   private ArrayList<Hashtable<String, LocalTime>> activationPeriods = new ArrayList<Hashtable<String, LocalTime>>();
   private String id;
   private String type;
   private boolean isConnected;
   private Double position;
   private int timeToConnect;
   private LocalTime startTime;
   private LocalTime endTime;

   /** 
    * @param id
    * @param type
    * @param position
    * @param timeToConnect
    */
   public Device(String id, String type, double position, int timeToConnect){
      this.id = id;
      this.type = type;
      this.position = position;
      this.timeToConnect = timeToConnect;
   }
   
   /** 
    * @param start
    * @param durationInMinutes
    */
   public void scheduleDeviceActivation(LocalTime start, int durationInMinutes){
      LocalTime end = start.plusMinutes(durationInMinutes);
      Hashtable<String, LocalTime> newTable = new Hashtable<String, LocalTime>();
      newTable.put("startTime", start);
      newTable.put("endTime", end);
      activationPeriods.add(newTable);
   }
   
   /** 
    * @return ArrayList<Hashtable<String, LocalTime>>
    */
   public ArrayList<Hashtable<String, LocalTime>> getActivationPeriods() {
      return this.activationPeriods;
   }
   
   /** 
    * @param newTable
    * @return LocalTime
    */
   public LocalTime getActivationPeriodsStart(Hashtable<String, LocalTime> newTable) {
      return newTable.get("startTime");
   }

   
   /** 
    * @param newTable
    * @return LocalTime
    */
   public LocalTime getActivationPeriodsEnd(Hashtable<String, LocalTime> newTable) {
      return newTable.get("endTime");
   }
   
   
   /** 
    * @return String
    */
   public String getId() {
      return this.id;
   }

   
   /** 
    * @param id
    */
   public void setId(String id) {
      this.id = id;
   }

   
   /** 
    * @return String
    */
   public String getType() {
      return this.type;
   }

   
   /** 
    * @param type
    */
   public void setType(String type) {
      this.type = type;
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
    * @return Double
    */
   public Double getPosition() {
      return this.position;
   }

   
   /** 
    * @param position
    */
   public void setPosition(Double position) {
      this.position = position;
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
}
