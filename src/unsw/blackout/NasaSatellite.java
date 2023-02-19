package unsw.blackout;
import java.util.*;
import java.time.Duration;
import java.time.LocalTime;

public class NasaSatellite extends Satellite{
   private List<String> deviceHandle = Arrays.asList("HandheldDevice", "LaptopDevice", "DesktopDevice");
   private int maxConnection = 6;

   /** 
    * @param id
    * @param type
    * @param height
    * @param position
    */
   public NasaSatellite(String id, String type, double height, double position) {
      super(id, type, height, position);
      this.setVelocity(5100.0 / 60);
      this.setAngularVelocity(this.getVelocity()/this.getHeight());
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
                  if (i.getPosition() >= 30 && i.getPosition() <= 40) {
                     List<Device> outDevice = new ArrayList<Device>();
                     for (Device c : this.getConnections()) {
                        if (MathsHelper.satelliteIsVisibleFromDevice(this.getPosition(), this.getHeight(), c.getPosition()) == false) {
                           outDevice.add(c);
                        }
                     } 
                     Comparator<Device> compareBytimeDevice = (Device o1, Device o2) -> o1.getStartTime().compareTo( o2.getStartTime() );
                     Collections.sort(outDevice, compareBytimeDevice);
                     if (outDevice.size()>=1) {
                        this.removeConnectionsDevice(outDevice.get(0));
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
                  int c = Math.toIntExact(active) - 11;
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
      int c = Math.toIntExact(active) - 10;
      i.setMinutesActive(c);
   }
   
   /** 
    * @return List<String>
    */
   public List<String> getDeviceHandle() {
      return this.deviceHandle;
   }
}
