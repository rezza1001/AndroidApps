package g.rezza.moch.mobilesales.holder;

/**
 * Created by rezza on 09/01/18.
 */

public class EventCategoryHolder {
    public String name;
    public String id;
    public String date;
    public String time;
    public long  price = 0;
    public int    quantity = 0;

    public String getPriceAdultStr(){
        return price +"";
    }

}
