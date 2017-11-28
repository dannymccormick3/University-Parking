package cs4278.vupark;

import android.graphics.Color;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 11/14/2017.
 */

public class ParkingLot {
    private String name;
    private Polygon lot;
    private String permits;
    private List<Integer> availableSpots;

    ParkingLot(String name, Polygon lot) {
        this.name = name;
        this.lot = lot;
    }

    public String getName() {
        return name;
    }

    public String getPermits() {
        return permits;
    }
}
