package g.rezza.moch.kiospos.holder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import g.rezza.moch.kiospos.db.Database;

/**
 * Created by Rezza on 9/4/17.
 */

public class BluethoothHolder {

    public static final String TAG = "BluethoothHolder";

    public String name = null;
    public String macaddress = null;
    public int bondState = 0 ;

    public static final String TABLE_NAME = "BLUETHOOTH_DEVICE";
    public static final String NAME = "NAME";
    public static final String MAC_ADDRESS = "MAC_ADDRESS";
    public static final String BOUND_SATATE = "BOUND_SATATE";

    public String getCreateTable(){
        String create = "create table " + TABLE_NAME + " "
                + "(" + MAC_ADDRESS + " varchar(30) primary key ," +
                " " + NAME + " varchar(50) NOT NULL," +
                " " + BOUND_SATATE + " int(11) NOT NULL" +
                ")";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(MAC_ADDRESS, macaddress);
        contentValues.put(BOUND_SATATE, bondState);
        return contentValues;
    }

    public static BluethoothHolder getData(Database pDB){
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        res.moveToFirst();
        BluethoothHolder user = new BluethoothHolder();
        while (res.isAfterLast() == false) {
            user.bondState  = Integer.parseInt(res.getString(res.getColumnIndex(BOUND_SATATE)));
            user.macaddress = res.getString(res.getColumnIndex(MAC_ADDRESS));
            user.name       = res.getString(res.getColumnIndex(NAME));
            Log.d(TAG, "getData: "+ user.name);
            res.moveToNext();
        }
        pDB.close();
        return user;
    }

}
