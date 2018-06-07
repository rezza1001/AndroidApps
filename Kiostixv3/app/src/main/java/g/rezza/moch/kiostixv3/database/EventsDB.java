package g.rezza.moch.kiostixv3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;

import g.rezza.moch.kiostixv3.connection.Database;
import g.rezza.moch.kiostixv3.datastatic.App;

/**
 * Created by rezza on 21/03/18.
 */

public class EventsDB  {

    public String id;
    public String name;
    public String category;
    public String img_url;
    public String venue;
    public int sqno = 0;


    public static final String TAG          = "EventsDB";
    public static final String TABLE_NAME   = "EVENT_DB";

    public static final String FIELD_ID     = "ID";
    public static final String FIELD_NAME   = "NAME";
    public static final String FIELD_CATEGORY  = "CATEGORY";
    public static final String FIELD_IMAGE  = "IMAGE";
    public static final String FIELD_VENUE  = "VENUE";
    public static final String FIELD_SQNO = "SQNO";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID + " varchar(30) NOT NULL," +
                " " + FIELD_NAME + " varchar(255) NOT NULL," +
                " " + FIELD_CATEGORY + " varchar(255)  NULL," +
                " " + FIELD_IMAGE + " text  NULL," +
                " " + FIELD_VENUE + " text  NULL," +
                " " + FIELD_SQNO + " INTEGER DEFAULT 0 ," +
                "  PRIMARY KEY (" + FIELD_ID + "))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_CATEGORY, category);
        contentValues.put(FIELD_IMAGE, img_url);
        contentValues.put(FIELD_VENUE, venue);
        contentValues.put(FIELD_SQNO, sqno);
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

        boolean x = false;
        try {
            Database pDB = new Database(context);
            x = pDB.insert(TABLE_NAME, createContentValues());
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        Log.d(TAG, "Insert Event "+ this.name+ " -> "+ x);
        return x;
    }

    public ArrayList<EventsDB> getDatas(Context context, String pCategory){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        ArrayList<EventsDB> data = new ArrayList<>();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME
                        +" WHERE ("+ FIELD_CATEGORY+" = '"+ pCategory+"' OR  '" +App.CATEGORY_ALL +"'='"+pCategory+"')"
                , null);
        try {
            while (res.moveToNext()){
                EventsDB event = new EventsDB();
                event.id       = res.getString(res.getColumnIndex(FIELD_ID));
                event.name     = res.getString(res.getColumnIndex(FIELD_NAME));
                event.category    = res.getString(res.getColumnIndex(FIELD_CATEGORY));
                event.img_url    = res.getString(res.getColumnIndex(FIELD_IMAGE));
                event.venue    = res.getString(res.getColumnIndex(FIELD_VENUE));
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


    public ArrayList<EventsDB> getNews(Context context, String pCategory){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        ArrayList<EventsDB> data = new ArrayList<>();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME
                +" WHERE ("+ FIELD_CATEGORY+" = '"+ pCategory+"' OR  '" +App.CATEGORY_ALL +"'='"+pCategory+"')"
                +" ORDER BY "+FIELD_SQNO+" LIMIT 10", null);
        try {
            while (res.moveToNext()){
                EventsDB event = new EventsDB();
                event.id       = res.getString(res.getColumnIndex(FIELD_ID));
                event.name     = res.getString(res.getColumnIndex(FIELD_NAME));
                event.category    = res.getString(res.getColumnIndex(FIELD_CATEGORY));
                event.img_url    = res.getString(res.getColumnIndex(FIELD_IMAGE));
                event.venue    = res.getString(res.getColumnIndex(FIELD_VENUE));
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

}
