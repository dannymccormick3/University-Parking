package cs4278.vupark;

import android.app.ActionBar;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String[] mLotNames = {"Terrace Place Garage"};
    private double[][][] mLotCoordinates = {
            {{36.150149, -86.800308}, {36.150577, -86.799402}, {36.150287, -86.799186}, {36.149849, -86.800100}}
    };

    private ArrayList<ParkingLot> mParkingLots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Draw the parking lots on the map
        assert(mLotCoordinates.length == mLotNames.length);
        for(int i = 0; i < mLotNames.length; i++) {
            String name = mLotNames[i];
            double[][] coordinates = mLotCoordinates[i];
            // create a new clickable polygon
            PolygonOptions polygonOps = new PolygonOptions().clickable(true);

            // add the coordinates to the polygon
            for(double[] coordinate : coordinates) {
                polygonOps.add(new LatLng(coordinate[0], coordinate[1]));
            }

            // adjust the style of the polygon
            polygonOps.strokeWidth(3.5f).fillColor(Color.RED);

            // add the polygon to the map
            Polygon polygon = mMap.addPolygon(polygonOps);

            // create a new Parking Lot object associated with the polygon
            ParkingLot myLot = new ParkingLot(name, polygon);
            polygon.setTag(myLot);

            // add the lot to mParkingLots
            mParkingLots.add(myLot);
        }

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                String text = ((ParkingLot)polygon.getTag()).getName();
                Toast.makeText(MapsActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        // Move the camera to Terrace Place Garage
        LatLng terracePlaceGarage = new LatLng(36.150285, -86.799749);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(terracePlaceGarage, 15.0f));

    }

}
