package com.example.projectui.enums;

public enum Region {
    NORTH_WEST("North West"),
    NORTH_EAST("North East"),
    SOUTH_WEST("South West"),
    SOUTH_EAST("South East");

    private String region;

    Region(String region) {
        this.region = region;
    }

    public String getUrl() {
        return region;
    }
}
