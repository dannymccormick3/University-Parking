package cs4278.vupark;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String username;
    private ArrayList<ParkingLot> mParkingLots = new ArrayList<>();
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> listViewAdapter;

    private Button park_button;
    private Button lots_button;
    private Button account_button;
    private ImageButton info_button;

    private ViewAnimator animator;
    private TextView lot_name;
    private Button reserve_button;
    private Button register_button;
    private ListView lot_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        park_button = findViewById(R.id.park_button);
        lots_button = findViewById(R.id.lots_button);
        account_button = findViewById(R.id.account_button);
        info_button = findViewById(R.id.help_button);

        animator = findViewById(R.id.animator);
        lot_name = findViewById(R.id.lot_name);
        reserve_button = findViewById(R.id.reserve_button);
        register_button = findViewById(R.id.register_button);
        lot_list = findViewById(R.id.lot_list);

        Intent incomingIntent = getIntent();
        ArrayList<String> names = incomingIntent.getStringArrayListExtra("names");
        ArrayList<PolygonOptions> polys = incomingIntent.getParcelableArrayListExtra("polys");
        for (int i = 0; i < names.size(); i++){
            mParkingLots.add(new ParkingLot(names.get(i), polys.get(i)));
        }
        username = incomingIntent.getStringExtra("username");
        //TODO: Update this to findViewById for list view instead of null.
        //listViewAdapter = new ArrayAdapter<String>(this, null, listItems);
        //setListAdapter(listViewAdapter);
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

        for(int i = 0; i < mParkingLots.size(); i++){
            ParkingLot lot = mParkingLots.get(i);
            Polygon poly = mMap.addPolygon(lot.getPolyOps());
            lot.setPolygon(poly);
            poly.setTag(lot);
        }

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                final ParkingLot curLot = (ParkingLot)polygon.getTag();
                String text = curLot.getName();
                Toast.makeText(MapsActivity.this, text, Toast.LENGTH_SHORT).show();
                new AsyncTask() {
                    @Override
                    protected ArrayList<Integer> doInBackground(Object[] objects) {
                        DBConnection mConnection = new DBConnection();
                        String permit = mConnection.getPermit(username);
                        ArrayList<Integer> spaces = mConnection.getAvailableSpots(curLot, permit);
                        return spaces;
                    }

                    @Override
                    protected void onPostExecute(Object spaces){
                        listItems = new ArrayList<String>();
                        for(Integer i: (ArrayList<Integer>)spaces){
                            listItems.add("Space " + i);
                            //TODO: Update this to fill in the bottom section of UI
                            //Toast.makeText(MapsActivity.this, "TEST" + i, Toast.LENGTH_SHORT).show();
                        }
                        listViewAdapter.notifyDataSetChanged();
                    }
                }.execute();
            }
        });


        // Move the camera to Terrace Place Garage
        LatLng terracePlaceGarage = new LatLng(36.150285, -86.799749);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(terracePlaceGarage, 15.0f));

    }

}
