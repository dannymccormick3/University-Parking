package cs4278.vupark;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 11/15/2017.
 * Class to hold all methods connecting to server/database.
 */

public class DBConnection {
    public DBConnection() {

    }

    /*
     * Function takes in a username and password and returns whether that is a valid user.
     */
    public boolean validateCredentials(String username, String password) {
        //TODO: Update to actually connect with server.
        if (username.equals("bad") || password.equals("bad")) {
            return false;
        }
        return true;
    }

    /*
     * Function takes in a username and password, returns true if that user can be added to the
     * database, false otherwise (because a user with that username already exists).
     */
    public boolean addUser(String username, String password) {
        //TODO: Update to add send new user.
        return false;
    }

    /*
     * Function takes in a Reservation object and tries to save that reservation to the database.
     * Returns true if successful, false otherwise.
     */
    public boolean makeReservation(Reservation reservation) {
        //TODO: Update parameters and body to actually send reservation
        return false;
    }

    public List<Reservation> getReservations(String username){
        //TODO: Connect to database and get list of all reservations under this username.
        return null;
    }
}
