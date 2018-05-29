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

public class UserDB {

    public int    id;
    public String email;
    public String token;
    public String password;
    public int    mine;

    public static final String TAG   = "UserDB";

    public static final String TABLE_NAME       = "USERS";

    public static final String FIELD_ID         = "ID";
    public static final String FIELD_EMAIL      = "EMAIL";
    public static final String FIELD_TOKEN      = "TOKEN";
    public static final String FIELD_MINE       = "MINE";
    public static final String FIELD_PASSWORD   = "PASSWORD";


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " int NOT NULL," +
                " " + FIELD_EMAIL + " varchar(50) NULL," +
                " " + FIELD_TOKEN + " varchar(100) NULL," +
                " " + FIELD_PASSWORD + " varchar(20) NULL," +
                " " + FIELD_MINE  + " int default 0," +
                "  PRIMARY KEY (" + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_EMAIL, email);
        contentValues.put(FIELD_TOKEN, token);
        contentValues.put(FIELD_MINE, mine);
        contentValues.put(FIELD_PASSWORD, password);
        return contentValues;
    }

    public void clearData(Context context){
        try {
            Database pDB = new Database(context);
            pDB.delete(TABLE_NAME);
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }

    public void deleteData(Context context, String id){
        try {
            Database pDB = new Database(context);
            pDB.delete(TABLE_NAME, " "+ FIELD_ID+" = "+ id);
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

    public void getMine(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+ " WHERE "+ FIELD_MINE +"=1 ", null);
        try {
            while (res.moveToNext()){
                this.id         = res.getInt(res.getColumnIndex(FIELD_ID));
                this.email      = res.getString(res.getColumnIndex(FIELD_EMAIL));
                this.token      = res.getString(res.getColumnIndex(FIELD_TOKEN));
                this.mine       = res.getInt(res.getColumnIndex(FIELD_MINE));
                this.password   = res.getString(res.getColumnIndex(FIELD_PASSWORD));
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
