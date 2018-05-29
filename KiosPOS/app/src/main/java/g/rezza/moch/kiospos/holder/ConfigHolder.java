package g.rezza.moch.kiospos.holder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import g.rezza.moch.kiospos.db.Database;

/**
 * Created by Rezza on 10/9/17.
 */

public class ConfigHolder {
    public static final String TAG = "ConfigHolder";
    public String ip_address = "";
    public String port = "";

    public static final String TABLE_NAME = "CONFIG";
    public static final String IP_ADDRESS = "IP_ADDRESS";
    public static final String PORT = "PORT";

    public String getCreateTable(){
        String create = "create table " + TABLE_NAME + " "
                + "(" + IP_ADDRESS + " varchar(15) primary key ," +
                " " + PORT + " varchar(200) NOT NULL" +
                ")";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(IP_ADDRESS, ip_address);
        contentValues.put(PORT, port);
        return contentValues;
    }

    public void getData(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        try {
            while (res.moveToNext()){
                this.ip_address = res.getString(res.getColumnIndex(IP_ADDRESS));
                this.port = res.getString(res.getColumnIndex(PORT));
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
            Database pDB = new Database(context);
            pDB.delete(TABLE_NAME, "");
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

    }

    public boolean insert(Context context){
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

}
