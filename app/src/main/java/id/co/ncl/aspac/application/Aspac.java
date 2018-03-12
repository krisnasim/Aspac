package id.co.ncl.aspac.application;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import id.co.ncl.aspac.background.PusherListener;
import id.co.ncl.aspac.database.AspacDatabase;
import id.co.ncl.aspac.database.AspacSQLite;
import id.co.ncl.aspac.database.DatabaseManager;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

public class Aspac extends Application {

    private AspacSQLite myDb;

    @Override
    public void onCreate() {
        super.onCreate();
        myDb = new AspacSQLite(getApplicationContext());
        DatabaseManager.initializeInstance(myDb);
        Intent pusherIntent = new Intent(this, PusherListener.class);
        startService(pusherIntent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
