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

public class TransHystoryDB {

    public static final String TAG          = "TransHystoryDB";
    public static final String TABLE_NAME   = "TRANS_HISTORY_DB";

    public static final String FIELD_ORDER_NO       = "ORDER_NO";
    public static final String FIELD_EVENT_NAME     = "EVENT_NAME";
    public static final String FIELD_SCHEDULE       = "SCHEDULE";
    public static final String FIELDVISIT_TIME      = "VISIT_TIME";
    public static final String FIELD_ORDER_TOTAL    = "ORDER_TOTAL";
    public static final String FIELD_EXPIRED_DATE   = "EXPIRED_DATE";
    public static final String FIELD_VENUE_DETAILS  = "VENUE_DETAILS";
    public static final String FIELD_STATUS         = "STATUS";
    public static final String FIELD_PAYMENT        = "PAYMENT";
    public static final String FIELD_QUANTITY       = "SC_TIME";

    public String order_no;
    public String event_name;
    public String schedule;
    public String visit_time;
    public String order_total;
    public String status;
    public String payment;
    public int quantity;
    public String expired_date;
    public String venue_details;



    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ORDER_NO + " varchar(30) NOT NULL," +
                " " + FIELD_STATUS + " varchar(30) NOT NULL," +
                " " + FIELD_EVENT_NAME + " varchar(225)  NULL," +
                " " + FIELD_PAYMENT + "  varchar(30)  NULL," +
                " " + FIELD_QUANTITY + " varchar(10)   NULL," +
                " " + FIELD_SCHEDULE + " varchar(20)   NULL," +
                " " + FIELDVISIT_TIME + " varchar(20)   NULL," +
                " " + FIELD_ORDER_TOTAL + " varchar(30)   NULL," +
                " " + FIELD_EXPIRED_DATE + " varchar(20)   NULL," +
                " " + FIELD_VENUE_DETAILS + " text   NULL," +
                "  PRIMARY KEY (" + FIELD_ORDER_NO + "))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ORDER_NO, order_no);
        contentValues.put(FIELD_STATUS, status);
        contentValues.put(FIELD_EVENT_NAME, event_name);
        contentValues.put(FIELD_PAYMENT, payment);
        contentValues.put(FIELD_QUANTITY, quantity);
        contentValues.put(FIELD_SCHEDULE, schedule);
        contentValues.put(FIELDVISIT_TIME, visit_time);
        contentValues.put(FIELD_ORDER_TOTAL, order_total);
        contentValues.put(FIELD_EXPIRED_DATE, expired_date);
        contentValues.put(FIELD_VENUE_DETAILS, venue_details);
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

    public ArrayList<TransHystoryDB> getDatas(Context context, String status){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        ArrayList<TransHystoryDB> data = new ArrayList<>();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME + " WHERE "+ FIELD_STATUS+"='"+status+"'", null);
        try {
            while (res.moveToNext()){
                TransHystoryDB event = new TransHystoryDB();
                event.order_no       = res.getString(res.getColumnIndex(FIELD_ORDER_NO));
                event.status        = res.getString(res.getColumnIndex(FIELD_STATUS));
                event.event_name    = res.getString(res.getColumnIndex(FIELD_EVENT_NAME));
                event.payment       = res.getString(res.getColumnIndex(FIELD_PAYMENT));
                event.quantity      = res.getInt(res.getColumnIndex(FIELD_QUANTITY));
                event.schedule      = res.getString(res.getColumnIndex(FIELD_SCHEDULE));
                event.visit_time      = res.getString(res.getColumnIndex(FIELDVISIT_TIME));
                event.order_total      = res.getString(res.getColumnIndex(FIELD_ORDER_TOTAL));
                event.expired_date      = res.getString(res.getColumnIndex(FIELD_EXPIRED_DATE));
                event.venue_details      = res.getString(res.getColumnIndex(FIELD_VENUE_DETAILS));
                data.add(event);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return data;
    }

    public ArrayList<TransHystoryDB> getDataOthers(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        ArrayList<TransHystoryDB> data = new ArrayList<>();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME + " WHERE "+ FIELD_STATUS+" NOT IN ('Pending','Paid')", null);
        try {
            while (res.moveToNext()){
                TransHystoryDB event = new TransHystoryDB();
                event.order_no       = res.getString(res.getColumnIndex(FIELD_ORDER_NO));
                event.status     = res.getString(res.getColumnIndex(FIELD_STATUS));
                event.event_name    = res.getString(res.getColumnIndex(FIELD_EVENT_NAME));
                event.payment    = res.getString(res.getColumnIndex(FIELD_PAYMENT));
                event.quantity    = res.getInt(res.getColumnIndex(FIELD_QUANTITY));

                event.schedule      = res.getString(res.getColumnIndex(FIELD_SCHEDULE));
                event.visit_time      = res.getString(res.getColumnIndex(FIELDVISIT_TIME));
                event.order_total      = res.getString(res.getColumnIndex(FIELD_ORDER_TOTAL));
                event.expired_date      = res.getString(res.getColumnIndex(FIELD_EXPIRED_DATE));
                event.venue_details      = res.getString(res.getColumnIndex(FIELD_VENUE_DETAILS));
                data.add(event);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return data;
    }

}
