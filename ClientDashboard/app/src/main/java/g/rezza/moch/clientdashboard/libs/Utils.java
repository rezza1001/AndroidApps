package g.rezza.moch.clientdashboard.libs;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import g.rezza.moch.clientdashboard.holder.ExcelHolder;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Rezza on 10/2/17.
 */

public class Utils {

    public static Date toDate(String date,String format){
        String startDateString = date;
        DateFormat df = new SimpleDateFormat(format);
        Date startDate = null;
        try {
            startDate = df.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    public static long getDifferenceDate(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return elapsedDays;
    }

    public static boolean exportToExcel(String name, ArrayList<ExcelHolder> pDatas) {
        boolean exportStatus    = true;
        long time               = System.currentTimeMillis();
        final String fileName   = name+"_"+time+".xls";
        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard,"Kios Dashboard");

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory.getAbsolutePath(), fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("TRANSACTION", 0);

            try {
                int rowHeader = 0;
                for (ExcelHolder data: pDatas){
                    sheet.addCell(new Label(rowHeader, 0, data.parameter)); // column and row

                    ArrayList<String> dataValue = data.datas;
                    for (int i=0; i<dataValue.size(); i++){
                        String sData = dataValue.get(i);
                        sheet.addCell(new Label(rowHeader, (i+1), sData));
                    }
                    rowHeader ++;
                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                exportStatus = false;
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                exportStatus = false;
                e.printStackTrace();
            }
        } catch (IOException e) {
            exportStatus = false;
            e.printStackTrace();
        }

        return exportStatus;
    }

    public static String getParameterValue(String key){
        switch (key){
            case "1":
                return "Today";
            case "2":
                return "Yesterday";
            case "3":
                return "Last 7 Days";
            case "4":
                return "This Month";
            case "5":
                return "Last Month";
            default:
                return "Last 30 Days";
        }
    }

    private ArrayList<Integer> mColors = new ArrayList<>();
    public Utils Colors(){
        mColors.add(Color.DKGRAY);
        mColors.add(Color.RED);
        mColors.add(Color.BLACK);
        mColors.add(Color.BLUE);
        mColors.add(Color.GREEN);
        mColors.add(Color.YELLOW);
        mColors.add(Color.CYAN);
        mColors.add(Color.MAGENTA);
        mColors.add(Color.LTGRAY);
        return Utils.this;
    }

    public Integer getColor(int index){
        return mColors.get(index);
    }

}
