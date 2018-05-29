package g.rezza.moch.grupia.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import g.rezza.moch.grupia.R;
import g.rezza.moch.grupia.adapter.ContactAdapter;
import g.rezza.moch.grupia.component.ChapterDialog;
import g.rezza.moch.grupia.component.SearchViw;
import g.rezza.moch.grupia.listHolder.ContactLHolder;

/**
 * Created by rezza on 20/02/18.
 */

public class MemberFragment extends Fragment{

    private TextView txvw_value_00;
    private ListView lsvw_member_00;
    private ContactAdapter adapter;
    private CircleImageView imvw_search_00;
    private SearchViw serchvw_00;
    private ArrayList<ContactLHolder> mHolders = new ArrayList<>();
    private ArrayList<ContactLHolder> fHolders = new ArrayList<>();

    public static Fragment newInstance(int color) {
        Fragment frag   = new MemberFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_member_fragment, container, false);
        txvw_value_00   = view.findViewById(R.id.txvw_value_00);
        lsvw_member_00  = view.findViewById(R.id.lsvw_member_00);
        imvw_search_00  = view.findViewById(R.id.imvw_search_00);
        serchvw_00      = view.findViewById(R.id.serchvw_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter         = new ContactAdapter(getActivity(), mHolders);
        lsvw_member_00.setAdapter(adapter);
        requestData();
        filter("");

        txvw_value_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChapterDialog chapterDialog = new ChapterDialog(getActivity());
                chapterDialog.create();
                chapterDialog.setOnClickListener(new ChapterDialog.OnClickListener() {
                    @Override
                    public void onClick(String chapter) {
                        txvw_value_00.setText(chapter);
                    }
                });
            }
        });

        imvw_search_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serchvw_00.show();
                imvw_search_00.setVisibility(View.GONE);
            }
        });

        final View v = getActivity().getCurrentFocus();
        serchvw_00.setOnBackListener(new SearchViw.OnBackListener() {
            @Override
            public void onBack() {
                imvw_search_00.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        serchvw_00.setOnSearchListenr(new SearchViw.OnSearchListenr() {
            @Override
            public void onSearch(String text) {
                filter(text);
            }
        });
    }

    public void requestData(){
        {
            ContactLHolder contact =  new ContactLHolder();
            contact.id      = "1";
            contact.name    = "Khonsa Khoirunisa";
            contact.imageUrl= "https://preview.ibb.co/kooiJH/Raisa.jpg";
            contact.no      = "B0119";
            fHolders.add(contact);
        }
        {
            ContactLHolder contact =  new ContactLHolder();
            contact.id      = "1";
            contact.name    = "Sarah Adelia";
            contact.imageUrl= "https://image.ibb.co/dmOpax/facebook_dp_for_girls_4.jpg";
            contact.no      = "B0111";
            fHolders.add(contact);
        }
        {
            ContactLHolder contact =  new ContactLHolder();
            contact.id      = "2";
            contact.name    = "Risa Awaliyah";
            contact.imageUrl= "https://image.ibb.co/b80cvx/Sri.jpg";
            contact.no      = "B0112";
            fHolders.add(contact);
        }
        {
            ContactLHolder contact =  new ContactLHolder();
            contact.id      = "3";
            contact.name    = "Dicky Rahman";
            contact.imageUrl= "https://scontent-sit4-1.cdninstagram.com/vp/0e1beab9e3a9c1eb8a97eaf38420e87c/5B114CB8/t51.2885-19/s150x150/27881008_560919174241738_1870227463045382144_n.jpg";
            contact.no      = "B0113";
            fHolders.add(contact);
        }
        {
            ContactLHolder contact =  new ContactLHolder();
            contact.id      = "3";
            contact.name    = "Fikri Akbar";
            contact.imageUrl= "https://scontent-sit4-1.cdninstagram.com/vp/033b893c555378227df12364ccda96b7/5B14C1AE/t51.2885-19/s150x150/13649234_214681305594925_732160418_a.jpg";
            contact.no      = "B0114";
            fHolders.add(contact);
        }
        {
            ContactLHolder contact =  new ContactLHolder();
            contact.id      = "3";
            contact.name    = "Indra Nugraha";
            contact.imageUrl= "https://scontent-sit4-1.cdninstagram.com/vp/7a8df7ce55fda4df6ee855eb33853590/5B111F1F/t51.2885-19/s150x150/25016858_757395727763957_3043942935851696128_n.jpg";
            contact.no      = "B0115";
            fHolders.add(contact);
        }
        {
            ContactLHolder contact =  new ContactLHolder();
            contact.id      = "3";
            contact.name    = "Hadist Luqman";
            contact.imageUrl= "https://scontent-sit4-1.cdninstagram.com/vp/1a68d02fca5293f2094385a9c9b7f1d1/5B0DB3B8/t51.2885-19/s150x150/20582358_371313846637387_813531913271640064_a.jpg";
            contact.no      = "B0116";
            fHolders.add(contact);
        }
    }

    public void filter(String s){
        mHolders.clear();
        if (s.isEmpty()){
            for (ContactLHolder holder:fHolders){
                mHolders.add(holder);
            }
        }
        else {
            for (ContactLHolder holder:fHolders){
                if (holder.name.toUpperCase().contains(s.toUpperCase())){
                    mHolders.add(holder);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}
