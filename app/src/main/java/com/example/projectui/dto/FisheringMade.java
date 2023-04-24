package com.example.projectui.dto;

import com.example.projectui.enums.Country;
import com.example.projectui.enums.Region;

import java.sql.Blob;

public class FisheringMade {

    private Long fisheringId;


    private String location;

    private Blob pictureOfFish;

    private Integer weightKg;

    private Country country;

    private Region region;

    private TypeOfFish typeOfFish;

    //blockchain
    private String fisheringHash;

    public Long getFisheringId() {
        return fisheringId;
    }

    public void setFisheringId(Long fisheringId) {
        this.fisheringId = fisheringId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Blob getPictureOfFish() {
        return pictureOfFish;
    }

    public void setPictureOfFish(Blob pictureOfFish) {
        this.pictureOfFish = pictureOfFish;
    }

    public Integer getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Integer weightKg) {
        this.weightKg = weightKg;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public TypeOfFish getTypeOfFish() {
        return typeOfFish;
    }

    public void setTypeOfFish(TypeOfFish typeOfFish) {
        this.typeOfFish = typeOfFish;
    }

    public String getFisheringHash() {
        return fisheringHash;
    }

    public void setFisheringHash(String fisheringHash) {
        this.fisheringHash = fisheringHash;
    }
}
