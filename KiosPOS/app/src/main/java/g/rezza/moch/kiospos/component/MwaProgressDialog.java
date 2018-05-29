package g.rezza.moch.kiospos.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;

public class MwaProgressDialog extends ProgressDialog {
	
	private String mMessage;
	
	public MwaProgressDialog(Context p_context) {
		super (p_context);
		setProgressStyle(ProgressDialog.STYLE_SPINNER);
		setCancelable(false);
		setMessage(getDefaultMessage());
    }
	
	@Override
	public void setMessage(CharSequence p_message) {
		super.setMessage(Html.fromHtml(p_message.toString()));
	}
	
	public final void setMessage(String p_message) {
		super.setMessage(Html.fromHtml(mMessage = p_message));
	}

	public final String getMessage() {
		return mMessage;
	}
	
	public final String getDefaultMessage() {
		return "<b>Please wait . . . </b>";
	}
	
}