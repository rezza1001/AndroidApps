package g.rezza.moch.clientdashboard.holder;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Rezza on 10/3/17.
 */

public class ExcelHolder {
    public String parameter;
    public ArrayList<String > datas = new ArrayList<>();

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setDatas(ArrayList<String> datas) {
        this.datas = datas;
    }
    public void addData(String data){
        datas.add(data);
    }
}
