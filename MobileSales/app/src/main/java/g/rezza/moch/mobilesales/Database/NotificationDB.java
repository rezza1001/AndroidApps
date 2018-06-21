package g.rezza.moch.mobilesales.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Connection.Database;

/**
 * Created by rezza on 22/01/18.
 */

public class NotificationDB {

    public int id;
    public String user_id_req;
    public String what;
    public String when;
    public String who;
    public String message;
    public String nominal;
    public String account_name_req;
    public String account_no_req;
    public int isRead = 1;
    public int status = 1;
    public String status_desc;

    public static final String TAG   = "NotificationDB";

    public static final String TABLE_NAME       = "NOTIFICATION";

    public static final String FIELD_ID       = "ID";
    public static final String FIELD_USER_ID_REQ       = "USER_ID_REQ";
    public static final String FIELD_WHAT       = "FWHAT";
    public static final String FIELD_WHEN       = "FWHEN";
    public static final String FIELD_ACC_NAME_REQ        = "ACCOUNT_NAME_REQ";
    public static final String FIELD_ACC_NO_REQ        = "ACCOUNT_NO_REQ";
    public static final String FIELD_WH0        = "FWHO";
    public static final String FIELD_MESSAGE        = "MESSAGE";
    public static final String FIELD_NOMINAL        = "NOMINAL";
    public static final String FIELD_STATUS         = "STATUS";
    public static final String FIELD_STATUS_DESC    = "STATUS_DESC";
    public static final String FIELD_READ           = "FREAD";


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " int NOT NULL," +
                " " + FIELD_USER_ID_REQ    + " int NULL," +
                " " + FIELD_WHAT + " varchar(100) NULL," +
                " " + FIELD_ACC_NAME_REQ + " varchar(100) NULL," +
                " " + FIELD_ACC_NO_REQ + " varchar(50) NULL," +
                " " + FIELD_WHEN  + " varchar(20) NULL," +
                " " + FIELD_WH0  + " varchar(50) NULL," +
                " " + FIELD_MESSAGE  + " text NULL," +
                " " + FIELD_NOMINAL     + " varchar(20) NULL," +
                " " + FIELD_STATUS  + " int default 1," +
                " " + FIELD_STATUS_DESC     + " varchar(50) NULL," +
                " " + FIELD_READ + " int default 1," +
                "  PRIMARY KEY ("   + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_USER_ID_REQ, user_id_req);
        contentValues.put(FIELD_WHAT, what);
        contentValues.put(FIELD_WHEN, when);
        contentValues.put(FIELD_WH0, who);
        contentValues.put(FIELD_MESSAGE, message);
        contentValues.put(FIELD_NOMINAL, nominal);
        contentValues.put(FIELD_ACC_NAME_REQ, account_name_req);
        contentValues.put(FIELD_ACC_NO_REQ, account_no_req);
        contentValues.put(FIELD_STATUS, status);
        contentValues.put(FIELD_STATUS_DESC, status_desc);
        contentValues.put(FIELD_READ, isRead);
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

    public void clearData(Context context, String where){
        try {
            Database pDB = new Database(context);
            pDB.delete(TABLE_NAME, " "+ FIELD_STATUS+"  in("+ where+")");
            Log.d(TAG,"DELETE FROM "+TABLE_NAME+" WHERE "+ FIELD_STATUS+"  in( "+ where+")");
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

    public void getData(Context context, String where){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME +" WHERE "+ where, null);
        try {
            while (res.moveToNext()){
                this.id             = res.getInt(res.getColumnIndex(FIELD_ID));
                this.user_id_req    = res.getString(res.getColumnIndex(FIELD_USER_ID_REQ));
                this.account_name_req    = res.getString(res.getColumnIndex(FIELD_ACC_NAME_REQ));
                this.account_no_req    = res.getString(res.getColumnIndex(FIELD_ACC_NO_REQ));
                this.what           = res.getString(res.getColumnIndex(FIELD_WHAT));
                this.when           = res.getString(res.getColumnIndex(FIELD_WHEN));
                this.who            = res.getString(res.getColumnIndex(FIELD_WH0));
                this.message        = res.getString(res.getColumnIndex(FIELD_MESSAGE));
                this.nominal        = res.getString(res.getColumnIndex(FIELD_NOMINAL));
                this.status         = res.getInt(res.getColumnIndex(FIELD_STATUS));
                this.status_desc         = res.getString(res.getColumnIndex(FIELD_STATUS_DESC));
                this.isRead         = res.getInt(res.getColumnIndex(FIELD_READ));
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

    public ArrayList<NotificationDB> getData(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        ArrayList<NotificationDB> notifs = new ArrayList<>();
        try {
            while (res.moveToNext()){
                NotificationDB notif = new NotificationDB();
                notif.id             = res.getInt(res.getColumnIndex(FIELD_ID));
                notif.user_id_req    = res.getString(res.getColumnIndex(FIELD_USER_ID_REQ));
                notif.account_name_req    = res.getString(res.getColumnIndex(FIELD_ACC_NAME_REQ));
                notif.account_no_req    = res.getString(res.getColumnIndex(FIELD_ACC_NO_REQ));
                notif.what           = res.getString(res.getColumnIndex(FIELD_WHAT));
                notif.when           = res.getString(res.getColumnIndex(FIELD_WHEN));
                notif.who            = res.getString(res.getColumnIndex(FIELD_WH0));
                notif.message        = res.getString(res.getColumnIndex(FIELD_MESSAGE));
                notif.nominal        = res.getString(res.getColumnIndex(FIELD_NOMINAL));
                notif.status         = res.getInt(res.getColumnIndex(FIELD_STATUS));
                notif.status_desc         = res.getString(res.getColumnIndex(FIELD_STATUS_DESC));
                notif.isRead         = res.getInt(res.getColumnIndex(FIELD_READ));
                notifs.add(notif);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return notifs;
    }


    public ArrayList<NotificationDB> getDatas(Context context, String where){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME +" WHERE "+ where, null);
        ArrayList<NotificationDB> notifs = new ArrayList<>();
        try {
            while (res.moveToNext()){
                NotificationDB notif = new NotificationDB();
                notif.id             = res.getInt(res.getColumnIndex(FIELD_ID));
                notif.user_id_req    = res.getString(res.getColumnIndex(FIELD_USER_ID_REQ));
                notif.account_name_req    = res.getString(res.getColumnIndex(FIELD_ACC_NAME_REQ));
                notif.account_no_req    = res.getString(res.getColumnIndex(FIELD_ACC_NO_REQ));
                notif.what           = res.getString(res.getColumnIndex(FIELD_WHAT));
                notif.when           = res.getString(res.getColumnIndex(FIELD_WHEN));
                notif.who            = res.getString(res.getColumnIndex(FIELD_WH0));
                notif.message        = res.getString(res.getColumnIndex(FIELD_MESSAGE));
                notif.nominal        = res.getString(res.getColumnIndex(FIELD_NOMINAL));
                notif.status         = res.getInt(res.getColumnIndex(FIELD_STATUS));
                notif.status_desc         = res.getString(res.getColumnIndex(FIELD_STATUS_DESC));
                notif.isRead         = res.getInt(res.getColumnIndex(FIELD_READ));
                notifs.add(notif);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return notifs;
    }

}
