package g.rezza.moch.mobilesales.Connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import g.rezza.moch.mobilesales.Database.BalanceDB;
import g.rezza.moch.mobilesales.Database.BookingDB;
import g.rezza.moch.mobilesales.Database.CompanyDB;
import g.rezza.moch.mobilesales.Database.IdentityTypeDB;
import g.rezza.moch.mobilesales.Database.ListSalesDB;
import g.rezza.moch.mobilesales.Database.ListStoreDB;
import g.rezza.moch.mobilesales.Database.NotificationDB;
import g.rezza.moch.mobilesales.Database.SalesDB;
import g.rezza.moch.mobilesales.Database.SchedulesDB;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.Database.UserInfoDB;

/**
 * Created by rezza on 11/01/18.
 */

public class Database extends SQLiteOpenHelper {

    public static final String TAG = "RZ Database";

    public static final String DATABASE_NAME = "TAPPSALES.db";
    private static final int DB_VERSION =16;

    private HashMap hp;
    public Database(Context contex) {
        super(contex, DATABASE_NAME , null, DB_VERSION);
        Log.d(TAG,"ACCESS DB ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate New Database ");
        db.execSQL("DROP TABLE IF EXISTS "+ UserDB.TABLE_NAME);
        db.execSQL(new UserDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ UserInfoDB.TABLE_NAME);
        db.execSQL(new UserInfoDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ CompanyDB.TABLE_NAME);
        db.execSQL(new CompanyDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ ListStoreDB.TABLE_NAME);
        db.execSQL(new ListStoreDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ BalanceDB.TABLE_NAME);
        db.execSQL(new BalanceDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ NotificationDB.TABLE_NAME);
        db.execSQL(new NotificationDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ ListSalesDB.TABLE_NAME);
        db.execSQL(new ListSalesDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ IdentityTypeDB.TABLE_NAME);
        db.execSQL(new IdentityTypeDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ BookingDB.TABLE_NAME);
        db.execSQL(new BookingDB().getCreateTable());

        db.execSQL("DROP TABLE IF EXISTS "+ SchedulesDB.TABLE_NAME);
        db.execSQL(new SchedulesDB().getCreateTable());


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"VERSION  "+ oldVersion + " <> "+ newVersion);
        switch (newVersion){
            case 12:
                onCreate(db);
                break;
            case 16:
                db.execSQL("DROP TABLE IF EXISTS "+ BookingDB.TABLE_NAME);
                db.execSQL(new BookingDB().getCreateTable());
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