package unsw.blackout;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.time.LocalTime;

public class BlueOriginSatellite extends Satellite{
   private List<String> deviceHandle = Arrays.asList("HandheldDevice", "LaptopDevice", "DesktopDevice");
   private int maxConnection = 10;
   private int maxLaptops = 5;
   private int maxDesktops  = 2;

   

   /** 
    * @param id
    * @param type
    * @param height
    * @param position
    */
   public BlueOriginSatellite(String id, String type, double height, double position) {
      super(id, type, height, position);
      this.setVelocity(8500.0 / 60);
      this.setAngularVelocity(this.getVelocity()/this.getHeight());
   }


   /** 
    * @return Double
    */

   
   /** 
    * @param i
    * @param now
    * @param currentDay
    */
   @Override
   public void addConnections(Device i, LocalTime now,int currentDay) {
      if (i.getActivationPeriods() ==null){
         return;
      }
      if (i.getIsConnected()){
         return;
      }
      for (Hashtable<String, LocalTime> curTable: i.getActivationPeriods()){
         if (isBetween(now, i.getActivationPeriodsStart(curTable), i.getActivationPeriodsEnd(curTable))){
            if (deviceHandle.contains(i.getType()) && MathsHelper.satelliteIsVisibleFromDevice(this.getPosition(), this.getHeight(), i.getPosition())){
               String deviceType = i.getType();
               if (this.getNumberOfConnections() < maxConnection) {
                  if (deviceType.equals("HandheldDevice")){
                     ConnectInfo newConnection = new ConnectInfo(i.getId(), this.getId(),now, true, i.getTimeToConnect());
                     this.addConnectInfos(newConnection);
                     this.addConnectionsDevice(i);
                     i.setIsConnected(true);
                     i.setStartTime(now);
                  }
                  if (deviceType.equals("LaptopDevice") && this.getNumberOfLaptopDeviceConnections() < maxLaptops){
                     ConnectInfo newConnection = new ConnectInfo(i.getId(), this.getId(),now, true, i.getTimeToConnect());
                     this.addConnectInfos(newConnection);
                     this.addConnectionsDevice(i);
                     i.setIsConnected(true);
                     i.setStartTime(now);
                  }
                  if (deviceType.equals("LaptopDevice") && this.getNumberOfDesktopDeviceConnections() < maxDesktops){
                     ConnectInfo newConnection = new ConnectInfo(i.getId(), this.getId(),now, true, i.getTimeToConnect());
                     this.addConnectInfos(newConnection);
                     this.addConnectionsDevice(i);
                     i.setIsConnected(true);
                     i.setStartTime(now);
                  }
               }
            }
         }
      }
   }
   
   /** 
    * @param i
    * @param now
    * @param currentDay
    */
   @Override
   public void removeConnections(Device i, LocalTime now,int currentDay) {
      
      if (this.getConnections().contains(i) && i.getIsConnected()) {
         boolean timeToDrop = true;
         for (Hashtable<String, LocalTime> curTable: i.getActivationPeriods()){
            if (isBetween(now, i.getActivationPeriodsStart(curTable), i.getActivationPeriodsEnd(curTable))){
               timeToDrop = false;
            }
         }
         if (MathsHelper.satelliteIsVisibleFromDevice(this.getPosition(), this.getHeight(), i.getPosition()) == false || timeToDrop){
            i.setEndTime(now);
            for (ConnectInfo newConnection: this.getConnectInfos()) {
               if (i.getId().equals(newConnection.getDeviceId()) && newConnection.getIsConnected()) {
                  newConnection.setEndTime(now);
                  newConnection.setCurrentDay(currentDay);
                  i.setEndTime(now);
                  Duration duration = Duration.between(i.getStartTime(), i.getEndTime());
                  long active = duration.toMinutes();
                  int c = Math.toIntExact(active) - i.getTimeToConnect()-1;
                  newConnection.setMinutesActive(c);
                  newConnection.setIsConnected(false);
                  i.setIsConnected(false);
                  break;
               }
            }
         }
      }
   }
   
   /** 
    * @param i
    * @param now
    */
   @Override
   public void stopNow(ConnectInfo i, LocalTime now) {
      // i.setEndTime(now);
      Duration duration = Duration.between(i.getStartTime(), now);
      long active = duration.toMinutes();
      int c = Math.toIntExact(active) - i.getTimeToConnect();
      i.setMinutesActive(c);
   }

   
   /** 
    * @return List<String>
    */
   public List<String> getDeviceHandle() {
      return this.deviceHandle;
   }
   
}
