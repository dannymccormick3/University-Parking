package cs4278.vupark;

import android.content.Intent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Danny on 12/10/2017.
 */
public class MapsActivityTest {
    @Test
    public void testMapParsing() throws Exception {
        MapsActivity mapsActivity = new MapsActivity();
        Thread.sleep(2000);
        HashMap<String, Object> lotMap = mapsActivity.getLotMap();
        ArrayList<ParkingLot> mLots = mapsActivity.getmParkingLots();
        if(lotMap != null) {
            for (String lotKey : lotMap.keySet()) {
                HashMap<String, Object> lot = (HashMap) lotMap.get(lotKey);
                assertTrue(lot.containsKey("title"));
                String lotName = lot.get("title").toString();
                boolean inLots = false;
                for (ParkingLot curLot : mLots) {
                    if (curLot.getName() == lotName) {
                        inLots = true;
                    }
                }
                assertTrue(inLots);
            }
        }
    }

}