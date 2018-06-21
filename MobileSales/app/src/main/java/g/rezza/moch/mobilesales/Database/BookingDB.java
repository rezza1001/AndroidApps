package g.rezza.moch.mobilesales.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Connection.Database;

/**
 * Created by rezza on 27/02/18.
 */

public class BookingDB {

    public String event_id          = "";
    public String event_name        = "";
    public String event_image       = "";
    public String event_category    = "";
    public String event_guest       = "";
    public String event_date        = "";
    public String event_time        = "";
    public String event_venue       = "";

    public static final String TAG   = "BookingDB";

    public static final String TABLE_NAME       = "BOOKING_DB";

    public static final String FIELD_EVENTID         = "EVENTID";
    public static final String FIELD_EVENTNAME       = "EVENTNAME";
    public static final String FIELD_EVENTCATEGORY   = "EVENTCATEGORY";
    public static final String FIELD_EVENTGUEST      = "EVENTGUEST";
    public static final String FIELD_EVENTIMAGE      = "EVENTIMAGE";
    public static final String FIELD_EVENTDATE       = "EVENTDATE";
    public static final String FIELD_EVENTTIME       = "EVENTTIME";
    public static final String FIELD_EVENTVENUE      = "EVENTVENUE";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_EVENTID    + " varchar(30) NOT NULL," +
                " " + FIELD_EVENTNAME    + " varchar(100) NULL," +
                " " + FIELD_EVENTCATEGORY + " text," +
                " " + FIELD_EVENTIMAGE + " text," +
                " " + FIELD_EVENTGUEST + " text," +
                " " + FIELD_EVENTVENUE + " text," +
                " " + FIELD_EVENTDATE + " varchar(100) ," +
                " " + FIELD_EVENTTIME + " varchar(100) ," +
                "  PRIMARY KEY ("   + FIELD_EVENTID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_EVENTID,        event_id);
        contentValues.put(FIELD_EVENTNAME,      event_name);
        contentValues.put(FIELD_EVENTCATEGORY,  event_category);
        contentValues.put(FIELD_EVENTGUEST,     event_guest);
        contentValues.put(FIELD_EVENTIMAGE,     event_image);
        contentValues.put(FIELD_EVENTDATE,      event_date);
        contentValues.put(FIELD_EVENTTIME,      event_time);
        contentValues.put(FIELD_EVENTVENUE,     event_venue);
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
        clearData(context);
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

    public int update(Context context, String where){
        Log.d(TAG, "Update Data ");
        int x = 0;
        try {
            Database pDB = new Database(context);
            x = pDB.update(TABLE_NAME, createContentValues(), where);
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return x;
    }


    public void getData(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME , null);
        try {
            while (res.moveToNext()){
                this.event_id        = res.getString(res.getColumnIndex(FIELD_EVENTID));
                this.event_name      = res.getString(res.getColumnIndex(FIELD_EVENTNAME));
                this.event_category  = res.getString(res.getColumnIndex(FIELD_EVENTCATEGORY));
                this.event_guest     = res.getString(res.getColumnIndex(FIELD_EVENTGUEST));
                this.event_image     = res.getString(res.getColumnIndex(FIELD_EVENTIMAGE));
                this.event_date      = res.getString(res.getColumnIndex(FIELD_EVENTDATE));
                this.event_time      = res.getString(res.getColumnIndex(FIELD_EVENTTIME));
                this.event_venue     = res.getString(res.getColumnIndex(FIELD_EVENTVENUE));
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

    public JSONObject getCategory(){
        JSONObject joObject = null;
        try {
             joObject = new JSONObject(event_category);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return joObject;
    }

    public JSONObject getGuest(){
        JSONObject joObject = null;
        try {
            joObject = new JSONObject(event_guest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return joObject;
    }
}