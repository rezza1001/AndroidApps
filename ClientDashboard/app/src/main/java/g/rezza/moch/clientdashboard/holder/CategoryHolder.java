package g.rezza.moch.clientdashboard.holder;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rezza on 9/26/17.
 */

public class CategoryHolder {

    public  String event_id         = "";
    public  String category_id      = "";
    public  String category_name    = "";
    public  boolean selected        = true;
    public int color                = 0;

    public JSONObject pack(){
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", this.event_id);
            jo.put("category_id", this.category_id);
            jo.put("category_name", this.category_name);
            jo.put("selected", this.selected);
            jo.put("color", color);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public CategoryHolder unpack(JSONObject jo){
        try {
            this.event_id       = jo.getString("event_id");
            this.category_id    = jo.getString("category_id");
            this.category_name  = jo.getString("category_name");
            this.selected       = jo.getBoolean("selected");
            this.color          = jo.getInt("color");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }



}
