package unsw.blackout;
import java.util.*;
import java.time.LocalTime;
import java.time.Duration;

import org.json.JSONArray;
import org.json.JSONObject;



public class Blackout {
    private String HandheldDevice = "HandheldDevice";
    private String LaptopDevice  = "LaptopDevice";
    private String DesktopDevice  = "DesktopDevice";
    private List<Device> deviceList= new ArrayList<Device>();
    private List<Satellite> satelliteList= new ArrayList<Satellite>();
    private LocalTime now = LocalTime.of(0, 0);
    private int currentDay = 0;
    private int minute = 0;
    
    /** 
     * @param id
     * @param type
     * @param position
     */
    public void createDevice(String id, String type, double position) {
        int timeToConnect = 0;

        if (HandheldDevice.equals(type)){
            timeToConnect = 1;
        }
        else if (LaptopDevice.equals(type)){
            timeToConnect = 2;
        }        
        else if (DesktopDevice.equals(type)){
            timeToConnect = 5;
        }
        Device newDevice = new Device(id, type, position, timeToConnect);
        // System.out.println(timeToConnect);
        deviceList.add(newDevice);
    }
  
    
    /** 
     * @param id
     * @param type
     * @param height
     * @param position
     */
    public void createSatellite(String id, String type, double height, double position) {
        // double position1 = double(position);
        if (type.equals("SpaceXSatellite")) {
            SpaceXSatellite newSatellite = new SpaceXSatellite(id,type,height,position);
            satelliteList.add(newSatellite);
        }
        else if (type.equals("NasaSatellite")){
            NasaSatellite newSatellite = new NasaSatellite(id,type,height,position);
            satelliteList.add(newSatellite);
        }
        else if (type.equals("BlueOriginSatellite")){
            BlueOriginSatellite newSatellite = new BlueOriginSatellite(id,type,height,position);
            satelliteList.add(newSatellite);
        }
        else if (type.equals("SovietSatellite")){
            SovietSatellite newSatellite = new SovietSatellite(id,type,height,position);
            satelliteList.add(newSatellite);
        }
    }
  

    /** 
     * @param id
     */
    public void removeSatellite(String id) {

        Iterator itr = satelliteList.iterator();
        while (itr.hasNext())
        {
            Satellite x = (Satellite)itr.next();
            if (id.equals(x.getId())) {
                itr.remove();
                break;
            }
        }
    }
  
    
    /** 
     * @param id
     */
    public void removeDevice(String id) {
        Iterator itr = deviceList.iterator();
        while (itr.hasNext())
        {
            Device x = (Device)itr.next();
            if (id.equals(x.getId())) {
                itr.remove();
                break;
            }
        }
    }

    
    /** 
     * @param id
     * @param newPosition
     */
    public void moveDevice(String id, double newPosition) {
        for (Device i : deviceList) {
            if (id.equals(i.getId())) {
                i.setPosition(newPosition);
                break;
            }
        }
    }

    public void addPossibleConnection() {
        cleanPossibleConnection();
        for (Satellite i : satelliteList) {
            List<String> handle = i.getDeviceHandle();
            for (Device c : deviceList) {
                if (MathsHelper.satelliteIsVisibleFromDevice(i.getPosition(), i.getHeight(), c.getPosition()) && handle.contains(c.getType())) {
                    i.addPossibleConnections(c);
                }
            }
        }
    }
    public void cleanPossibleConnection() {
        // MathsHelper math = new MathsHelper();
        for (Satellite i : satelliteList) {
            i.cleanPossibleConnections();
        }
    }


    
    /** 
     * @param devices
     * @param satellites
     */
    public void assemblyJSON(JSONArray devices, JSONArray satellites) {
        //sort the list 
        List<Device> deviceL = deviceList;
        Comparator<Device> compareByIdDevice = (Device o1, Device o2) -> o1.getId().compareTo( o2.getId() );
        Collections.sort(deviceL, compareByIdDevice);
        List<Satellite> SatelliteL = satelliteList;
        Comparator<Satellite> compareByIdSatellite = (Satellite o1, Satellite o2) -> o1.getId().compareTo( o2.getId() );
        Collections.sort(SatelliteL, compareByIdSatellite);        
        //rebuild the PossibleConnection list
        cleanPossibleConnection();
        addPossibleConnection();
        //add item to json
        for (Device i : deviceList) {
            JSONObject temp = new JSONObject();
            temp.put("id", i.getId());
            temp.put("isConnected", i.getIsConnected());
            temp.put("position", i.getPosition());
            
            temp.put("activationPeriods", i.getActivationPeriods());
            temp.put("type", i.getType());
            devices.put(temp);
        }
        for (Satellite i : satelliteList) {
            JSONObject temp1 = new JSONObject();
            temp1.put("id", i.getId());
            temp1.put("velocity", i.getVelocity());
            temp1.put("position", i.getPosition());
            JSONArray possibleConnectionArray = new JSONArray();
            deviceL = i.getPossibleConnections();
            compareByIdDevice = (Device o1, Device o2) -> o1.getId().compareTo( o2.getId() );
            Collections.sort(deviceL, compareByIdDevice);
            for (Device c: deviceL) {
                possibleConnectionArray.put(c.getId());
            }
            temp1.put("possibleConnections", possibleConnectionArray);
            temp1.put("type", i.getType());
            deviceL = i.getConnections();
            compareByIdDevice = (Device o1, Device o2) -> o1.getId().compareTo( o2.getId() );
            Collections.sort(deviceL, compareByIdDevice);
            JSONArray connectionArray = new JSONArray();

            for (ConnectInfo c: i.getConnectInfos()) {
                JSONObject connectionObject = new JSONObject();
                //only show connection in current day
                if (Integer.compare(currentDay, c.getCurrentDay()) == 0) {
                    connectionObject.put("deviceId", c.getDeviceId());
                    connectionObject.put("endTime", c.getEndTime());
                    connectionObject.put("minutesActive", c.getMinutesActive());
                    connectionObject.put("satelliteId", c.getSatelliteConnectedId());
                    connectionObject.put("startTime", c.getStartTime());
                    connectionArray.put(connectionObject);
                }
            }
            temp1.put("connections", connectionArray);
            temp1.put("height", i.getHeight());
            satellites.put(temp1);
        }
    }
    
    /** 
     * @return JSONObject
     */
    public JSONObject showWorldState() {
        JSONObject result = new JSONObject();
        JSONArray devices = new JSONArray();
        JSONArray satellites = new JSONArray();
        assemblyJSON(devices, satellites);
        // TODO:

        result.put("devices", devices);
        result.put("satellites", satellites);

        // TODO: you'll want to replace this for Task2
        result.put("currentTime", now);

        return result;
    }



    
    /** 
     * @param deviceId
     * @param start
     * @param durationInMinutes
     */
    //task2-------------------------------------------------------


    public void scheduleDeviceActivation(String deviceId, LocalTime start, int durationInMinutes) {
        for (Device i : deviceList) {
            if (deviceId.equals(i.getId())) {
                i.scheduleDeviceActivation(start, durationInMinutes);
                break;
            }
        }
    }

    
    /** 
     * @param tickDurationInMinutes
     */
    public void simulate(int tickDurationInMinutes) {
        int iter = 0;
        //update the world by minute
        while (iter < tickDurationInMinutes){
            List<Satellite> SatelliteL = satelliteList;
            Comparator<Satellite> compareByAngle = (Satellite o1, Satellite o2) -> o1.getPosition().compareTo( o2.getPosition() );
            Collections.sort(SatelliteL, compareByAngle);
            for (Satellite i : SatelliteL) {
                List<Device> deviceL = deviceList;
                Comparator<Device> compareByIdDevice = (Device o1, Device o2) -> o1.getId().compareTo( o2.getId() );
                Collections.sort(deviceL, compareByIdDevice);
                for (Device c : deviceL) {
                    i.removeConnections(c, now, currentDay);
                    i.addConnections(c, now, currentDay);
                }
                i.setPosition(i.getPosition(), 1);
                
            }
            this.minute = this.minute + 1;
            if (minute == 1441) {
                minute = 1;
                currentDay = currentDay + 1;
            }
            now = now.plusMinutes(1);
            iter++;
        }
        //get active time at end of simulate
        for (Satellite i : satelliteList) {
            for (ConnectInfo c : i.getConnectInfos()) {
                if (c.getIsConnected()) {
                    i.stopNow(c, now);
                }
            }
        }
        // System.out.print(now);
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args){
        Blackout newUni = new Blackout();
        newUni.createDevice("Device1", "LaptopDevice", 90);
        newUni.createDevice("Device2", "LaptopDevice", 91);
        newUni.createDevice("Device3", "LaptopDevice", 92);
        newUni.createDevice("Device4", "LaptopDevice", 93);
        newUni.createDevice("Device5", "LaptopDevice", 94);
        newUni.createDevice("Device6", "LaptopDevice", 95);
        newUni.createDevice("Device7", "LaptopDevice", 96);
        newUni.createDevice("Device8", "LaptopDevice", 97);
        newUni.createDevice("Device9", "LaptopDevice", 98);
        newUni.createDevice("Device10", "LaptopDevice", 99);

        newUni.createSatellite("Satellite1", "SovietSatellite", 6000, 90);
        // newUni.createSatellite("Satellite2", "NasaSatellite", 10000, 50);
        // newUni.createSatellite("Satellite3", "NasaSatellite", 10000, 0);
        // newUni.createSatellite("Satellite4", "NasaSatellite", 10000, 359);
        // newUni.moveDevice("DeviceA", 211);
        newUni.scheduleDeviceActivation("Device1", LocalTime.of(0, 0), 400);
        newUni.scheduleDeviceActivation("Device2", LocalTime.of(2, 0), 400);
        newUni.scheduleDeviceActivation("Device3", LocalTime.of(4, 0), 400);
        newUni.scheduleDeviceActivation("Device4", LocalTime.of(6, 0), 400);
        newUni.scheduleDeviceActivation("Device5", LocalTime.of(8, 0), 400);
        newUni.scheduleDeviceActivation("Device6", LocalTime.of(10, 0), 400);
        newUni.scheduleDeviceActivation("Device7", LocalTime.of(0, 0), 400);
        newUni.scheduleDeviceActivation("Device8", LocalTime.of(0, 0), 400);
        // newUni.scheduleDeviceActivation("Device9", LocalTime.of(0, 0), 400);
        // newUni.scheduleDeviceActivation("Device10", LocalTime.of(0, 0), 400);
        // newUni.scheduleDeviceActivation("DeviceA", LocalTime.of(0, 0), 400);
        // newUni.scheduleDeviceActivation("DeviceA", LocalTime.of(12, 30), 400);
        // newUni.scheduleDeviceActivation("DeviceB", LocalTime.of(0, 0), 400);
        // newUni.scheduleDeviceActivation("DeviceC", LocalTime.of(0, 0), 400);
        // newUni.scheduleDeviceActivation("DeviceC", LocalTime.of(15, 0), 300);
        // System.out.println(newUni.showWorldState());
        newUni.printSatelliteList();
        newUni.simulate(1440);
        System.out.println(newUni.showWorldState());
        newUni.printSatelliteList();
        // newUni.simulate(720);
        // newUni.printSatelliteList();

        // System.out.println(newUni.currentDay);
        // ;
    }
    public void printDeviceList(){
        for (Device i : deviceList) {
            System.out.println(i.getId() + " " + i.getType());
        }
    }
    public void printSatelliteList(){
        for (Satellite i : satelliteList) {
            System.out.println(i.getId() + " " + i.getType() + " "+i.getPosition());
            for (ConnectInfo c : i.getConnectInfos()) {
                System.out.println(c.getDeviceId() + " " + c.getStartTime() + " " + c.getEndTime() + " " + c.getMinutesActive());
            }
            System.out.println("-------------");
        }
        
    }    
}
