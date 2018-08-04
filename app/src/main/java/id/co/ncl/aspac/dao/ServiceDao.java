package id.co.ncl.aspac.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import id.co.ncl.aspac.database.AspacSQLite;
import id.co.ncl.aspac.database.DatabaseManager;
import id.co.ncl.aspac.model.Service;

/**
 * Created by jonat on 30/11/2017.
 */

public class ServiceDao {

    //fields required
    private SQLiteDatabase db;
    private AspacSQLite dbHelper;
    private DatabaseManager dbManager;

    public ServiceDao(DatabaseManager manager) {
        this.dbManager = manager;
        this.dbHelper = this.dbManager.getHelper();
        this.db = this.dbManager.openDatabase();
    }

    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, null, null, null, null, null);
        cursor.moveToFirst();
        services = cursorToServices(cursor);

        return services;
    }

    public List<Service> getAllRoutine() {
        List<Service> services = new ArrayList<>();
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, "repair_id IS NULL", null, null, null, null);
        cursor.moveToFirst();
        services = cursorToServices(cursor);

        return services;
    }

    public List<Service> getAllRepair() {
        List<Service> services = new ArrayList<>();
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, "repair_id IS NOT NULL", null, null, null, null);
        cursor.moveToFirst();
        services = cursorToServices(cursor);

        return services;
    }

    public Service get(long serviceID) {
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, dbHelper.SERVICE_COLUMN_ID + "=" + serviceID, null, null, null, null);
        boolean result = cursor.moveToFirst();
        if(result) {

        } else {

        }
        return cursorToService(cursor);
    }

    public long insert(Service service) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.SERVICE_COLUMN_NO_LPS, service.getNoLPS());
        //CUSTOMER BRANCH
        values.put(dbHelper.SERVICE_COLUMN_CB_ID, service.getCBID());
        values.put(dbHelper.SERVICE_COLUMN_CB_NAME, service.getName());
        values.put(dbHelper.SERVICE_COLUMN_CB_ADDRESS, service.getAddress());
        values.put(dbHelper.SERVICE_COLUMN_CB_OFFICE_PHONE_NUM, service.getOfficePhoneNumber());
        values.put(dbHelper.SERVICE_COLUMN_CUST_ID, service.getCustomerID());
        values.put(dbHelper.SERVICE_COLUMN_CUST_NAME, service.getCustomerName());
        //TECHNICIAN

        long rowID = 0;

        try {
            rowID = db.insert(dbHelper.TABLE_SERVICE, null, values);
            Log.d("insertServiceDAO", "Insert successful! Row ID: "+rowID);
        }
        catch (RuntimeException exception) {
            exception.getCause();
            exception.getLocalizedMessage();
        }
        return rowID;
    }

    public long insertRoutine(Service service) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.SERVICE_COLUMN_NO_LPS, service.getNoLPS());
        //CUSTOMER BRANCH
        values.put(dbHelper.SERVICE_COLUMN_CUST_ID, service.getCustomerID());
        values.put(dbHelper.SERVICE_COLUMN_CUST_NAME, service.getCustomerName());
        values.put(dbHelper.SERVICE_COLUMN_CB_ID, service.getCBID());
        values.put(dbHelper.SERVICE_COLUMN_CB_NAME, service.getName());
        values.put(dbHelper.SERVICE_COLUMN_CB_ADDRESS, service.getAddress());
        values.put(dbHelper.SERVICE_COLUMN_CB_OFFICE_PHONE_NUM, service.getOfficePhoneNumber());
        //TECHNICIAN

        long rowID = 0;

        try {
            rowID = db.insert(dbHelper.TABLE_SERVICE, null, values);
            Log.d("insertServiceDAO", "Insert successful! Row ID: "+rowID);
        }
        catch (RuntimeException exception) {
            exception.getCause();
            exception.getLocalizedMessage();
        }
        return rowID;
    }

    public long insertRepair(Service service) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.SERVICE_COLUMN_NO_LPS, service.getNoLPS());
        //CUSTOMER BRANCH
        values.put(dbHelper.SERVICE_COLUMN_CUST_ID, service.getCustomerID());
        values.put(dbHelper.SERVICE_COLUMN_CUST_NAME, service.getCustomerName());
        values.put(dbHelper.SERVICE_COLUMN_CB_ID, service.getCBID());
        values.put(dbHelper.SERVICE_COLUMN_CB_NAME, service.getName());
        values.put(dbHelper.SERVICE_COLUMN_CB_ADDRESS, service.getAddress());
        values.put(dbHelper.SERVICE_COLUMN_CB_OFFICE_PHONE_NUM, service.getOfficePhoneNumber());
        values.put(dbHelper.SERVICE_COLUMN_REPAIR_ID, service.getCustomerName());
        values.put(dbHelper.SERVICE_COLUMN_KERUSAKAN, service.getCustomerName());
        //TECHNICIAN

        long rowID = 0;

        try {
            rowID = db.insert(dbHelper.TABLE_SERVICE, null, values);
            Log.d("insertServiceDAO", "Insert successful! Row ID: "+rowID);
        }
        catch (RuntimeException exception) {
            exception.getCause();
            exception.getLocalizedMessage();
        }
        return rowID;
    }

    public void update(Service service) {

    }

    public void delete(Service service) {
        long id = service.getId();
        int count = db.delete(dbHelper.TABLE_SERVICE, dbHelper.SERVICE_COLUMN_ID + " = " + id, null);
        Log.d("deleteServiceDao", "Amount deleted rows: "+count);
    }

    public void deleteAll() {
        db.execSQL("delete from "+ dbHelper.TABLE_SERVICE);
        Log.d("deleteServiceDao", "Table deleted");
    }

    public int getCount() {
        int count = 0;
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, null, null, null, null, null);
        count = cursor.getCount();
        Log.d("countResult", "Number of rows: "+count);

        return count;
    }

    public int getCountRoutine() {
        int count = 0;
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, "repair_id IS NULL", null, null, null, null);
        count = cursor.getCount();
        Log.d("countResult", "Number of rows: "+count);

        return count;
    }

    public int getCountRepair() {
        int count = 0;
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, "repair_id IS NOT NULL", null, null, null, null);
        count = cursor.getCount();
        Log.d("countResult", "Number of rows: "+count);

        return count;
    }

    public void openConnection() {
        this.db = this.dbManager.openDatabase();
    }

    public void closeConnection() {
        this.dbManager.closeDatabase();
        Log.d("databaseCon", "closing database connection..");
    }

    private Service cursorToService(Cursor cursor) {
        Service ser = new Service();
        ser.setId(cursor.getInt(0));
        ser.setNoLPS(cursor.getString(1));
        ser.setCustomerID(cursor.getInt(2));
        ser.setCustomerName(cursor.getString(3));
        ser.setCBID(cursor.getInt(4));
        ser.setName(cursor.getString(5));
        ser.setAddress(cursor.getString(6));
        ser.setOfficePhoneNumber(cursor.getString(7));

        return ser;
    }

    private List<Service> cursorToServices(Cursor cursor) {
        List<Service> services = new ArrayList<>();

        for (int w = 0; w < cursor.getCount(); w++) {
            Service ser = new Service();
            ser.setId(cursor.getInt(0));
            ser.setNoLPS(cursor.getString(1));
            ser.setCustomerID(cursor.getInt(2));
            ser.setCustomerName(cursor.getString(3));
            ser.setCBID(cursor.getInt(4));
            ser.setName(cursor.getString(5));
            ser.setAddress(cursor.getString(6));
            ser.setOfficePhoneNumber(cursor.getString(7));


            services.add(ser);
            cursor.moveToNext();
        }
        return services;
    }
}
