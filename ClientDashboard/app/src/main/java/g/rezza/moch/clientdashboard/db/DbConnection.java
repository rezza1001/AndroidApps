package g.rezza.moch.clientdashboard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.HashMap;

import g.rezza.moch.clientdashboard.holder.ConfigHolder;
import g.rezza.moch.clientdashboard.holder.ParameterHolder;
import g.rezza.moch.clientdashboard.holder.UserSHolder;

/**
 * Created by Rezza on 2/13/2017.
 */

public class DbConnection extends SQLiteOpenHelper {
    public static final String TAG = "DbConnection";
    public static final String DATABASE_NAME = "clientdb.db";
    private static final int DB_VERSION = 6;

    private HashMap hp;
    public DbConnection(Context contex) {
        super(contex, DATABASE_NAME , null, DB_VERSION);
        Log.d("TAGRZ","ACCESS DB ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG ,"Create "+ ParameterHolder.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ParameterHolder.TABLE_NAME);
        db.execSQL(new ParameterHolder().getCreateTable());
        db.execSQL("DROP TABLE IF EXISTS "+ ConfigHolder.TABLE_NAME);
        db.execSQL(new ConfigHolder().getCreateTable());
        db.execSQL("DROP TABLE IF EXISTS "+ UserSHolder.TABLE_NAME);
        db.execSQL(new UserSHolder().getCreateTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"VERSION  "+ oldVersion + " <> "+ newVersion);
        switch (newVersion){
            case 6:
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
