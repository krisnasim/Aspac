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
import id.co.ncl.aspac.model.Machine;
import id.co.ncl.aspac.model.Service;

/**
 * Created by jonat on 30/11/2017.
 */

public class ServiceDao {

    //fields required
    private SQLiteDatabase db;
    private AspacSQLite dbHelper;
    private DatabaseManager dbManager;
    private MachineDao machineDao;

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

    public List<Integer> getAllRoutineIDs() {
        List<Integer> servicesID = new ArrayList<>();
        String[] columns = {"_id"};
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, columns, "repair_id IS NULL", null, null, null, null);
        cursor.moveToFirst();
        for (int w = 0; w < cursor.getCount(); w++) {
            servicesID.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        return servicesID;
    }

    public List<Integer> getAllRepairIDs() {
        List<Integer> servicesID = new ArrayList<>();
        String[] columns = {"_id"};
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, columns, "repair_id IS NOT NULL", null, null, null, null);
        cursor.moveToFirst();
        for (int w = 0; w < cursor.getCount(); w++) {
            servicesID.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        return servicesID;
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
        values.put(dbHelper.SERVICE_COLUMN_REPAIR_ID, service.getRepairdID());
        values.put(dbHelper.SERVICE_COLUMN_KERUSAKAN, service.getKerusakan());

        Log.d("insertRepair", service.getNoLPS());
        Log.d("insertRepair", String.valueOf(service.getCustomerID()));
        Log.d("insertRepair", service.getCustomerName());
        Log.d("insertRepair", String.valueOf(service.getCBID()));
        Log.d("insertRepair", service.getName());
        Log.d("insertRepair", service.getAddress());
        Log.d("insertRepair", service.getOfficePhoneNumber());
        Log.d("insertRepair", String.valueOf(service.getRepairdID()));
        Log.d("insertRepair", service.getKerusakan());
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
        dbHelper.delete(db, "service");
        dbHelper.delete(db, "machine");
        dbHelper.createServiceTable(db);
        dbHelper.createMachineTable(db);
        //db.execSQL("delete from "+ dbHelper.TABLE_SERVICE);
        Log.d("deleteServiceDao", "Table deleted");
    }

    public void deleteAllRoutine() {
        //delete all machine first
        machineDao = new MachineDao(dbManager);
        List<Service> services = getAllRoutine();
        Log.d("dataSize", "the number of services for routine item are: "+ String.valueOf(services.size()));
        for (int a=0; a<services.size(); a++) {
            machineDao.deleteByID(services.get(a).getId());
        }
        //then, check if all machine is deleted. in case repair cases are not opened yet
        List<Machine> machines = machineDao.getAll();
        Log.d("dataSize", "the number of machine for routine item are: "+ String.valueOf(machines.size()));
        if(machines.size() <= 0) {
            Log.d("machineFlush", "flushing all machines and tables");
            machineDao.deleteAll();
        }
        machineDao.closeConnection();
        //then, delete the services
        db.execSQL("delete from "+ dbHelper.TABLE_SERVICE + " where " + dbHelper.SERVICE_COLUMN_REPAIR_ID + " IS NULL");
        //another check to see if all services is deleted, in case no repair case opened yet
        List<Integer> serviceIDs = getAllRoutineIDs();
        if(serviceIDs.size() <= 0) {
            Log.d("serviceFlush", "flushing all services and tables");
            dbHelper.delete(db, "service");
            dbHelper.createServiceTable(db);
        }
        Log.d("deleteServiceDao", "All Routine data deleted");
    }

    public void deleteAllRepair() {
        //delete all machine first
        machineDao = new MachineDao(dbManager);
        List<Service> services = getAllRepair();
        Log.d("dataSize", "the number of services for repair item are: "+ String.valueOf(services.size()));
        List<Machine> oldMachines = machineDao.getAll();
        Log.d("dataSize", "the number of machine for repair item before deletion are: "+ String.valueOf(oldMachines.size()));
        for (int a=0; a<services.size(); a++) {
            machineDao.deleteByID(services.get(a).getId());
        }
        //then, check if all machine is deleted. in case routine cases are not opened yet
        List<Machine> machines = machineDao.getAll();
        Log.d("dataSize", "the number of machine for repair item are: "+ String.valueOf(machines.size()));
        if(machines.size() <= 0) {
            machineDao.deleteAll();
        }
        machineDao.closeConnection();
        //then, delete the services
        db.execSQL("delete from "+ dbHelper.TABLE_SERVICE + " where " + dbHelper.SERVICE_COLUMN_REPAIR_ID + " IS NOT NULL");
        //another check to see if all services is deleted, in case no routine case opened yet
        List<Integer> serviceIDs = getAllRepairIDs();
        if(serviceIDs.size() <= 0) {
            Log.d("serviceFlush", "flushing all services and tables");
            dbHelper.delete(db, "service");
            dbHelper.createServiceTable(db);
        }
        Log.d("deleteServiceDao", "All Repair data deleted");
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
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null,  dbHelper.SERVICE_COLUMN_REPAIR_ID + " IS NULL", null, null, null, null);
        count = cursor.getCount();
        Log.d("countResult", "Number of rows: "+count);

        return count;
    }

    public int getCountRepair() {
        int count = 0;
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, dbHelper.SERVICE_COLUMN_REPAIR_ID + " IS NOT NULL", null, null, null, null);
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
        ser.setCBID(cursor.getInt(2));
        ser.setName(cursor.getString(3));
        ser.setAddress(cursor.getString(4));
        ser.setOfficePhoneNumber(cursor.getString(5));
        ser.setRepairdID(cursor.getInt(6));
        ser.setKerusakan(cursor.getString(7));
        ser.setCustomerID(cursor.getInt(8));
        ser.setCustomerName(cursor.getString(9));

        return ser;
    }

    private List<Service> cursorToServices(Cursor cursor) {
        List<Service> services = new ArrayList<>();

        for (int w = 0; w < cursor.getCount(); w++) {
            Service ser = new Service();
//            ser.setId(cursor.getInt(0));
//            ser.setNoLPS(cursor.getString(1));
//            ser.setCustomerID(cursor.getInt(2));
//            ser.setCustomerName(cursor.getString(3));
//            ser.setCBID(cursor.getInt(4));
//            ser.setName(cursor.getString(5));
//            ser.setAddress(cursor.getString(6));
//            ser.setOfficePhoneNumber(cursor.getString(7));
            ser.setId(cursor.getInt(0));
            ser.setNoLPS(cursor.getString(1));
            ser.setCBID(cursor.getInt(2));
            ser.setName(cursor.getString(3));
            ser.setAddress(cursor.getString(4));
            ser.setOfficePhoneNumber(cursor.getString(5));
            ser.setRepairdID(cursor.getInt(6));
            ser.setKerusakan(cursor.getString(7));
            ser.setCustomerID(cursor.getInt(8));
            ser.setCustomerName(cursor.getString(9));


            services.add(ser);
            cursor.moveToNext();
        }
        return services;
    }
}
