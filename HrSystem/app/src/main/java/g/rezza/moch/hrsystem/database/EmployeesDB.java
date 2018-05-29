package g.rezza.moch.hrsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import g.rezza.moch.hrsystem.connection.Database;

/**
 * Created by rezza on 27/01/18.
 */

public class EmployeesDB {

    public int      id;
    public int      user_id;
    public String   email;
    public String   name;
    public String   address;
    public int      gender;
    public String   phone;
    public String   alt_phone;
    public String   dob;
    public String   npwp;
    public String   org_desc;
    public int      org;
    public String      pob;

    public static final String TAG   = "EmployeesDB";

    public static final String TABLE_NAME       = "EMPLOYEES";

    public static final String FIELD_ID    = "ID";
    public static final String FIELD_USER_ID    = "USER_ID";
    public static final String FIELD_EMAIL      = "EMAIL";
    public static final String FIELD_NAME       = "NAME";
    public static final String FIELD_ADDRESS    = "ADDRESS";
    public static final String FIELD_GENDER     = "GENDER";
    public static final String FIELD_PHONE      = "PHONE";
    public static final String FIELD_ALTPHONE   = "ALTPHONE";
    public static final String FIELD_DOB        = "DOB";
    public static final String FIELD_POB        = "POB";
    public static final String FIELD_NPWP       = "NPWP";
    public static final String FIELD_ORG        = "ORG";
    public static final String FIELD_ORG_DESC   = "ORG_DESC";


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " int NOT NULL," +
                " " + FIELD_USER_ID    + " int NOT NULL," +
                " " + FIELD_EMAIL + " varchar(50) NULL," +
                " " + FIELD_NAME + " varchar(100) NULL," +
                " " + FIELD_ADDRESS + " varchar(225) NULL," +
                " " + FIELD_GENDER + " int default 1," +
                " " + FIELD_PHONE + " varchar(20) NULL," +
                " " + FIELD_ALTPHONE + " varchar(20) NULL," +
                " " + FIELD_DOB + " varchar(20) NULL," +
                " " + FIELD_POB + " varchar(100) NULL," +
                " " + FIELD_NPWP + " varchar(30) NULL," +
                " " + FIELD_ORG_DESC + " varchar(50) NULL," +
                " " + FIELD_ORG+ " int default 0," +
                "  PRIMARY KEY (" + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_USER_ID, user_id);
        contentValues.put(FIELD_EMAIL, email);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_GENDER, gender);
        contentValues.put(FIELD_PHONE, phone);
        contentValues.put(FIELD_ALTPHONE, alt_phone);
        contentValues.put(FIELD_DOB, dob);
        contentValues.put(FIELD_NPWP, npwp);
        contentValues.put(FIELD_ORG, org);
        contentValues.put(FIELD_ORG_DESC, org_desc);
        contentValues.put(FIELD_POB, pob);
        contentValues.put(FIELD_ADDRESS, address);
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

    public void getData(Context context, String user){
        Database pDB        = new Database(context);
        SQLiteDatabase db   = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+
                                     " WHERE "+ FIELD_USER_ID +"="+ user, null);
        try {
            while (res.moveToNext()){
                Log.d(TAG,res.getString(res.getColumnIndex(FIELD_ID)));
                this.id         = res.getInt(res.getColumnIndex(FIELD_ID));
                this.user_id    = res.getInt(res.getColumnIndex(FIELD_USER_ID));
                this.email      = res.getString(res.getColumnIndex(FIELD_EMAIL));
                this.name       = res.getString(res.getColumnIndex(FIELD_NAME));
                this.gender     = res.getInt(res.getColumnIndex(FIELD_GENDER));
                this.phone      = res.getString(res.getColumnIndex(FIELD_PHONE));
                this.alt_phone  = res.getString(res.getColumnIndex(FIELD_ALTPHONE));
                this.dob        = res.getString(res.getColumnIndex(FIELD_DOB));
                this.npwp       = res.getString(res.getColumnIndex(FIELD_NPWP));
                this.org_desc   = res.getString(res.getColumnIndex(FIELD_ORG_DESC));
                this.pob        = res.getString(res.getColumnIndex(FIELD_POB));
                this.address    = res.getString(res.getColumnIndex(FIELD_ADDRESS));
                this.org        = res.getInt(res.getColumnIndex(FIELD_ORG));
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
