package cs4278.vupark;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String username;
    private ArrayList<ParkingLot> mParkingLots = new ArrayList<>();
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> listViewAdapter;
    private boolean mapReadyToBePainted = false;

    private Button park_button;
    private Button lots_button;
    private Button account_button;
    private ImageButton info_button;

    private ViewAnimator animator;
    private TextView lot_name;
    private Button reserve_button;
    private Button register_button;
    private ListView lot_listview;

    private ParkingLot curLot;
    private TextView lot_name_entry;
    private int curSpot;
    private String curSpotName;
    private TextView spot_entry;
    private String spot_cost = "FREE";
    private TextView cost_entry;

    private Button park_car_button;
    private Button cancel_reservation_button;

    private TextView confirmation_lot_name_entry;
    private TextView confirmation_spot_entry;
    private TextView confirmation_cost_entry;
    private Button leave_spot_button;
    private FirebaseDatabase database;

    private ParkingLot constructParkingLot(String name, double[][] coordinates){
        PolygonOptions polygonOps = new PolygonOptions().clickable(true);

        // add the coordinates to the polygon
        for(double[] coordinate : coordinates) {
            polygonOps.add(new LatLng(coordinate[0], coordinate[1]));
        }
        // adjust the style of the polygon
        polygonOps.strokeWidth(3.5f).fillColor(Color.RED);

        return new ParkingLot(name, polygonOps);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        database = FirebaseDatabase.getInstance();
        DatabaseReference lotRef = database.getReference("lots");
        lotRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> lotMap = (HashMap)dataSnapshot.getValue();
                for(String lotKey: lotMap.keySet()){
                    HashMap<String, Object> lot = (HashMap)lotMap.get(lotKey);
                    String lotName = lot.get("title").toString();
                    HashMap<String, Double> polygon = (HashMap)lot.get("polygon");
                    Log.d("HELP", polygon.get("x1").toString());
                    double[][] coordinates = {
                            {polygon.get("x1"),
                                    polygon.get("y1")},
                            {polygon.get("x2"),
                                    polygon.get("y2")},
                            {polygon.get("x3"),
                                    polygon.get("y3")},
                            {polygon.get("x4"),
                                    polygon.get("y4")}};
                    mParkingLots.add(constructParkingLot(lotName, coordinates));
                }
                if(mapReadyToBePainted){
                    paintLots();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load lot info from database",
                        Toast.LENGTH_LONG).show();
            }
        });

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
        lot_listview = findViewById(R.id.lot_list);

        park_car_button = findViewById(R.id.park_car_button);
        cancel_reservation_button = findViewById(R.id.cancel_reservation_button);
        lot_name_entry = findViewById(R.id.lot_name_entry);
        spot_entry = findViewById(R.id.spot_entry);
        cost_entry = findViewById(R.id.cost_entry);

        confirmation_lot_name_entry = findViewById(R.id.confirmation_lot_name_entry);
        confirmation_spot_entry = findViewById(R.id.confirmation_spot_entry);
        confirmation_cost_entry = findViewById(R.id.confirmation_cost_entry);
        leave_spot_button = findViewById(R.id.leave_spot_button);

        Intent incomingIntent = getIntent();
        ArrayList<String> names = incomingIntent.getStringArrayListExtra("names");
        username = incomingIntent.getStringExtra("username");
        listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        lot_listview.setAdapter(listViewAdapter);
        lot_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String spotString = listItems.get(position);
                curSpotName = spotString;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int spotNumber = Integer.parseInt(spotString.substring(6)); //Parse "Space <num>"
                        Reservation r = new Reservation(new Date(), lot_name.getText().toString(), spotNumber);
                        DBConnection mConnection = new DBConnection();
                        mConnection.makeReservation(r);
                    }
                }).start();

                if(curSpot != position) {
                    if(curSpot >= 0) {
//                        String new_text = listItems.get(curSpot).replace(" selected", "");
//                        listItems.set(curSpot, new_text);
                    }
                    curSpot = position;
//                    listItems.set(position, listItems.get(position) + " selected");
                    listViewAdapter.notifyDataSetChanged();
                }

            }
        });

        reserve_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curSpot != -1) {
                    // @TODO UPDATE DATABASE TO SEE A SPOT IS RESERVED
                    lot_name_entry.setText(curLot.getName());
                    spot_entry.setText(curSpotName);
                    cost_entry.setText(spot_cost);

                    animator.setDisplayedChild(2);
                }
            }
        });

        park_car_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curSpot != -1) {
                    // @TODO UPDATE DATABASE TO SEE NO LONGER PARKED
                    confirmation_lot_name_entry.setText(curLot.getName());
                    confirmation_spot_entry.setText(curSpotName);
                    confirmation_cost_entry.setText(spot_cost);

                    animator.setDisplayedChild(3);
                }
            }
        });

        cancel_reservation_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // @TODO UPDATE DATABASE TO SEE NO LONGER REGISTERED
                curSpot = -1;
                animator.setDisplayedChild(1);
                lot_listview.clearChoices();
                lot_listview.setSelection(-1);
                lot_listview.setAdapter(listViewAdapter);
            }
        });

        register_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // @TODO UPDATE DATABASE TO SEE YOU ARE PARKED
                if(curSpot != -1) {
                    confirmation_lot_name_entry.setText(curLot.getName());
                    confirmation_spot_entry.setText(curSpotName);
                    confirmation_cost_entry.setText(spot_cost);

                    animator.setDisplayedChild(3);
                }
            }
        });

        leave_spot_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curSpot != -1) {
                    // @TODO UPDATE DATABASE TO SEE NO LONGER PARKED

                    curSpot = -1;
                    animator.setDisplayedChild(1);
                    lot_listview.clearChoices();
                    lot_listview.setSelection(-1);
                    lot_listview.setAdapter(listViewAdapter);
                }
            }
        });
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
        mapReadyToBePainted = true;
        if (mParkingLots.size() > 0){
            paintLots();
            mapReadyToBePainted = false;
        }
    }

    private void paintLots(){

        for(int i = 0; i < mParkingLots.size(); i++){
            ParkingLot lot = mParkingLots.get(i);
            Polygon poly = mMap.addPolygon(lot.getPolyOps());
            lot.setPolygon(poly);
            poly.setTag(lot);
        }

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {onPolygonClicked(polygon);}
        });

        // Move the camera to Terrace Place Garage
        LatLng terracePlaceGarage = new LatLng(36.150285, -86.799749);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(terracePlaceGarage, 15.0f));
    }

    private void onPolygonClicked(Polygon polygon){
        curLot = (ParkingLot)polygon.getTag();
        lot_name.setText(curLot.getName());
        animator.setDisplayedChild(1);
        listViewAdapter.clear();
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
                curSpot = -1;
                for(Integer i: (ArrayList<Integer>)spaces){
                    listItems.add("Space " + i);
                }
                listViewAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
