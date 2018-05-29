package g.rezza.moch.kiospos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.HashMap;

import g.rezza.moch.kiospos.holder.BluethoothHolder;
import g.rezza.moch.kiospos.holder.ConfigHolder;
import g.rezza.moch.kiospos.holder.ImageHolder;

/**
 * Created by Rezza on 2/13/2017.
 */

public class Database extends SQLiteOpenHelper {

    public static final String TAG = "RZ Database";

    public static final String DATABASE_NAME = "kios.db";
    private static final int DB_VERSION = 1;

    private HashMap hp;
    public Database(Context contex) {
        super(contex, DATABASE_NAME , null, DB_VERSION);
        Log.d(TAG,"ACCESS DB ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+ BluethoothHolder.TABLE_NAME);
        db.execSQL(new BluethoothHolder().getCreateTable());
        Log.d(TAG,"Create "+ ImageHolder.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ImageHolder.TABLE_NAME);
        db.execSQL(new ImageHolder().getCreateTable());
        db.execSQL("DROP TABLE IF EXISTS "+ ConfigHolder.TABLE_NAME);
        db.execSQL(new ConfigHolder().getCreateTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"VERSION  "+ oldVersion + " <> "+ newVersion);
        switch (newVersion){
            case 1:
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
