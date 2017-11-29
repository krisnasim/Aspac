package id.co.ncl.aspac.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import id.co.ncl.aspac.entities.Service;

/**
 * Created by Jonathan Simananda on 29/11/2017.
 */

@Database(entities = Service.class, version = 1)
public abstract class AspacDatabase extends RoomDatabase {
    //put your DAOs here
    //public abstract ProductDao productDao();
}
