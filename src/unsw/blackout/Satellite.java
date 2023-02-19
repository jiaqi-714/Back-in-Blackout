package unsw.blackout;
import java.util.*;

import unsw.blackout.Device;

import java.time.LocalTime;

public class Satellite {
   private List<Device> connections = new ArrayList<Device>();
   private List<Device> possibleConnections = new ArrayList<Device>();
   private ArrayList<ConnectInfo> connectInfos= new ArrayList<ConnectInfo>();
   private List<String> deviceHandle;
   private String id;
   private String type;
   private Double height;
   private Double position;
   private Double velocity;
   private Double angularVelocity;
   
   
   /** 
    * @param id
    * @param type
    * @param height
    * @param position
    */
   public Satellite(String id, String type, double height, double position){
       this.id = id;
       this.type = type;
       this.height = height;
       this.position = position;
   }
      
   public void setAngularVelocity(Double angularVelocity) {
      this.angularVelocity = angularVelocity;
   }
      /** 
    * @param i
    */
   public void addConnectInfos(ConnectInfo i) {
      connectInfos.add(i);
   }

   
   /** 
    * @return ArrayList<ConnectInfo>
    */
   public ArrayList<ConnectInfo> getConnectInfos(){
      return connectInfos;
   }

   
   /** 
    * @return Double
    */
   public Double getAngularVelocity() {
      return this.angularVelocity;
   }
   
   /** 
    * @return List<Device>
    */
   public List<Device> getConnections() {
      return this.connections;
   }
   // public void addConnections("DeviceC", LocalTime.of(1, 0)) {

   // }
   public void addConnectionsDevice(Device i) {
      this.connections.add(i);
   }

   
   /** 
    * @param i
    */
   public void removeConnectionsDevice(Device i) {
      this.connections.remove(i);
   }

   
   /** 
    * @return List<String>
    */
   public List<String> getDeviceHandle() {
      return this.deviceHandle;
   }
   
   /** 
    * @return List<Device>
    */
   public List<Device> getPossibleConnections() {
      return this.possibleConnections;
   }
   
   /** 
    * @param i
    */
   public void addPossibleConnections(Device i) {
      possibleConnections.add(i);
   }
   public void cleanPossibleConnections() {
      this.possibleConnections.clear();
   }

   
   
   /** 
    * @param candidate
    * @param start
    * @param end
    * @return boolean
    */
   public boolean isBetween(LocalTime candidate, LocalTime start, LocalTime end) {
      if (candidate.isAfter(start) && candidate.isBefore(end)) {
         return true;
      }
      else if (candidate.equals(start) || candidate.equals(end)){
         return true;
      }
      else{
         return false;
      }
   }

   
   /** 
    * @param i
    * @param now
    * @param currentDay
    */
   public void addConnections(Device i, LocalTime now,int currentDay) {
   }
   
   /** 
    * @param i
    * @param now
    * @param currentDay
    */
   public void removeConnections(Device i, LocalTime now, int currentDay) {
   }
   
   /** 
    * @param i
    * @param now
    */
   public void stopNow(ConnectInfo i, LocalTime now) {
   }

   
   /** 
    * @return int
    */
   public int getNumberOfConnections() {
      return connections.size();
   }
   
   /** 
    * @return int
    */
   public int getNumberOfHandheldDeviceConnections() {
      int total = 0;
      for (Device i :connections){
         String type = i.getType();
         if (type.equals("HandheldDevice")) {
            total = total + 1;
         }
      }
      return total;
   }

   
   /** 
    * @return int
    */
   public int getNumberOfLaptopDeviceConnections() {
      int total = 0;
      for (Device i :connections){
         String type = i.getType();
         if (type.equals("LaptopDevice")) {
            total = total + 1;
         }
      }
      return total;
   }
   
   /** 
    * @return int
    */
   public int getNumberOfDesktopDeviceConnections() {
      int total = 0;
      for (Device i :connections){
         String type = i.getType();
         if (type.equals("DesktopDevice")) {
            total = total + 1;
         }
      }
      return total;
   }



   
   /** 
    * @param velocity
    */
   public void setVelocity(Double velocity) {
      this.velocity = velocity;
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
    * @return Double
    */
   public Double getHeight() {
      return this.height;
   }

   
   /** 
    * @param height
    */
   public void setHeight(Double height) {
      this.height = height;
   }

   
   /** 
    * @return Double
    */
   public Double getPosition() {
      return this.position;
   }

   
   /** 
    * @param position
    * @param minutePass
    */
   public void setPosition(Double position, int minutePass) {
      this.position = position + minutePass*getAngularVelocity();
      if (position >= 360){
         this.position = this.position - 360;
      }
   }

   
   /** 
    * @param position
    */
   public void setPositionForSOVIET(Double position) {
      this.position = position;
   }
   
   /** 
    * @return Double
    */
   public Double getVelocity() {
      return this.velocity;
   }


}
