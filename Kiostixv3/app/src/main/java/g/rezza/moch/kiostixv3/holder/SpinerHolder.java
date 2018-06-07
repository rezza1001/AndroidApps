package g.rezza.moch.kiostixv3.holder;

/**
 * Created by rezza on 11/01/18.
 */

public class SpinerHolder {
    public String key;
    public String value;
    public int category;

    public SpinerHolder(String key, String value, int category){
        this.key        = key;
        this.value      = value;
        this.category   = category;
    }

    public SpinerHolder(int key, String value, int category){
        this.key        = key + "";
        this.value      = value;
        this.category   = category;
    }
    public SpinerHolder(){

    }
}
