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

public class ListSalesDB {

    public int    user_id;
    public String name;
    public float  balance;
    public float  total_sales;

    public static final String TAG   = "ListSalesDB";

    public static final String TABLE_NAME       = "ListSalesDB";

    public static final String USER_ID          = "USER_ID";
    public static final String NAME             = "NAME";
    public static final String BALANCE          = "BALANCE";
    public static final String TOTAL_SALES      = "TOTAL_SALES";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + USER_ID    + " int NOT NULL," +
                " " + NAME + " varchar(50) NULL," +
                " " + BALANCE + " varchar(20) NULL," +
                " " + TOTAL_SALES + " varchar(20) NULL," +
                "  PRIMARY KEY (" + USER_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, user_id);
        contentValues.put(NAME, name);
        contentValues.put(BALANCE, balance);
        contentValues.put(TOTAL_SALES, total_sales);
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

    public ArrayList<ListSalesDB> getData(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);

        ArrayList<ListSalesDB> datas = new ArrayList<>();

        try {
            while (res.moveToNext()){
                ListSalesDB data = new ListSalesDB();
                data.user_id        = res.getInt(res.getColumnIndex(USER_ID));
                data.name           = res.getString(res.getColumnIndex(NAME));
                data.total_sales    = res.getLong(res.getColumnIndex(TOTAL_SALES));
                data.balance        = res.getLong(res.getColumnIndex(BALANCE));
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

}
