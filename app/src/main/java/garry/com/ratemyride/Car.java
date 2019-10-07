package garry.com.ratemyride;

import java.util.ArrayList;

public class Car {
    Owner owner;
    private String make;
    private String model;
    private int year;
    private String dbID;
    private ArrayList<String> carImageUrls = new ArrayList<>();

    public void setCurrentCar(int currentCar) {
        this.currentCar = currentCar;
    }

    private int currentCar = 0;


    public int getCurrentCar() {
        return currentCar;
    }

    public float getTotalRating() {
        return totalRating;
    }

    private float totalRating;
    private float posRatings;
    private float numOfRatings;


    public Car() {

    }

    Car(Owner ownerIn, String makeIn, String modelIn, int yearIn, float posRatingsIn, float numOfRatingsIn, float totalRatingIn, String dbIDIn) {
        this.owner = ownerIn;
        this.make = makeIn;
        this.model = modelIn;
        this.year = yearIn;
        this.posRatings = posRatingsIn;
        this.numOfRatings = numOfRatingsIn;
        this.totalRating = totalRatingIn;
        this.dbID = dbIDIn;

    }

    public Owner getOwner() {
        return owner;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getDbID() {
        return dbID;
    }

    public void addImageUrl(int position, String urlIn) {
        if (carImageUrls.size() < 10) {
            if (carImageUrls.size() == position) {
                carImageUrls.add(urlIn);
            } else {
                carImageUrls.set(position, urlIn);
            }
            //carImageUrls.add(urlIn);
        }
    }

    public ArrayList<String> getCarImageUrls() {
        return carImageUrls;
    }

    public String getCarImageUrl(int position) {
        return carImageUrls.get(position);
    }

    public float getPosRatings() {
        return posRatings;
    }

    void deleteImageUrl(int position) {
        carImageUrls.remove(position);
        /*for(int i = position; i < carImageUrls.size(); i++) {
                String url = carImageUrls.get(i);
                String newKey = String.valueOf(i - 1);
                carImageUrls.remove(String.valueOf(i));
                carImageUrls.put(newKey, url);
        }*/
    }

    public float getNumOfRatings() {
        return numOfRatings;
    }


}
