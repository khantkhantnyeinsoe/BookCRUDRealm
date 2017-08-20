package com.example.khantkhantnyeinsoe.bookwithrealm;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by khantkhantnyeinsoe on 8/6/17.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        Realm.init(this);
        super.onCreate();

    }
}
