package ru.baccasoft.eatster.model;

import java.util.ArrayList;
import java.util.List;

public class RestaurantPhotosModel {

    private String logo;
    private String mainPhoto;
    private List<String> photos;

    public RestaurantPhotosModel() {
        logo = "";
        mainPhoto = "";
        photos = new ArrayList();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

}
