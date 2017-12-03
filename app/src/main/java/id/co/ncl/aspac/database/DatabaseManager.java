package id.co.ncl.aspac.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jonat on 30/11/2017.
 */

public class DatabaseManager {

    private Integer mOpenCounter = 0;
    private static DatabaseManager instance;
    //private static SQLiteOpenHelper mDatabaseHelper;
    private static AspacSQLite mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(AspacSQLite helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter += 1;
        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter -= 1;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    public synchronized AspacSQLite getHelper() {
        if (mDatabaseHelper == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return mDatabaseHelper;
    }
}
