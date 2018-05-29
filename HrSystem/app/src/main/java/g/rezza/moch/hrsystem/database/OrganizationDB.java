package g.rezza.moch.hrsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import g.rezza.moch.hrsystem.connection.Database;

/**
 * Created by rezza on 27/01/18.
 */

public class OrganizationDB {

    public int id;
    public String name;
    public String description;
    public int parent;
    public int level;

    public static final String TAG   = "OrganizationDB";

    public static final String TABLE_NAME       = "ORGANIZATION";

    public static final String FIELD_ID         = "ID";
    public static final String FIELD_NAME       = "NAME";
    public static final String FIELD_DESC       = "DESCRIPTION";
    public static final String FIELD_PARENT     = "PARENT";
    public static final String FIELD_LEVEL      = "LEVEL";


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " int NOT NULL," +
                " " + FIELD_NAME + " varchar(50) NULL," +
                " " + FIELD_DESC + " varchar(150) NULL," +
                " " + FIELD_PARENT + " int default 0," +
                " " + FIELD_LEVEL + " int default 0," +
                "  PRIMARY KEY (" + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_DESC, description);
        contentValues.put(FIELD_PARENT, parent);
        contentValues.put(FIELD_LEVEL, level);
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

    public void getData(Context context, String id){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT *  FROM " + TABLE_NAME+
                                    " WHERE "+ FIELD_ID +"=id ", null);
        try {
            while (res.moveToNext()){
                this.id             = res.getInt(res.getColumnIndex(FIELD_ID));
                this.name           = res.getString(res.getColumnIndex(FIELD_NAME));
                this.description    = res.getString(res.getColumnIndex(FIELD_DESC));
                this.parent         = res.getInt(res.getColumnIndex(FIELD_PARENT));
                this.level          = res.getInt(res.getColumnIndex(FIELD_LEVEL));
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

    public ArrayList<OrganizationDB> getDatas(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT *  FROM " + TABLE_NAME, null);
        ArrayList<OrganizationDB> data = new ArrayList<>();
        try {
            while (res.moveToNext()){
                OrganizationDB org = new OrganizationDB();
                org.id             = res.getInt(res.getColumnIndex(FIELD_ID));
                org.name           = res.getString(res.getColumnIndex(FIELD_NAME));
                org.description    = res.getString(res.getColumnIndex(FIELD_DESC));
                org.parent         = res.getInt(res.getColumnIndex(FIELD_PARENT));
                org.level          = res.getInt(res.getColumnIndex(FIELD_LEVEL));
                data.add(org);
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
