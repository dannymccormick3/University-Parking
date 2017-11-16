package cs4278.vupark;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Danny on 11/15/2017.
 * Class to hold an instance of a reservation.
 */

public class Reservation {
    private Date mStartDate;
    private Time mStartTime;
    private ParkingLot mLot;
    private int mDuration;
    private int mSpotNumber;

    public Reservation(Date startDate, Time startTime, int duration, ParkingLot lot, int spotNumber){
        mStartDate = startDate;
        mStartTime = startTime;
        mDuration = duration;
        mLot = lot;
        mSpotNumber = spotNumber;
    }

    public Date getStartDate(){
        return mStartDate;
    }

    public void setStartDate(Date startDate){
        mStartDate = startDate;
    }

    public Time getStartTime(){
        return mStartTime;
    }

    public void setStartTime(Time startTime){
        mStartTime = startTime;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration){
        mDuration = duration;
    }

    public ParkingLot getParkingLot() {
        return mLot;
    }

    public void setGarageNumber(ParkingLot lot) {
        mLot = lot;
    }

    public int getmSpotNumber() {
        return mSpotNumber;
    }

    public void setSpotNumber(int spotNumber) {
        mSpotNumber = spotNumber;
    }
}
