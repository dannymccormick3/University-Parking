package cs4278.vupark;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Danny on 11/15/2017.
 * Class to hold all methods connecting to server/database.
 */

public class DBConnection {
    private HashMap<String,String> usernames = new HashMap<>();
    private List<Reservation> curReservations = new ArrayList<>();
    private HashMap<String, String> permitMap = new HashMap<>();
    private ArrayList<ParkingLot> availableLots = new ArrayList<>();
    private double[][] coordinates = {
            {36.150149, -86.800308}, {36.150577, -86.799402}, {36.150287, -86.799186}, {36.149849, -86.800100}};
    public DBConnection() {
        usernames.put("dmccormick", "dpassword");
        permitMap.put("dmccormick", "C");
        usernames.put("aragan", "apassword");
        permitMap.put("aragan", "D");
        Polygon lotPolygon = null;
        PolygonOptions polygonOps = new PolygonOptions().clickable(true);

        // add the coordinates to the polygon
        for(double[] coordinate : coordinates) {
            polygonOps.add(new LatLng(coordinate[0], coordinate[1]));
        }

        // adjust the style of the polygon
        polygonOps.strokeWidth(3.5f).fillColor(Color.RED);
        ParkingLot myLot = new ParkingLot("Terrace Place", polygonOps);
        availableLots.add(myLot);
        Date startDateTime = new Date(2017,12,9,10, 30);
        Reservation newReservation = new Reservation(startDateTime, "Terrace", 10);
        curReservations.add(newReservation);
    }

    public void createAccount(String username, String password) {
        //TODO: update to actually connect with server.
        try {
            Thread.sleep(2500);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        usernames.put(username, password);
        return;
    }

    /*
     * Function takes in a username and password and returns whether that is a valid user.
     */
    public boolean validateCredentials(String username, String password) {
        //TODO: Update to actually connect with server.
        try {
            Thread.sleep(250);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        if (usernames.containsKey(username)){
            if(usernames.get(username).equals(password)){
                return true;
            }
        }
        return false;
    }

    /*
     * Function takes in a username and password, returns true if that user can be added to the
     * database, false otherwise (because a user with that username already exists).
     */
    public boolean addUser(String username, String password) {
        //TODO: Update to add send new user.
        try {
            Thread.sleep(250);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        usernames.put(username, password);
        return false;
    }

    /*
     * Function takes in a Reservation object and tries to save that reservation to the database.
     * Returns true if successful, false otherwise.
     */
    public boolean makeReservation(Reservation reservation) {
        //TODO: Update parameters and body to actually send reservation
        try {
            Thread.sleep(250);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        curReservations.add(reservation);
        return false;
    }

    public List<Reservation> getReservations(String username){
        //TODO: Connect to database and get list of all reservations under this username.
        return curReservations;
    }

    public void setPermit(String username, String permit) {
        //TODO: Connect to database and send the user's permit.
        permitMap.put(username, permit);
        return;
    }

    public String getPermit(String username) {
        //TODO: Connect to database and get the user's permit.
        return "C";
    }

    public ArrayList<ParkingLot> getAvailableLots() {
        //TODO: Connect to database and get list of all available lots.
        return availableLots;
    }

    public ArrayList<Integer> getAvailableSpots(ParkingLot lot, String permit) {
        //TODO: Connect to database and get list of all available spots.
        ArrayList<Integer> spots = new ArrayList<>();
        spots.add(10);
        spots.add(11);
        return spots;
    }
}
