package com.example.projectui.service;

import android.app.Activity;
import android.location.Location;

import java.util.function.Consumer;

public interface LocationService {

    void getLocation(Activity activity, Consumer<Location> locationConsumer);
}
