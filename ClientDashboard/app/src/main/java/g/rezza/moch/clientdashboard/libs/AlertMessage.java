package g.rezza.moch.clientdashboard.libs;

/**
 * Created by Rezza on 10/2/17.
 */

public class AlertMessage {

    public static final int ENGLISH     = 2;
    public static final int INDONESIA   = 1;

    public static final int FIELD_REQUIRED_PARAMETER        = 1;
    public static final int RANGE_DATE_MUST_BE_30_DAYS      = 2;
    public static final int DATA_SAVED_TO_FOLDER            = 3;
    public static final int DOWNLOAD_REPORT_FAILED          = 4;
    public static final int PLEASE_WAIT                     = 5;
    public static final int DOWNLOAD_REPORT                 = 6;
    public static final int LOGOUT                          = 7;
    public static final int SIGN_IN                         = 8;
    public static final int CHEKING_DATA                    = 9;
    public static final int REQUEST_DATA                    = 10;
    public static final int LOAD_MORE                       = 11;



    public static String getMessage(int messageID, int type){
        String mMessage = "";
            switch (type){
                case 2:
                    if (messageID == FIELD_REQUIRED_PARAMETER){
                        mMessage = "Report parameter are required!";
                    }
                    else if (messageID == RANGE_DATE_MUST_BE_30_DAYS){
                        mMessage = "Rentang Tanggal tidak boleh lebih dari 30 hari";
                    }
                    else if (messageID == DATA_SAVED_TO_FOLDER){
                        mMessage = "Report saved in directories Kiosk Dashboard";
                    }
                    else if (messageID == DOWNLOAD_REPORT_FAILED){
                        mMessage = "Report download failed";
                    }
                    else if (messageID == PLEASE_WAIT){
                        mMessage = "Please Wait...";
                    }
                    else if (messageID == DOWNLOAD_REPORT){
                        mMessage = "Downloading Report";
                    }
                    else if (messageID == LOGOUT){
                        mMessage = "Logout";
                    }
                    else if (messageID == SIGN_IN){
                        mMessage = "Sign In";
                    }
                    else if (messageID == CHEKING_DATA){
                        mMessage = "Checking Data";
                    }
                    else if (messageID == REQUEST_DATA){
                        mMessage = "Requesting Data";
                    }
                    else if (messageID == LOAD_MORE){
                        mMessage = "Load More";
                    }

                    break;
                case 1:
                    if (messageID == FIELD_REQUIRED_PARAMETER){
                        mMessage = "Parameter laporan harus diisi!";
                    }
                    else if (messageID == RANGE_DATE_MUST_BE_30_DAYS){
                        mMessage = "Rentang Tanggal tidak boleh lebih dari 30 hari";
                    }
                    else if (messageID == DATA_SAVED_TO_FOLDER){
                        mMessage = "Laporan tersimpan di direktori Kios Dashboard";
                    }
                    else if (messageID == DOWNLOAD_REPORT_FAILED){
                        mMessage = "Gagal mengunduhan Laporan";
                    }
                    else if (messageID == PLEASE_WAIT){
                        mMessage = "Silahkan Tunggu...";
                    }
                    else if (messageID == DOWNLOAD_REPORT){
                        mMessage = "Mengunduh Laporan";
                    }
                    else if (messageID == LOGOUT){
                        mMessage = "Keluar";
                    }
                    else if (messageID == SIGN_IN){
                        mMessage = "Masuk";
                    }
                    else if (messageID == CHEKING_DATA){
                        mMessage = "Memeriksa Data";
                    }
                    else if (messageID == REQUEST_DATA){
                        mMessage = "Meminta Data";
                    }
                    else if (messageID == LOAD_MORE){
                        mMessage = "Load More";
                    }
                    break;
            }
            return mMessage;
    }
}
