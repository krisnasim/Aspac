package id.co.ncl.aspac.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.co.ncl.aspac.database.AspacSQLite;
import id.co.ncl.aspac.database.DatabaseManager;
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
        for (int w = 0; w < cursor.getCount(); w++) {
            Machine mac = new Machine();
            mac.setId(cursor.getInt(0));
            mac.setTempServiceID(cursor.getInt(1));
            mac.setName(cursor.getString(2));
            mac.setSerialNumber(cursor.getString(3));

            machines.add(mac);
            cursor.moveToNext();
        }
        return machines;
    }

    public List<Machine> getAllByServiceID(String serviceID) {
        List<Machine> machines = new ArrayList<>();

        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, null, dbHelper.MACHINE_COLUMN_SERVICE_ID + "='" + serviceID + "'", null, null, null, null);
        cursor.moveToFirst();
        for (int w = 0; w < cursor.getCount(); w++) {
            Machine mac = new Machine();
            mac.setId(cursor.getInt(0));
            mac.setTempServiceID(cursor.getInt(1));
            mac.setName(cursor.getString(2));
            mac.setSerialNumber(cursor.getString(3));
            mac.setMachineID(cursor.getString(4));

            machines.add(mac);
            cursor.moveToNext();
        }
        return machines;
    }

    public List<Machine> getAllByNoLPS(String serviceID) {
        List<Machine> machines = new ArrayList<>();

        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, null, dbHelper.MACHINE_COLUMN_SERVICE_NO_LPS + "='" + serviceID + "'", null, null, null, null);
        cursor.moveToFirst();
        for (int w = 0; w < cursor.getCount(); w++) {
            Machine mac = new Machine();
            mac.setId(cursor.getInt(0));
            mac.setTempServiceID(cursor.getInt(1));
            mac.setName(cursor.getString(2));
            mac.setSerialNumber(cursor.getString(3));
            mac.setMachineID(cursor.getString(4));
            mac.setServiceID(cursor.getString(5));

            machines.add(mac);
            cursor.moveToNext();
        }
        return machines;
    }

//    public List<Integer> getAllRoutineIDs() {
//        List<Integer> machinesID = new ArrayList<>();
//        String[] columns = {"_id"};
//        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, columns, "_id IS NOT NULL", null, null, null, null);
//        cursor.moveToFirst();
//        for (int w = 0; w < cursor.getCount(); w++) {
//            Machine mac = new Machine();
//            mac.setId(cursor.getInt(0));
//            mac.setTempServiceID(cursor.getInt(1));
//            mac.setName(cursor.getString(2));
//            mac.setSerialNumber(cursor.getString(3));
//
//            machines.add(mac);
//            cursor.moveToNext();
//        }
//        return machinesID;
//    }

    public List<Machine> getAllRepair(long serviceID) {
        List<Machine> machines = new ArrayList<>();

        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, null, dbHelper.MACHINE_COLUMN_MACHINE_ID + " IS NOT NULL", null, null, null, null);
        cursor.moveToFirst();
        for (int w = 0; w < cursor.getCount(); w++) {
            Machine mac = new Machine();
            mac.setId(cursor.getInt(0));
            mac.setTempServiceID(cursor.getInt(1));
            mac.setName(cursor.getString(2));
            mac.setSerialNumber(cursor.getString(3));
            mac.setMachineID(cursor.getString(4));

            machines.add(mac);
            cursor.moveToNext();
        }
        return machines;
    }

//    public Machine get(String machineID) {
//        Machine mac = new Machine();
//
//        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, null, dbHelper.MACHINE_COLUMN_MACHINE_ID + "=" + machineID, null, null, null, null);
//        cursor.moveToFirst();
//        Log.d("result", String.valueOf(cursor.getCount()));
//        mac.setId(cursor.getInt(0));
//        mac.setMachineID(cursor.getString(1));
//        mac.setTempServiceID(cursor.getInt(2));
//        mac.setBrand(cursor.getString(3));
//        mac.setModel(cursor.getString(4));
//        mac.setSerialNumber(cursor.getString(5));
//        mac.setSalesNumber(cursor.getString(6));
//
//        return mac;
//    }

    public Machine getByTempID(String machineID) {
        Machine mac = new Machine();

        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, null, dbHelper.MACHINE_COLUMN_TEMP_SERVICE_ID + "=" + machineID, null, null, null, null);
        cursor.moveToFirst();
        Log.d("result", String.valueOf(cursor.getCount()));
        mac.setId(cursor.getInt(0));
        mac.setTempServiceID(cursor.getInt(1));
        mac.setName(cursor.getString(2));
        mac.setSerialNumber(cursor.getString(3));

        return mac;
    }

    public Machine getByRepairID(String machineID) {
        Machine mac = new Machine();

        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, null, dbHelper.MACHINE_COLUMN_SERVICE_ID + "=" + machineID, null, null, null, null);
        cursor.moveToFirst();
        Log.d("result", String.valueOf(cursor.getCount()));
        mac.setId(cursor.getInt(0));
        mac.setTempServiceID(cursor.getInt(1));
        mac.setName(cursor.getString(2));
        mac.setSerialNumber(cursor.getString(3));

        return mac;
    }

    public Machine getByMachineID(String machineID) {
        Machine mac = new Machine();

        Cursor cursor = db.query(dbHelper.TABLE_MACHINE, null, dbHelper.MACHINE_COLUMN_MACHINE_ID + "=" + machineID, null, null, null, null);
        cursor.moveToFirst();
        Log.d("result", String.valueOf(cursor.getCount()));
        mac.setId(cursor.getInt(0));
        mac.setName(cursor.getString(2));
        mac.setSerialNumber(cursor.getString(3));
        mac.setMachineID(cursor.getString(4));

        return mac;
    }

    public long insert(Machine machine) {
        ContentValues values = new ContentValues();
        //values.put(dbHelper.MACHINE_COLUMN_MACHINE_ID, machine.getMachineID());
        values.put(dbHelper.MACHINE_COLUMN_TEMP_SERVICE_ID, machine.getTempServiceID());
        //values.put(dbHelper.MACHINE_COLUMN_BRAND, machine.getBrand());
        values.put(dbHelper.MACHINE_COLUMN_NAME, machine.getName());
        values.put(dbHelper.MACHINE_COLUMN_SERIAL_NUM, machine.getSerialNumber());
        //values.put(dbHelper.MACHINE_COLUMN_SALES_NUM, machine.getSalesNumber());
        values.put(dbHelper.MACHINE_COLUMN_SERVICE_ID, machine.getTempServiceID());
        Log.d("INSERTLPS", machine.getNoLPS());
        values.put(dbHelper.MACHINE_COLUMN_SERVICE_NO_LPS, machine.getNoLPS());

        long rowID = 0;

        try {
            rowID = db.insert(dbHelper.TABLE_MACHINE, null, values);
            Log.d("insertMachineDAO", "Insert successful! Row ID: "+rowID);
        }
        catch (RuntimeException exception) {
            exception.getCause();
            exception.getLocalizedMessage();
        }
        return machine.getTempServiceID();
    }

    public long insertRepairMachine(Machine machine, int service_id) {
        ContentValues values = new ContentValues();
        //values.put(dbHelper.MACHINE_COLUMN_TEMP_SERVICE_ID, machine.getTempServiceID());
        //values.put(dbHelper.MACHINE_COLUMN_BRAND, machine.getBrand());
        values.put(dbHelper.MACHINE_COLUMN_NAME, machine.getName());
        values.put(dbHelper.MACHINE_COLUMN_SERIAL_NUM, machine.getSerialNumber());
        //values.put(dbHelper.MACHINE_COLUMN_SALES_NUM, machine.getSalesNumber());
        values.put(dbHelper.MACHINE_COLUMN_SERVICE_ID, machine.getNoLPS());
        Log.d("INSERTREPAIR", machine.getMachineID());
        values.put(dbHelper.MACHINE_COLUMN_MACHINE_ID, machine.getMachineID());
        values.put(dbHelper.MACHINE_COLUMN_SERVICE_ID, service_id);
        Log.d("INSERTLPS", machine.getNoLPS());
        values.put(dbHelper.MACHINE_COLUMN_SERVICE_NO_LPS, machine.getNoLPS());

        long rowID = 0;

        try {
            rowID = db.insert(dbHelper.TABLE_MACHINE, null, values);
            Log.d("insertMachineDAO", "Insert successful! Row ID: "+rowID);
        }
        catch (RuntimeException exception) {
            exception.getCause();
            exception.getLocalizedMessage();
        }
        return service_id;
    }

    public void update(Machine machine) {

    }

    public void delete(Machine machine) {
        long id = machine.getId();
        int count = db.delete(dbHelper.TABLE_MACHINE, dbHelper.MACHINE_COLUMN_ID + " = " + id, null);
        Log.d("deleteMachineDao", "Amount deleted rows: "+count);
    }

    public void deleteByID(int id) {
        int count = db.delete(dbHelper.TABLE_MACHINE, dbHelper.MACHINE_COLUMN_ID + " = " + id, null);
        Log.d("deleteMachineDao", "Amount deleted rows: "+count);
        //this.dbManager.closeDatabase();
        //Log.d("databaseCon", "closing database connection..");
    }

    public void deleteByNoLPS(String noLPS) {
        int count = db.delete(dbHelper.TABLE_MACHINE, dbHelper.MACHINE_COLUMN_SERVICE_NO_LPS + " = '" + noLPS + "'", null);
        Log.d("deleteMachineDao", "Amount deleted rows: "+count);
    }

    public void deleteAll() {
        dbHelper.delete(db, "machine");
        dbHelper.createMachineTable(db);
        Log.d("deleteMachineDao", "Table deleted");
    }

    public void openConnection() {
        this.db = this.dbManager.openDatabase();
    }

    public void closeConnection() {
        this.dbManager.closeDatabase();
        Log.d("databaseCon", "closing database connection..");
    }

}
