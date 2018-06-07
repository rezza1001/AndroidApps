package g.rezza.moch.kiostixv3.holder;

/**
 * Created by rezza on 11/02/18.
 */

public class EventsList {

    public String name;
    public String id;
    public String venue;
    public String img_url;

    public EventsList (String id, String name, String url, String  venue){
        this.id     = id;
        this.name   = name;
        img_url     = url;
        this.venue  = venue;
    }
}
