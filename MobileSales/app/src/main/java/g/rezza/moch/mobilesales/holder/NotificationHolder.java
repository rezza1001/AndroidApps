package g.rezza.moch.mobilesales.holder;

/**
 * Created by rezza on 04/01/18.
 */

public class NotificationHolder {

    public static final int INBOX = 1;
    public static final int OUTBOX = 2;

    public static final int IN_PROGRESS = 6;
    public static final int APPROVE = 7;
    public static final int SUCCESS = 4;
    public static final int REJECT = 5;

    public String id;
    public String user_id_req;
    public String what;
    public String when;
    public String who;
    public String message;
    public String nominal;
    public String account_name;
    public String account_no;
    public boolean isRead = false;
    public int status = 1;
    public String status_desc = "";


}
