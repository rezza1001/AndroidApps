package g.rezza.moch.mobilesales.Activity.Product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import g.rezza.moch.mobilesales.Activity.LoginActivity;
import g.rezza.moch.mobilesales.Database.BookingDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.component.SimpleDatePicker;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;

public class EventGuestActivity extends ActivityDtl {

    private EditextStandardC edtx_fullname_00;
    private EditextStandardC edtx_email_00;
    private EditextStandardC edtx_phone_00;
    private SimpleDatePicker smpr_date_00;
    private AutoCompleteTextView edtx_city_00;
    private Button           bbtn_action_00;
    private TextView         txvw_name_00;
    private BookingDB        myBooking = new BookingDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_guest);
        myBooking.getData(this);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(getResources().getString(R.string.header_guestinfo));

        edtx_fullname_00 = (EditextStandardC)   findViewById(R.id.edtx_fullname_00);
        edtx_email_00    = (EditextStandardC)    findViewById(R.id.edtx_email_00);
        edtx_phone_00    = (EditextStandardC)    findViewById(R.id.edtx_phone_00);
        smpr_date_00     = (SimpleDatePicker)    findViewById(R.id.smpr_date_00);
        edtx_city_00     = (AutoCompleteTextView)            findViewById(R.id.edtx_city_00);
        bbtn_action_00   = (Button)              findViewById(R.id.bbtn_action_00);
        txvw_name_00     = (TextView)           findViewById(R.id.txvw_name_00);

        edtx_fullname_00.setTitle(getResources().getString(R.string.full_name));
        edtx_fullname_00.setMandatory(true);
        edtx_fullname_00.setInputType(InputType.TYPE_CLASS_TEXT);

        edtx_email_00.setTitle(getResources().getString(R.string.email_address));
        edtx_email_00.setMandatory(true);
        edtx_email_00.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        edtx_phone_00.setTitle(getResources().getString(R.string.phone_number));
        edtx_phone_00.setMandatory(true);
        edtx_phone_00.setInputType(InputType.TYPE_CLASS_PHONE);

        smpr_date_00.setTitle(getResources().getString(R.string.dob));
        smpr_date_00.setMandatory(true);

        txvw_name_00.setText(myBooking.event_name);

        loadCity();
        registerListener();

        if (!myBooking.event_guest.isEmpty()){
            try {
                JSONObject data = new JSONObject(myBooking.event_guest);
                edtx_fullname_00.setValue(data.getString("NAME"));
                edtx_email_00.setValue(data.getString("EMAIL"));
                edtx_phone_00.setValue(data.getString("PHONE"));
                edtx_city_00.setText(data.getString("CITY"));
                smpr_date_00.setValue(data.getString("DOB"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCity(){
        ArrayList<String> emails = new ArrayList<>();
        emails.add("Jakarta");
        emails.add("Bandung");
        emails.add("Sukabumi");
        emails.add("Bekasi");
        emails.add("Majalengka");
        addEmailsToAutoComplete(emails);
    }

    private void registerListener(){
        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validation()){
                    return;
                }
                JSONObject jobGuest = new JSONObject();
                try {
                    jobGuest.put("NAME",edtx_fullname_00.getValue());
                    jobGuest.put("EMAIL",edtx_email_00.getValue());
                    jobGuest.put("PHONE",edtx_phone_00.getValue());
                    jobGuest.put("CITY",edtx_city_00.getText().toString());
                    jobGuest.put("DOB",smpr_date_00.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(EventGuestActivity.this, EventSummaryActivity.class);
                myBooking.event_guest = jobGuest.toString();
                myBooking.insert(EventGuestActivity.this);
                startActivity(intent);
                EventGuestActivity.this.finish();
            }
        });
    }

    private boolean validation(){
        if (edtx_fullname_00.getValue().isEmpty()){
            edtx_fullname_00.showNotif(r.getString(R.string.field_required));
            return false;
        }
        else  if (edtx_email_00.getValue().isEmpty()){
            edtx_email_00.showNotif(r.getString(R.string.field_required));
            return false;
        }
        else  if (edtx_phone_00.getValue().isEmpty()){
            edtx_phone_00.showNotif(r.getString(R.string.field_required));
            return false;
        }
        else  if (smpr_date_00.getValue().isEmpty()){
            edtx_phone_00.showNotif(r.getString(R.string.field_required));
            return false;
        }
        else  if (edtx_city_00.getText().toString().isEmpty()){
            Toast.makeText(this,r.getString(R.string.city)+ " Required!" , Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(EventGuestActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        edtx_city_00.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventGuestActivity.this, EventCategoryActivity.class);
        startActivity(intent);
        EventGuestActivity.this.finish();
    }
}
