package g.rezza.moch.kiostixv3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.kiostixv3.connection.Database;

/**
 * Created by rezza on 01/03/18.
 */

public class BookingDB {

    public String order_id          = "";
    public String event_id          = "";
    public String payment_id        = "";
    public String expired_date        = "";
    public String trans_status      = "";
    public String virtual_account   = "0";
    public String event_name        = "";
    public String event_venue       = "";
    public String event_desc        = "";
    public String event_term        = "";
    public String event_image       = "";
    public String event_category    = "";
    public String event_payment     = "";
    public String event_guest       = "";
    public String event_date        = "";
    public String event_time        = "";
    public String event_time_code   = "";
    public String event_start_date  = "";
    public String event_end_date    = "";

    public static final String TAG   = "BookingDB";

    public static final String TABLE_NAME       = "BOOKING_DB";

    public static final String FIELD_ORDERID         = "ORDERID";
    public static final String FIELD_VA              = "VA";
    public static final String FIELD_EXP_DATE        = "EXP_DATE";
    public static final String FIELD_EVENTID         = "EVENTID";
    public static final String FIELD_EVENTNAME       = "EVENTNAME";
    public static final String FIELD_EVENTPAYMENT    = "EVENTPAYMENT";
    public static final String FIELD_EVENTVENUE      = "EVENTVENUE";
    public static final String FIELD_EVENTCATEGORY   = "EVENTCATEGORY";
    public static final String FIELD_EVENTGUEST      = "EVENTGUEST";
    public static final String FIELD_EVENTIMAGE      = "EVENTIMAGE";
    public static final String FIELD_EVENTDATE       = "EVENTDATE";
    public static final String FIELD_EVENTTIME       = "EVENTTIME";
    public static final String FIELD_EVENTTIME_CODE  = "EVENTTIME_CODE";
    public static final String FIELD_EVENTTERM       = "EVENTTERM";
    public static final String FIELD_EVENTDESC       = "EVENTDESC";
    public static final String FIELD_EVENT_START_DATE     = "EVENT_START_DATE";
    public static final String FIELD_EVENT_END_DATE       = "EVENT_END_DATE";
    public static final String FIELD_PAYMENT_ID      = "PAYMENT_ID";
    public static final String FIELD_TRANS_STATUS    = "TRANS_STATUS";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ORDERID    + " varchar(30) NOT NULL," +
                " " + FIELD_PAYMENT_ID    + " varchar(30)  NULL," +
                " " + FIELD_TRANS_STATUS    + " varchar(30)  NULL," +
                " " + FIELD_VA    + " varchar(30) NULL," +
                " " + FIELD_EVENTID    + " varchar(30) NOT NULL," +
                " " + FIELD_EVENTNAME    + " varchar(100) NULL," +
                " " + FIELD_EVENTCATEGORY + " text," +
                " " + FIELD_EVENTIMAGE + " text," +
                " " + FIELD_EVENTGUEST + " text," +
                " " + FIELD_EVENTVENUE + " text," +
                " " + FIELD_EVENTPAYMENT + " text," +
                " " + FIELD_EVENTTERM + " text NULL," +
                " " + FIELD_EVENTDESC + " text NULL," +
                " " + FIELD_EVENTDATE + " text NULL ," +
                " " + FIELD_EVENTTIME + " text NULL ," +
                " " + FIELD_EVENTTIME_CODE + " varchar(30) NULL ," +
                " " + FIELD_EVENT_START_DATE + " varchar(50) NULL ," +
                " " + FIELD_EVENT_END_DATE + " varchar(50) NULL ," +
                " " + FIELD_EXP_DATE + " varchar(50) NULL ," +
                "  PRIMARY KEY ("   + FIELD_EVENTID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ORDERID,        order_id);
        contentValues.put(FIELD_EVENTID,        event_id);
        contentValues.put(FIELD_EVENTNAME,      event_name);
        contentValues.put(FIELD_EVENTCATEGORY,  event_category);
        contentValues.put(FIELD_EVENTGUEST,     event_guest);
        contentValues.put(FIELD_EVENTIMAGE,     event_image);
        contentValues.put(FIELD_EVENTDATE,      event_date);
        contentValues.put(FIELD_EVENTTIME,      event_time);
        contentValues.put(FIELD_EVENTTIME_CODE,      event_time_code);
        contentValues.put(FIELD_EVENTVENUE,     event_venue);
        contentValues.put(FIELD_EVENTPAYMENT,   event_payment);
        contentValues.put(FIELD_EVENTDESC,     event_desc);
        contentValues.put(FIELD_EVENTTERM,     event_term);
        contentValues.put(FIELD_EVENT_START_DATE,     event_start_date);
        contentValues.put(FIELD_EVENT_END_DATE,     event_end_date);
        contentValues.put(FIELD_PAYMENT_ID,     payment_id);
        contentValues.put(FIELD_TRANS_STATUS,     trans_status);
        contentValues.put(FIELD_VA,     virtual_account);
        contentValues.put(FIELD_EXP_DATE,     expired_date);
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
                this.order_id        = res.getString(res.getColumnIndex(FIELD_ORDERID));
                this.event_id        = res.getString(res.getColumnIndex(FIELD_EVENTID));
                this.event_name      = res.getString(res.getColumnIndex(FIELD_EVENTNAME));
                this.event_category  = res.getString(res.getColumnIndex(FIELD_EVENTCATEGORY));
                this.event_guest     = res.getString(res.getColumnIndex(FIELD_EVENTGUEST));
                this.event_image     = res.getString(res.getColumnIndex(FIELD_EVENTIMAGE));
                this.event_date      = res.getString(res.getColumnIndex(FIELD_EVENTDATE));
                this.event_time      = res.getString(res.getColumnIndex(FIELD_EVENTTIME));
                this.event_time_code = res.getString(res.getColumnIndex(FIELD_EVENTTIME_CODE));
                this.event_venue     = res.getString(res.getColumnIndex(FIELD_EVENTVENUE));
                this.event_payment   = res.getString(res.getColumnIndex(FIELD_EVENTPAYMENT));
                this.event_desc      = res.getString(res.getColumnIndex(FIELD_EVENTDESC));
                this.event_term      = res.getString(res.getColumnIndex(FIELD_EVENTTERM));
                this.event_start_date   = res.getString(res.getColumnIndex(FIELD_EVENT_START_DATE));
                this.event_end_date     = res.getString(res.getColumnIndex(FIELD_EVENT_END_DATE));
                this.payment_id         = res.getString(res.getColumnIndex(FIELD_PAYMENT_ID));
                this.trans_status       = res.getString(res.getColumnIndex(FIELD_TRANS_STATUS));
                this.virtual_account       = res.getString(res.getColumnIndex(FIELD_VA));
                this.expired_date       = res.getString(res.getColumnIndex(FIELD_EXP_DATE));
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
