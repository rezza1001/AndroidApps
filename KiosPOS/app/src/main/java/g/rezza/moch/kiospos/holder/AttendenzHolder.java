package g.rezza.moch.kiospos.holder;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rezza on 10/13/17.
 */

public class AttendenzHolder {
    public String   id  = "";
    public String   nama = "Guest";
    public String   email = "";
    public String   gender = "";
    public String   city = "";
    public String   birth = "";
    public boolean  complete = false;

    public AttendenzHolder(){
        Long currentTime = Calendar.getInstance().getTimeInMillis();
        id = currentTime+"";
    }

    public JSONObject pack(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", id);
            jo.put("name", nama);
            jo.put("gender", gender);
            jo.put("city", city);
            jo.put("birth", birth);
            jo.put("complete", complete);
            jo.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public AttendenzHolder unpack(JSONObject jo){
        try {
            id = jo.getString("id");
            nama = jo.getString("name");
            gender = jo.getString("gender");
            city = jo.getString("city");
            birth = jo.getString("birth");
            email = jo.getString("email");
            complete = jo.getBoolean("complete");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
