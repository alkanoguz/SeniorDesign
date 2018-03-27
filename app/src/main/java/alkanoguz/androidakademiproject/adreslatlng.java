package alkanoguz.androidakademiproject;

/**
 * Created by oguz9 on 22.3.2018.
 */

public class adreslatlng {
    public static String address;
    public static double lat;
    public static double lng;
    public adreslatlng(String address,double lat,double lng){
        this.address += address;
        this.lat += lat;
        this.lng += lng;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
