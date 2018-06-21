package g.rezza.moch.clientdashboard.holder;

/**
 * Created by Rezza on 8/16/17.
 */

public class KeyValueHolder {
    private String key;
    private String value;

    public KeyValueHolder(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
