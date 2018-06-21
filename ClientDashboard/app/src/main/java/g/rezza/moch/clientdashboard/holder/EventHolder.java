package g.rezza.moch.clientdashboard.holder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rezza on 9/26/17.
 */

public class EventHolder  {
    public String event_id      = "";
    public String event_name    = "";
    public boolean isSelected   = false;

    public ArrayList<CategoryHolder> categories;

    public EventHolder (){
        categories = new ArrayList<>();
    }

    public JSONObject pack(){
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", this.event_id);
            jo.put("event_name", this.event_name);
            jo.put("isSelected", this.isSelected);
                JSONArray ja = new JSONArray();
                for (CategoryHolder category : categories){
                    ja.put(category.pack());
                }
            jo.put("categories",ja);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public EventHolder unpack(JSONObject jo){
        try {
            this.event_id   = jo.getString("event_id");
            this.event_name = jo.getString("event_name");
            this.isSelected = jo.getBoolean("isSelected");
            JSONArray ja    = jo.getJSONArray("categories");
            for (int i=0; i<ja.length(); i++){
                JSONObject data = ja.getJSONObject(i);
                CategoryHolder ctg = new CategoryHolder().unpack(data);
                this.categories.add(ctg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
