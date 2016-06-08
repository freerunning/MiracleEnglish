package com.dingyu.miracleenglish.application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dyu on 16-6-6.
 */

public class MiracleApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration.Builder builder = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded();
        Realm.setDefaultConfiguration(builder.build());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
