package g.rezza.moch.kiostixv3.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.HashMap;

import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.database.EventsDB;
import g.rezza.moch.kiostixv3.database.MyTixDB;
import g.rezza.moch.kiostixv3.database.PaymentDB;
import g.rezza.moch.kiostixv3.database.SchedulesDB;
import g.rezza.moch.kiostixv3.database.TransHystoryDB;

/**
 * Created by rezza on 11/01/18.
 */

public class Database extends SQLiteOpenHelper {

    public static final String TAG = "RZ Database";

    public static final String DATABASE_NAME = "KISTIXV3.db";
    private static final int DB_VERSION = 26;

    private HashMap hp;
    public Database(Context contex) {
        super(contex, DATABASE_NAME , null, DB_VERSION);
        Log.d(TAG,"ACCESS DB ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate New Database ");
        db.execSQL("DROP TABLE IF EXISTS "+ BookingDB.TABLE_NAME);
        db.execSQL(new BookingDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ MyTixDB.TABLE_NAME);
        db.execSQL(new MyTixDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ SchedulesDB.TABLE_NAME);
        db.execSQL(new SchedulesDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ EventsDB.TABLE_NAME);
        db.execSQL(new EventsDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ CustomerDB.TABLE_NAME);
        db.execSQL(new CustomerDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ PaymentDB.TABLE_NAME);
        db.execSQL(new PaymentDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ TransHystoryDB.TABLE_NAME);
        db.execSQL(new TransHystoryDB().getCreateTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"VERSION  "+ oldVersion + " <> "+ newVersion);
        switch (newVersion){
            case 26:
                onCreate(db);
                break;
        }

    }

    public boolean insert (String pTable, ContentValues pContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(pTable, null, pContent);
        return true;
    }

    public int update (String pTable, ContentValues pContent, String where) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(pTable,pContent,where,null);
    }

    public void updateColumn (String pTable, String query, String pWhere) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"UPDATE "+pTable+ " SET " +query+ " WHERE "+ pWhere);
        db.execSQL("UPDATE "+pTable+ " SET " +query+ " WHERE "+ pWhere);
    }

    public boolean delete(String pTable, String where) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(pTable,where,null);
        return true;
    }

}