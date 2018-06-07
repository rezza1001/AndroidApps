package g.rezza.moch.kiostixv3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.connection.Database;
import g.rezza.moch.kiostixv3.lib.Parse;

/**
 * Created by rezza on 15/03/18.
 */

public class CustomerDB {

    public static final String TAG          = "ScheduleHolder";
    public static final String TABLE_NAME   = "CUSTOMER_DB";

    public static final String FIELD_ID         = "ID";
    public static final String FIELD_NAME       = "NAME";
    public static final String FIELD_EMAIL      = "EMAIL";
    public static final String FIELD_PHONE      = "PHONE";
    public static final String FIELD_DOB        = "DOB";
    public static final String FIELD_IDENTITY   = "IDENTITY";
    public static final String FIELD_TOKEN      = "TOKEN";
    public static final String FIELD_PASSWORD   = "PASSWORD";
    public static final String FIELD_GENDER     = "GENDER";
    public static final String FIELD_POB        = "POB";
    public static final String FIELD_ID_TYPE    = "ID_TYPE";
    public static final String FIELD_COUNTRY    = "COUNTRY";
    public static final String FIELD_PROVINCE   = "PROVINCE";
    public static final String FIELD_CITY       = "CITY";
    public static final String FIELD_ADDRESS    = "ADDRESS";
    public static final String FIELD_ZIP_CODE   = "ZIP_CODE";

    public String id = "";
    public String name;
    public String email;
    public String phone;
    public String dob;
    public String token = "";
    public String identity;
    public String password;
    public String gender;
    public String pob;
    public String id_type;
    public String country;
    public String province;
    public String city;
    public String address;
    public String zip_code;

    public boolean selected = false;


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID + " varchar(30) NOT NULL," +
                " " + FIELD_NAME + " varchar(20) NOT NULL," +
                " " + FIELD_EMAIL + " varchar(100)  NULL," +
                " " + FIELD_PHONE + " varchar(50)  NULL," +
                " " + FIELD_DOB + " varchar(50)  NULL," +
                " " + FIELD_IDENTITY + " varchar(50)  NULL," +
                " " + FIELD_PASSWORD + " varchar(50)  NULL," +
                " " + FIELD_GENDER + " varchar(20)  NULL," +
                " " + FIELD_POB + " varchar(50)  NULL," +
                " " + FIELD_ID_TYPE + " varchar(50)  NULL," +
                " " + FIELD_COUNTRY + " varchar(50)  NULL," +
                " " + FIELD_PROVINCE + " varchar(50)  NULL," +
                " " + FIELD_CITY + " varchar(50)  NULL," +
                " " + FIELD_ZIP_CODE + " varchar(20)  NULL," +
                " " + FIELD_ADDRESS + " text  NULL," +
                " " + FIELD_TOKEN + " text  NULL," +
                "  PRIMARY KEY (" + FIELD_ID + "))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID,     id);
        contentValues.put(FIELD_NAME,   name);
        contentValues.put(FIELD_EMAIL,  email);
        contentValues.put(FIELD_PHONE,  phone);
        contentValues.put(FIELD_DOB,    dob);
        contentValues.put(FIELD_TOKEN,  token);
        contentValues.put(FIELD_IDENTITY,  identity);
        contentValues.put(FIELD_PASSWORD,  password);
        contentValues.put(FIELD_GENDER,  gender);
        contentValues.put(FIELD_POB,  pob);
        contentValues.put(FIELD_ID_TYPE,  id_type);
        contentValues.put(FIELD_COUNTRY,  country);
        contentValues.put(FIELD_PROVINCE,  province);
        contentValues.put(FIELD_CITY,  city);
        contentValues.put(FIELD_ZIP_CODE,  zip_code);
        contentValues.put(FIELD_ADDRESS,  address);
        return contentValues;
    }

    public void clearData(Context context){
        try {
            Database pDB = new Database(context);
            pDB.delete(TABLE_NAME, "");
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }

    public boolean insert(Context context){
        Log.d(TAG, "Insert Data ");
        boolean x = false;
        try {
            Database pDB = new Database(context);
            x = pDB.insert(TABLE_NAME, createContentValues());
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        return x;
    }

    public void getData(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        try {
            while (res.moveToNext()){
                this.id       = res.getString(res.getColumnIndex(FIELD_ID));
                this.name     = res.getString(res.getColumnIndex(FIELD_NAME));
                this.email    = res.getString(res.getColumnIndex(FIELD_EMAIL));
                this.phone    = res.getString(res.getColumnIndex(FIELD_PHONE));
                this.dob      = res.getString(res.getColumnIndex(FIELD_DOB));
                this.token    = res.getString(res.getColumnIndex(FIELD_TOKEN));
                this.identity = res.getString(res.getColumnIndex(FIELD_IDENTITY));
                this.password = res.getString(res.getColumnIndex(FIELD_PASSWORD));
                this.gender   = res.getString(res.getColumnIndex(FIELD_GENDER));
                this.pob      = res.getString(res.getColumnIndex(FIELD_POB));
                this.id_type  = res.getString(res.getColumnIndex(FIELD_ID_TYPE));
                this.country  = res.getString(res.getColumnIndex(FIELD_COUNTRY));
                this.province = res.getString(res.getColumnIndex(FIELD_PROVINCE));
                this.city     = res.getString(res.getColumnIndex(FIELD_CITY));
                this.zip_code = res.getString(res.getColumnIndex(FIELD_ZIP_CODE));
                this.address  = res.getString(res.getColumnIndex(FIELD_ADDRESS));
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }

    }



}
