package g.rezza.moch.mobilesales.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import g.rezza.moch.mobilesales.Connection.Database;

/**
 * Created by rezza on 11/01/18.
 */

public class CompanyDB {

    public int user_id;
    public String code;
    public String name;
    public String address;
    public String phone1;
    public String phone2;
    public int identity_type;
    public String identity_no;
    public int location_id;

    public static final String TAG   = "CompanyDB";

    public static final String TABLE_NAME       = "MERCHANT";

    public static final String FIELD_USER       = "USER";
    public static final String FIELD_CODE       = "CODE";
    public static final String FIELD_NAME       = "NAME";
    public static final String FIELD_ADDRESS    = "ADDRESS";
    public static final String FIELD_PHONE1     = "PHONE1";
    public static final String FIELD_PHONE2     = "PHONE2";
    public static final String FIELD_IDTYPE     = "ID_TYPE";
    public static final String FIELD_IDNO       = "ID_NO";
    public static final String FIELD_LOCATION   = "LOCATION";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_USER    + " int NOT NULL," +
                " " + FIELD_NAME    + " varchar(50) NULL," +
                " " + FIELD_ADDRESS + " varchar(100) NULL," +
                " " + FIELD_PHONE1  + " varchar(15) NULL," +
                " " + FIELD_PHONE2  + " varchar(15) NULL," +
                " " + FIELD_CODE     + " varchar(30) NULL," +
                " " + FIELD_IDTYPE  + " int NULL," +
                " " + FIELD_IDNO    + " varchar(30) NULL," +
                " " + FIELD_LOCATION + " int NULL," +
                "  PRIMARY KEY ("   + FIELD_USER +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_USER, user_id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_ADDRESS, address);
        contentValues.put(FIELD_PHONE1, phone1);
        contentValues.put(FIELD_PHONE2, phone2);
        contentValues.put(FIELD_CODE, code);
        contentValues.put(FIELD_IDTYPE, identity_type);
        contentValues.put(FIELD_IDNO, identity_no);
        contentValues.put(FIELD_LOCATION, location_id);
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

    public int update(Context context, String where){
        Log.d(TAG, "Update Data ");
        int x = 0;
        try {
            Database pDB = new Database(context);
            x = pDB.update(TABLE_NAME, createContentValues(), where);
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        return x;
    }

    public void getData(Context context, String userid){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME +" WHERE "+FIELD_USER+"="+userid, null);
        try {
            while (res.moveToNext()){
                this.user_id        = res.getInt(res.getColumnIndex(FIELD_USER));
                this.name           = res.getString(res.getColumnIndex(FIELD_NAME));
                this.address        = res.getString(res.getColumnIndex(FIELD_ADDRESS));
                this.phone1         = res.getString(res.getColumnIndex(FIELD_PHONE1));
                this.phone2         = res.getString(res.getColumnIndex(FIELD_PHONE2));
                this.code           = res.getString(res.getColumnIndex(FIELD_CODE));
                this.identity_type  = res.getInt(res.getColumnIndex(FIELD_IDTYPE));
                this.identity_no    = res.getString(res.getColumnIndex(FIELD_IDNO));
                this.location_id    = res.getInt(res.getColumnIndex(FIELD_LOCATION));
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
