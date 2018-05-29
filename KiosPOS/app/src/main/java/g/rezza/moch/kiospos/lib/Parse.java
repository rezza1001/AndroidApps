package g.rezza.moch.kiospos.lib;

/**
 * Created by Rezza on 11/10/17.
 */

public class Parse {

    public static String formatTextMultiLine(String text, int maxline){
        int countline = 1;
        String value = "";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<text.length(); i++){
            char chr = text.charAt(i);
            value = value + chr;

            if (i % maxline == 0 && i != 0) {
                sb.append(value.trim()).append(" ").append("-");
                sb.append("\n");
                value = "";
                countline ++;
            }
            else {
                sb.append(value);
                value = "";
            }
        }
        if (countline > 2){
            StringBuilder mSb = new StringBuilder();
            mSb.append(sb.toString().split("\n")[0]).append("\n");
            mSb.append(sb.toString().split("\n")[1].substring(0, maxline-1)).append("-");
            return mSb.toString();
        }
        else {
            return sb.toString();
        }

    }

    public static String formatTextTrim(String text, int maxline){
        if (text.length() > maxline){
            return text.substring(0,maxline+1);
        }
        else {
            return text;
        }
    }


}
