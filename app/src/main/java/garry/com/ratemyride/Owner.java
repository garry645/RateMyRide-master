package garry.com.ratemyride;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

public class Owner {

    private String email;
    public String username;
    private int raterScore;
    private int carsOwned;
    private int carSlots;


    private int totalRatings;

    public Owner() {

    }

    public Owner(String emailIn, String usernameIn) {
        this.email = emailIn;
        this.username = usernameIn;
        this.raterScore = 1;
        this.carsOwned = 0;
        this.carSlots = 30;
        this.totalRatings = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getRaterScore() {
        return raterScore;
    }

    public int getCarsOwned() {
        return carsOwned;
    }

    public void setCarsOwned(int carsIn) { this.carsOwned = carsIn; }

    public int getCarSlots() {
        return carSlots;
    }

    public void setRaterScore(int raterScoreIn) {
        this.raterScore = raterScoreIn;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatingsIn) {
        this.totalRatings = totalRatingsIn;
    }


    public void setCarSlots(int carSlotsIn) {
        this.carSlots = carSlotsIn;
    }
    }
