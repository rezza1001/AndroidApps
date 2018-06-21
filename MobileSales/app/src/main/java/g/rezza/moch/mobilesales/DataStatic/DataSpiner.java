package g.rezza.moch.mobilesales.DataStatic;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Database.IdentityTypeDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.SpinerHolder;

/**
 * Created by rezza on 11/01/18.
 */

public class DataSpiner {

    public static final int CATEGORY_GENDER         = 1;
    public static final int CATEGORY_IDTYPE_PERSON  = 2;
    public static final int CATEGORY_IDTYPE_COMPANY = 3;

    public static ArrayList<SpinerHolder>  getData(int category, Context context){
        Resources r = context.getResources();
        ArrayList<SpinerHolder> holders = new ArrayList<>();
        switch (category){
            case CATEGORY_GENDER:
                holders.add(new SpinerHolder("1",r.getString(R.string.male),CATEGORY_GENDER));
                holders.add(new SpinerHolder("2",r.getString(R.string.female),CATEGORY_GENDER));
                break;
            default:
                IdentityTypeDB idDB = new IdentityTypeDB();
                ArrayList<IdentityTypeDB> typeDBS=  idDB.getData(context, category);
                for (IdentityTypeDB typeDB: typeDBS){
                    holders.add(new SpinerHolder(typeDB.id,typeDB.name,typeDB.category));
                }
                break;
        }
        return holders;
    }

}
