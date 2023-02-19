package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.time.LocalTime;

import test.test_helpers.DummyConnection;
import test.test_helpers.ResponseHelper;
import test.test_helpers.TestHelper;

@TestInstance(value = Lifecycle.PER_CLASS)
public class OwnTests1 {
   @Test
   public void testOneAndTwoHalfDay() {

       String initialWorldState = new ResponseHelper(LocalTime.of(0, 0))
           .expectSatellite("NasaSatellite", "Satellite1", 10000, 30, 85, new String[] { "DeviceA" })
           .expectSatellite("NasaSatellite", "Satellite2", 10000, 50, 85, new String[] { "DeviceA" })
           .expectSatellite("NasaSatellite", "Satellite3", 10000, 0, 85, new String[] { "DeviceA" })
           .expectSatellite("NasaSatellite", "Satellite4", 10000, 359, 85, new String[] { "DeviceA" })
           .expectDevice("HandheldDevice", "DeviceA", 30, false, new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(6, 40) }, { LocalTime.of(12, 30), LocalTime.of(19, 10) } })
           .toString();

       // then simulates for half a day
       String afterHalfADay = new ResponseHelper(LocalTime.of(12, 0))
           .expectSatellite("NasaSatellite", "Satellite1", 10000, 36.12, 85,
               new String[] { "DeviceA" })
           .expectSatellite("NasaSatellite", "Satellite2", 10000, 56.12, 85,
               new String[] { "DeviceA" })
           .expectSatellite("NasaSatellite", "Satellite3", 10000, 6.12, 85,
               new String[] { "DeviceA" },
               new DummyConnection[] {
                   new DummyConnection("DeviceA", LocalTime.of(0, 0), LocalTime.of(6, 41), 390), //
               })
           .expectSatellite("NasaSatellite", "Satellite4", 10000, 5.12, 85,
               new String[] { "DeviceA" })
           .expectDevice("HandheldDevice", "DeviceA", 30, false, new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(6, 40) }, { LocalTime.of(12, 30), LocalTime.of(19, 10) } })
           .toString();

       String afteronehalfDay = new ResponseHelper(LocalTime.of(12, 0))
           .expectSatellite("NasaSatellite", "Satellite1", 10000, 48.36, 85,
               new String[] { "DeviceA" })
           .expectSatellite("NasaSatellite", "Satellite2", 10000, 68.36, 85,
               new String[] { "DeviceA" })
           .expectSatellite("NasaSatellite", "Satellite3", 10000, 18.36, 85,
               new String[] { "DeviceA" })
           .expectSatellite("NasaSatellite", "Satellite4", 10000, 17.36, 85,
               new String[] { "DeviceA" },
               new DummyConnection[] {
                   new DummyConnection("DeviceA", LocalTime.of(0, 0), LocalTime.of(6, 41), 390),
                    //
               })
           .expectDevice("HandheldDevice", "DeviceA", 30, false, new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(6, 40) }, { LocalTime.of(12, 30), LocalTime.of(19, 10) } })
           .toString();

       
       TestHelper plan = new TestHelper()
           .createDevice("HandheldDevice", "DeviceA", 30)
           .createSatellite("NasaSatellite", "Satellite1", 10000, 30)
           .createSatellite("NasaSatellite", "Satellite2", 10000, 50)
           .createSatellite("NasaSatellite", "Satellite3", 10000, 0)
           .createSatellite("NasaSatellite", "Satellite4", 10000, 359)
           .scheduleDeviceActivation("DeviceA", LocalTime.of(0, 0), 400)
           .scheduleDeviceActivation("DeviceA", LocalTime.of(12, 30), 400)
           .showWorldState(initialWorldState)
           .simulate(2160)
           .showWorldState(afteronehalfDay);
           // .removeSatellite("Satellite4")
           // .simulate(1440)
           // .showWorldState(aftertwohalfDay);
       plan.executeTestPlan();
   }

   @Test
   public void testDeviceConnectType() {
      //test satellite can only connect to certain type of device

      String initialWorldState = new ResponseHelper(LocalTime.of(0, 0))
          .expectSatellite("SovietSatellite", "Soviet1", 10000, 30, 100, new String[] {})
          .expectDevice("HandheldDevice", "DeviceA", 30, false, new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(6, 40) }, { LocalTime.of(12, 30), LocalTime.of(19, 10) } })
          .toString();

      // then simulates for half a day
      String afterHalfADay = new ResponseHelper(LocalTime.of(12, 0))
         .expectSatellite("SovietSatellite", "Soviet1", 10000, 37.2, 100, new String[] {})
         .expectDevice("HandheldDevice", "DeviceA", 30, false, new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(6, 40) }, { LocalTime.of(12, 30), LocalTime.of(19, 10) } })
         .toString();

      // then simulates for Next half a day
      String afterNextHalfADay = new ResponseHelper(LocalTime.of(0, 0))
         .expectSatellite("SovietSatellite", "Soviet1", 10000, 44.4, 100, new String[] {"DeviceB"},
         new DummyConnection[] {
             new DummyConnection("DeviceB", LocalTime.of(13, 0), LocalTime.of(15, 01), 110),
         })
         .expectDevice("HandheldDevice", "DeviceA", 30, false, new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(6, 40) }, { LocalTime.of(12, 30), LocalTime.of(19, 10) } })
         .expectDevice("DesktopDevice", "DeviceB", 50, false, new LocalTime[][] {  { LocalTime.of(13, 0), LocalTime.of(15, 00) } })
         .toString();
      
      TestHelper plan = new TestHelper()
          .createDevice("HandheldDevice", "DeviceA", 30)
          .createSatellite("SovietSatellite", "Soviet1", 10000, 30)
          .scheduleDeviceActivation("DeviceA", LocalTime.of(0, 0), 400)
          .scheduleDeviceActivation("DeviceA", LocalTime.of(12, 30), 400)
          .showWorldState(initialWorldState)
          .simulate(720)
          .showWorldState(afterHalfADay)
          .createDevice("DesktopDevice", "DeviceB", 50)
          .scheduleDeviceActivation("DeviceB", LocalTime.of(13, 00), 120)
          .simulate(720)
          .showWorldState(afterNextHalfADay);
      plan.executeTestPlan();
  }

  @Test
    public void testSovietSatelliteMovementStartOutsideRegionNegativeVelocity() {
        String finalWorldState = new ResponseHelper(LocalTime.of(1, 40))
            // the result shows that now it's going backwards! which is what we want
            .expectSatellite("SovietSatellite", "FalconAndTheWinterSoldier", 6000, 208.33, -100, new String[] {})
            .toString();

        TestHelper plan = new TestHelper() //
            // this satellite is starting outside of the region and is close to 190 
            // so it should have a -100 velocity
            .createSatellite("SovietSatellite", "FalconAndTheWinterSoldier", 6000, 210) //
            .simulate(100)
            .showWorldState(finalWorldState);
        plan.executeTestPlan();
    }
}
