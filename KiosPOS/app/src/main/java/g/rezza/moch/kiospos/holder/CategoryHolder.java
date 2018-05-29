package g.rezza.moch.kiospos.holder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rezza on 10/12/17.
 */

public class CategoryHolder {
    public String id;
    public String value;
    public String price;
    public boolean checked = false;

    public JSONObject pack(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("id",id);
            jo.put("value",value);
            jo.put("price",price);
            jo.put("checked",checked);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public CategoryHolder unpack(JSONObject jo){
        try {
            id      = jo.getString("id");
            value   = jo.getString("value");
            price   = jo.getString("price");
            checked = jo.getBoolean("checked");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
