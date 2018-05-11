package alkanoguz.androidakademiproject;

import java.util.ArrayList;

    public class Movie {
    private String title, thumbnailUrl;
    private int year;
    private String rating;
    private ArrayList<String> genre;
    private String lat;
    private String lng;

    public Movie() {
    }

    public Movie(String name, String thumbnailUrl, String rating,String lng,String lat) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.lat = lat;
        this.rating = rating;
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }
        public String getLat() {
            return lat;
        }
        public void setLat(String lat) {
            this.lat = lat;
        }
        public String getLng() {
            return lng;
        }
        public void setLng(String lng) {
            this.lng = lng;
        }


        public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }




}