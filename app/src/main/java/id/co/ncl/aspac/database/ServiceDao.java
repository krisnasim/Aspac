package id.co.ncl.aspac.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import id.co.ncl.aspac.model.Service;

/**
 * Created by jonat on 30/11/2017.
 */

public class ServiceDao {

    //fields required
    //private String[] columns;
    private SQLiteDatabase db;
    private AspacSQLite dbHelper;
    private DatabaseManager dbManager;

    public ServiceDao(DatabaseManager manager) {
        this.dbManager = manager;
        this.dbHelper = this.dbManager.getHelper();
        this.db = this.dbManager.openDatabase();
//        columns = new String[] {
//                                dbHelper.SERVICE_COLUMN_DATE_SERVICE,
//                                dbHelper.SERVICE_COLUMN_TYPE_SERVICE,
//                                dbHelper.SERVICE_COLUMN_CB_ID,
//                                dbHelper.SERVICE_COLUMN_CB_CODE,
//                                dbHelper.SERVICE_COLUMN_CB_INITIAL,
//                                dbHelper.SERVICE_COLUMN_CB_NAME,
//                                dbHelper.SERVICE_COLUMN_CB_STATUS,
//                                dbHelper.SERVICE_COLUMN_CB_PIC,
//                                dbHelper.SERVICE_COLUMN_CB_PIC_PHONE_NUM,
//                                dbHelper.SERVICE_COLUMN_CB_PIC_EMAIL,
//                                dbHelper.SERVICE_COLUMN_CB_KW,
//                                dbHelper.SERVICE_COLUMN_CB_SLJJ,
//                                dbHelper.SERVICE_COLUMN_CB_ADDRESS,
//                                dbHelper.SERVICE_COLUMN_CB_REGENCY_ID,
//                                dbHelper.SERVICE_COLUMN_CB_PROVINCE_ID,
//                                dbHelper.SERVICE_COLUMN_CB_POST_CODE,
//                                dbHelper.SERVICE_COLUMN_CB_OFFICE_PHONE_NUM,
//                                dbHelper.SERVICE_COLUMN_CB_FAX,
//                                dbHelper.SERVICE_COLUMN_CB_CUST_ID,
//                                dbHelper.SERVICE_COLUMN_CB_COOR_ID,
//                                dbHelper.SERVICE_COLUMN_CB_TECH_ID,
//                                dbHelper.SERVICE_COLUMN_CB_SALES_ID,
//                                dbHelper.SERVICE_COLUMN_CB_USERNAME,
//                                dbHelper.SERVICE_COLUMN_CB_PASSWORD,
//                                dbHelper.SERVICE_COLUMN_CB_REMEMBER_TOKEN,
//                                dbHelper.SERVICE_COLUMN_CB_CREATED_AT,
//                                dbHelper.SERVICE_COLUMN_CB_UPDATED_AT,
//                                dbHelper.SERVICE_COLUMN_T_ID,
//                                dbHelper.SERVICE_COLUMN_T_USERNAME,
//                                dbHelper.SERVICE_COLUMN_T_NAME,
//                                dbHelper.SERVICE_COLUMN_T_DOB,
//                                dbHelper.SERVICE_COLUMN_T_EMAIL,
//                                dbHelper.SERVICE_COLUMN_T_API_TOKEN,
//                                dbHelper.SERVICE_COLUMN_T_ROLE_ID,
//                                dbHelper.SERVICE_COLUMN_T_BRANCH_ID,
//                                dbHelper.SERVICE_COLUMN_T_SUPERIOR_ID,
//                                dbHelper.SERVICE_COLUMN_T_CREATED_AT,
//                                dbHelper.SERVICE_COLUMN_T_UPDATED_AT};
    }

    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        Cursor cursor = db.query(dbHelper.TABLE_SERVICE, null, null, null, null, null, null);
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
        //values.put(dbHelper.SERVICE_COLUMN_ID, "");
        values.put(dbHelper.SERVICE_COLUMN_DATE_SERVICE, service.getDateService());
        values.put(dbHelper.SERVICE_COLUMN_TYPE_SERVICE, service.getTypeService());
        values.put(dbHelper.SERVICE_COLUMN_NO_LPS, service.getNoLPS());
        //CUSTOMER BRANCH
        values.put(dbHelper.SERVICE_COLUMN_CB_ID, service.getCBID());
        values.put(dbHelper.SERVICE_COLUMN_CB_CODE, service.getCode());
        values.put(dbHelper.SERVICE_COLUMN_CB_INITIAL, service.getInitial());
        values.put(dbHelper.SERVICE_COLUMN_CB_NAME, service.getName());
        values.put(dbHelper.SERVICE_COLUMN_CB_STATUS, service.getStatus());
        values.put(dbHelper.SERVICE_COLUMN_CB_PIC, service.getPIC());
        values.put(dbHelper.SERVICE_COLUMN_CB_PIC_PHONE_NUM, service.getPICPhoneNumber());
        values.put(dbHelper.SERVICE_COLUMN_CB_PIC_EMAIL, service.getPICEmail());
        values.put(dbHelper.SERVICE_COLUMN_CB_KW, service.getKW());
        values.put(dbHelper.SERVICE_COLUMN_CB_SLJJ, service.getSLJJ());
        values.put(dbHelper.SERVICE_COLUMN_CB_ADDRESS, service.getAddress());
        values.put(dbHelper.SERVICE_COLUMN_CB_REGENCY_ID, service.getRegencyID());
        values.put(dbHelper.SERVICE_COLUMN_CB_PROVINCE_ID, service.getProvinceID());
        values.put(dbHelper.SERVICE_COLUMN_CB_POST_CODE, service.getPostCode());
        values.put(dbHelper.SERVICE_COLUMN_CB_OFFICE_PHONE_NUM, service.getOfficePhoneNumber());
        values.put(dbHelper.SERVICE_COLUMN_CB_FAX, service.getFax());
        values.put(dbHelper.SERVICE_COLUMN_CB_CUST_ID, service.getCustomerID());
        values.put(dbHelper.SERVICE_COLUMN_CB_COOR_ID, service.getCoordinatorID());
        values.put(dbHelper.SERVICE_COLUMN_CB_TECH_ID, service.getTeknisiID());
        values.put(dbHelper.SERVICE_COLUMN_CB_SALES_ID, service.getSalesID());
        values.put(dbHelper.SERVICE_COLUMN_CB_USERNAME, service.getUsername());
        values.put(dbHelper.SERVICE_COLUMN_CB_PASSWORD, service.getPassword());
        values.put(dbHelper.SERVICE_COLUMN_CB_REMEMBER_TOKEN, service.getRememberToken());
        values.put(dbHelper.SERVICE_COLUMN_CB_CREATED_AT, service.getCreatedAt());
        values.put(dbHelper.SERVICE_COLUMN_CB_UPDATED_AT, service.getUpdatedAt());
        //TECHNICIAN
        values.put(dbHelper.SERVICE_COLUMN_T_ID, service.getTID());
        values.put(dbHelper.SERVICE_COLUMN_T_USERNAME, service.getTusername());
        values.put(dbHelper.SERVICE_COLUMN_T_NAME, service.getTname());
        values.put(dbHelper.SERVICE_COLUMN_T_DOB, service.getDob());
        values.put(dbHelper.SERVICE_COLUMN_T_EMAIL, service.getEmail());
        values.put(dbHelper.SERVICE_COLUMN_T_API_TOKEN, service.getApiToken());
        values.put(dbHelper.SERVICE_COLUMN_T_ROLE_ID, service.getRoleID());
        values.put(dbHelper.SERVICE_COLUMN_T_BRANCH_ID, service.getBranchID());
        values.put(dbHelper.SERVICE_COLUMN_T_SUPERIOR_ID, service.getSuperiorID());
        values.put(dbHelper.SERVICE_COLUMN_T_CREATED_AT, service.getTcreatedAt());
        values.put(dbHelper.SERVICE_COLUMN_T_UPDATED_AT, service.getTupdatedAt());

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
        ser.setDateService(cursor.getString(1));
        ser.setTypeService(cursor.getInt(2));
        ser.setNoLPS(cursor.getString(3));
        ser.setCBID(cursor.getInt(4));
        ser.setCode(cursor.getString(5));
        ser.setInitial(cursor.getString(6));
        ser.setName(cursor.getString(7));
        ser.setStatus(cursor.getString(8));
        ser.setPIC(cursor.getString(9));
        ser.setPICPhoneNumber(cursor.getString(10));
        ser.setPICEmail(cursor.getString(11));
        ser.setKW(cursor.getInt(12));
        ser.setSLJJ(cursor.getString(13));
        ser.setAddress(cursor.getString(14));
        ser.setRegencyID(cursor.getInt(15));
        ser.setProvinceID(cursor.getInt(16));
        ser.setPostCode(cursor.getString(17));
        ser.setOfficePhoneNumber(cursor.getString(18));
        ser.setFax(cursor.getString(19));
        ser.setCustomerID(cursor.getInt(20));
        ser.setCoordinatorID(cursor.getInt(21));
        ser.setTeknisiID(cursor.getInt(22));
        ser.setSalesID(cursor.getInt(23));
        ser.setUsername(cursor.getString(24));
        ser.setPassword(cursor.getString(25));
        ser.setRememberToken(cursor.getString(26));
        ser.setCreatedAt(cursor.getString(27));
        ser.setUpdatedAt(cursor.getString(28));
        ser.setTID(cursor.getInt(29));
        ser.setTusername(cursor.getString(30));
        ser.setTname(cursor.getString(31));
        ser.setDob(cursor.getString(32));
        ser.setEmail(cursor.getString(33));
        ser.setApiToken(cursor.getString(34));
        ser.setRoleID(cursor.getInt(35));
        ser.setBranchID(cursor.getInt(36));
        ser.setSuperiorID(cursor.getString(37));
        ser.setTcreatedAt(cursor.getString(38));
        ser.setTupdatedAt(cursor.getString(39));

        return ser;
    }

    private List<Service> cursorToServices(Cursor cursor) {
        List<Service> services = new ArrayList<>();

        for (int w = 0; w < cursor.getCount(); w++) {
            Service ser = new Service();
            ser.setId(cursor.getInt(0));
            ser.setDateService(cursor.getString(1));
            ser.setTypeService(cursor.getInt(2));
            ser.setNoLPS(cursor.getString(3));
            ser.setCBID(cursor.getInt(4));
            ser.setCode(cursor.getString(5));
            ser.setInitial(cursor.getString(6));
            ser.setName(cursor.getString(7));
            ser.setStatus(cursor.getString(8));
            ser.setPIC(cursor.getString(9));
            ser.setPICPhoneNumber(cursor.getString(10));
            ser.setPICEmail(cursor.getString(11));
            ser.setKW(cursor.getInt(12));
            ser.setSLJJ(cursor.getString(13));
            ser.setAddress(cursor.getString(14));
            ser.setRegencyID(cursor.getInt(15));
            ser.setProvinceID(cursor.getInt(16));
            ser.setPostCode(cursor.getString(17));
            ser.setOfficePhoneNumber(cursor.getString(18));
            ser.setFax(cursor.getString(19));
            ser.setCustomerID(cursor.getInt(20));
            ser.setCoordinatorID(cursor.getInt(21));
            ser.setTeknisiID(cursor.getInt(22));
            ser.setSalesID(cursor.getInt(23));
            ser.setUsername(cursor.getString(24));
            ser.setPassword(cursor.getString(25));
            ser.setRememberToken(cursor.getString(26));
            ser.setCreatedAt(cursor.getString(27));
            ser.setUpdatedAt(cursor.getString(28));
            ser.setTID(cursor.getInt(29));
            ser.setTusername(cursor.getString(30));
            ser.setTname(cursor.getString(31));
            ser.setDob(cursor.getString(32));
            ser.setEmail(cursor.getString(33));
            ser.setApiToken(cursor.getString(34));
            ser.setRoleID(cursor.getInt(35));
            ser.setBranchID(cursor.getInt(36));
            ser.setSuperiorID(cursor.getString(37));
            ser.setTcreatedAt(cursor.getString(38));
            ser.setTupdatedAt(cursor.getString(39));

            services.add(ser);
            cursor.moveToNext();
        }

        return services;
    }
}
