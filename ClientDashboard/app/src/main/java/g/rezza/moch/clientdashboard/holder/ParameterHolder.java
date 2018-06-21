package g.rezza.moch.clientdashboard.holder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import g.rezza.moch.clientdashboard.db.DbConnection;

/**
 * Created by Rezza on 10/9/17.
 */

public class ParameterHolder {
    public static final String TAG = "ParameterHolder";
    public String event_id = "";
    public String paramter = "";

    public static final String TABLE_NAME = "PARAMS";
    public static final String EVENT_ID = "EVENT";
    public static final String PARAMETER = "PARAM";

    public String getCreateTable(){
        String create = "create table " + TABLE_NAME + " "
                + "(" + EVENT_ID + " varchar(10) primary key ," +
                " " + PARAMETER + " varchar(200) NOT NULL"+
                ")";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_ID, event_id);
        contentValues.put(PARAMETER, paramter);
        return contentValues;
    }

    public void getData(Context context){
        DbConnection pDB = new DbConnection(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        try {
            while (res.moveToNext()){
                this.event_id = res.getString(res.getColumnIndex(EVENT_ID));
                this.paramter = res.getString(res.getColumnIndex(PARAMETER));
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

    public void clearData(Context context){
        try {
            DbConnection pDB = new DbConnection(context);
            pDB.delete(TABLE_NAME, "");
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

    }

    public boolean insert(Context context){
        boolean x = false;
        try {
            DbConnection pDB = new DbConnection(context);
            x = pDB.insert(TABLE_NAME, createContentValues());
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        return x;
    }

}
