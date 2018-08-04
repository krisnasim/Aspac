package id.co.ncl.aspac.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jonathan Simananda on 30/11/2017.
 */

public class AspacSQLite extends SQLiteOpenHelper {

    //the database info
    private static final String DATABASE_NAME = "aspac_service";
    private static final int DATABASE_VERSION = 1;

    //the service table info
    //table name
    public static final String TABLE_SERVICE = "service";
    //column info
    public static final String SERVICE_COLUMN_ID = "_id";
    public static final String SERVICE_COLUMN_NO_LPS = "no_lps";
    //customer branch portion
    public static final String SERVICE_COLUMN_CUST_ID = "cust_id";
    public static final String SERVICE_COLUMN_CUST_NAME = "cust_name";
    public static final String SERVICE_COLUMN_CB_ID = "cb_id";
    public static final String SERVICE_COLUMN_CB_NAME = "cb_name";
    public static final String SERVICE_COLUMN_CB_ADDRESS = "cb_address";
    public static final String SERVICE_COLUMN_CB_OFFICE_PHONE_NUM = "cb_office_phone_number";
    public static final String SERVICE_COLUMN_REPAIR_ID = "repair_id";
    public static final String SERVICE_COLUMN_KERUSAKAN = "kerusakan";

    //the machine table info
    //table name
    public static final String TABLE_MACHINE = "machine";
    //column info
    public static final String MACHINE_COLUMN_ID = "_id";
    public static final String MACHINE_COLUMN_TEMP_SERVICE_ID = "temp_service_id";
    public static final String MACHINE_COLUMN_NAME = "name";
    public static final String MACHINE_COLUMN_SERIAL_NUM = "serial_number";
    public static final String MACHINE_COLUMN_MACHINE_ID = "machine_id";
    //foreign key to service
    public static final String MACHINE_COLUMN_SERVICE_ID = "service_id";

    //the sparepart table info
    //table name
    public static final String TABLE_SPAREPART = "sparepart";
    //column info
    public static final String SPAREPART_COLUMN_ID = "_id";
    public static final String SPAREPART_COLUMN_SPAREPART_ID = "sparepart_id";
    public static final String SPAREPART_COLUMN_CODE = "code";
    public static final String SPAREPART_COLUMN_NAME = "name";
    //foreign key to machine
    public static final String SPAREPART_COLUMN_MACHINE_ID = "machine_id";

    //service table sql creation
    private static final String SERVICE_TABLE_CREATE = "create table "
            + TABLE_SERVICE + "( "
            + SERVICE_COLUMN_ID + " integer primary key autoincrement, "
            + SERVICE_COLUMN_NO_LPS + " text, "
            + SERVICE_COLUMN_CB_ID + " integer, "
            + SERVICE_COLUMN_CB_NAME + " text, "
            + SERVICE_COLUMN_CB_ADDRESS + " text, "
            + SERVICE_COLUMN_CB_OFFICE_PHONE_NUM + " text, "
            + SERVICE_COLUMN_REPAIR_ID + " text, "
            + SERVICE_COLUMN_KERUSAKAN + " text, "
            + SERVICE_COLUMN_CUST_ID + " integer, "
            + SERVICE_COLUMN_CUST_NAME + " string);";

    //machine table sql creation
    private static final String MACHINE_TABLE_CREATE = "create table "
            + TABLE_MACHINE + "( "
            + MACHINE_COLUMN_ID + " integer primary key autoincrement, "
            + MACHINE_COLUMN_TEMP_SERVICE_ID + " integer, "
            + MACHINE_COLUMN_NAME + " string, "
            + MACHINE_COLUMN_SERIAL_NUM + " text, "
            + MACHINE_COLUMN_MACHINE_ID + " text, "
            + MACHINE_COLUMN_SERVICE_ID + " integer, "
            + "foreign key (" + MACHINE_COLUMN_SERVICE_ID + ") REFERENCES " + TABLE_SERVICE + " (" + SERVICE_COLUMN_ID + "));";

    //sparepart table sql creation
    private static final String SPAREPART_TABLE_CREATE = "create table "
            + TABLE_SPAREPART + "( "
            + SPAREPART_COLUMN_ID + " integer primary key autoincrement, "
            + SPAREPART_COLUMN_SPAREPART_ID + " text, "
            + SPAREPART_COLUMN_CODE + " text, "
            + SPAREPART_COLUMN_NAME + " text, "
            + SPAREPART_COLUMN_MACHINE_ID + " integer, "
            + "foreign key (" + SPAREPART_COLUMN_MACHINE_ID + ") REFERENCES " + TABLE_MACHINE + " (" + MACHINE_COLUMN_ID + "));";

    public AspacSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //adding foreign key feature
        sqLiteDatabase.execSQL("PRAGMA foreign_keys='ON';");
        sqLiteDatabase.execSQL(SERVICE_TABLE_CREATE);
        sqLiteDatabase.execSQL(MACHINE_TABLE_CREATE);
        sqLiteDatabase.execSQL(SPAREPART_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //no upgrading for now. Log will tell
        Log.d("onUpgradeNotif", "No upgrade will be done. just destoy and recreate the database");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SPAREPART);
        onCreate(sqLiteDatabase);
    }

    public void delete(SQLiteDatabase sqLiteDatabase, String tableName) {
        Log.d("onDelete", "Deleting the mentioned database if exists");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
}
