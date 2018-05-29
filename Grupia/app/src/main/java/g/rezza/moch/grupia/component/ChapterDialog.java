package g.rezza.moch.grupia.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;

import g.rezza.moch.grupia.R;
import g.rezza.moch.grupia.adapter.ChapterAdapter;

/**
 * Created by rezza on 26/02/18.
 */

public class ChapterDialog extends Dialog {

    private ListView lsvw_chapter_00;
    private ArrayList<String> mHolders = new ArrayList<>();
    private ChapterAdapter adapter;

    public ChapterDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.component_dialog_chapter);
        lsvw_chapter_00 = (ListView) findViewById(R.id.lsvw_chapter_00);
        adapter = new ChapterAdapter(getContext(), mHolders);
        lsvw_chapter_00.setAdapter(adapter);

        adapter.setOnSelectedItemListener(new ChapterAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(String holder, int position) {
                if (mListener != null){
                    mListener.onClick(holder);
                }
                ChapterDialog.this.dismiss();
            }
        });
    }

    public void create(){
        this.show();

        mHolders.add("HCI CHAPTER JPUTSEL");
        mHolders.add("HCI CHAPTER JBTANG");
        mHolders.add("HCI CHAPTER JTBEK");
        mHolders.add("HCI CHAPTER DEBORA");
        mHolders.add("HCI CHAPTER BANDUNG");
        mHolders.add("HCI CHAPTER JATENG");
        mHolders.add("HCI CHAPTER BALI");
        mHolders.add("HCI CHAPTER ACEH");
        mHolders.add("HCI CHAPTER MAKASSAR");
        mHolders.add("HCI CHAPTER KALTENG");
        mHolders.add("HCI REGIONAL JATIM");
        mHolders.add("HCI CHAPTER GRESIK – JATIM");
        mHolders.add("HCI CHAPTER SIDOARJO – JATIM");
        mHolders.add("HCI CHAPTER PLAT AG – JATIM");
        mHolders.add("HCI CHAPTER PLAT S – JATIM");
        mHolders.add("HCI CHAPTER SURABAYA – JATIM");
        mHolders.add("HCI CHAPTER PLAT N – JATIM");
        adapter.notifyDataSetChanged();
    }

    private OnClickListener mListener;
    public void setOnClickListener(OnClickListener positifClickListener){
        mListener = positifClickListener;
    }

    /*
     * Interface Listener
     */
    public interface OnClickListener{
        public void onClick(String chapter);
    }
}
