package com.example.toni.myapplication;

import android.app.Application;
import android.util.Log;

import com.example.toni.myapplication.models.Board;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by toni on 24/03/2018.
 */

public class MyAplication extends Application{

    public static AtomicInteger BoardId = new AtomicInteger();

    public void onCreate(){
        super.onCreate();
        Log.d("Realm App", "Esto se genera antes de que los activities se inicialicen");

        Realm.init(this);
        setUpRealmConfig();

        Realm realm  =  Realm.getDefaultInstance();
        BoardId = getIdByTable(realm, Board.class);
        realm.close();
    }


    private void setUpRealmConfig(){
        RealmConfiguration config = new RealmConfiguration.
                Builder().build();

        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();

        return (results.size() > 0 ) ? new AtomicInteger(results.max( "id").intValue()) : new AtomicInteger();
    }
}
