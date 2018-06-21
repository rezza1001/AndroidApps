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

public class UserDB {
    public static final int TYPE_PRABAYAR       = 0;
    public static final int TYPE_PASCABAYAR     = 1;

    public int    id;
    public String email;
    public String password;
    public String token;
    public String fcm_token = "";
    public int role_id;
    public String role_dec;
    public String account;
    public float  balance;
    public int    parent;
    public int    update    = 0;
    public int    type      = 0; // 0: Pra | 1 : Pasca
    public int    mine = 0;

    public static final String TAG   = "UserDB";

    public static final String TABLE_NAME       = "USERS";

    public static final String FIELD_ID         = "ID";
    public static final String FIELD_PARENT     = "PARENT";
    public static final String FIELD_EMAIL      = "EMAIL";
    public static final String FIELD_PASSWORD   = "PASSWORD";
    public static final String FIELD_TOKEN      = "TOKEN";
    public static final String FIELD_FCM_TOKEN  = "FCM_TOKEN";
    public static final String FIELD_ROLEID     = "ROLE_ID";
    public static final String FIELD_ROLEDESC   = "ROLE_DESC";
    public static final String FIELD_ACCOUNT    = "ACCOUNT";
    public static final String FIELD_BALANCE    = "BALANCE";
    public static final String FIELD_MINE       = "MINE";
    public static final String FIELD_TYPE       = "TYPE";
    public static final String FIELD_UPDATE     = "ISUPDATE";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " int NOT NULL," +
                " " + FIELD_PARENT    + " int NOT NULL," +
                " " + FIELD_EMAIL + " varchar(50) NULL," +
                " " + FIELD_PASSWORD + " varchar(100) NULL," +
                " " + FIELD_TOKEN + " varchar(100) NULL," +
                " " + FIELD_FCM_TOKEN + " varchar(255) NULL," +
                " " + FIELD_ROLEID + " int NULL," +
                " " + FIELD_ROLEDESC + " varchar(50) NULL," +
                " " + FIELD_ACCOUNT + " varchar(30) NULL," +
                " " + FIELD_BALANCE + " varchar(30) NULL," +
                " " + FIELD_MINE + " int NULL," +
                " " + FIELD_TYPE + " int default 0," +
                " " + FIELD_UPDATE + " int NULL," +
                "  PRIMARY KEY (" + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_PARENT, parent);
        contentValues.put(FIELD_EMAIL, email);
        contentValues.put(FIELD_PASSWORD, password);
        contentValues.put(FIELD_TOKEN, token);
        contentValues.put(FIELD_ROLEID, role_id);
        contentValues.put(FIELD_ROLEDESC, role_dec);
        contentValues.put(FIELD_ACCOUNT, account);
        contentValues.put(FIELD_BALANCE, balance);
        contentValues.put(FIELD_MINE, mine);
        contentValues.put(FIELD_UPDATE, update);
        contentValues.put(FIELD_TYPE, type);
        contentValues.put(FIELD_FCM_TOKEN, fcm_token);
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

    public int updateFcmToken(Context context, String token){
        Log.d(TAG, "Update Token "+ token);
        int x = 0;
        this.fcm_token = token;
        try {
            Database pDB = new Database(context);
            x = pDB.update(TABLE_NAME, createContentValues(), " "+ FIELD_MINE +"=1 ");
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        return x;
    }

    public UserDB getData(Context context, String where){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+ " WHERE "+ where, null);
        try {
            while (res.moveToNext()){
                this.id         = res.getInt(res.getColumnIndex(FIELD_ID));
                this.parent     = res.getInt(res.getColumnIndex(FIELD_PARENT));
                this.email      = res.getString(res.getColumnIndex(FIELD_EMAIL));
                this.password   = res.getString(res.getColumnIndex(FIELD_PASSWORD));
                this.token      = res.getString(res.getColumnIndex(FIELD_TOKEN));
                this.role_id    = res.getInt(res.getColumnIndex(FIELD_ROLEID));
                this.role_dec   = res.getString(res.getColumnIndex(FIELD_ROLEDESC));
                this.account    = res.getString(res.getColumnIndex(FIELD_ACCOUNT));
                this.balance    = res.getLong(res.getColumnIndex(FIELD_BALANCE));
                this.mine       = res.getInt(res.getColumnIndex(FIELD_MINE));
                this.update       = res.getInt(res.getColumnIndex(FIELD_UPDATE));
                this.type       = res.getInt(res.getColumnIndex(FIELD_TYPE));
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return this;
    }

    public void getMine(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+ " WHERE "+ FIELD_MINE +"=1 ", null);
        try {
            while (res.moveToNext()){
                this.id         = res.getInt(res.getColumnIndex(FIELD_ID));
                this.parent     = res.getInt(res.getColumnIndex(FIELD_PARENT));
                this.email      = res.getString(res.getColumnIndex(FIELD_EMAIL));
                this.password   = res.getString(res.getColumnIndex(FIELD_PASSWORD));
                this.token      = res.getString(res.getColumnIndex(FIELD_TOKEN));
                this.role_id    = res.getInt(res.getColumnIndex(FIELD_ROLEID));
                this.role_dec   = res.getString(res.getColumnIndex(FIELD_ROLEDESC));
                this.account    = res.getString(res.getColumnIndex(FIELD_ACCOUNT));
                this.balance    = res.getLong(res.getColumnIndex(FIELD_BALANCE));
                this.mine       = res.getInt(res.getColumnIndex(FIELD_MINE));
                this.update     = res.getInt(res.getColumnIndex(FIELD_UPDATE));
                this.type       = res.getInt(res.getColumnIndex(FIELD_TYPE));
                this.fcm_token       = res.getString(res.getColumnIndex(FIELD_FCM_TOKEN));
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
