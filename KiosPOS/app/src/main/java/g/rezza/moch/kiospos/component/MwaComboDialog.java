package g.rezza.moch.kiospos.component;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class MwaComboDialog {

	private AlertDialog _1_aldi_11;
	private Builder 	_1_bldr_11;
	
	private String   mTitle;
	private String[] mItems;
	private Object   mObject;
		
	public MwaComboDialog(Context p_context) {			
		_1_bldr_11 = new Builder(p_context);
		mTitle     = "";
		mItems 	   = null;
		mObject    = null;
	}

	public final MwaComboDialog setTitle(String p_title) {
		mTitle = p_title == null ? "" : p_title;
		return this;
	}
	
	public final MwaComboDialog setItems(String[] p_items) {
		mItems = p_items;
		return this;
	}
	
	public final MwaComboDialog setObject(Object p_object) {
		mObject = p_object;
		return this;
	}
	
	public final MwaComboDialog create() {
		if (!mTitle.equals("")) {
			_1_bldr_11.setTitle(mTitle);
		}
		if (mItems != null && mItems.length > 0) {
			_1_bldr_11.setItems(mItems, new DialogInterface.OnClickListener() {
	    		@Override
	    	    public void onClick(DialogInterface p_dialog, int p_index) {
	    			onSelected(p_index, mItems[p_index]);
	    			_1_aldi_11.cancel();
	    	    }
	    	});
		}
		_1_aldi_11 = _1_bldr_11.create();
		return this;
	}
	
	public final void show() {
		_1_aldi_11.show();
	}
	
	public void onSelected(int p_index, String p_item) { 
		;
	}
	
	public final Object getObject() {
		return mObject;
	}
	
	public final String getTitle() {
		return mTitle;
	}
	
	public final String[] getItems() {
		return mItems;
	}
		
}