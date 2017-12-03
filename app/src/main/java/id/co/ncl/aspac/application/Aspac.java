package id.co.ncl.aspac.application;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.multidex.MultiDex;

import id.co.ncl.aspac.database.AspacDatabase;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

public class Aspac extends Application {

    //public AspacDatabase db;
    //public final static String DATABASE_NAME = "aspac_room_database";

    @Override
    public void onCreate() {
        super.onCreate();
        //db = Room.databaseBuilder(getApplicationContext(), AspacDatabase.class, DATABASE_NAME).build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//    public AspacDatabase getDatabase() {
//        return db;
//    }
}
