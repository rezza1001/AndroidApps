package g.rezza.moch.mobilesales.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import g.rezza.moch.mobilesales.Connection.Database;

/**
 * Created by rezza on 18/01/18.
 */

public class BalanceDB {
    public float    balance = 0;
    public boolean  sync        = true;
    public String   last_update;

    public static final String TAG   = "BalanceDB";

    public static final String TABLE_NAME       = "BalanceDB";


    public static final String FIELD_BALANCE     = "BALANCE";
    public static final String FIELD_LAST_UPDATE = "LAST_UPDATE";
    public static final String FIELD_SYNC        = "SYNC";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_BALANCE    + " varchar(50) ," +
                " " + FIELD_LAST_UPDATE + " datetime ," +
                " " + FIELD_SYNC + " INTEGER DEFAULT 0 )";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_BALANCE, balance);
        contentValues.put(FIELD_LAST_UPDATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Log.d(TAG,"Sync Before Update "+ sync);
        if (sync){
            contentValues.put(FIELD_SYNC, 1);
        }
        else {
            contentValues.put(FIELD_SYNC, 0);
        }

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
        this.sync = false;
        try {
            Database pDB = new Database(context);
            x = pDB.insert(TABLE_NAME, createContentValues());
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        return x;
    }

    public void Syncronize(Context context){
        Database pDB = new Database(context);
        this.sync = true;
        try {
            pDB.update(TABLE_NAME,createContentValues(),"");
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }


    }

    public boolean isSync(){
        return sync;
    }

    public void getData(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);

        try {
            while (res.moveToNext()){
                this.balance        = res.getLong(res.getColumnIndex(FIELD_BALANCE));
                this.last_update    = res.getString(res.getColumnIndex(FIELD_LAST_UPDATE));
                int syn             = res.getInt(res.getColumnIndex(FIELD_SYNC));
                this.sync           = (syn > 0);
                Log.d(TAG, "SYNC : "+  syn );
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
