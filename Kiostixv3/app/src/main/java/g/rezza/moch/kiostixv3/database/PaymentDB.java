package g.rezza.moch.kiostixv3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.connection.Database;
import g.rezza.moch.kiostixv3.lib.Parse;

/**
 * Created by rezza on 15/03/18.
 */

public class PaymentDB {

    public static final String TAG          = "PaymentDB";
    public static final String TABLE_NAME   = "PAYMENT_DB";

    public static final String FIELD_ID             = "ID";
    public static final String FIELD_NAME           = "NAME";
    public static final String FIELD_FEE_INFO       = "FEE_INFO";
    public static final String FIELD_FEE_VALUE      = "FEE_VALUE";
    public static final String FIELD_IMAGE          = "IMAGE_URL";
    public static final String FIELD_DESCRIPTION    = "DESCRIPTION";

    public String id;
    public String name;
    public String fee_info;
    public String fee_value;
    public String image_url;
    public String description = "";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID + " varchar(30) NOT NULL," +
                " " + FIELD_NAME + " varchar(20) NOT NULL," +
                " " + FIELD_FEE_INFO + " varchar(50)  NULL," +
                " " + FIELD_FEE_VALUE + " varchar(50)  NULL," +
                " " + FIELD_IMAGE + " text  NULL," +
                " " + FIELD_DESCRIPTION + " text  NULL," +
                "  PRIMARY KEY (" + FIELD_ID + "))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_FEE_INFO, fee_info);
        contentValues.put(FIELD_IMAGE, image_url);
        contentValues.put(FIELD_FEE_VALUE, fee_value);
        contentValues.put(FIELD_DESCRIPTION, description);
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

    public ArrayList<PaymentDB> getDatas(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        ArrayList<PaymentDB> data = new ArrayList<>();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        try {
            while (res.moveToNext()){
                PaymentDB event = new PaymentDB();
                event.id          = res.getString(res.getColumnIndex(FIELD_ID));
                event.name        = res.getString(res.getColumnIndex(FIELD_NAME));
                event.fee_info    = res.getString(res.getColumnIndex(FIELD_FEE_INFO));
                event.fee_value   = res.getString(res.getColumnIndex(FIELD_FEE_VALUE));
                event.image_url   = res.getString(res.getColumnIndex(FIELD_IMAGE));
                event.description = res.getString(res.getColumnIndex(FIELD_DESCRIPTION));
                data.add(event);
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


    public void getData(Context context, String id){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+" WHERE "+ FIELD_ID+"='"+id+"'", null);
        try {
            while (res.moveToNext()){
                this.id          = res.getString(res.getColumnIndex(FIELD_ID));
                this.name        = res.getString(res.getColumnIndex(FIELD_NAME));
                this.fee_info    = res.getString(res.getColumnIndex(FIELD_FEE_INFO));
                this.fee_value   = res.getString(res.getColumnIndex(FIELD_FEE_VALUE));
                this.image_url   = res.getString(res.getColumnIndex(FIELD_IMAGE));
                this.description = res.getString(res.getColumnIndex(FIELD_DESCRIPTION));
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
