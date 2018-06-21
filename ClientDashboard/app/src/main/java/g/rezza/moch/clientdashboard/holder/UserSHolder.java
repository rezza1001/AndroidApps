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

public class UserSHolder {
    public static final String TAG = "UserSHolder";
    public String id = "";
    public String name = "";
    public String email = "";
    public String password ="";

    public static final String TABLE_NAME = "USERS";
    public static final String NAME = "NAME";
    public static final String ID = "ID";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    public String getCreateTable(){
        String create = "create table " + TABLE_NAME + " "
                + "(" + ID + " varchar(10) primary key ," +
                " " + NAME + " varchar(200) NOT NULL," +
                " " + EMAIL + " varchar(200) NOT NULL," +
                " " + PASSWORD + " varchar(30) NOT NULL" +
                ")";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(ID, id);
        contentValues.put(EMAIL, email);
        contentValues.put(PASSWORD, password);
        return contentValues;
    }

    public void getData(Context context){
        DbConnection pDB = new DbConnection(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        try {
            while (res.moveToNext()){
                this.id = res.getString(res.getColumnIndex(ID));
                this.name = res.getString(res.getColumnIndex(NAME));
                this.email = res.getString(res.getColumnIndex(EMAIL));
                this.password = res.getString(res.getColumnIndex(PASSWORD));
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
