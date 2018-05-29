package g.rezza.moch.hrsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import g.rezza.moch.hrsystem.connection.Database;

/**
 * Created by rezza on 27/01/18.
 */

public class AbsentDB {

    public int absent_header;
    public String check_in = "";
    public String check_out = "";
    public String status = "";
    public String status_desc = "";
    public String note = "";
    public String work_date = "";
    public String work_time = "";
    public String work_end_time = "";
    public String late = "";

    public static final String TAG   = "AbsentDB";

    public static final String TABLE_NAME       = "ABSENT";

    public static final String FIELD_ABSENT_HEDAER  = "ABSENT_HEDAER";
    public static final String FIELD_CHECK_IN       = "CHECK_IN";
    public static final String FIELD_CHECK_OUT      = "CHECK_OUT";
    public static final String FIELD_STATUS         = "STATUS";
    public static final String FIELD_STATUS_DESC    = "STATUS_DESC";
    public static final String FIELD_NOTE           = "NOTE";
    public static final String FIELD_WORK_DATE      = "WORK_DATE";
    public static final String FIELD_WORK_TIME      = "WORK_TIME";
    public static final String FIELD_WORK_END_TIME      = "WORK_END_TIME";
    public static final String FIELD_MAX_LATE       = "MAX_LATE";


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ABSENT_HEDAER      + " int NOT NULL," +
                " " + FIELD_CHECK_IN + " varchar(20) NULL," +
                " " + FIELD_CHECK_OUT + " varchar(20) NULL," +
                " " + FIELD_STATUS + " int NULL," +
                " " + FIELD_STATUS_DESC + " varchar(100) NULL," +
                " " + FIELD_NOTE + " varchar(100) NULL," +
                " " + FIELD_WORK_DATE + " varchar(20) NULL," +
                " " + FIELD_WORK_TIME + " varchar(20) NULL," +
                " " + FIELD_WORK_END_TIME + " varchar(20) NULL," +
                " " + FIELD_MAX_LATE + " int NULL )";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ABSENT_HEDAER, absent_header );
        contentValues.put(FIELD_CHECK_IN, check_in);
        contentValues.put(FIELD_CHECK_OUT, check_out);
        contentValues.put(FIELD_STATUS, status);
        contentValues.put(FIELD_STATUS_DESC, status_desc);
        contentValues.put(FIELD_NOTE, note);
        contentValues.put(FIELD_WORK_DATE, work_date);
        contentValues.put(FIELD_WORK_TIME, work_time);
        contentValues.put(FIELD_WORK_END_TIME, work_end_time);
        contentValues.put(FIELD_MAX_LATE,late);
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

    public void deleteData(Context context, String where){
        try {
            Database pDB = new Database(context);
            pDB.delete(TABLE_NAME, where);
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

    public void update(Context context){
        Log.d(TAG, "update Data ");
        clearData(context);
        try {
            Calendar calendar     = Calendar.getInstance();
            Date currentTime      = calendar.getTime();
            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            Database pDB = new Database(context);
            pDB.update(TABLE_NAME, createContentValues(), FIELD_WORK_DATE+" = '"+formatDate.format(currentTime)+"'");
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

    }

    public ArrayList<AbsentDB> getData(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+ " ORDER BY "+ FIELD_WORK_DATE+" DESC", null);
        ArrayList<AbsentDB> absents = new ArrayList<>();
        try {
            while (res.moveToNext()){
                AbsentDB absent = new AbsentDB();
                absent.absent_header = res.getInt(res.getColumnIndex(FIELD_ABSENT_HEDAER));
                absent.check_in      = res.getString(res.getColumnIndex(FIELD_CHECK_IN));
                absent.check_out     = res.getString(res.getColumnIndex(FIELD_CHECK_OUT));
                absent.status        = res.getString(res.getColumnIndex(FIELD_STATUS));
                absent.status_desc   = res.getString(res.getColumnIndex(FIELD_STATUS_DESC));
                absent.note          = res.getString(res.getColumnIndex(FIELD_NOTE));
                absent.work_date     = res.getString(res.getColumnIndex(FIELD_WORK_DATE));
                absent.work_time     = res.getString(res.getColumnIndex(FIELD_WORK_TIME));
                absent.work_end_time = res.getString(res.getColumnIndex(FIELD_WORK_END_TIME));
                absent.late          = res.getString(res.getColumnIndex(FIELD_MAX_LATE));
                absents.add(absent);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        Log.d(TAG,"SIZE : "+ absents.size());
        return absents;
    }

    public void getToday(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();

        Calendar calendar     = Calendar.getInstance();
        Date currentTime      = calendar.getTime();
        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+" WHERE "+
                FIELD_WORK_DATE+" = '"+formatDate.format(currentTime)+"'", null);
        try {
            while (res.moveToNext()){
                this.absent_header = res.getInt(res.getColumnIndex(FIELD_ABSENT_HEDAER));
                this.check_in      = res.getString(res.getColumnIndex(FIELD_CHECK_IN));
                this.check_out     = res.getString(res.getColumnIndex(FIELD_CHECK_OUT));
                this.status        = res.getString(res.getColumnIndex(FIELD_STATUS));
                this.status_desc   = res.getString(res.getColumnIndex(FIELD_STATUS_DESC));
                this.note          = res.getString(res.getColumnIndex(FIELD_NOTE));
                this.work_date     = res.getString(res.getColumnIndex(FIELD_WORK_DATE));
                this.work_time     = res.getString(res.getColumnIndex(FIELD_WORK_TIME));
                this.work_end_time     = res.getString(res.getColumnIndex(FIELD_WORK_END_TIME));
                this.late          = res.getString(res.getColumnIndex(FIELD_MAX_LATE));
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
