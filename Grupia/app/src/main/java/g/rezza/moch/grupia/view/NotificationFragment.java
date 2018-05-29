package g.rezza.moch.grupia.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import g.rezza.moch.grupia.R;
import g.rezza.moch.grupia.adapter.NotificationAdapter;
import g.rezza.moch.grupia.listHolder.NotifLHolder;

/**
 * Created by rezza on 20/02/18.
 */

public class NotificationFragment extends Fragment implements View.OnClickListener{

    private ListView lsvw_notif_00;
    private NotificationAdapter adapter;
    private ArrayList<NotifLHolder> holders = new ArrayList<>();

    public static Fragment newInstance(int color) {
        Fragment frag   = new NotificationFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_notification_fragment, container, false);
        lsvw_notif_00   = view.findViewById(R.id.lsvw_notif_00);
        adapter         = new NotificationAdapter(getActivity(), holders);
        lsvw_notif_00.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestData();
    }

    @Override
    public void onClick(View view) {

    }

    private void requestData(){
        {
            NotifLHolder notif  = new NotifLHolder();
            notif.create_date   = "Apr 19, 2017";
            notif.created_by    = "hrvclubindonesia";
            notif.subject       = "HR-V Club Indonesia (HCI) Chapter DeBoRa Audiensi dengan Walikota Bogor";
            notif.body          = "Bertempat di balai kota Bogor, HR-V Club Indonesia (HCI) Chapter DeBoRa yang secara resmi di deklarasikan pada tanggal 19 April 2016";
            holders.add(notif);
        }
        {
            NotifLHolder notif  = new NotifLHolder();
            notif.create_date   = "Dec 26, 2017";
            notif.created_by    = "hrvclubindonesia";
            notif.subject       = "HR-V Club Chapter Sumut Resmi Dikukuhkan";
            notif.body          = "MEDAN : HR-V Club Indonesia atau HCI sebagai organisasi kumpulan pengguna mobil merk HR-V, terus mengembangkan sayapnya ke seluruh antero tanah";
            holders.add(notif);
        }
        {
            NotifLHolder notif  = new NotifLHolder();
            notif.create_date   = "Dec 26, 2017";
            notif.created_by    = "hrvclubindonesia";
            notif.subject       = "HCI Chapter Lampung Resmi Berdiri";
            notif.body          = "TELUK PANDAN – Provinsi Lampung secara resmi menjadi provinsi ke-17 untuk pendeklarasian HR-V Club Indonesia ( HCI) Chapter Lampung yang dipu";
            holders.add(notif);
        }
        {
            NotifLHolder notif  = new NotifLHolder();
            notif.create_date   = "Dec 26, 2017";
            notif.created_by    = "hrvclubindonesia";
            notif.subject       = "Jamnas Merah Putih HR-V Club Indonesia 2017 sukses digelar di Semarang";
            notif.body          = "Semarang, 20 Agustus 2017 HR-V Club Indonesia sukses menggelar Jambore Nasional Pertama pada tanggal 18-19 2017 di Semarang.  Serangkaia";
            holders.add(notif);
        }
        adapter.notifyDataSetChanged();
    }
}
