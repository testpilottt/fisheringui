package com.example.projectui.dto;

import com.example.projectui.enums.Country;
import com.example.projectui.enums.Region;

import java.time.LocalDateTime;

public class FisheringMade {

    private Long fisheringId;


    private String location;

    private byte[] pictureOfFishBase64;

    private Double weightKg;

    private Country country;

    private Region region;

    private TypeOfFish typeOfFish;

    //blockchain
    private String fisheringHash;

    private LocalDateTime timeLog;

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

    public byte[] getPictureOfFishBase64() {
        return pictureOfFishBase64;
    }

    public void setPictureOfFishBase64(byte[] pictureOfFishBase64) {
        this.pictureOfFishBase64 = pictureOfFishBase64;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
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

    public LocalDateTime getTimeLog() {
        return timeLog;
    }

    public void setTimeLog(LocalDateTime timeLog) {
        this.timeLog = timeLog;
    }
}
