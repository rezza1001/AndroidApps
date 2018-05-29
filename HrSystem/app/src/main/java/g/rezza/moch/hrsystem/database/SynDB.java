package g.rezza.moch.hrsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import g.rezza.moch.hrsystem.connection.Database;

/**
 * Created by rezza on 27/01/18.
 */

public class SynDB {

    public int id;
    public int version = 0;

    public static final String TAG   = "SynDB";

    public static final String TABLE_NAME       = "SYN";

    public static final String FIELD_ID         = "ID";
    public static final String FIELD_VERSION    = "VERSION_DATA";


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID      + " int NOT NULL," +
                " " + FIELD_VERSION + " int NULL," +
                "  PRIMARY KEY ("   + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_VERSION, version);
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
        clearData(context);
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

    public int getVersion(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        try {
            while (res.moveToNext()){
                this.id         = res.getInt(res.getColumnIndex(FIELD_ID));
                this.version      = res.getInt(res.getColumnIndex(FIELD_VERSION));
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return this.version;
    }
}
