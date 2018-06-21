package g.rezza.moch.mobilesales.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Connection.Database;

/**
 * Created by rezza on 23/01/18.
 */

public class IdentityTypeDB {
    public int id = 0;
    public String name = "";
    public int category = 0;

    public static final String TAG   = "IdentityTypeDB";

    public static final String TABLE_NAME       = "IDENTITY_TYPE";

    public static final String FIELD_ID         = "ID";
    public static final String FIELD_NAME       = "NAME";
    public static final String FIELD_CATEGORY   = "CATEGORY";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " int NOT NULL," +
                " " + FIELD_NAME    + " varchar(100) NULL," +
                " " + FIELD_CATEGORY + " int NULL," +
                "  PRIMARY KEY ("   + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_CATEGORY, category);
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

    public ArrayList<IdentityTypeDB> getData(Context context, int category){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME + " WHERE "+ FIELD_CATEGORY+" = "+category, null);
        ArrayList<IdentityTypeDB> datas = new ArrayList<>();

        try {
            while (res.moveToNext()){
                IdentityTypeDB data = new IdentityTypeDB();
                data.id        = res.getInt(res.getColumnIndex(FIELD_ID));
                data.name      = res.getString(res.getColumnIndex(FIELD_NAME));
                data.category  = res.getInt(res.getColumnIndex(FIELD_CATEGORY));
                datas.add(data);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return datas;
    }

    public void syncronize(Context context){
        clearData(context);
        insert(context);
    }
}

