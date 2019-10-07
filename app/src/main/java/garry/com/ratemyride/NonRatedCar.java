package garry.com.ratemyride;

public class NonRatedCar {


    private String dbID;
    public int currentCar = 0;

    private boolean carIsRated = false;
    boolean color = false;
    boolean design = false;

    public NonRatedCar() {

    }

    public NonRatedCar(String dbIdIn) {
        this.dbID = dbIdIn;
    }

    public String getDbID() {
        return dbID;
    }

    public boolean getCarIsRated() {
        return carIsRated;
    }

}
