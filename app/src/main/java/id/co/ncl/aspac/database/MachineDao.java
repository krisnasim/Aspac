package id.co.ncl.aspac.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.co.ncl.aspac.model.Machine;

/**
 * Created by jonat on 30/11/2017.
 */

public class MachineDao {

    //fields required
    private SQLiteDatabase db;
    private AspacSQLite dbHelper;
    private DatabaseManager dbManager;

    public MachineDao(DatabaseManager manager) {
        this.dbManager = manager;
        this.dbHelper = this.dbManager.getHelper();
        this.db = this.dbManager.openDatabase();
    }

    public List<Machine> getAll() {
        List<Machine> machines = new ArrayList<>();
        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Machine mac = new Machine();
            mac.setMachineID(cursor.getString(1));
            mac.setBrand(cursor.getString(2));
            mac.setModel(cursor.getString(3));
            mac.setSerialNumber(cursor.getString(4));
            mac.setSalesNumber(cursor.getString(5));

            machines.add(mac);
        }
        return machines;
    }

    public List<Machine> getAllByServiceID(int serviceID) {
        List<Machine> machines = new ArrayList<>();

        return machines;
    }

    public void insert(Machine machine) {
        ContentValues values = new ContentValues();
    }

    public void update(Machine machine) {

    }

    public void delete(Machine machine) {
        long id = machine.getId();
        int count = db.delete(dbHelper.TABLE_MACHINE, dbHelper.MACHINE_COLUMN_ID + " = " + id, null);
        Log.d("deleteMachineDao", "Amount deleted rows: "+count);
    }

    public void openConnection() {
        this.db = this.dbManager.openDatabase();
    }

    public void closeConnection() {
        this.dbManager.closeDatabase();
        Log.d("databaseCon", "closing database connection..");
    }

}
