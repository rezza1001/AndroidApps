package g.rezza.moch.kiostixv3.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.ChangePasswordActivity;
import g.rezza.moch.kiostixv3.activity.EditCustomerActivity;
import g.rezza.moch.kiostixv3.activity.OnBoardingActivity;
import g.rezza.moch.kiostixv3.activity.RegisterActivity;
import g.rezza.moch.kiostixv3.activity.SignInActivity;
import g.rezza.moch.kiostixv3.component.TextViewProfile;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.database.MyTixDB;
import g.rezza.moch.kiostixv3.database.SchedulesDB;
import g.rezza.moch.kiostixv3.lib.LoadingScreen;

/**
 * Created by rezza on 09/02/18.
 */

public class MyAccountFragment extends Fragment {
    private static final String TAG = "MyAccountFragment";
    private static final int RESULT_LOAD_IMG = 64;

    private RelativeLayout  rvly_changepwd_00;
    private RelativeLayout  rvly_logout_00;
    private RelativeLayout  rvly_active_00;
    private RelativeLayout  rvly_nonactive_00;
    private RelativeLayout  rvly_changeimage_00;
    private TextView        txvw_name_00;
    private TextView        txvw_change_00;
    private TextViewProfile txpf_email_00;
    private TextViewProfile txpf_phone_00;
    private TextViewProfile txpf_idnumber_00;
    private TextViewProfile txpf_gender_00;
    private TextViewProfile txpf_pob_00;
    private TextViewProfile txpf_dob_00;
    private TextViewProfile txpf_idtype_00;
    private TextViewProfile txpf_country_00;
    private TextViewProfile txpf_province_00;
    private TextViewProfile txpf_city_00;
    private TextViewProfile txpf_address_00;
    private TextViewProfile txpf_pos_00;
    private LoadingScreen   mLoading;
    private Button          bbtn_register_00;
    private Button          bbtn_login_00;
    private CircleImageView imvw_account_00;

    public static Fragment newInstance() {
        Fragment frag   = new MyAccountFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view           = inflater.inflate(R.layout.view_myaccount_fragment, container, false);
        rvly_changepwd_00   = view.findViewById(R.id.rvly_changepwd_00);
        rvly_logout_00      = view.findViewById(R.id.rvly_logout_00);
        txvw_name_00        = view.findViewById(R.id.txvw_name_00);
        txvw_change_00      = view.findViewById(R.id.txvw_change_00);
        txpf_email_00       = view.findViewById(R.id.txpf_email_00);
        txpf_phone_00       = view.findViewById(R.id.txpf_phone_00);
        txpf_idnumber_00    = view.findViewById(R.id.txpf_idnumber_00);
        txpf_gender_00      = view.findViewById(R.id.txpf_gender_00);
        txpf_pob_00         = view.findViewById(R.id.txpf_pob_00);
        txpf_dob_00         = view.findViewById(R.id.txpf_dob_00);
        txpf_idtype_00      = view.findViewById(R.id.txpf_idtype_00);
        txpf_country_00     = view.findViewById(R.id.txpf_country_00);
        txpf_province_00    = view.findViewById(R.id.txpf_province_00);
        txpf_city_00        = view.findViewById(R.id.txpf_city_00);
        txpf_address_00     = view.findViewById(R.id.txpf_address_00);
        txpf_pos_00         = view.findViewById(R.id.txpf_pos_00);

        rvly_nonactive_00   = view.findViewById(R.id.rvly_nonactive_00);
        rvly_active_00      = view.findViewById(R.id.rvly_active_00);
        bbtn_login_00       = (Button) view.findViewById(R.id.bbtn_login_00);
        bbtn_register_00    = (Button) view.findViewById(R.id.bbtn_register_00);

        rvly_changeimage_00 = view.findViewById(R.id.rvly_changeimage_00);
        imvw_account_00     = view.findViewById(R.id.imvw_account_00);

        mLoading            = new LoadingScreen(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txpf_email_00.setTitle(getResources().getString(R.string.email));
        txpf_email_00.setImage(R.mipmap.ic_mail_white);
        txpf_phone_00.setTitle(getResources().getString(R.string.handphone));
        txpf_phone_00.setImage(R.mipmap.ic_phone_white);
        txpf_idtype_00.setTitle(getResources().getString(R.string.id_type));
        txpf_idtype_00.setImage(R.drawable.ic_credit_card);
        txpf_idnumber_00.setTitle(getResources().getString(R.string.no_identity));
        txpf_idnumber_00.setImage(R.mipmap.ic_identity_white);
        txpf_gender_00.setTitle(getResources().getString(R.string.gender));
        txpf_gender_00.setImage(R.drawable.ic_gender);
        txpf_pob_00.setTitle(getResources().getString(R.string.pob));
        txpf_pob_00.setImage(R.drawable.ic_location_on);
        txpf_dob_00.setTitle(getResources().getString(R.string.dob));
        txpf_dob_00.setImage(R.drawable.ic_date_range);
        txpf_country_00.setTitle(getResources().getString(R.string.country));
        txpf_country_00.setImage(R.drawable.ic_flag);
        txpf_province_00.setTitle(getResources().getString(R.string.province));
        txpf_province_00.setImage(R.drawable.ic_location_on);
        txpf_city_00.setTitle(getResources().getString(R.string.city));
        txpf_city_00.setImage(R.drawable.ic_location_on);
        txpf_address_00.setTitle(getResources().getString(R.string.address));
        txpf_address_00.setImage(R.drawable.ic_address);
        txpf_pos_00.setTitle(getResources().getString(R.string.zip_code));
        txpf_pos_00.setImage(R.drawable.ic_markunread_mailbox);

        rvly_changepwd_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        rvly_logout_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoading.setTitle("Please Wait ...");
                mLoading.show();
                handler.sendEmptyMessageDelayed(1,1000);
            }
        });

        txvw_change_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditCustomerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        bbtn_login_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.putExtra("FROM","2");
                startActivity(intent);
                getActivity().finish();
            }
        });

        bbtn_register_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("FROM","2");
                startActivity(intent);
                getActivity().finish();
            }
        });

        rvly_changeimage_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPemission();
            }
        });

        loadFromDB();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                loadFromDB();
                break;
            case RESULT_LOAD_IMG:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imvw_account_00.setImageBitmap(selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void loadFromDB(){
        CustomerDB customerDB = new CustomerDB();
        customerDB.getData(getActivity());
        if (customerDB.id.isEmpty()){
            rvly_active_00.setVisibility(View.GONE);
            rvly_nonactive_00.setVisibility(View.VISIBLE);
        }
        else {
            rvly_active_00.setVisibility(View.VISIBLE);
            rvly_nonactive_00.setVisibility(View.GONE);

            txpf_email_00.setValue(customerDB.email);
            txpf_phone_00.setValue(customerDB.phone);
            txpf_idnumber_00.setValue(customerDB.identity);
            txvw_name_00.setText(customerDB.name);
            txpf_gender_00.setValue(customerDB.gender.equals("1")?"Laki-Laki" : "Perempuan");
            txpf_pob_00.setValue(customerDB.pob);

            txpf_dob_00.setValue(customerDB.dob);
            txpf_idtype_00.setValue(customerDB.id_type);
            txpf_country_00.setValue(customerDB.country);
            txpf_city_00.setValue(customerDB.city);
            txpf_province_00.setValue(customerDB.province);
            txpf_address_00.setValue(customerDB.address);
            txpf_pos_00.setValue(customerDB.zip_code);
        }

    }

    private void logout(){
        CustomerDB customerDB = new CustomerDB();
        customerDB.clearData(getActivity());

        BookingDB bookingDB = new BookingDB();
        bookingDB.clearData(getActivity());

        SchedulesDB schedulesDB = new SchedulesDB();
        schedulesDB.clearData(getActivity());

        MyTixDB myTixDB = new MyTixDB();
        myTixDB.clearData(getActivity());

        mLoading.dimiss();
        Intent intent = new Intent(getActivity(), OnBoardingActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 1:
                    logout();
            }
            return false;
        }
    });

    private void getImageFromGalery(){
        Intent imageGallery = new Intent(Intent.ACTION_PICK);
        imageGallery.setType("image/*");
        startActivityForResult(imageGallery, RESULT_LOAD_IMG);
    }


    /*
     *  PERMISSION GALLERY
     */

    private void requestPemission(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        if(!hasPermissions(getActivity(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
        else {
            getImageFromGalery();
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getImageFromGalery();
        }

    }

}
