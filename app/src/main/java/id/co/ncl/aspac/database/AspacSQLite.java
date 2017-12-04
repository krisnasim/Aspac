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
    public static final String SERVICE_COLUMN_DATE_SERVICE = "date_service";
    public static final String SERVICE_COLUMN_TYPE_SERVICE = "type_service";
    //customer branch portion
    public static final String SERVICE_COLUMN_CB_ID = "cb_id";
    public static final String SERVICE_COLUMN_CB_CODE = "cb_code";
    public static final String SERVICE_COLUMN_CB_INITIAL = "cb_initial";
    public static final String SERVICE_COLUMN_CB_NAME = "cb_name";
    public static final String SERVICE_COLUMN_CB_STATUS = "cb_status";
    public static final String SERVICE_COLUMN_CB_PIC = "cb_pic";
    public static final String SERVICE_COLUMN_CB_PIC_PHONE_NUM = "cb_pic_phone_number";
    public static final String SERVICE_COLUMN_CB_PIC_EMAIL = "cb_pic_email";
    public static final String SERVICE_COLUMN_CB_KW = "cb_kw";
    public static final String SERVICE_COLUMN_CB_SLJJ = "cb_sljj";
    public static final String SERVICE_COLUMN_CB_ADDRESS = "cb_address";
    public static final String SERVICE_COLUMN_CB_REGENCY_ID = "cb_regency_id";
    public static final String SERVICE_COLUMN_CB_PROVINCE_ID = "cb_province_id";
    public static final String SERVICE_COLUMN_CB_POST_CODE = "cb_post_code";
    public static final String SERVICE_COLUMN_CB_OFFICE_PHONE_NUM = "cb_office_phone_number";
    public static final String SERVICE_COLUMN_CB_FAX = "cb_fax";
    public static final String SERVICE_COLUMN_CB_CUST_ID = "cb_customer_id";
    public static final String SERVICE_COLUMN_CB_COOR_ID = "cb_coordinator_id";
    public static final String SERVICE_COLUMN_CB_TECH_ID = "cb_teknisi_id";
    public static final String SERVICE_COLUMN_CB_SALES_ID = "cb_sales_id";
    public static final String SERVICE_COLUMN_CB_USERNAME = "cb_username";
    public static final String SERVICE_COLUMN_CB_PASSWORD = "cb_password";
    public static final String SERVICE_COLUMN_CB_REMEMBER_TOKEN = "cb_remember_token";
    public static final String SERVICE_COLUMN_CB_CREATED_AT = "cb_created_at";
    public static final String SERVICE_COLUMN_CB_UPDATED_AT = "cb_updated_at";
    //technician portion
    public static final String SERVICE_COLUMN_T_ID = "t_id";
    public static final String SERVICE_COLUMN_T_USERNAME = "t_username";
    public static final String SERVICE_COLUMN_T_NAME = "t_name";
    public static final String SERVICE_COLUMN_T_DOB = "t_dob";
    public static final String SERVICE_COLUMN_T_EMAIL = "t_email";
    public static final String SERVICE_COLUMN_T_API_TOKEN = "t_api_token";
    public static final String SERVICE_COLUMN_T_ROLE_ID = "t_role_id";
    public static final String SERVICE_COLUMN_T_BRANCH_ID = "t_branch_id";
    public static final String SERVICE_COLUMN_T_SUPERIOR_ID = "t_superior_id";
    public static final String SERVICE_COLUMN_T_CREATED_AT = "t_created_at";
    public static final String SERVICE_COLUMN_T_UPDATED_AT = "t_updated_at";

    //the machine table info
    //table name
    public static final String TABLE_MACHINE = "machine";
    //column info
    public static final String MACHINE_COLUMN_ID = "_id";
    public static final String MACHINE_COLUMN_MACHINE_ID = "machine_id";
    public static final String MACHINE_COLUMN_BRAND = "brand";
    public static final String MACHINE_COLUMN_MODEL = "model";
    public static final String MACHINE_COLUMN_SERIAL_NUM = "serial_number";
    public static final String MACHINE_COLUMN_SALES_NUM = "sales_number";
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
            + SERVICE_COLUMN_DATE_SERVICE + " text, "
            + SERVICE_COLUMN_TYPE_SERVICE + " integer, "
            + SERVICE_COLUMN_CB_ID + " integer, "
            + SERVICE_COLUMN_CB_CODE + " text, "
            + SERVICE_COLUMN_CB_INITIAL + " text, "
            + SERVICE_COLUMN_CB_NAME + " text, "
            + SERVICE_COLUMN_CB_STATUS + " text, "
            + SERVICE_COLUMN_CB_PIC + " text, "
            + SERVICE_COLUMN_CB_PIC_PHONE_NUM + " text, "
            + SERVICE_COLUMN_CB_PIC_EMAIL + " text, "
            + SERVICE_COLUMN_CB_KW + " integer, "
            + SERVICE_COLUMN_CB_SLJJ + " text, "
            + SERVICE_COLUMN_CB_ADDRESS + " text, "
            + SERVICE_COLUMN_CB_REGENCY_ID + " integer, "
            + SERVICE_COLUMN_CB_PROVINCE_ID + " integer, "
            + SERVICE_COLUMN_CB_POST_CODE + " text, "
            + SERVICE_COLUMN_CB_OFFICE_PHONE_NUM + " text, "
            + SERVICE_COLUMN_CB_FAX + " text, "
            + SERVICE_COLUMN_CB_CUST_ID + " integer, "
            + SERVICE_COLUMN_CB_COOR_ID + " integer, "
            + SERVICE_COLUMN_CB_TECH_ID + " integer, "
            + SERVICE_COLUMN_CB_SALES_ID + " integer, "
            + SERVICE_COLUMN_CB_USERNAME + " text, "
            + SERVICE_COLUMN_CB_PASSWORD + " text, "
            + SERVICE_COLUMN_CB_REMEMBER_TOKEN + " text, "
            + SERVICE_COLUMN_CB_CREATED_AT + " text, "
            + SERVICE_COLUMN_CB_UPDATED_AT + " text, "
            + SERVICE_COLUMN_T_ID + " integer, "
            + SERVICE_COLUMN_T_USERNAME + " text, "
            + SERVICE_COLUMN_T_NAME + " text, "
            + SERVICE_COLUMN_T_DOB + " text, "
            + SERVICE_COLUMN_T_EMAIL + " text, "
            + SERVICE_COLUMN_T_API_TOKEN + " text, "
            + SERVICE_COLUMN_T_ROLE_ID + " integer, "
            + SERVICE_COLUMN_T_BRANCH_ID + " integer, "
            + SERVICE_COLUMN_T_SUPERIOR_ID + " text, "
            + SERVICE_COLUMN_T_CREATED_AT + " text, "
            + SERVICE_COLUMN_T_UPDATED_AT + " text);";

    //service table sql creation
    private static final String MACHINE_TABLE_CREATE = "create table "
            + TABLE_MACHINE + "( "
            + MACHINE_COLUMN_ID + " integer primary key autoincrement, "
            + MACHINE_COLUMN_MACHINE_ID + " integer, "
            + MACHINE_COLUMN_BRAND + " text not null, "
            + MACHINE_COLUMN_MODEL + " text not null, "
            + MACHINE_COLUMN_SERIAL_NUM + " text not null, "
            + MACHINE_COLUMN_SALES_NUM + " text not null, "
            + MACHINE_COLUMN_SERVICE_ID + " integer, "
            + "foreign key (" + MACHINE_COLUMN_SERVICE_ID + ") REFERENCES " + TABLE_SERVICE + " (" + SERVICE_COLUMN_ID + "));";

    //service table sql creation
    private static final String SPAREPART_TABLE_CREATE = "create table "
            + TABLE_SPAREPART + "( "
            + SPAREPART_COLUMN_ID + " integer primary key autoincrement, "
            + SPAREPART_COLUMN_SPAREPART_ID + " text not null, "
            + SPAREPART_COLUMN_CODE + " text not null, "
            + SPAREPART_COLUMN_NAME + " text not null, "
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
