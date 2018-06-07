package g.rezza.moch.kiostixv3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.connection.Database;

/**
 * Created by rezza on 01/03/18.
 */

public class MyTixDB {

    public String trans_id          = "";
    public String event_id          = "";
    public String event_name;
    public String event_venue;
    public String event_time;
    public String event_date;
    public String event_price;
    public String event_status;
    public String event_status_desc;
    public String event_expired_date;
    public String event_expired_time;

    public static final String TAG   = "MyTixDB";

    public static final String TABLE_NAME       = "MYTIX_DB";

    public static final String FIELD_TRANS_ID        = "TRANS_ID";
    public static final String FIELD_EVENTID         = "EVENTID";
    public static final String FIELD_EVENTNAME       = "EVENTNAME";
    public static final String FIELD_EVENTVENUE      = "EVENTVENUE";
    public static final String FIELD_EVENTDATE       = "EVENTDATE";
    public static final String FIELD_EVENTTIME       = "EVENTTIME";
    public static final String FIELD_EVENTPRICE      = "EVENTPRICE";
    public static final String FIELD_EVENTSTATUS      = "EVENTSTATUS";
    public static final String FIELD_STATUS_DESC      = "STATUS_DESC";
    public static final String FIELD_EXP_DATE      = "EXP_DATE";
    public static final String FIELD_EXP_TIME      = "EXP_TIME";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_TRANS_ID    + " varchar(30) NOT NULL," +
                " " + FIELD_EVENTID    + " varchar(30) NOT NULL," +
                " " + FIELD_EVENTNAME  + " varchar(100) NULL," +
                " " + FIELD_EVENTVENUE + " text," +
                " " + FIELD_EVENTDATE + " varchar(100) ," +
                " " + FIELD_EVENTTIME + " varchar(100) ," +
                " " + FIELD_EVENTPRICE + " varchar(20) ," +
                " " + FIELD_EVENTSTATUS + " varchar(2) ," +
                " " + FIELD_STATUS_DESC + " varchar(100) ," +
                " " + FIELD_EXP_DATE + " varchar(30) ," +
                " " + FIELD_EXP_TIME + " varchar(30) ," +
                "  PRIMARY KEY ("   + FIELD_TRANS_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_TRANS_ID,        trans_id);
        contentValues.put(FIELD_EVENTID,        event_id);
        contentValues.put(FIELD_EVENTNAME,      event_name);
        contentValues.put(FIELD_EVENTDATE,      event_date);
        contentValues.put(FIELD_EVENTTIME,      event_time);
        contentValues.put(FIELD_EVENTVENUE,     event_venue);
        contentValues.put(FIELD_EVENTPRICE,     event_price);
        contentValues.put(FIELD_EVENTSTATUS,    event_status);
        contentValues.put(FIELD_STATUS_DESC,    event_status_desc);
        contentValues.put(FIELD_EXP_DATE,       event_expired_date);
        contentValues.put(FIELD_EXP_TIME,       event_expired_time);
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


    public ArrayList<MyTixDB> getData(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME , null);
        ArrayList<MyTixDB> list = new ArrayList<>();
        try {
            while (res.moveToNext()){
                MyTixDB tix = new MyTixDB();
                tix.event_id        = res.getString(res.getColumnIndex(FIELD_EVENTID));
                tix.event_name      = res.getString(res.getColumnIndex(FIELD_EVENTNAME));
                tix.event_date      = res.getString(res.getColumnIndex(FIELD_EVENTDATE));
                tix.event_time      = res.getString(res.getColumnIndex(FIELD_EVENTTIME));
                tix.event_venue     = res.getString(res.getColumnIndex(FIELD_EVENTVENUE));
                tix.event_price     = res.getString(res.getColumnIndex(FIELD_EVENTPRICE));
                tix.event_status     = res.getString(res.getColumnIndex(FIELD_EVENTSTATUS));
                tix.event_status_desc     = res.getString(res.getColumnIndex(FIELD_STATUS_DESC));
                tix.event_expired_date     = res.getString(res.getColumnIndex(FIELD_EXP_DATE));
                tix.event_expired_time     = res.getString(res.getColumnIndex(FIELD_EXP_TIME));

                list.add(tix);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return list;
    }

    public void getData(Context context, String where){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME +" where "+ FIELD_EVENTID +"='"+ where +"'", null);
        try {
            while (res.moveToNext()){
                this.event_id        = res.getString(res.getColumnIndex(FIELD_EVENTID));
                this.event_name      = res.getString(res.getColumnIndex(FIELD_EVENTNAME));
                this.event_date      = res.getString(res.getColumnIndex(FIELD_EVENTDATE));
                this.event_time      = res.getString(res.getColumnIndex(FIELD_EVENTTIME));
                this.event_venue     = res.getString(res.getColumnIndex(FIELD_EVENTVENUE));
                this.event_price     = res.getString(res.getColumnIndex(FIELD_EVENTPRICE));
                this.event_status           = res.getString(res.getColumnIndex(FIELD_EVENTSTATUS));
                this.event_status_desc      = res.getString(res.getColumnIndex(FIELD_STATUS_DESC));
                this.event_expired_date     = res.getString(res.getColumnIndex(FIELD_EXP_DATE));
                this.event_expired_time     = res.getString(res.getColumnIndex(FIELD_EXP_TIME));
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
