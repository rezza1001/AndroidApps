package g.rezza.moch.mobilesales.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import g.rezza.moch.mobilesales.Connection.Database;
import g.rezza.moch.mobilesales.lib.Parse;

/**
 * Created by rezza on 15/03/18.
 */

public class SchedulesDB {

    public static final String TAG          = "ScheduleHolder";
    public static final String TABLE_NAME   = "SCHEDULER_DB";

    public static final String FIELD_ID     = "ID";
    public static final String FIELD_NAME   = "NAME";
    public static final String FIELD_VENUE  = "VENUE";
    public static final String FIELD_DATE   = "DATE";
    public static final String FIELD_DATA   = "DATA";

    public String id;
    public String name;
    public String venue;
    public String date;
    public String data;

    public boolean selected = false;


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID + " varchar(30) NOT NULL," +
                " " + FIELD_NAME + " varchar(20) NOT NULL," +
                " " + FIELD_VENUE + " varchar(20)  NULL," +
                " " + FIELD_DATE + " varchar(20)  NULL," +
                " " + FIELD_DATA + " text  NULL," +
                "  PRIMARY KEY (" + FIELD_ID + "))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_VENUE, venue);
        contentValues.put(FIELD_DATE, date);
        contentValues.put(FIELD_DATA, data);
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

    public ArrayList<SchedulesDB> getDatas(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        ArrayList<SchedulesDB> data = new ArrayList<>();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        try {
            while (res.moveToNext()){
                SchedulesDB event = new SchedulesDB();
                event.id       = res.getString(res.getColumnIndex(FIELD_ID));
                event.name     = res.getString(res.getColumnIndex(FIELD_NAME));
                event.venue    = res.getString(res.getColumnIndex(FIELD_VENUE));
                event.date    = res.getString(res.getColumnIndex(FIELD_DATE));
                event.data    = res.getString(res.getColumnIndex(FIELD_DATA));
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

    public String getMaxprice(){
        long max_price = 0;
        int index = 0;
        try {
            JSONArray ja = new JSONArray(this.data);
            for (int i=0; i<ja.length(); i++){
                Log.d(TAG,ja.getJSONObject(i).toString());
                long price = 0;
                try {
                    price = Long.parseLong(ja.getJSONObject(i).getString("ticket_price"));
                }catch (Exception e){}

                if (max_price < price){
                    max_price = price;
                }
                index++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (index == 1){
            return Parse.toCurrnecy(""+max_price);
        }
        else {
            return "0 - " + Parse.toCurrnecy(""+max_price);
        }

    }

}
