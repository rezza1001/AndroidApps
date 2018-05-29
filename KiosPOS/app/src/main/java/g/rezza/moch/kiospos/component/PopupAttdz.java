package g.rezza.moch.kiospos.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.holder.AttendenzHolder;

/**
 * Created by Rezza on 8/23/17.
 */

public class PopupAttdz extends Dialog {

    private MyEditext       myedtx_name_31;
    private MyDatePicker    mydtpk_birth_32;
    private GenderOption    gopt_gender_33;
    private AutoComplete    acpl_city_34;
    private Button          bbtn_ok_30;
    private AttendenzHolder mHolder;

    public PopupAttdz(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.component_pop_attdz);

        myedtx_name_31  =   (MyEditext)         findViewById(R.id.myedtx_name_31);
        mydtpk_birth_32 =   (MyDatePicker)      findViewById(R.id.mydtpk_birth_32);
        gopt_gender_33  =   (GenderOption)      findViewById(R.id.gopt_gender_33);
        acpl_city_34    =   (AutoComplete)      findViewById(R.id.acpl_city_34);
        bbtn_ok_30      =   (Button)            findViewById(R.id.bbtn_ok_30);

        myedtx_name_31.setTitle("Full Name ");
        mydtpk_birth_32.setTitle("Birth Date");
        gopt_gender_33.setTitle("Gender");
        acpl_city_34.setTitle("City (In accordance with valid ID)");
        bbtn_ok_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCekData()){
                    dismiss();
                    if(mListener != null){
                        mListener.onClick(getData());
                    }
                }
            }
        });
    }

    public void setData(AttendenzHolder holder){
        if (holder.complete){
            myedtx_name_31.setValue(holder.nama);
            mydtpk_birth_32.setValue(holder.birth);
            gopt_gender_33.setValue(holder.gender);
            acpl_city_34.setValue(holder.city);
        }
        mHolder = holder;

    }

    public AttendenzHolder getData(){
        return mHolder;
    }

    private boolean onCekData(){
        P:{
            if (myedtx_name_31.getValue().equals("")){
                Toast.makeText(getContext(),"Full Name Required !", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (mydtpk_birth_32.getValue().equals("")){
                Toast.makeText(getContext(),"Birth Date Required !", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (acpl_city_34.getValue().equals("")){
                Toast.makeText(getContext(),"City Required !", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (gopt_gender_33.getValue().equals("")){
                Toast.makeText(getContext(),"Gender Required !", Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                mHolder.nama    = myedtx_name_31.getValue();
                mHolder.birth   = mydtpk_birth_32.getValue();
                mHolder.gender  = gopt_gender_33.getValue();
                mHolder.city    = acpl_city_34.getValue();
                mHolder.complete= true;
                return true;
            }
        }
    }
    public void disable(){
        myedtx_name_31.disable();
        mydtpk_birth_32.disable();
        gopt_gender_33.disable();
        acpl_city_34.disable();
    }

    private OnPositifClickListener mListener;
    public void setOnPositifClickListener(OnPositifClickListener positifClickListener){
        mListener = positifClickListener;
    }
    public interface OnPositifClickListener{
        public void onClick(AttendenzHolder holder);
    }

}
