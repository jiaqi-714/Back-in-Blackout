package unsw.blackout;
import java.util.*;
import java.time.Duration;
import java.time.LocalTime;

public class SovietSatellite extends Satellite{
   private int maxConnection = 9;
   private List<String> deviceHandle = Arrays.asList("LaptopDevice","DesktopDevice");
   private boolean inRange;
   
   /** 
    * @param id
    * @param type
    * @param height
    * @param position
    */
   public SovietSatellite(String id, String type, double height, double position) {
      super(id, type, height, position);
      this.setVelocity(6000.0 / 60);
      this.setAngularVelocity(this.getVelocity()/this.getHeight());
      if (position >= 140 && position <= 190) {
         inRange = true;
      }
      else {
         inRange = false;
      }
      if (position < 345 && inRange == false && position > 190) {
         this.setVelocity(-this.getVelocity());
      }
   }
   
   
   /** 
    * @param position
    * @param minutePass
    */
   @Override
   public void setPosition(Double position, int minutePass) {
      Double result = this.getPosition() + minutePass*(this.getVelocity()/this.getHeight());
      if (result >= 140 && result <= 190) {
         inRange = true;
      }
      if (inRange) {
         if (Double.compare(result, 140) < 0) {
            this.setVelocity(-this.getVelocity());
         }
         else if (Double.compare(result, 190) > 0) {
            this.setVelocity(-this.getVelocity());
         }
         
      }
      this.setPositionForSOVIET(result);
   }

   
   /** 
    * @param i
    * @param now
    * @param currentDay
    */
   @Override
   public void addConnections(Device i, LocalTime now,int currentDay) {
      if (i.getActivationPeriods() == null){
         return;
      }
      if (i.getIsConnected() == true){
         return;
      }
      for (Hashtable<String, LocalTime> curTable: i.getActivationPeriods()){
         if (isBetween(now, i.getActivationPeriodsStart(curTable), i.getActivationPeriodsEnd(curTable))){
            if (deviceHandle.contains(i.getType()) && MathsHelper.satelliteIsVisibleFromDevice(this.getPosition(), this.getHeight(), i.getPosition())){
               if (this.getNumberOfConnections() < maxConnection) {
                  ConnectInfo newConnection = new ConnectInfo(i.getId(), this.getId(),now, true, i.getTimeToConnect());
                  this.addConnectInfos(newConnection);
                  this.addConnectionsDevice(i);
                  i.setIsConnected(true);
                  i.setStartTime(now);
               }
               if (this.getNumberOfConnections() >= maxConnection) {
                  List<Device> outDevice = this.getConnections();
                  Comparator<Device> compareBytimeDevice = (Device o1, Device o2) -> o1.getStartTime().compareTo( o2.getStartTime() );
                  Collections.sort(outDevice, compareBytimeDevice);
                  //remove oldest one
                  for (ConnectInfo newConnection: this.getConnectInfos()) {
                     if (outDevice.get(0).getId().equals(newConnection.getDeviceId()) && newConnection.getIsConnected()) {
                        newConnection.setEndTime(now);
                        newConnection.setCurrentDay(currentDay);
                        Duration duration = Duration.between(newConnection.getStartTime(), newConnection.getEndTime());
                        long active = duration.toMinutes();
                        int c = Math.toIntExact(active) - (i.getTimeToConnect()*2) - 1;
                        newConnection.setMinutesActive(c);
                        newConnection.setIsConnected(false);
                        break;
                     }
                  }
                  outDevice.get(0).setIsConnected(false);
                  this.removeConnectionsDevice(outDevice.get(0));
                  //add new one
                  this.addConnectionsDevice(i);
                  ConnectInfo newConnection = new ConnectInfo(i.getId(), this.getId(), now, true, i.getTimeToConnect());
                  this.addConnectInfos(newConnection);
                  i.setIsConnected(true);
                  i.setStartTime(now);
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
            for (ConnectInfo newConnection: this.getConnectInfos()) {
               if (i.getId().equals(newConnection.getDeviceId()) && newConnection.getIsConnected()) {
                  newConnection.setEndTime(now);
                  newConnection.setCurrentDay(currentDay);
                  i.setEndTime(now);
                  Duration duration = Duration.between(i.getStartTime(), i.getEndTime());
                  long active = duration.toMinutes();
                  int c = Math.toIntExact(active) - 1 - (i.getTimeToConnect())*2;
                  // System.out.println(i.getTimeToConnect());
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
    * @param currentConnectionInfo
    * @param now
    */
   @Override
   public void stopNow(ConnectInfo currentConnectionInfo, LocalTime now) {
      // i.setEndTime(now);
      Duration duration = Duration.between(currentConnectionInfo.getStartTime(), now);
      long active = duration.toMinutes();
      int c = Math.toIntExact(active) - (currentConnectionInfo.getTimeToConnect()*2);
      currentConnectionInfo.setMinutesActive(c);
   }

   

   
   /** 
    * @return List<String>
    */
   public List<String> getDeviceHandle() {
      return this.deviceHandle;
   }

}
