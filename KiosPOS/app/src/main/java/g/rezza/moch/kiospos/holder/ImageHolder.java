package g.rezza.moch.kiospos.holder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import g.rezza.moch.kiospos.db.Database;

/**
 * Created by Rezza on 9/4/17.
 */

public class ImageHolder {

    public static final String TAG = "ImageHolder";

    public String name = null;
    public byte[] image;

    public static final String TABLE_NAME = "IMAGES";
    public static final String NAME = "NAME";
    public static final String BLOB = "BLOB";

    public String getCreateTable(){
        String create = "create table " + TABLE_NAME + " "
                + "(" + BLOB + " BLOB ," +
                " " + NAME + " varchar(50) primary key" +
                ")";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(BLOB, image);
        return contentValues;
    }

    public static ImageHolder getData(Database pDB){
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        res.moveToFirst();
        ImageHolder user = new ImageHolder();
        while (res.isAfterLast() == false) {
            user.name       = res.getString(res.getColumnIndex(NAME));
            user.image      = res.getBlob(res.getColumnIndex(BLOB));
            res.moveToNext();
        }
        pDB.close();
        return user;
    }

}
