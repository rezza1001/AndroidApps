package g.rezza.moch.hrsystem.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.hrsystem.ChangePasswordActivity;
import g.rezza.moch.hrsystem.R;
import g.rezza.moch.hrsystem.component.EditextStandardC;
import g.rezza.moch.hrsystem.component.TextKeyValueView;
import g.rezza.moch.hrsystem.database.EmployeesDB;
import g.rezza.moch.hrsystem.database.UserDB;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Konstantin on 22.12.2014.
 */
public class ProfileFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String BUILDING = "Building";
    public static final String BOOK = "Book";


    private View containerView;
    protected int res;
    private Bitmap bitmap;
    private UserDB user;

    private TextView txvw_name_00;
    private TextView txvw_position_00;
    private TextView txvw_emial_company_00;

    private TextKeyValueView edtx_pob_00;
    private TextKeyValueView edtx_dob_00;
    private TextKeyValueView edtx_address_00;
    private TextKeyValueView edtx_gender_00;
    private TextKeyValueView edtx_selfmail_00;
    private TextKeyValueView edtx_phone1_00;
    private TextKeyValueView edtx_phone2_00;
    private RelativeLayout bbtn_password_00;

    public static ProfileFragment newInstance() {
        ProfileFragment contentFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());
        user = new UserDB();
        user.getMine(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_profile_fragment, container, false);
        txvw_name_00            = (TextView) rootView.findViewById(R.id.txvw_name_00);
        txvw_position_00        = (TextView) rootView.findViewById(R.id.txvw_position_00);
        txvw_emial_company_00   = (TextView) rootView.findViewById(R.id.txvw_emial_company_00);
        edtx_pob_00             = (TextKeyValueView) rootView.findViewById(R.id.edtx_pob_00) ;
        edtx_dob_00             = (TextKeyValueView) rootView.findViewById(R.id.edtx_dob_00) ;
        edtx_address_00         = (TextKeyValueView) rootView.findViewById(R.id.edtx_address_00) ;
        edtx_gender_00          = (TextKeyValueView) rootView.findViewById(R.id.edtx_gender_00) ;
        edtx_selfmail_00        = (TextKeyValueView) rootView.findViewById(R.id.edtx_selfmail_00) ;
        edtx_phone1_00          = (TextKeyValueView) rootView.findViewById(R.id.edtx_phone1_00) ;
        edtx_phone2_00          = (TextKeyValueView) rootView.findViewById(R.id.edtx_phone2_00) ;
        bbtn_password_00        = (RelativeLayout)   rootView.findViewById(R.id.bbtn_password_00);


        edtx_pob_00.setTitle(getResources().getString(R.string.pob));
        edtx_pob_00.setReadOnly(true);

        edtx_dob_00.setTitle(getResources().getString(R.string.dob));
        edtx_dob_00.setReadOnly(true);

        edtx_address_00.setTitle(getResources().getString(R.string.address));
        edtx_address_00.setReadOnly(true);

        edtx_gender_00.setTitle(getResources().getString(R.string.gender));
        edtx_gender_00.setReadOnly(true);

        edtx_selfmail_00.setTitle(getResources().getString(R.string.email_address));
        edtx_selfmail_00.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        edtx_phone1_00.setTitle(getResources().getString(R.string.phone1));
        edtx_phone1_00.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_phone2_00.setTitle(getResources().getString(R.string.phone2));
        edtx_phone2_00.setInputType(InputType.TYPE_CLASS_PHONE);

        loadDb();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bbtn_password_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void loadDb(){
        EmployeesDB employee = new EmployeesDB();
        employee.getData(getActivity(), user.id+ "");

        txvw_emial_company_00.setText(user.email);
        txvw_name_00.setText(employee.name);
        txvw_position_00.setText(employee.org_desc);
        edtx_pob_00.setValue(employee.pob);
        edtx_dob_00.setValue(employee.dob);
        edtx_address_00.setValue(employee.address);
        edtx_gender_00.setValue(employee.gender == 1?getResources().getString(R.string.male) : getResources().getString(R.string.female));

        edtx_selfmail_00.setValue(employee.email);
        edtx_phone1_00.setValue(employee.phone);
        edtx_phone2_00.setValue(employee.alt_phone);
    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
//                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
//                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap);
//                containerView.draw(canvas);
//                ProfileFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}

