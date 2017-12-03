package id.co.ncl.aspac.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.co.ncl.aspac.model.Sparepart;

/**
 * Created by jonat on 30/11/2017.
 */

public class SparepartDao {

    //fields required
    private SQLiteDatabase db;
    private AspacSQLite dbHelper;
    private DatabaseManager dbManager;

    public SparepartDao(DatabaseManager manager) {
        this.dbManager = manager;
        this.dbHelper = this.dbManager.getHelper();
        this.db = this.dbManager.openDatabase();
    }

    public List<Sparepart> getAll() {
        List<Sparepart> spareparts = new ArrayList<>();
        Cursor cursor = db.query(dbHelper.TABLE_SPAREPART, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sparepart spa = new Sparepart();
            spa.setCode(cursor.getString(1));
            spa.setName(cursor.getString(2));

            spareparts.add(spa);
        }

        return spareparts;
    }

    public List<Sparepart> getAllByMachineID(int machineID) {
        List<Sparepart> spareparts = new ArrayList<>();

        return spareparts;
    }

    public void insert(Sparepart sparepart) {
        ContentValues values = new ContentValues();
    }

    public void update(Sparepart sparepart) {

    }

    public void delete(Sparepart sparepart) {
        long id = sparepart.getId();
        int count = db.delete(dbHelper.TABLE_SPAREPART, dbHelper.SPAREPART_COLUMN_ID + " = " + id, null);
        Log.d("deleteSparepartDao", "Amount deleted rows: "+count);
    }

    public void openConnection() {
        this.db = this.dbManager.openDatabase();
    }

    public void closeConnection() {
        this.dbManager.closeDatabase();
        Log.d("databaseCon", "closing database connection..");
    }

}
